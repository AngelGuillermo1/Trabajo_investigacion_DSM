package com.example.gestion_estudiantes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gestion_estudiantes.Estudiantes.Estudiante

@Composable
fun EstudiantesList(estudiantes: List<Estudiante>, onItemClick: (Estudiante) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(estudiantes) { estudiante ->
            EstudianteItem(estudiante = estudiante, onClick = { onItemClick(estudiante) })
        }
    }
}

@Composable
fun EstudianteItem(estudiante: Estudiante, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Nombre del estudiante: ${estudiante.nombreEstudiantes}",
                style = MaterialTheme.typography.h6
            )
            Text(
                text = "Numero de carnet: ${estudiante.numeroCarnet}",
                style = MaterialTheme.typography.body1
            )
            Text(
                text = "Plan de estudios: ${estudiante.planEstudios}",
                style = MaterialTheme.typography.body2
            )
            Text(
                text = "Correo electronico del estudiante: ${estudiante.email}",
                style = MaterialTheme.typography.body2
            )
            Text(
                text = "Numero de telofono: ${estudiante.telefono}",
                style = MaterialTheme.typography.body2
            )
        }
    }
}