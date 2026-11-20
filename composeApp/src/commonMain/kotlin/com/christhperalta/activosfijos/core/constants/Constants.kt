package com.christhperalta.activosfijos.core.constants

object Constants {
    const val ACCESS_CODE_HASH = "3c9e5afb309bd2363144c0d4013b8cbd17f3b97be13e1ada94b7e1fac0922252"
    const val RNC_VALIDATION_URL = "http://solucionessap.solmantech.com:9091/sl/RNC"

    /**
     * Timeouts del cliente HTTP, compartidos por el `actual` de Android y el de iOS para que no
     * se desincronicen.
     *
     * Son holgados a propósito: la primera petición contra el Service Layer de SAP paga el
     * arranque en frío del servidor, y además suele ir por VPN, así que los 15 s de conexión
     * originales se agotaban antes de que el servidor llegara a responder.
     */
    const val HTTP_REQUEST_TIMEOUT_MILLIS = 60_000L
    const val HTTP_CONNECT_TIMEOUT_MILLIS = 30_000L
    const val HTTP_SOCKET_TIMEOUT_MILLIS = 60_000L
}