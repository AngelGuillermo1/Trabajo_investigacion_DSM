package com.example.gestion_estudiantes

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
import androidx.navigation.compose.rememberNavController
import com.example.gestion_estudiantes.Estudiantes.Estudiante
import com.example.gestion_estudiantes.ui.theme.GestionEstudiantesTheme
import com.google.firebase.database.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GestionEstudiantesTheme {
                val navController = rememberNavController()
                AppNavigation(navController = navController)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToAdd: () -> Unit,
    onNavigateToEdit: (Estudiante) -> Unit
) {
    var estudiantes by remember { mutableStateOf(listOf<Estudiante>()) }
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val refEstudiantes: DatabaseReference = database.getReference("estudiantes")

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
            FloatingActionButton(onClick = onNavigateToAdd) {
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
                    index = index + 1,
                    onClick = { onNavigateToEdit(estudiante) }
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