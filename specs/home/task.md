# Tareas — Home: contadores reales de activos fijos

Checklist de ejecución de [`plan.md`](./plan.md), basado en [`spec.md`](./spec.md).
Se marca `[x]` a medida que cada tarea queda hecha.

Estado: **fases 1-9 completas** (54 / 60) — la fase 10 necesita VPN al servidor SAP

---

## Fase 1 — Despejar el terreno

- [x] Borrar `feature/home/data/api/remote/HomeApiService.kt` (versión POS)
- [x] Borrar `feature/home/data/api/remote/quotationsDto/DocumentLine.kt`
- [x] Borrar `feature/home/data/api/remote/quotationsDto/QuotationsDto.kt`
- [x] Borrar `feature/home/data/api/remote/quotationsDto/RncDto.kt`
- [x] Borrar `feature/home/domain/model/Quotations.kt` y `Rnc.kt`
- [x] Borrar los 14 archivos de `feature/home/presentation/components/` (`B1PosLogo`,
      `CotizacionSectionHeader`, `DocumentTypeSelector`, `FiscalInfoScreen`, `HeaderSection`,
      `InfoItem`, `ItemsBadge`, `ManualSearchDialog`, `OutlinedActionButton`, `ProductSection`,
      `ProductSectionHeader`, `QuotationCard`, `RncInput`, `TotalBox`)
- [x] Confirmar con un grep que ningún archivo fuera de `feature/home/` referencia lo borrado

## Fase 2 — Core: excepción y recursos

- [x] Crear `core/utils/SessionExpiredException.kt`
- [x] Añadir `dialog_accept_label` = "Aceptar" a `composeResources/values/strings.xml`
- [x] Añadir `session_expired_alert_message` a `strings.xml`
- [x] Añadir el parámetro `confirmLabel` a `SessionExpiredAlertDialog` y reenviarlo a
      `CustomAlertDialog`
- [x] Verificar que el codegen de Compose Resources generó los dos strings nuevos

## Fase 3 — Dominio de home

- [x] Crear `feature/home/domain/model/AssetCountSummary.kt` con los cuatro contadores y
      `verifiedCount` / `totalAssets` / `progress` derivados
- [x] Reescribir `feature/home/domain/repository/HomeRepository.kt` con
      `getAssetCountSummary(): Result<AssetCountSummary>`

## Fase 4 — Data de home

- [x] Crear `feature/home/data/remote/api/HomeApiService.kt` con la firma
      `(client, serverConfigRepository, sessionRepository)`
- [x] Implementar `getBaseUrl()` según el patrón de `PosApiService`
- [x] Implementar el helper `count(filter)`: `GET .../Items/$count` + `parameter("$filter", …)`
      + cookie `B1SESSION`
- [x] En `count(filter)`: chequear `HttpStatusCode.Unauthorized` → `SessionExpiredException`
      **antes** del `SapException` genérico
- [x] En `count(filter)`: parsear con `bodyAsText().trim().toInt()` (no `body<T>()`)
- [x] Convertir el `IllegalStateException` de `getSession()` en `SessionExpiredException`
- [x] Añadir las cuatro funciones públicas y sus `$filter` como constantes privadas
- [x] Reescribir `feature/home/data/repository/HomeRepositoryImpl.kt` con
      `coroutineScope { async { … } }` para las cuatro llamadas en paralelo
- [x] Envolver en `try/catch` → `Result` como `LoginRepositoryImpl`

## Fase 5 — Estado y ViewModel

- [x] Reescribir `HomeUiState.kt` (`isLoading`, `summary`, `error`, `sessionExpired`)
- [x] Reescribir `HomeEvent.kt` (`Retry`, `SessionExpiredAccepted`)
- [x] Reescribir `HomeViewModel.kt` con `(HomeRepository, SessionRepository)` y carga en `init`
- [x] En el `onFailure`: `SessionExpiredException` primero (deja `error = null`), luego
      `SapException`, luego el genérico
- [x] Implementar `onEvent(Retry)` y `onEvent(SessionExpiredAccepted)` (con `clearSession()`)

## Fase 6 — Mover la UI del Home a su feature

- [x] Mover `InventoryProgressCard` a `InventoryUi.kt` como `internal` y parametrizarlo por
      `verifiedCount` / `totalAssets` / `progress`
- [x] Actualizar la llamada de `CountSummaryScreen` a la nueva firma
- [x] Crear `feature/home/presentation/components/HeroSummaryCard.kt`, recibiendo
      `AssetCountSummary` y mostrando `pendingToScanCount` con subtítulo "activos por contar"
- [x] Crear `feature/home/presentation/components/StatusGrid.kt` (incluye `StatusCard`)
- [x] En `StatusGrid`: comentar la `StatusCard` de "No encontrados" y poner
      `Spacer(Modifier.weight(1f))` en su lugar
- [x] Crear `feature/home/presentation/components/ScannedAssetsCard.kt`
- [x] Borrar de `InventoryScreens.kt`: `InventoryHomeScreen`, los cuatro composables privados
      mudados, `greetingForCurrentHour()` y `String.initials()`

## Fase 7 — HomeScreen

- [x] Reescribir `HomeScreen.kt` con `viewModel: HomeViewModel = koinViewModel()` y el callback
      `onSessionExpired`
- [x] Conservar el `Scaffold`: `TopAppBar` con iniciales, saludo por hora, nombre de usuario e
      `InventoryBottomBar`
- [x] Estado cargando: `CircularProgressIndicator` en lugar del resumen
- [x] Estado error: mensaje + botón "Reintentar" que dispara `Retry`
- [x] Estado con datos: `HeroSummaryCard` + `StatusGrid` + `InventoryProgressCard`
- [x] Mantener `InventoryRepository` inyectado solo para `scannedAssets.size`
- [x] Añadir `if (uiState.sessionExpired) SessionExpiredAlertDialog(...)` con `onConfirmation`
      que dispara `SessionExpiredAccepted` y luego `onSessionExpired()`

## Fase 8 — DI y navegación

- [x] `AppModule.kt`: actualizar el import de `HomeApiService` a la ruta nueva
- [x] Mover el binding de `HomeRepository` de `databaseModule` a `repositoryModule`
- [x] Revisar a mano que el orden de los `get()` de `viewModel { HomeViewModel(get(), get()) }`
      coincide con `(HomeRepository, SessionRepository)` — el DI es posicional y compila igual
      si está mal
- [x] `AppNavigation.kt`, rama `is Home ->`: añadir `onSessionExpired` que vacía el back stack
      y deja `Login`
- [x] **Checkpoint**: `.\gradlew.bat :composeApp:assembleDebug` pasa

## Fase 9 — Tests

- [x] `HomeRepositoryImplTest`: `totalAssets` == suma de los cuatro
- [x] `HomeRepositoryImplTest`: `verifiedCount` excluye los no escaneados y `progress` da `0f`
      con `totalAssets == 0`
- [x] `HomeRepositoryImplTest`: un `SapException` produce `Result.failure`
- [x] `HomeViewModelTest`: carga → éxito, carga → error, `Retry` recarga
- [x] `HomeViewModelTest`: `SessionExpiredException` deja `sessionExpired = true` y
      `error == null`
- [x] `HomeViewModelTest`: `SessionExpiredAccepted` llama a `clearSession()` y baja el flag
- [x] `.\gradlew.bat :composeApp:allTests` pasa

## Fase 10 — Verificación manual contra SAP

Requiere red del servidor (`10.10.10.209:50000`) y dispositivo o emulador **Android** (en iOS
falla por el certificado autofirmado).

- [ ] Configurar servidor y base de datos en la pantalla de Configuración
- [ ] Login con usuario válido
- [ ] Los cuatro contadores del Home coinciden con los `$filter` ejecutados a mano contra el
      Service Layer
- [ ] `Items/$count` devuelve el entero en texto plano (si no, aplicar el plan B dentro del
      helper `count()`)
- [ ] Sesión expirada: el modal aparece sin botón de cancelar y no se cierra al tocar fuera ni
      con el botón atrás
- [ ] "Aceptar" deja la pantalla de Login y el botón atrás no regresa a Home
