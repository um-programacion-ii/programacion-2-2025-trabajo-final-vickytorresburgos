package org.mycompany.myapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.mycompany.myapp.data.AsientoVentaItem
import org.mycompany.myapp.data.EventoDTO

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CargaNombresScreen(
    evento: EventoDTO,
    asientos: List<AsientoUI>,
    onBack: () -> Unit,
    onConfirm: (List<AsientoVentaItem>) -> Unit
) {
    val nombresMap = remember { mutableStateMapOf<AsientoUI, String>() }

    val formularioValido = asientos.all { asiento ->
        (nombresMap[asiento] ?: "").isNotBlank()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ingrese sus Datos") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        bottomBar = {
            Surface(tonalElevation = 8.dp, shadowElevation = 8.dp) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total a pagar:", style = MaterialTheme.typography.titleMedium)
                        Text(
                            "$ ${asientos.size * (evento.precioEntrada ?: 0.0)}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val listaFinal = asientos.map { asiento ->
                                AsientoVentaItem(
                                    fila = asiento.fila,
                                    columna = asiento.columna,
                                    nombrePersona = nombresMap[asiento] ?: ""
                                )
                            }
                            onConfirm(listaFinal)
                        },
                        enabled = formularioValido,
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                    ) {
                        Text("Confirmar Compra")
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Completa los nombres para cada entrada:",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(asientos) { asiento ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(end = 16.dp)
                            ) {
                                Icon(Icons.Default.Person, contentDescription = null)
                                Text(
                                    text = "${asiento.fila}-${asiento.columna}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            OutlinedTextField(
                                value = nombresMap[asiento] ?: "",
                                onValueChange = { nuevoNombre ->
                                    nombresMap[asiento] = nuevoNombre
                                },
                                label = { Text("Nombre y Apellido") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(64.dp))
                }
            }
        }
    }
}