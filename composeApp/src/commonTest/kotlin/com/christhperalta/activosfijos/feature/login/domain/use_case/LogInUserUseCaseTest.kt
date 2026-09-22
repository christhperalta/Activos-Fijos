package com.christhperalta.activosfijos.feature.login.domain.use_case

import com.christhperalta.activosfijos.feature.login.domain.model.LoginResponse
import com.christhperalta.activosfijos.feature.login.domain.repository.LoginRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LogInUserUseCaseTest {

    @Test
    fun invoke_delegates_to_repository_and_returns_success() = runTest {
        val expected = LoginResponse(
            odataMetadata = "meta",
            sessionId = "sess1",
            sessionTimeout = 30,
            version = "2.0"
        )
        val repo = FakeLoginRepository(result = Result.success(expected))
        val useCase = LogInUserUseCase(repo)

        val result = useCase("admin", "123")

        assertTrue(result.isSuccess)
        assertEquals(expected, result.getOrThrow())
        assertEquals("admin", repo.lastUserName)
        assertEquals("123", repo.lastPassword)
    }

    @Test
    fun invoke_delegates_to_repository_and_returns_failure() = runTest {
        val error = Exception("Invalid credentials")
        val repo = FakeLoginRepository(result = Result.failure(error))
        val useCase = LogInUserUseCase(repo)

        val result = useCase("admin", "wrong")

        assertTrue(result.isFailure)
        assertEquals(error, result.exceptionOrNull())
    }

    @Test
    fun invoke_passes_correct_parameters() = runTest {
        val repo = FakeLoginRepository(result = Result.success(
            LoginResponse(null, null, null, null)
        ))
        val useCase = LogInUserUseCase(repo)

        useCase("testUser", "testPass")

        assertEquals("testUser", repo.lastUserName)
        assertEquals("testPass", repo.lastPassword)
    }
}

class FakeLoginRepository(
    private val result: Result<LoginResponse>
) : LoginRepository {

    var lastUserName: String? = null
    var lastPassword: String? = null

    override suspend fun userLogin(userName: String, password: String): Result<LoginResponse> {
        lastUserName = userName
        lastPassword = password
        return result
    }
}
