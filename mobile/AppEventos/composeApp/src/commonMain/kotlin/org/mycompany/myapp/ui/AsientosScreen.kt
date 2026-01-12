package org.mycompany.myapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.mycompany.myapp.data.EventoDTO
import org.mycompany.myapp.data.MapaAsientosResponse
import org.mycompany.myapp.network.NetworkClient


@Serializable
data class AsientoUI(val fila: Int, val columna: Int)

enum class AsientoEstado { LIBRE, TU_SELECCION, VENDIDO, BLOQUEADO }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsientosScreen(
    evento: EventoDTO,
    jwtToken: String,
    asientosPreseleccionados: List<AsientoUI> = emptyList(),
    onBack: () -> Unit,
    onConfirmSelection: (List<AsientoUI>) -> Unit
) {
    var mapaOcupados by remember { mutableStateOf<Map<AsientoUI, AsientoEstado>>(emptyMap()) }

    var seleccionados by remember { mutableStateOf(asientosPreseleccionados) }

    var isLoading by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    val jsonParser = remember {
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }

    LaunchedEffect(evento.id) {
        try {
            val response = NetworkClient.client.get("/api/eventos/${evento.id}/asientos") {
                bearerAuth(jwtToken)
            }
            if (response.status == HttpStatusCode.OK) {
                val jsonString = response.bodyAsText()
                println("JSON Mapa Asientos: $jsonString")

                try {
                    val mapaResponse = jsonParser.decodeFromString<MapaAsientosResponse>(jsonString)
                    val nuevoMapa = mutableMapOf<AsientoUI, AsientoEstado>()

                    mapaResponse.asientos.forEach { dto ->
                        val asiento = AsientoUI(dto.fila, dto.columna)

                        // VENDIDOS (Rojo)
                        if (dto.estado.equals("Vendido", ignoreCase = true)) {
                            nuevoMapa[asiento] = AsientoEstado.VENDIDO
                        }
                        // BLOQUEADOS (Naranja)
                        else if (dto.estado.equals("Bloqueado", ignoreCase = true)) {
                            nuevoMapa[asiento] = AsientoEstado.BLOQUEADO
                        }
                    }
                    mapaOcupados = nuevoMapa

                } catch (e: Exception) {
                    println("Error procesando mapa: ${e.message}")
                    errorMsg = "Error de datos"
                }
            } else {
                errorMsg = "Error servidor: ${response.status}"
            }
        } catch (e: Exception) {
            errorMsg = "Error red: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Seleccionar Ubicaciones")
                        Text("${evento.titulo}", style = MaterialTheme.typography.labelMedium)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                tonalElevation = 8.dp,
                shadowElevation = 8.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Seleccionados: ${seleccionados.size}/4")
                        Text(
                            "$ ${seleccionados.size * (evento.precioEntrada ?: 0.0)}",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { onConfirmSelection(seleccionados) },
                        enabled = seleccionados.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                    ) {
                        Text("Continuar")
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // REFERENCIAS
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ReferenciaItem(Color(0xFFB71C1C), "Vendido")   // Rojo
                ReferenciaItem(Color(0xFFFFB300), "Bloqueado") // Naranja/Amarillo
                ReferenciaItem(MaterialTheme.colorScheme.primary, "Tu selección")
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                ReferenciaItem(MaterialTheme.colorScheme.surfaceVariant, "Libre")
            }

            Text("PANTALLA / ESCENARIO", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))
            HorizontalDivider(thickness = 4.dp, color = Color.Gray)
            Spacer(modifier = Modifier.height(24.dp))

            if (isLoading) {
                CircularProgressIndicator()
            } else if (errorMsg != null) {
                Text(errorMsg!!, color = MaterialTheme.colorScheme.error)
            } else {
                // LA GRILLA
                val columnas = evento.columnAsientos ?: 1
                val filas = evento.filaAsientos ?: 1

                LazyVerticalGrid(
                    columns = GridCells.Fixed(columnas),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filas * columnas) { index ->
                        val filaActual = (index / columnas) + 1
                        val colActual = (index % columnas) + 1
                        val asientoActual = AsientoUI(filaActual, colActual)

                        val estadoServidor = mapaOcupados[asientoActual]

                        val isSelected = seleccionados.contains(asientoActual)

                        val estadoFinal = when {
                            isSelected -> AsientoEstado.TU_SELECCION
                            estadoServidor == AsientoEstado.VENDIDO -> AsientoEstado.VENDIDO
                            estadoServidor == AsientoEstado.BLOQUEADO -> AsientoEstado.BLOQUEADO
                            else -> AsientoEstado.LIBRE
                        }

                        AsientoItem(
                            fila = filaActual,
                            col = colActual,
                            estado = estadoFinal,
                            onClick = {
                                if (estadoFinal == AsientoEstado.LIBRE || estadoFinal == AsientoEstado.TU_SELECCION) {
                                    if (isSelected) {
                                        seleccionados = seleccionados - asientoActual
                                    } else {
                                        if (seleccionados.size < 4) {
                                            seleccionados = seleccionados + asientoActual
                                        }
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AsientoItem(fila: Int, col: Int, estado: AsientoEstado, onClick: () -> Unit) {
    // DEFINICIÓN DE COLORES
    val backgroundColor = when (estado) {
        AsientoEstado.VENDIDO -> Color(0xFFB71C1C)
        AsientoEstado.BLOQUEADO -> Color(0xFFFFB300)
        AsientoEstado.TU_SELECCION -> MaterialTheme.colorScheme.primary
        AsientoEstado.LIBRE -> MaterialTheme.colorScheme.surfaceVariant
    }

    val contentColor = when (estado) {
        AsientoEstado.TU_SELECCION, AsientoEstado.VENDIDO -> Color.White
        else -> Color.Black
    }

    val borderModifier = if (estado == AsientoEstado.LIBRE) {
        Modifier.border(1.dp, Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
    } else Modifier

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .background(backgroundColor, shape = RoundedCornerShape(4.dp))
            .then(borderModifier)
            .clickable(
                // Deshabilitar el click si es Vendido o Bloqueado por otro
                enabled = estado == AsientoEstado.LIBRE || estado == AsientoEstado.TU_SELECCION
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        when (estado) {
            AsientoEstado.VENDIDO -> Icon(
                imageVector = Icons.Default.Block,
                contentDescription = "Vendido",
                tint = Color.White.copy(alpha = 0.7f)
            )
            AsientoEstado.BLOQUEADO -> Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Bloqueado",
                tint = Color.Black.copy(alpha = 0.6f)
            )
            AsientoEstado.TU_SELECCION -> Text(
                text = "${fila}-${col}",
                style = MaterialTheme.typography.labelSmall,
                color = contentColor,
                fontWeight = FontWeight.Bold
            )
            AsientoEstado.LIBRE -> Text(
                text = "$col",
                style = MaterialTheme.typography.labelSmall,
                color = contentColor.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
fun ReferenciaItem(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(12.dp).background(color, RoundedCornerShape(2.dp)))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text, style = MaterialTheme.typography.labelSmall, fontSize = 10.sp)
    }
}