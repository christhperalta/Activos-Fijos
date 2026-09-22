package com.christhperalta.activosfijos.feature.login.presentation

import com.christhperalta.activosfijos.core.utils.SapException
import com.christhperalta.activosfijos.feature.login.domain.model.LoginResponse
import com.christhperalta.activosfijos.feature.login.domain.repository.LoginRepository
import com.christhperalta.activosfijos.feature.login.domain.use_case.LogInUserUseCase
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
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

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
    fun initialUiState_has_default_values() = runTest(testDispatcher) {
        val vm = LoginViewModel(createUseCase(Result.success(anyResponse())))
        val state = vm.uiState.value
        assertEquals(false, state.isLoading)
        assertNull(state.loginResponse)
        assertNull(state.error)
        assertEquals("", state.user)
        assertEquals("", state.password)
    }

    @Test
    fun onUserNameChanged_updates_user() = runTest(testDispatcher) {
        val vm = LoginViewModel(createUseCase(Result.success(anyResponse())))
        vm.onEvent(LoginEvents.OnUserNameChanged("admin"))
        assertEquals("admin", vm.uiState.value.user)
    }

    @Test
    fun onPasswordChanged_updates_password() = runTest(testDispatcher) {
        val vm = LoginViewModel(createUseCase(Result.success(anyResponse())))
        vm.onEvent(LoginEvents.OnPasswordChanged("secret"))
        assertEquals("secret", vm.uiState.value.password)
    }

    @Test
    fun onLoginClicked_with_empty_user_does_nothing() = runTest(testDispatcher) {
        val useCase = TrackingUseCase(Result.success(anyResponse()))
        val vm = LoginViewModel(useCase)
        vm.onEvent(LoginEvents.OnPasswordChanged("secret"))
        vm.onEvent(LoginEvents.OnLoginClicked)
        assertEquals(0, useCase.invokeCount)
        assertNull(vm.uiState.value.loginResponse)
        assertEquals(false, vm.uiState.value.isLoading)
    }

    @Test
    fun onLoginClicked_with_empty_password_does_nothing() = runTest(testDispatcher) {
        val useCase = TrackingUseCase(Result.success(anyResponse()))
        val vm = LoginViewModel(useCase)
        vm.onEvent(LoginEvents.OnUserNameChanged("admin"))
        vm.onEvent(LoginEvents.OnLoginClicked)
        assertEquals(0, useCase.invokeCount)
        assertNull(vm.uiState.value.loginResponse)
        assertEquals(false, vm.uiState.value.isLoading)
    }

    @Test
    fun onLoginClicked_with_blank_user_does_nothing() = runTest(testDispatcher) {
        val useCase = TrackingUseCase(Result.success(anyResponse()))
        val vm = LoginViewModel(useCase)
        vm.onEvent(LoginEvents.OnUserNameChanged("   "))
        vm.onEvent(LoginEvents.OnPasswordChanged("pass"))
        vm.onEvent(LoginEvents.OnLoginClicked)
        assertEquals(0, useCase.invokeCount)
    }

    @Test
    fun onLoginClicked_with_blank_password_does_nothing() = runTest(testDispatcher) {
        val useCase = TrackingUseCase(Result.success(anyResponse()))
        val vm = LoginViewModel(useCase)
        vm.onEvent(LoginEvents.OnUserNameChanged("admin"))
        vm.onEvent(LoginEvents.OnPasswordChanged("   "))
        vm.onEvent(LoginEvents.OnLoginClicked)
        assertEquals(0, useCase.invokeCount)
    }

    @Test
    fun login_success_sets_loginResponse() = runTest(testDispatcher) {
        val expected = LoginResponse("meta", "sess123", 30, "2.0")
        val vm = LoginViewModel(createUseCase(Result.success(expected)))
        vm.onEvent(LoginEvents.OnUserNameChanged("admin"))
        vm.onEvent(LoginEvents.OnPasswordChanged("123"))
        vm.onEvent(LoginEvents.OnLoginClicked)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(expected, vm.uiState.value.loginResponse)
        assertEquals(false, vm.uiState.value.isLoading)
        assertNull(vm.uiState.value.error)
    }

    @Test
    fun login_success_sets_isLoading_during_execution() = runTest(testDispatcher) {
        val vm = LoginViewModel(createUseCase(Result.success(anyResponse())))
        vm.onEvent(LoginEvents.OnUserNameChanged("admin"))
        vm.onEvent(LoginEvents.OnPasswordChanged("123"))
        vm.onEvent(LoginEvents.OnLoginClicked)
        assertEquals(true, vm.uiState.value.isLoading)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(false, vm.uiState.value.isLoading)
    }

    @Test
    fun login_with_SapException_sets_sap_error() = runTest(testDispatcher) {
        val sapError = SapException(code = -1, errorMessage = "Invalid user or password")
        val vm = LoginViewModel(createUseCase(Result.failure(sapError)))
        vm.onEvent(LoginEvents.OnUserNameChanged("admin"))
        vm.onEvent(LoginEvents.OnPasswordChanged("123"))
        vm.onEvent(LoginEvents.OnLoginClicked)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals("Error de SAP: -1 - Invalid user or password", vm.uiState.value.error)
        assertEquals(false, vm.uiState.value.isLoading)
        assertNull(vm.uiState.value.loginResponse)
    }

    @Test
    fun login_with_SapException_null_code_and_message() = runTest(testDispatcher) {
        val sapError = SapException(code = null, errorMessage = null)
        val vm = LoginViewModel(createUseCase(Result.failure(sapError)))
        vm.onEvent(LoginEvents.OnUserNameChanged("admin"))
        vm.onEvent(LoginEvents.OnPasswordChanged("123"))
        vm.onEvent(LoginEvents.OnLoginClicked)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals("Error de SAP: null - null", vm.uiState.value.error)
    }

    @Test
    fun login_with_generic_exception_sets_connection_error() = runTest(testDispatcher) {
        val genericError = Exception("Network timeout")
        val vm = LoginViewModel(createUseCase(Result.failure(genericError)))
        vm.onEvent(LoginEvents.OnUserNameChanged("admin"))
        vm.onEvent(LoginEvents.OnPasswordChanged("123"))
        vm.onEvent(LoginEvents.OnLoginClicked)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals("Error de conexión: Network timeout", vm.uiState.value.error)
        assertEquals(false, vm.uiState.value.isLoading)
        assertNull(vm.uiState.value.loginResponse)
    }

    @Test
    fun login_with_generic_exception_null_message() = runTest(testDispatcher) {
        val genericError = Exception("null")
        val vm = LoginViewModel(createUseCase(Result.failure(genericError)))
        vm.onEvent(LoginEvents.OnUserNameChanged("admin"))
        vm.onEvent(LoginEvents.OnPasswordChanged("123"))
        vm.onEvent(LoginEvents.OnLoginClicked)
        testDispatcher.scheduler.advanceUntilIdle()
        assertNotNull(vm.uiState.value.error)
        assertEquals(true, vm.uiState.value.error!!.contains("null"))
    }

    @Test
    fun multiple_user_changes_keep_latest() = runTest(testDispatcher) {
        val vm = LoginViewModel(createUseCase(Result.success(anyResponse())))
        vm.onEvent(LoginEvents.OnUserNameChanged("first"))
        vm.onEvent(LoginEvents.OnUserNameChanged("second"))
        vm.onEvent(LoginEvents.OnUserNameChanged("third"))
        assertEquals("third", vm.uiState.value.user)
    }

    @Test
    fun consecutive_events_do_not_override_each_other() = runTest(testDispatcher) {
        val vm = LoginViewModel(createUseCase(Result.success(anyResponse())))
        vm.onEvent(LoginEvents.OnUserNameChanged("user1"))
        vm.onEvent(LoginEvents.OnPasswordChanged("pass1"))
        assertEquals("user1", vm.uiState.value.user)
        assertEquals("pass1", vm.uiState.value.password)
    }

    private fun createUseCase(result: Result<LoginResponse>): LogInUserUseCase {
        return object : LogInUserUseCase(FakeLoginRepo(result)) {
            override suspend fun invoke(
                userName: String,
                password: String
            ): Result<LoginResponse> = result
        }
    }

    private fun anyResponse() = LoginResponse(null, null, null, null)
}

class TrackingUseCase(
    private val result: Result<LoginResponse>
) : LogInUserUseCase(FakeLoginRepo(result)) {
    var invokeCount = 0
    var lastUserName: String? = null
    var lastPassword: String? = null

    override suspend fun invoke(userName: String, password: String): Result<LoginResponse> {
        invokeCount++
        lastUserName = userName
        lastPassword = password
        return result
    }
}

class FakeLoginRepo(
    private val result: Result<LoginResponse>
) : LoginRepository {
    override suspend fun userLogin(userName: String, password: String): Result<LoginResponse> = result
}
