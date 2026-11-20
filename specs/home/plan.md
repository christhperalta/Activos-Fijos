# Plan de implementación — Home: contadores reales de activos fijos

Plan de ejecución de [`spec.md`](./spec.md). El seguimiento tarea por tarea va en
[`task.md`](./task.md).

> **Estado: fases 1-9 completas.** `assembleDebug` y los 51 tests unitarios pasan (16 nuevos del
> Home). Queda la fase 10, la verificación manual contra SAP, que necesita VPN al servidor.
>
> **Una desviación durante la ejecución**: se añadió la interfaz `AssetCountApi` para poder
> testear `HomeRepositoryImpl`, que en el plan dependía de la clase concreta `HomeApiService`.
> El motivo está en la sección 2 del spec.

## Principio de orden

La reescritura de `feature/home` rompe la compilación a mitad de camino si se hace en mal
orden: `HomeViewModel` depende de `HomeRepository`, que depende de `HomeApiService`, y Koin
los cablea a los tres por posición. Así que el orden es **de adentro hacia afuera** —
core → domain → data → presentación → DI → navegación — y se deja para el final todo lo que
solo puede verificarse con el proyecto compilando.

Dos excepciones que van primero porque son independientes de todo lo demás:

- **El borrado del código POS.** Los 14 componentes de cotización y los DTOs de `quotationsDto`
  no los usa nadie fuera de `feature/home`, y el `HomeScreen` actual tampoco los toca
  (solo delega en `InventoryHomeScreen`). Se pueden borrar en el paso 1 sin romper nada, y eso
  deja el terreno despejado: menos ruido en los greps y en los errores del compilador durante
  el resto del trabajo.
- **Los recursos de strings.** Añadirlos temprano permite que el codegen de Compose Resources
  genere `Res.string.dialog_accept_label` y `session_expired_alert_message` antes de que el
  código los referencie, evitando un error de "unresolved reference" confuso.

## Fases

### Fase 1 — Despejar el terreno

Borrar el código heredado del proyecto POS y verificar con una compilación que nada externo
dependía de él. Este es el único punto donde el borrado se puede comprobar aislado: si falla
aquí, el problema es el borrado y nada más.

- Borrar `feature/home/data/api/remote/` completo (el `HomeApiService` viejo y
  `quotationsDto/{DocumentLine,QuotationsDto,RncDto}.kt`).
- Borrar `feature/home/domain/model/{Quotations,Rnc}.kt`.
- Borrar los 14 archivos de `feature/home/presentation/components/`.
- Vaciar `HomeUiState.kt` y `HomeEvent.kt` de contenido POS, y `HomeRepository.kt` /
  `HomeRepositoryImpl.kt` / `HomeViewModel.kt` de sus cuerpos actuales.

Quedará roto temporalmente: `AppModule.kt` (4 imports) y, si se vacían los archivos en lugar de
dejarlos como esqueletos, las referencias cruzadas dentro de la propia feature. Es esperado; se
resuelve en las fases 2-7. **Checkpoint de compilación: no.**

### Fase 2 — Core: excepción y recursos

Lo compartido, que no depende de nada de `feature/home`.

- `core/utils/SessionExpiredException.kt` nuevo.
- `strings.xml`: `dialog_accept_label` = "Aceptar" y `session_expired_alert_message`.
- `SessionExpiredAlertDialog`: añadir el parámetro `confirmLabel` con
  `stringResource(Res.string.dialog_accept_label)` por defecto y reenviarlo a
  `CustomAlertDialog`, que ya lo acepta.

**Checkpoint de compilación: no** (el resto de la feature sigue a medias), pero sí conviene
confirmar que el codegen generó los dos strings nuevos.

### Fase 3 — Dominio de home

- `feature/home/domain/model/AssetCountSummary.kt` — cuatro campos, uno por endpoint, y
  `verifiedCount` / `totalAssets` / `progress` derivados.
- `feature/home/domain/repository/HomeRepository.kt` — `suspend fun getAssetCountSummary(): Result<AssetCountSummary>`.

### Fase 4 — Data de home

- `feature/home/data/remote/api/HomeApiService.kt` (ruta nueva, la convención de `login` y
  `product`). Firma idéntica a la de hoy: `(client, serverConfigRepository, sessionRepository)`,
  así que la línea de Koin en `networkModule` **no cambia**.
  - El helper `count(filter)` es el corazón: un solo sitio donde viven la URL, la cookie, el
    orden de los chequeos de error y el parseo de texto plano.
  - Orden de chequeos, que importa: primero `HttpStatusCode.Unauthorized` →
    `SessionExpiredException`; después `!isSuccess()` → `SapException`; al final
    `bodyAsText().trim().toInt()`.
  - Envolver `sessionRepository.getSession()` para que su `IllegalStateException` también se
    convierta en `SessionExpiredException`.
- `feature/home/data/repository/HomeRepositoryImpl.kt` — `coroutineScope { async { ... } }` para
  las cuatro llamadas en paralelo, y `try/catch` → `Result` como `LoginRepositoryImpl`.

### Fase 5 — Estado y ViewModel

- `HomeUiState.kt` — `isLoading`, `summary`, `error`, `sessionExpired`.
- `HomeEvent.kt` — `Retry`, `SessionExpiredAccepted`.
- `HomeViewModel.kt` — `(HomeRepository, SessionRepository)`; carga en `init`; en el
  `onFailure`, `SessionExpiredException` **primero** (pone `sessionExpired = true` y deja
  `error = null`), luego `SapException`, luego el genérico.

### Fase 6 — Mover la UI del Home a su feature

La fase con más riesgo de romper cosas ajenas, porque toca `InventoryScreens.kt` (1166 líneas,
compartido con 10 pantallas). Orden dentro de la fase:

1. Mover `InventoryProgressCard` a `InventoryUi.kt` como `internal` y parametrizarlo por los
   valores que pinta (`verifiedCount`, `totalAssets`, `progress`) en vez de por
   `InventorySummary`. Esto es lo que permite que lo usen el Home (con `AssetCountSummary`) y
   `CountSummaryScreen` (con `InventorySummary`) sin que ninguna feature importe el dominio de
   la otra. **Hacerlo primero**, porque `CountSummaryScreen` lo usa y conviene arreglar su
   llamada en el mismo movimiento.
2. Crear `feature/home/presentation/components/` con `HeroSummaryCard.kt`, `StatusGrid.kt`
   (incluye `StatusCard`) y `ScannedAssetsCard.kt`, copiados de `InventoryScreens.kt`.
   - `HeroSummaryCard` pasa a recibir `AssetCountSummary` y a mostrar `pendingToScanCount`,
     con el subtítulo "activos por contar".
   - En `StatusGrid`, comentar la `StatusCard` de "No encontrados" y poner
     `Spacer(Modifier.weight(1f))` en su lugar, para que la rejilla no se descuadre y
     restaurarla sea descomentar.
3. Borrar de `InventoryScreens.kt`: `InventoryHomeScreen`, los cuatro composables privados que
   se mudaron y los helpers `greetingForCurrentHour()` / `String.initials()`.

### Fase 7 — HomeScreen

- Reescribir `HomeScreen.kt`: dueño del `HomeViewModel` vía `koinViewModel()` — el que
  corresponde, porque el ViewModel está registrado con el DSL `viewModel { }` de Koin; el
  `koinInject()` que usan `LoginScreen`, `PdfScreen` y `ScannerScreen` funciona pero se salta
  el scoping de ciclo de vida.
- Conserva el `Scaffold` que hoy tiene `InventoryHomeScreen`: `TopAppBar` con iniciales, saludo
  por hora y nombre de usuario, e `InventoryBottomBar`.
- Tres estados en el bloque de resumen: cargando, error con botón "Reintentar", y datos.
- `InventoryRepository` sigue inyectado **solo** para `scannedAssets.size`.
- El modal: `if (uiState.sessionExpired) SessionExpiredAlertDialog(...)` con
  `onConfirmation` que dispara `SessionExpiredAccepted` y luego `onSessionExpired()`.

### Fase 8 — DI y navegación

Lo último del código, porque cierra el circuito y es lo que hace compilable todo lo anterior.

- `AppModule.kt`:
  - `networkModule` — la línea de `HomeApiService` no cambia; sí el import, por la ruta nueva.
  - Mover el binding de `HomeRepository` de `databaseModule` a `repositoryModule`, donde
    corresponde.
  - `viewModelModule` — `viewModel { HomeViewModel(get(), get()) }` se queda **igual escrito**;
    lo que cambia es qué resuelve (`HomeRepository`, `SessionRepository` en lugar de
    `GetStoreConfigUseCase`, `HomeRepository`). Es la trampa de este cambio: el DI es
    posicional y la línea compila igual aunque el orden esté mal, así que hay que revisar el
    constructor a mano.
- `AppNavigation.kt`, rama `is Home ->`: añadir `onSessionExpired` que vacía el back stack y
  deja `Login`. No se añade ningún `NavKey`, así que `NavKeys.kt` y `NavConfig.kt` no se tocan.

**Checkpoint de compilación: sí.** `.\gradlew.bat :composeApp:assembleDebug` debe pasar aquí.
Es el primer punto donde el proyecto vuelve a estar entero.

### Fase 9 — Tests

Nuevos tests en `composeApp/src/commonTest/.../feature/home/`, siguiendo `LoginViewModelTest.kt`
y `LogInUserUseCaseTest.kt` (que ya usan fakes manuales, sin librería de mocking):

- `HomeRepositoryImplTest` — `totalAssets` == suma de los cuatro; `verifiedCount` excluye los no
  escaneados; `progress` da `0f` cuando `totalAssets == 0`; un `SapException` produce
  `Result.failure`.
- `HomeViewModelTest` — carga → éxito; carga → error; `Retry` recarga; un
  `SessionExpiredException` deja `sessionExpired = true` **y** `error == null`;
  `SessionExpiredAccepted` llama a `clearSession()` y baja el flag.

`.\gradlew.bat :composeApp:allTests`.

### Fase 10 — Verificación manual contra SAP

Lo único que no se puede automatizar aquí, y lo que de verdad valida el spec. Requiere estar en
la red del servidor (`10.10.10.209:50000`) y un dispositivo o emulador **Android** — en iOS
falla por el `handleChallenge` por defecto de `CreateHttpClient.ios.kt`, que no acepta el
certificado autofirmado.

1. Configurar servidor y base de datos en la pantalla de Configuración.
2. Login con usuario válido (obligatorio: el `B1SESSION` vive solo en memoria).
3. Comparar los cuatro contadores del Home con los mismos `$filter` ejecutados a mano contra el
   Service Layer.
4. Confirmar en la respuesta cruda que `Items/$count` devuelve el entero en texto plano. Si esa
   versión del Service Layer no lo soportara, el plan B (`$inlinecount=allpages` o contar
   `value`) se aplica dentro del helper `count()`: un único punto a cambiar.
5. Sesión expirada: invalidarla en el servidor (`POST /b1s/v1/Logout` con el mismo `B1SESSION`,
   o esperar el `SessionTimeout`) y pulsar "Reintentar". El modal debe aparecer sin botón de
   cancelar, no cerrarse al tocar fuera ni con el botón atrás, y "Aceptar" debe dejar el Login
   sin que atrás regrese a Home.

## Riesgos y cómo se mitigan

| Riesgo | Mitigación |
|---|---|
| El DI posicional acepta un orden de `get()` equivocado y compila igual | Revisar el constructor de `HomeViewModel` a mano al cerrar la fase 8; el `HomeViewModelTest` lo cazaría en tiempo de test, no de compilación |
| `Items/$count` no soportado por esa versión del Service Layer | Toda la URL vive en un solo helper `count()`; el plan B es un cambio local |
| Borrar de más en `InventoryScreens.kt` y romper otra pantalla | La fase 6 toca ese archivo sola, y el checkpoint de la fase 8 lo verifica; `InventoryProgressCard` es el único composable del Home que otra pantalla reutiliza, y se mueve en lugar de borrarse |
| El 401 queda tapado como error genérico | El orden de chequeos en `count()` es explícito en el spec y lo cubre `HomeViewModelTest` |

## Lo que este plan deja sin resolver, a propósito

Está en la sección 6 del spec, pero vale repetir lo que quedará visible para el usuario de la
app: el Home mostrará datos reales mientras `CountSummaryScreen` sigue mostrando los de
`FakeInventoryRepository`. La detección de sesión expirada solo vivirá en `HomeApiService`, así
que una sesión caducada en el escáner o en configuración seguirá dando un error genérico. Y
quedarán huérfanos `Constants.RNC_VALIDATION_URL` y `core/quotation/QuotationCart` con su
`quotationModule`.

## Commits sugeridos

Siguiendo el estilo del historial (`feat(inventory): …`, `fix(navigation): …`):

1. `refactor(home): remove legacy POS quotation code` — fase 1.
2. `feat(core): add session expired exception and dialog labels` — fase 2.
3. `feat(home): add asset count domain and SAP count service` — fases 3-4.
4. `feat(home): add asset count view model` — fase 5.
5. `refactor(home): move home UI into its own feature` — fases 6-7.
6. `feat(home): wire asset counts and session expiry` — fase 8.
7. `test(home): cover asset count repository and view model` — fase 9.
