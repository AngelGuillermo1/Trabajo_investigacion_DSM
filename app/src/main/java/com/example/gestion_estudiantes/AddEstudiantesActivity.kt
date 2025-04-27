package com.example.gestion_estudiantes

import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gestion_estudiantes.Estudiantes.Estudiante
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.gestion_estudiantes.ui.theme.GestionEstudiantesTheme

@Composable
fun AddEstudiantesScreen(
    estudiante: Estudiante? = null,
    onNavigateBack: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var carnet by remember { mutableStateOf("") }
    var plan by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var telefonoEstudiante by remember { mutableStateOf("") }

    LaunchedEffect(estudiante) {
        estudiante?.let {
            nombre = it.nombreEstudiantes ?: ""
            carnet = it.numeroCarnet ?: ""
            plan = it.planEstudios ?: ""
            correo = it.email ?: ""
            telefonoEstudiante = it.telefono ?: ""
        }
    }

    val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("estudiantes")
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del Estudiante") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = carnet,
            onValueChange = { carnet = it },
            label = { Text("Número de Carnet") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = plan,
            onValueChange = { plan = it },
            label = { Text("Plan de Estudios") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo Electrónico") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = telefonoEstudiante,
            onValueChange = { telefonoEstudiante = it },
            label = { Text("Teléfono") },
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onNavigateBack) {
                Text("Cancelar")
            }
            if (estudiante != null) {
                Button(onClick = {
                    database.child(estudiante.key!!).removeValue().addOnSuccessListener {
                        Toast.makeText(
                            context,
                            "Registro eliminado con éxito",
                            Toast.LENGTH_SHORT
                        ).show()
                        onNavigateBack()
                    }.addOnFailureListener {
                        Toast.makeText(
                            context,
                            "Error al eliminar el registro",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }) {
                    Text("Eliminar")
                }
            }
            Button(onClick = {
                if (nombre.isNotEmpty() && carnet.isNotEmpty() && plan.isNotEmpty() &&
                    correo.isNotEmpty() && telefonoEstudiante.isNotEmpty()
                ) {
                    val estudianteNuevo = Estudiante(
                        key = estudiante?.key ?: database.push().key,
                        nombreEstudiantes = nombre,
                        numeroCarnet = carnet,
                        planEstudios = plan,
                        email = correo,
                        telefono = telefonoEstudiante
                    )
                    estudianteNuevo.key?.let { key ->
                        database.child(key).setValue(estudianteNuevo).addOnSuccessListener {
                            Toast.makeText(
                                context,
                                "Se guardó con éxito",
                                Toast.LENGTH_SHORT
                            ).show()
                            onNavigateBack()
                        }.addOnFailureListener {
                            Toast.makeText(
                                context,
                                "Error al guardar",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Por favor, completa todos los campos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }) {
                Text("Guardar")
            }
        }
    }
}