package com.example.gestion_estudiantes

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gestion_estudiantes.Estudiantes.Estudiante
import com.example.gestion_estudiantes.ui.theme.GestionEstudiantesTheme
import com.google.firebase.database.*

class MainActivity : ComponentActivity() {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val refEstudiantes: DatabaseReference = database.getReference("estudiantes")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GestionEstudiantesTheme {
                MainScreen()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainScreen() {
        var estudiantes by remember { mutableStateOf(listOf<Estudiante>()) }

        // Firebase listener to fetch data
        LaunchedEffect(Unit) {
            refEstudiantes.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val estudiantesList = mutableListOf<Estudiante>()
                    for (dato in dataSnapshot.children) {
                        val estudiante: Estudiante? = dato.getValue(Estudiante::class.java)
                        estudiante?.key = dato.key
                        estudiante?.let { estudiantesList.add(it) }
                    }
                    estudiantes = estudiantesList
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Registro de alumnos UDB") },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { navigateToAddEstudiante() }) {
                    Text("+")
                }
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                itemsIndexed(estudiantes) { index: Int, estudiante: Estudiante ->
                    EstudianteItem(
                        estudiante = estudiante,
                        index = index + 1, // Add 1 to make it 1-based
                        onClick = { navigateToEditEstudiante(estudiante) }
                    )
                }
            }
        }
    }

    @Composable
    fun EstudianteItem(estudiante: Estudiante, index: Int, onClick: () -> Unit) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clickable { onClick() },
            elevation = 4.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Estudiante $index", style = MaterialTheme.typography.headlineSmall)
                Text(text = "Nombre: ${estudiante.nombreEstudiantes}", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Carnet: ${estudiante.numeroCarnet}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Plan de estudios: ${estudiante.planEstudios}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Email: ${estudiante.email}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Teléfono: ${estudiante.telefono}", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }

    private fun navigateToAddEstudiante() {
        val intent = Intent(this, AddEstudiantesActivity::class.java)
        intent.putExtra("accion", "a") // Agregar
        startActivity(intent)
    }

    private fun navigateToEditEstudiante(estudiante: Estudiante) {
        val intent = Intent(this, AddEstudiantesActivity::class.java)
        intent.putExtra("accion", "e") // Editar
        intent.putExtra("key", estudiante.key)
        intent.putExtra("nombreEstudiantes", estudiante.nombreEstudiantes)
        intent.putExtra("numeroCarnet", estudiante.numeroCarnet)
        intent.putExtra("planEstudios", estudiante.planEstudios)
        intent.putExtra("email", estudiante.email)
        intent.putExtra("telefono", estudiante.telefono)
        startActivity(intent)
    }
}