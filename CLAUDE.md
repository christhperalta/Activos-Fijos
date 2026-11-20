# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run

```shell
# Android
.\gradlew.bat :composeApp:assembleDebug

# iOS (framework only; use Xcode or the IDE run config to deploy)
.\gradlew.bat :composeApp:iosSimulatorArm64Main

# Tests
.\gradlew.bat :composeApp:allTests
```

No formatter, linter, or CI scripts exist. No pre-commit hooks.

## What this is

Kotlin Multiplatform (KMP) + Compose Multiplatform app for **fixed-asset inventory management**. Targets Android and iOS. Single module (`:composeApp`). Clean Architecture with feature-based packages under `com.christhperalta.activosfijos`.

The core inventory flow: scan asset barcode → look up asset info → classify condition (Good / Needs Maintenance / Pending Decommission) → register result → continue scanning.

## Architecture

### Feature structure

Each feature under `feature/` follows the same three-layer layout:

```
feature/<name>/
  presentation/   — Screen composable, ViewModel, UiState, Events, components/
  domain/         — data models, repository interfaces, use cases
  data/           — API services (Ktor), DTOs, repository implementations
```

The `inventory` feature currently uses `FakeInventoryRepository` — no real backend integration yet.

### Navigation

Navigation uses JetBrains `navigation3` (experimental). All route keys are `@Serializable data object/class : NavKey` defined in `core/navigation/NavKeys.kt`. The single `AppNavigation` composable in `core/navigation/AppNavigation.kt` owns the `NavBackStack` and all screen transitions.

Navigation rules to follow:
- Never pass a `NavController`/back stack into a Screen or ViewModel. Screens receive only typed callback lambdas (`onBack`, `onConfirm`, etc.).
- Stack manipulation happens exclusively inside `AppNavigation`.
- To add a new screen: add a `NavKey` in `NavKeys.kt`, add an `is <Key> -> NavEntry(...)` branch in `AppNavigation`, wire the new screen's callbacks.

### Dependency injection

Koin. All modules declared in `di/AppModule.kt`; assembled into `appModules`. Platform-specific bindings live in `PlatformModule` (expect/actual). `InventoryRepository` is currently bound to `FakeInventoryRepository` in `repositoryModule`.

### Local persistence

SQLDelight (`AppDatabase`). Schema files in `src/commonMain/sqldelight/`. All local config (server URL, store settings, manager credentials) is read/written through `ConfigDataSource` backed by SQLDelight queries. Changes to the schema require updating `.sq` files; codegen runs with the normal Gradle build.

### Shared session state

`SessionRepository` (token) and `UserNameRepository` (display name) are Koin singletons injected into API services that need them.

## Key conventions

- **Monetary values are `Long` (cents)** everywhere in domain and UI. DTO-to-domain conversion from `Double` happens at repository boundaries only.
- **Expect/actual** for platform code: `CreateHttpClient`, `DatabaseDriverFactory`, `SoundPlayer`, `Platform`, `PlatformModule`. Implement both `androidMain` and `iosMain` when adding platform-specific behavior.
- API services receive `HttpClient`, `ServerConfigRepository`, and `SessionRepository` via constructor injection — they build the base URL dynamically from stored config at call time, not at construction.
- The `InventoryRepository` is injected directly into `AppNavigation` (via `koinInject`) to register results inline on the "Good" path; all other paths go through the ViewModel.

## App startup

`App.kt` → `AppViewModel.resolveStartKey()` checks `ServerConfigRepository` for a saved URL. If present → `Login`; otherwise → `Onboarding`. Koin is initialized in `MyApplication.kt` (Android).
