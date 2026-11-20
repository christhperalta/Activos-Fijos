package com.christhperalta.activosfijos.feature.home.presentation

import com.christhperalta.activosfijos.core.shared.SessionRepository
import com.christhperalta.activosfijos.core.utils.SapException
import com.christhperalta.activosfijos.core.utils.SessionExpiredException
import com.christhperalta.activosfijos.feature.home.domain.model.AssetCountSummary
import com.christhperalta.activosfijos.feature.home.domain.repository.HomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loads_the_summary_on_init() = runTest(testDispatcher) {
        val expected = anySummary()
        val vm = createViewModel(Result.success(expected))

        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(expected, vm.uiState.value.summary)
        assertFalse(vm.uiState.value.isLoading)
        assertNull(vm.uiState.value.error)
        assertFalse(vm.uiState.value.sessionExpired)
    }

    @Test
    fun isLoading_is_true_while_the_summary_loads() = runTest(testDispatcher) {
        val vm = createViewModel(Result.success(anySummary()))

        assertTrue(vm.uiState.value.isLoading)

        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(vm.uiState.value.isLoading)
    }

    @Test
    fun SapException_sets_the_sap_error() = runTest(testDispatcher) {
        val vm = createViewModel(
            Result.failure(SapException(code = -1, errorMessage = "Invalid query option"))
        )

        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Error de SAP: -1 - Invalid query option", vm.uiState.value.error)
        assertFalse(vm.uiState.value.isLoading)
        assertNull(vm.uiState.value.summary)
        assertFalse(vm.uiState.value.sessionExpired)
    }

    @Test
    fun generic_exception_sets_the_connection_error() = runTest(testDispatcher) {
        val vm = createViewModel(Result.failure(Exception("Network timeout")))

        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Error de conexión: Network timeout", vm.uiState.value.error)
        assertFalse(vm.uiState.value.sessionExpired)
    }

    @Test
    fun SessionExpiredException_sets_sessionExpired_and_leaves_error_null() = runTest(testDispatcher) {
        val vm = createViewModel(Result.failure(SessionExpiredException()))

        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(vm.uiState.value.sessionExpired)
        // El modal es lo único que debe aparecer: si error no fuera null, la pantalla mostraría
        // además el mensaje en línea con el botón de reintentar.
        assertNull(vm.uiState.value.error)
        assertFalse(vm.uiState.value.isLoading)
    }

    @Test
    fun Retry_reloads_the_summary() = runTest(testDispatcher) {
        val repository = FakeHomeRepository(Result.failure(Exception("Network timeout")))
        val vm = HomeViewModel(repository, SessionRepository())
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(1, repository.calls)

        val expected = anySummary()
        repository.result = Result.success(expected)
        vm.onEvent(HomeEvent.Retry)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(2, repository.calls)
        assertEquals(expected, vm.uiState.value.summary)
        assertNull(vm.uiState.value.error)
    }

    @Test
    fun Retry_clears_the_previous_error() = runTest(testDispatcher) {
        val repository = FakeHomeRepository(Result.failure(Exception("Network timeout")))
        val vm = HomeViewModel(repository, SessionRepository())
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals("Error de conexión: Network timeout", vm.uiState.value.error)

        vm.onEvent(HomeEvent.Retry)

        assertNull(vm.uiState.value.error)
        assertTrue(vm.uiState.value.isLoading)
    }

    @Test
    fun SessionExpiredAccepted_clears_the_session_and_lowers_the_flag() = runTest(testDispatcher) {
        val sessionRepository = SessionRepository().apply { saveSession("B1SESSION-abc") }
        val vm = HomeViewModel(
            FakeHomeRepository(Result.failure(SessionExpiredException())),
            sessionRepository
        )
        testDispatcher.scheduler.advanceUntilIdle()
        assertTrue(vm.uiState.value.sessionExpired)

        vm.onEvent(HomeEvent.SessionExpiredAccepted)

        assertFalse(vm.uiState.value.sessionExpired)
        assertFailsWithIllegalState { sessionRepository.getSession() }
    }

    private fun createViewModel(result: Result<AssetCountSummary>) =
        HomeViewModel(FakeHomeRepository(result), SessionRepository())

    private fun anySummary() = AssetCountSummary(
        pendingToScanCount = 10,
        goodCount = 4,
        maintenanceCount = 3,
        pendingDecommissionCount = 2,
    )

    private inline fun assertFailsWithIllegalState(block: () -> Unit) {
        val thrown = try {
            block()
            null
        } catch (e: IllegalStateException) {
            e
        }
        assertTrue(thrown != null, "se esperaba que la sesión estuviera limpia")
    }
}

private class FakeHomeRepository(
    var result: Result<AssetCountSummary>
) : HomeRepository {
    var calls = 0

    override suspend fun getAssetCountSummary(): Result<AssetCountSummary> {
        calls++
        return result
    }
}
