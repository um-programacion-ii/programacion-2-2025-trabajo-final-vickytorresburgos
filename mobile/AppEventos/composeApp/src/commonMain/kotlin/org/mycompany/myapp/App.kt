package org.mycompany.myapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

import org.mycompany.myapp.data.EventoDTO
import org.mycompany.myapp.data.AsientoVentaItem
import org.mycompany.myapp.data.AsientoSolicitudDTO
import org.mycompany.myapp.data.BloqueoRequest
import org.mycompany.myapp.data.RealizarVentaRequest

import org.mycompany.myapp.network.NetworkClient
import org.mycompany.myapp.ui.AsientoUI
import org.mycompany.myapp.ui.AsientosScreen
import org.mycompany.myapp.ui.CargaNombresScreen
import org.mycompany.myapp.ui.ConfirmacionVentaScreen
import org.mycompany.myapp.ui.EventoDetailScreen
import org.mycompany.myapp.ui.HomeScreen
import org.mycompany.myapp.ui.LoginScreen
import org.mycompany.myapp.ui.MisComprasScreen
import org.mycompany.myapp.ui.RegisterScreen
import org.mycompany.myapp.ui.VentaExitosaScreen

enum class AppScreen {
    LOGIN, REGISTER, HOME, DETAIL, ASIENTOS, CARGA_NOMBRES, CONFIRMACION, MIS_COMPRAS, VENTA_EXITOSA
}

@Composable
@Preview
fun App() {
    MaterialTheme {
        var currentScreen by remember { mutableStateOf(AppScreen.LOGIN) }
        var savedToken by remember { mutableStateOf("") }
        var selectedEvent by remember { mutableStateOf<EventoDTO?>(null) }
        var selectedSeats by remember { mutableStateOf<List<AsientoUI>>(emptyList()) }
        var ticketsToConfirm by remember { mutableStateOf<List<AsientoVentaItem>>(emptyList()) }

        val scope = rememberCoroutineScope()
        var isProcessing by remember { mutableStateOf(false) }

        if (isProcessing) {
            Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background.copy(alpha=0.5f))) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        }

        when (currentScreen) {
            AppScreen.LOGIN -> {
                LoginScreen(
                    onLoginSuccess = { token ->
                        savedToken = token
                        currentScreen = AppScreen.HOME
                    },
                    // 2. CONEXIÓN CON LA PANTALLA DE REGISTRO
                    onNavigateToRegister = {
                        currentScreen = AppScreen.REGISTER
                    }
                )
            }
            // 3. PANTALLA DE REGISTRO
            AppScreen.REGISTER -> {
                RegisterScreen(
                    onRegisterSuccess = {
                        currentScreen = AppScreen.LOGIN
                    },
                    onNavigateBack = {
                        currentScreen = AppScreen.LOGIN
                    }
                )
            }
            AppScreen.HOME -> {
                HomeScreen(
                    jwtToken = savedToken,
                    onLogout = { savedToken = ""; currentScreen = AppScreen.LOGIN },
                    onEventClick = { evento -> selectedEvent = evento; currentScreen = AppScreen.DETAIL },
                    onMisComprasClick = { currentScreen = AppScreen.MIS_COMPRAS }
                )
            }
            AppScreen.DETAIL -> {
                if (selectedEvent != null) {
                    EventoDetailScreen(
                        eventoId = selectedEvent!!.id ?: 0L,
                        jwtToken = savedToken,
                        onBack = { currentScreen = AppScreen.HOME },
                        onBuyClick = { eventoFull ->
                            selectedEvent = eventoFull
                            currentScreen = AppScreen.ASIENTOS
                        }
                    )
                }
            }

            // SELECCIÓN Y BLOQUEO
            AppScreen.ASIENTOS -> {
                if (selectedEvent != null) {
                    AsientosScreen(
                        evento = selectedEvent!!,
                        jwtToken = savedToken,
                        asientosPreseleccionados = selectedSeats,
                        onBack = { currentScreen = AppScreen.DETAIL },

                        onConfirmSelection = { asientosElegidos ->
                            scope.launch {
                                isProcessing = true
                                try {
                                    val requestItems = asientosElegidos.map {
                                        AsientoSolicitudDTO(it.fila, it.columna)
                                    }

                                    val request = BloqueoRequest(
                                        eventoId = selectedEvent!!.id!!,
                                        asientos = requestItems
                                    )

                                    println("Enviando Bloqueo: $request")

                                    val response = NetworkClient.client.post("/api/flujo/bloquear-asientos") {
                                        bearerAuth(savedToken)
                                        contentType(ContentType.Application.Json)
                                        setBody(request)
                                    }

                                    if (response.status == HttpStatusCode.OK) {
                                        selectedSeats = asientosElegidos
                                        currentScreen = AppScreen.CARGA_NOMBRES
                                    } else {
                                        println("Error Bloqueo: ${response.status}")
                                    }
                                } catch (e: Exception) {
                                    println("Error Red: ${e.message}")
                                } finally {
                                    isProcessing = false
                                }
                            }
                        }
                    )
                }
            }

            // CARGA NOMBRES
            AppScreen.CARGA_NOMBRES -> {
                if (selectedEvent != null && selectedSeats.isNotEmpty()) {
                    CargaNombresScreen(
                        evento = selectedEvent!!,
                        asientos = selectedSeats,
                        onBack = { currentScreen = AppScreen.ASIENTOS },

                        onConfirm = { listaConNombres ->
                            ticketsToConfirm = listaConNombres.map {
                                AsientoVentaItem(it.fila, it.columna, it.nombrePersona)
                            }
                            currentScreen = AppScreen.CONFIRMACION
                        }
                    )
                }
            }

            // CONFIRMACIÓN Y VENTA FINAL
            AppScreen.CONFIRMACION -> {
                if (selectedEvent != null && ticketsToConfirm.isNotEmpty()) {
                    ConfirmacionVentaScreen(
                        evento = selectedEvent!!,
                        tickets = ticketsToConfirm,
                        onBack = { currentScreen = AppScreen.CARGA_NOMBRES },
                        onConfirmPurchase = {
                            scope.launch {
                                isProcessing = true
                                try {
                                    val requestVenta = RealizarVentaRequest(
                                        eventoId = selectedEvent!!.id!!,
                                        asientos = ticketsToConfirm
                                    )

                                    println("Enviando Venta Final: $requestVenta")

                                    val response = NetworkClient.client.post("/api/flujo/realizar-venta") {
                                        bearerAuth(savedToken)
                                        contentType(ContentType.Application.Json)
                                        setBody(requestVenta)
                                    }

                                    if (response.status == HttpStatusCode.OK) {
                                        println("¡VENTA EXITOSA!")
                                        selectedSeats = emptyList()
                                        ticketsToConfirm = emptyList()

                                        currentScreen = AppScreen.VENTA_EXITOSA
                                    } else {
                                        println("Error Venta: ${response.status}")
                                    }
                                } catch (e: Exception) {
                                    println("Error Red: ${e.message}")
                                } finally {
                                    isProcessing = false
                                }
                            }
                        }
                    )
                }
            }

            // PANTALLA DE ÉXITO
            AppScreen.VENTA_EXITOSA -> {
                VentaExitosaScreen(
                    onVolverInicio = {
                        selectedEvent = null
                        currentScreen = AppScreen.HOME
                    }
                )
            }

            // MIS COMPRAS
            AppScreen.MIS_COMPRAS -> {
                MisComprasScreen(
                    jwtToken = savedToken,
                    onBack = { currentScreen = AppScreen.HOME }
                )
            }
        }
    }
}