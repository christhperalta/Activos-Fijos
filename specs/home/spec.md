# Home: contadores reales de activos fijos desde SAP Service Layer

## Contexto

`feature/home/` es hoy código heredado del proyecto anterior (B1PosCotizador): su `data` y su
`domain` son de cotizaciones y validación de RNC, y `HomeUiState` / `HomeEvent` /
`HomeViewModel` modelan un carrito de ventas. Nada de eso corresponde a una app de inventario
de activos fijos. Peor: `HomeViewModel` está registrado en Koin pero **no lo usa nadie**, y
`HomeScreen` solo delega en `InventoryHomeScreen`, que vive en otra feature y se alimenta de
`FakeInventoryRepository` con datos inventados.

Este spec hace dos cosas a la vez:

1. **Reescribe `feature/home` completa** — data, domain y presentation — para que la feature
   sea realmente la del Home de activos fijos, respetando la arquitectura por features del
   proyecto (cada feature dueña de sus tres capas).
2. **Conecta el Home a SAP** con cuatro endpoints `$count`, de modo que las tarjetas muestren
   el estado real del inventario en lugar de datos falsos.

Y como el `B1SESSION` vive solo en memoria y caduca, el Home debe reaccionar a una sesión
expirada avisando al usuario y devolviéndolo al Login en lugar de dejar un error opaco.

### Por qué se puede borrar el código heredado sin miedo

Está completamente aislado. Fuera de `feature/home/`, lo único que lo referencia es:

- `core/navigation/AppNavigation.kt:21` — importa `HomeScreen`.
- `di/AppModule.kt:21-24` — importa `HomeApiService`, `HomeRepositoryImpl`, `HomeRepository`,
  `HomeViewModel`.

Los 14 componentes de `feature/home/presentation/components/` (`B1PosLogo`, `QuotationCard`,
`RncInput`, `DocumentTypeSelector`, `FiscalInfoScreen`, `TotalBox`, …) y todos los símbolos POS
(`QuotationResult`, `CreateQuotationParams`, `RncValidationStatus`, `DocumentType`,
`Quotations`, `Rnc`) se usan **solo dentro de `feature/home`**. Ninguna otra feature los toca.

## Endpoints

Todos son `GET` sobre la URL base guardada en configuración, devuelven **texto plano con un
entero** (no JSON), y requieren la cookie de sesión `B1SESSION`.

| Métrica | Path + query |
|---|---|
| Activos por contar (no escaneados) | `/b1s/v1/Items/$count?$filter=ItemType eq 'itFixedAssets' and U_FechaEscaneo eq null` |
| En buen estado | `/b1s/v1/Items/$count?$filter=ItemType eq 'itFixedAssets' and U_EstatusEscaneo eq 'BUEN_ESTADO'` |
| En mantenimiento | `/b1s/v1/Items/$count?$filter=ItemType eq 'itFixedAssets' and U_EstatusEscaneo eq 'MANTENIMIENTO'` |
| Pendientes de baja | `/b1s/v1/Items/$count?$filter=ItemType eq 'itFixedAssets' and U_EstatusEscaneo eq 'PENDIENTE_BAJA'` |

Decisiones tomadas:

- **Pantalla**: Home. El contador de `U_FechaEscaneo eq null` alimenta la tarjeta
  **"ACTIVOS A CONTAR"** (`HeroSummaryCard`).
- **Total**: `totalAssets` = suma de los cuatro contadores, derivado en el modelo.
- **"No encontrados"**: la `StatusCard` se **comenta** (no se usa por ahora); no hay endpoint
  que la alimente.
- **Error de red / SAP**: mensaje en pantalla con botón de reintento. **Sin** re-login
  silencioso.
- **Sesión expirada**: caso aparte. Modal "Sesión expirada" con un único botón **Aceptar** que
  devuelve al Login. Detalle en la sección 4.
- **Nombres**: se conservan `HomeApiService`, `HomeRepository`, `HomeUiState`, `HomeEvent`,
  `HomeViewModel`; se reescribe su contenido.
- **Modelo propio**: `feature/home` no depende del dominio de `feature/inventory`; define su
  `AssetCountSummary`.

## Patrón a reutilizar (no reinventar)

- `feature/login/data/remote/api/UserApiService.kt` — plantilla exacta de un servicio API:
  `getBaseUrl()` desde `ServerConfigRepository` (`"${protocol}://${host}:${port}"`),
  `client.get/post`, chequeo `response.status.isSuccess()`, `SapErrorResponse` →
  `SapException`.
- El `HomeApiService` actual, línea 57 — cómo se adjunta la sesión:
  `header("Cookie", "B1SESSION=${sessionRepository.getSession()}")`. Es lo único que vale la
  pena conservar del archivo.
- `core/utils/SapException.kt` — `SapException`, `SapErrorResponse`.
- `feature/login/data/repository/LoginRepositoryImpl.kt` — conversión `try/catch` →
  `Result.success/failure`.
- `core/presentation/SessionExpiredAlertDialog.kt` — **ya existe y nadie lo usa**: envuelve
  `CustomAlertDialog` con `enableBottomDismiss = false` y `onDismissRequest = { }`, o sea un
  modal no descartable con un solo botón. Es exactamente el modal que hace falta.
- `composeResources/values/strings.xml:206` — `session_expired_alert_title` = "Sesión
  expirada", ya definido.
- `feature/inventory/presentation/InventoryUi.kt` — `InventoryCardShape`,
  `InventorySurfaceCard`, `InventoryIconTile`, `InventoryBottomBar`, `conditionColor`,
  `conditionContainer`. Son `internal`, o sea accesibles desde `feature/home` (mismo módulo).
  El Home sigue usándolos: duplicar la paleta garantizaría que las dos features se
  desincronicen.
- `createHttpClient()` (`androidMain`/`iosMain`) — cliente único inyectado por Koin, ya con
  trust-all en Android para el certificado autofirmado del servidor SAP.

**Diferencia clave con el resto de servicios**: `$count` devuelve `text/plain`, no JSON. Hay
que leerlo con `response.bodyAsText().trim().toInt()`, **no** con `response.body<T>()`
(`ContentNegotiation` fallaría). Los `$filter` se pasan como `parameter("$filter", "...")`
para que Ktor codifique los espacios y comillas; `$count` va en el path.

## Estructura final de `feature/home`

```
feature/home/
  data/
    remote/api/HomeApiService.kt          REESCRITO — 4 llamadas $count
    repository/HomeRepositoryImpl.kt      REESCRITO
  domain/
    model/AssetCountSummary.kt            NUEVO
    repository/HomeRepository.kt          REESCRITO
  presentation/
    HomeScreen.kt                         REESCRITO — dueño del HomeViewModel y de la UI
    HomeViewModel.kt                      REESCRITO
    HomeUiState.kt                        REESCRITO
    HomeEvent.kt                          REESCRITO
    components/
      HeroSummaryCard.kt                  MOVIDO desde InventoryScreens.kt
      StatusGrid.kt                       MOVIDO (incluye StatusCard)
      ScannedAssetsCard.kt                MOVIDO
```

**Se borra** (todo POS, sin usuarios externos):

- `data/api/remote/` completo: el `HomeApiService` viejo y
  `quotationsDto/{DocumentLine,QuotationsDto,RncDto}.kt`.
- `domain/model/{Quotations,Rnc}.kt`.
- Los 14 archivos de `presentation/components/`.
- El contenido POS de `HomeUiState.kt` (`QuotationResult`, `CreateQuotationParams`,
  `RncValidationStatus`, `DocumentType`, `Tax_Credit`) y de `HomeEvent.kt`.

Nota de rutas: el servicio pasa de `data/api/remote/` a `data/remote/api/`, que es la
convención que ya usan `login` y `product`.

## Cambios

### 1. Dominio

- Nuevo `feature/home/domain/model/AssetCountSummary.kt`:
  ```kotlin
  data class AssetCountSummary(
      val pendingToScanCount: Int,
      val goodCount: Int,
      val maintenanceCount: Int,
      val pendingDecommissionCount: Int,
  ) {
      val verifiedCount: Int
          get() = goodCount + maintenanceCount + pendingDecommissionCount

      val totalAssets: Int
          get() = pendingToScanCount + verifiedCount

      val progress: Float
          get() = if (totalAssets == 0) 0f else verifiedCount.toFloat() / totalAssets
  }
  ```
  Un campo por endpoint y el resto derivado. No hay `notFoundCount`: no existe endpoint que lo
  alimente, y un campo siempre en `0` solo invita a confusión.
- `feature/home/domain/repository/HomeRepository.kt` reescrito:
  ```kotlin
  interface HomeRepository {
      suspend fun getAssetCountSummary(): Result<AssetCountSummary>
  }
  ```
- Nuevo `core/utils/SessionExpiredException.kt` (junto a `SapException.kt`, porque no es
  específico de una feature):
  ```kotlin
  class SessionExpiredException(
      errorMessage: String? = null,
  ) : Exception(errorMessage ?: "La sesión ha expirado")
  ```

### 2. Data

- `feature/home/data/remote/api/HomeApiService.kt` reescrito —
  `HomeApiService(client, serverConfigRepository, sessionRepository)` (la misma firma de hoy,
  así que la línea de Koin no cambia):
  - `getBaseUrl()` copiado del patrón de `PosApiService`.
  - `private suspend fun count(filter: String): Int` — único helper que hace el `GET` a
    `$baseUrl/b1s/v1/Items/\$count` con `parameter("\$filter", filter)` y la cookie
    `B1SESSION`.
    - Si `response.status == HttpStatusCode.Unauthorized` (401, lo que devuelve el Service
      Layer cuando el `B1SESSION` ya no vale) → `SessionExpiredException` con el mensaje de
      `SapErrorResponse` si viene. **Se evalúa antes** que el `SapException` genérico, para que
      el 401 no quede tapado.
    - Si `!isSuccess()` → `SapException` desde `SapErrorResponse`.
    - Si no, `bodyAsText().trim().toInt()`.
  - Cuatro funciones públicas: `countPendingToScan()`, `countGoodCondition()`,
    `countMaintenance()`, `countPendingDecommission()`.
  - Los cuatro `$filter` como constantes privadas del propio servicio.
  - El `IllegalStateException` de `sessionRepository.getSession()` ("No hay sesión activa") se
    convierte también en `SessionExpiredException`: es el mismo caso de usuario y hoy
    ocurriría si el proceso se reinicia estando en Home.
- `feature/home/data/remote/api/AssetCountApi.kt` nuevo — interfaz con las cuatro funciones de
  conteo, que `HomeApiService` implementa.

  **Desviación respecto al plan original, decidida al escribir los tests**: el plan hacía que
  `HomeRepositoryImpl` dependiera de la clase concreta `HomeApiService`, y eso lo vuelve
  imposible de probar. El proyecto no tiene librería de mocking ni `ktor-client-mock`, y
  `HomeApiService` necesita un `HttpClient` real en su constructor, así que un doble tendría que
  levantar un cliente HTTP de verdad. Con el contrato aparte, `HomeRepositoryImplTest` usa un
  doble en memoria. Coste: una interfaz más y una línea de Koin que pasa a
  `single<AssetCountApi> { HomeApiService(...) }`.
- `feature/home/data/repository/HomeRepositoryImpl.kt` reescrito — `HomeRepositoryImpl(apiService: AssetCountApi)`;
  lanza las cuatro peticiones **en paralelo** con `coroutineScope { async { ... } }`, arma el
  `AssetCountSummary` y envuelve todo en `try/catch` → `Result`, igual que
  `LoginRepositoryImpl`. No hay DTO ni mapper: la respuesta es un entero.

### 3. Presentación

- `HomeUiState.kt` reescrito:
  ```kotlin
  data class HomeUiState(
      val isLoading: Boolean = false,
      val summary: AssetCountSummary? = null,
      val error: String? = null,
      val sessionExpired: Boolean = false,
  )
  ```
  `sessionExpired` es un campo aparte de `error`: cuando es `true` se muestra el modal, no el
  mensaje en línea con botón de reintento.
- `HomeEvent.kt` reescrito: `data object Retry`, `data object SessionExpiredAccepted`.
- `HomeViewModel.kt` reescrito — `HomeViewModel(repository: HomeRepository, sessionRepository: SessionRepository)`:
  `StateFlow<HomeUiState>`, carga en `init`, `onEvent(Retry)` recarga. Formato de error
  idéntico al de `LoginViewModel`: `SapException` →
  `"Error de SAP: ${code} - ${errorMessage}"`, resto → `"Error de conexión: ${message}"`.
  **Primera rama** del `onFailure`: `SessionExpiredException` →
  `copy(isLoading = false, sessionExpired = true, error = null)`, sin tocar `error`, para que
  la UI no muestre modal y mensaje a la vez. `onEvent(SessionExpiredAccepted)` llama a
  `sessionRepository.clearSession()` y baja el flag, de modo que al volver el usuario a Home
  tras loguearse el estado arranque limpio.
- `HomeScreen.kt` reescrito — pasa de delegar en otra feature a ser la pantalla de verdad:
  ```kotlin
  @Composable
  fun HomeScreen(
      onScanner: () -> Unit,
      onSummary: () -> Unit,
      onAssets: () -> Unit,
      onSessionExpired: () -> Unit,
      viewModel: HomeViewModel = koinViewModel(),
      inventoryRepository: InventoryRepository = koinInject(),
      userNameRepository: UserNameRepository = koinInject(),
  )
  ```
  - Mantiene el `Scaffold` actual de `InventoryHomeScreen`: `TopAppBar` con iniciales, saludo
    por hora y nombre de usuario (`runCatching` sobre `UserNameRepository`), y
    `InventoryBottomBar` como barra inferior.
  - El bloque de resumen depende del `uiState`: mientras `isLoading`, un
    `CircularProgressIndicator` en lugar de `HeroSummaryCard` + `StatusGrid`; si `error != null`,
    mensaje con botón "Reintentar" que dispara `Retry`; si hay datos, `HeroSummaryCard` +
    `StatusGrid` + `InventoryProgressCard`.
  - `inventoryRepository` se mantiene **solo** para `scannedAssets.size` en la
    `ScannedAssetsCard`: sigue siendo `FakeInventoryRepository` porque no hay endpoint de
    activos escaneados en este spec.
- Componentes movidos a `feature/home/presentation/components/`, desde
  `feature/inventory/presentation/InventoryScreens.kt` (donde hoy son `private` y los usa
  **solo** el Home):
  - `HeroSummaryCard` (línea ~521) — pasa a recibir `AssetCountSummary` y a mostrar
    `summary.pendingToScanCount`, con el subtítulo "activos por contar" en lugar de "activos
    registrados".
  - `StatusGrid` + `StatusCard` (líneas ~558 y ~587) — la `StatusCard` de "No encontrados"
    queda **comentada** y "Pendiente de baja" se queda sola en su fila con un
    `Spacer(Modifier.weight(1f))` al lado, para conservar la rejilla y poder restaurarla sin
    rehacer el layout. Siguen usando `conditionColor`/`conditionContainer` y `AssetCondition`.
  - `ScannedAssetsCard` (línea ~615).
- **Se queda en `feature/inventory`**: `InventoryProgressCard` (línea ~633), porque también lo
  usa `CountSummaryScreen` (línea ~455). Pasa de `private` a `internal` y se mueve a
  `InventoryUi.kt`, junto al resto de lo compartido. Recibirá el `AssetCountSummary` del Home,
  así que hay que parametrizarlo por los valores que pinta (`verifiedCount`, `totalAssets`,
  `progress`) en vez de por el tipo concreto.
- `InventoryHomeScreen` se elimina de `InventoryScreens.kt`, junto con los cuatro composables
  privados que se mudan y los helpers `greetingForCurrentHour()` / `String.initials()`, que se
  van con la pantalla.

**Acoplamiento aceptado y documentado**: la presentación del Home usa `AssetCondition` y
`conditionColor`/`conditionContainer` de `feature/inventory`. Son el vocabulario de colores de
toda la app; extraerlos a `core` es la continuación limpia, pero toca muchos archivos de
inventory y queda fuera de este spec.

### 4. Sesión expirada (modal → Login)

Comportamiento pedido: si la sesión expira, aparece un modal que avisa que la sesión expiró y
que hay que volver a iniciar sesión; tiene un solo botón **Aceptar**; al pulsarlo el usuario
vuelve a la pantalla de Login.

**Modal — reutilizar `SessionExpiredAlertDialog`, no crear uno nuevo.** Ya cumple lo
necesario: título `Res.string.session_expired_alert_title`, icono `Icons.Outlined.AddAlert`,
`enableBottomDismiss = false` (no hay botón de cancelar) y `onDismissRequest = { }` (tocar
fuera o el botón atrás no lo cierra, así que la única salida es Aceptar). Dos ajustes:

- El botón de confirmación hoy sale de `dialog_confirm_label` = "Confirmar". Añadir
  `<string name="dialog_accept_label">Aceptar</string>` en
  `composeResources/values/strings.xml` y exponer un parámetro `confirmLabel` en
  `SessionExpiredAlertDialog` que lo reenvíe a `CustomAlertDialog` (que ya lo acepta), con
  `dialog_accept_label` por defecto.
- Añadir `<string name="session_expired_alert_message">Tu sesión ha expirado. Debes iniciar
  sesión de nuevo.</string>` y pasarlo como `errorMessage`, en lugar de dejar el texto vacío.
  Nota: ese texto se pinta con `colorScheme.error`; si se prefiere tono neutro para un mensaje
  informativo, añadir un parámetro de color con el `error` actual por defecto.

**Dónde se muestra.** En `HomeScreen`, dentro del mismo `Scaffold` y después del `Column` de
contenido:

```kotlin
if (uiState.sessionExpired) {
    SessionExpiredAlertDialog(
        onConfirmation = {
            viewModel.onEvent(HomeEvent.SessionExpiredAccepted)
            onSessionExpired()
        },
        errorMessage = stringResource(Res.string.session_expired_alert_message),
    )
}
```

**Navegación.** En `core/navigation/AppNavigation.kt`, rama `is Home ->` (línea ~121), añadir
el callback nuevo:

```kotlin
onSessionExpired = {
    while (backStack.isNotEmpty()) backStack.removeLastOrNull()
    backStack.add(Login)
},
```

Se vacía el back stack y `Login` queda como única entrada, para que el botón atrás no devuelva
a un Home sin sesión. Es el mismo gesto que ya hacen la rama `is Config ->` y la de línea ~211
(`removeLastOrNull()` + `add(Login)`), pero limpiando toda la pila. No se añade ningún `NavKey`,
así que `NavKeys.kt` y `NavConfig.kt` no se tocan. El `clearSession()` lo hace el ViewModel,
no la capa de navegación.

**Alcance.** La detección vive en `HomeApiService`. Los otros cuatro servicios
(`PosApiService`, `ProductApiService`, `StoreApiService`, `PdfApiService`) siguen sin ella;
generalizarla (un plugin de Ktor o un interceptor compartido) queda fuera de este spec.

### 5. DI (`di/AppModule.kt`)

- `networkModule`: `single<AssetCountApi> { HomeApiService(get(), get(), get()) }` — la firma del
  servicio no cambia, pero se registra por su interfaz (ver la desviación en la sección 2).
- `HomeRepository` está hoy mal colocado, en `databaseModule`. Mover su binding a
  `repositoryModule`: `single<HomeRepository> { HomeRepositoryImpl(get()) }`.
- `viewModelModule`: `viewModel { HomeViewModel(get(), get()) }` — ahora resuelve
  `(HomeRepository, SessionRepository)` en lugar de `(GetStoreConfigUseCase, HomeRepository)`.
  Ojo: el DI del proyecto es posicional, el orden debe coincidir con el constructor.
- `FakeInventoryRepository` sigue bindeado igual: cubre `assets`, `scannedAssets` y el flujo de
  escaneo.

### 6. Fuera de alcance

- `CountSummaryScreen` (`feature/inventory`) sigue leyendo el `InventorySummary` de
  `FakeInventoryRepository`. O sea que tras este cambio el Home muestra datos reales y la
  pantalla de resumen datos falsos: inconsistencia conocida, se resuelve cuando esa pantalla
  tenga su propio endpoint.
- `InventorySummary` se queda en `feature/inventory/domain` para el flujo de escaneo.
- No se registran escaneos contra SAP (escritura de `U_FechaEscaneo` / `U_EstatusEscaneo`).
- No se añade re-login automático ni persistencia de `B1SESSION`: al expirar la sesión el
  usuario vuelve al Login y escribe sus credenciales.
- La detección de sesión expirada no se generaliza al resto de servicios API.
- Quedan huérfanos y no se tocan: `Constants.RNC_VALIDATION_URL` (solo lo usaba el
  `validateRNC` que se borra) y `core/quotation/QuotationCart` con su `quotationModule`. Vale
  la pena una limpieza aparte.

## Verificación

1. Compilar: `.\gradlew.bat :composeApp:assembleDebug`. Es la comprobación más valiosa del
   borrado: si algo externo dependía del código POS, falla aquí.
2. Tests existentes: `.\gradlew.bat :composeApp:allTests`. Los tests de `feature/login` no se
   tocan; no hay tests del `feature/home` viejo que haya que borrar.
3. Nuevos tests unitarios en `composeApp/src/commonTest/.../feature/home/`, siguiendo
   `LoginViewModelTest.kt`:
   - `HomeRepositoryImplTest` con un fake del servicio: `totalAssets` == suma de los cuatro,
     `verifiedCount` excluye los no escaneados, `progress` con `totalAssets == 0` da `0f`, y un
     `SapException` produce `Result.failure`.
   - `HomeViewModelTest`: carga → éxito, carga → error, `Retry` recarga; un
     `SessionExpiredException` deja `sessionExpired = true` **y** `error == null`; y
     `SessionExpiredAccepted` llama a `clearSession()` y baja el flag.
4. Prueba manual en dispositivo/emulador Android en la red del servidor SAP
   (`10.10.10.209:50000`, HTTPS autofirmado — funciona en Android, **no** en iOS por el
   `handleChallenge` por defecto en `CreateHttpClient.ios.kt`):
   - Configurar servidor y base de datos en la pantalla de Configuración.
   - Login con usuario válido (necesario: el `B1SESSION` vive solo en memoria).
   - Al llegar a Home, los cuatro contadores deben coincidir con los mismos `$filter`
     ejecutados a mano contra el Service Layer.
5. Prueba manual de sesión expirada: con la app en Home, invalidar la sesión en el servidor
   (`POST /b1s/v1/Logout` con el mismo `B1SESSION`, o esperar el `SessionTimeout`) y pulsar
   "Reintentar". Debe aparecer el modal "Sesión expirada" sin botón de cancelar, no debe
   cerrarse al tocar fuera ni con el botón atrás del sistema, y "Aceptar" debe dejar la
   pantalla de Login sin que atrás regrese a Home.
6. Comprobar en la respuesta cruda que el servidor devuelve el entero en texto plano para
   `Items/$count`; si esa versión del Service Layer no soportara `$count`, el plan B es
   `?$select=ItemCode&$inlinecount=allpages` (o `$filter` + contar `value`) dentro del mismo
   `count()` helper — un único punto a cambiar.
