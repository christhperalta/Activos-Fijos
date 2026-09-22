# B1PosCotizador — AGENTS.md

## What this is

Kotlin Multiplatform (KMP) + Compose Multiplatform app targeting **Android** and **iOS**. Single module (`:composeApp`). Clean Architecture with feature-based packages under `com.christhperalta.b1poscotizador`.

## Build & run

```shell
./gradlew :composeApp:assembleDebug          # Android
./gradlew :composeApp:iosSimulatorArm64Main  # iOS (or use Xcode on iosApp/)
```

No formatter, linter, typecheck, or CI scripts exist. No pre-commit hooks.

## Key tech

| Concern | Choice |
|---|---|
| UI | Compose Multiplatform + Material3 |
| Navigation | JetBrains `navigation3` (experimental Compose Multiplatform navigation) |
| DI | Koin (`koin-compose`, `koin-compose-viewmodel`) |
| HTTP | Ktor (okhttp on Android, Darwin on iOS) |
| JSON | Kotlinx Serialization |
| Local DB | SQLDelight (schemas in `commonMain/sqldelight`) |
| Barcode | QRKit |
| PDF | pdfmp-compose |
| Dates | Kotlinx DateTime |
| Icons | Material Icons Extended |

## Source layout

```
composeApp/src/
  commonMain/kotlin/.../
    core/           — shared UI components, theme, navigation, utils
    di/             — Koin modules (AppModule.kt)
    feature/*/      — one dir per feature, each with:
      presentation/ — Screen, ViewModel, UiState, Events, components/
      domain/       — models, repository interfaces, use cases
      data/         — API services, DTOs, repository impls
    App.kt          — root composable
  androidMain/      — expect/actual: Platform, CreateHttpClient, DatabaseDriverFactory, SoundPlayer
  iosMain/          — expect/actual counterparts
```

## Platform code

Platform-specific implementations use `expect`/`actual` for:
- `CreateHttpClient` (Ktor engine)
- `DatabaseDriverFactory` (SQLDelight driver)
- `SoundPlayer`
- `Platform` / `PlatformModule` (Koin module with platform bindings)

Android entrypoint: `MainActivity.kt` (sets `enableEdgeToEdge` and calls `App()`). iOS entrypoint: `MainViewController.kt`.

## App startup flow

1. `App.kt` → `AppViewModel.resolveStartKey()` → checks if server config is saved
2. If config exists → `Login` screen; otherwise → `Onboarding` screen
3. Koin DI is initialized in `MyApplication.kt` (Android) via `startKoin { androidContext(); modules(appModules) }`

## SQLDelight

Database: `AppDatabase` in package `com.christhperalta.b1poscotizador.database`. Source files under `src/commonMain/sqldelight/`. Codegen runs as part of the normal Gradle build.

## Tests

Single file: `commonTest/.../ComposeAppCommonTest.kt` using `kotlin.test`. Run with:

```shell
./gradlew :composeApp:allTests
```

Test coverage is minimal.

## Performance & size notes

- **ProGuard/R8** is enabled in release builds (`isMinifyEnabled = true`). Keep rules in `proguard-rules.pro` for Ktor, Koin, SQLDelight, and Kotlinx Serialization.
- **Resource shrinking** enabled: `android.r8.optimizedResourceShrinking=true`
- **`android.builtInKotlin=true`** — uses the system Kotlin stdlib on Android 14+ to reduce APK size.
- **Monetary values use `Long` (cents)** throughout the domain/UI layer. Conversion from API `Double` happens at repository boundaries (DTO → domain mapping).
- **HttpClient has timeouts** configured: connect=15s, request=30s, socket=15s via `HttpTimeout` plugin on both platforms.
- **Large PNGs** in `composeResources/drawable/` (3.3 MB total: `img_1.png`, `img_2.png`, `img_3.png`, `empty_list.png`) should be converted to **WebP** for significant size savings.
