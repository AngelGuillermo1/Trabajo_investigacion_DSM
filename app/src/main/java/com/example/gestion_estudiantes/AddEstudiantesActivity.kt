package com.example.gestion_estudiantes

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gestion_estudiantes.Estudiantes.Estudiante
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddEstudiantesActivity : ComponentActivity() {
    private lateinit var database: DatabaseReference
    private var estudiantesKey: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Recibir datos del intent
        val accion = intent.getStringExtra("accion")
        estudiantesKey = intent.getStringExtra("key")
        val nombreEstudiantes = intent.getStringExtra("nombreEstudiantes") ?: ""
        val numeroCarnet = intent.getStringExtra("numeroCarnet") ?: ""
        val planEstudios = intent.getStringExtra("planEstudios") ?: ""
        val email = intent.getStringExtra("email") ?: ""
        val telefono = intent.getStringExtra("telefono") ?: ""

        setContent {
            AddEstudiantesScreen(
                nombreEstudiantes = nombreEstudiantes,
                numeroCarnet = numeroCarnet,
                planEstudios = planEstudios,
                email = email,
                telefono = telefono,
                isEditMode = accion == "e",
                onSave = { estudiante -> guardar(estudiante) },
                onDelete = { eliminar() },
                onCancel = { finish() }
            )
        }
    }

    @Composable
    fun AddEstudiantesScreen(
        nombreEstudiantes: String,
        numeroCarnet: String,
        planEstudios: String,
        email: String,
        telefono: String,
        isEditMode: Boolean,
        onSave: (Estudiante) -> Unit,
        onDelete: () -> Unit,
        onCancel: () -> Unit
    ) {
        var nombre by remember { mutableStateOf(nombreEstudiantes) }
        var carnet by remember { mutableStateOf(numeroCarnet) }
        var plan by remember { mutableStateOf(planEstudios) }
        var correo by remember { mutableStateOf(email) }
        var telefonoEstudiante by remember { mutableStateOf(telefono) }

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
                Button(onClick = onCancel) {
                    Text("Cancelar")
                }
                if (isEditMode) {
                    Button(onClick = onDelete) {
                        Text("Eliminar")
                    }
                }
                Button(onClick = {
                    if (nombre.isNotEmpty() && carnet.isNotEmpty() && plan.isNotEmpty() &&
                        correo.isNotEmpty() && telefonoEstudiante.isNotEmpty()
                    ) {
                        val estudiante = Estudiante(
                            key = estudiantesKey,
                            nombreEstudiantes = nombre,
                            numeroCarnet = carnet,
                            planEstudios = plan,
                            email = correo,
                            telefono = telefonoEstudiante
                        )
                        onSave(estudiante)
                    } else {
                        Toast.makeText(
                            this@AddEstudiantesActivity,
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

    private fun guardar(estudiante: Estudiante) {
        database = FirebaseDatabase.getInstance().getReference("estudiantes")

        if (estudiante.key != null) {
            // Actualizar registro existente
            database.child(estudiante.key!!).setValue(estudiante).addOnSuccessListener {
                Toast.makeText(this, "Se actualizó con éxito", Toast.LENGTH_SHORT).show()
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Agregar nuevo registro
            val newKey = database.push().key
            if (newKey != null) {
                estudiante.key = newKey
                database.child(newKey).setValue(estudiante).addOnSuccessListener {
                    Toast.makeText(this, "Se guardó con éxito", Toast.LENGTH_SHORT).show()
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "No se pudo generar una clave", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun eliminar() {
        if (estudiantesKey != null) {
            database = FirebaseDatabase.getInstance().getReference("estudiantes")
            database.child(estudiantesKey!!).removeValue().addOnSuccessListener {
                Toast.makeText(this, "Registro eliminado con éxito", Toast.LENGTH_SHORT).show()
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, "Error al eliminar el registro", Toast.LENGTH_SHORT).show()
            }
        }
    }
}