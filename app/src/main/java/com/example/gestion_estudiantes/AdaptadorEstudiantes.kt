package com.example.gestion_estudiantes

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.gestion_estudiantes.Estudiantes.Estudiante


class AdaptadorEstudiantes (private val context: Activity, var Estudiantes: List<Estudiante>) :
    ArrayAdapter<Estudiante>(context, R.layout.estudiante_layout, Estudiantes) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater = context.layoutInflater
        val rowView: View = convertView ?: layoutInflater.inflate(R.layout.estudiante_layout, parent, false)

        val tvNombreEstudiantes = rowView.findViewById<TextView>(R.id.tvNombreEstudiante)
        val tvNumeroCarnet = rowView.findViewById<TextView>(R.id.tvNumeroCarnet)
        val tvPlanEstudios = rowView.findViewById<TextView>(R.id.tvPlanEstudios)
        val tvEmail = rowView.findViewById<TextView>(R.id.tvEmail)
        val tvTelefono = rowView.findViewById<TextView>(R.id.tvTelefono)

        val estudiante = Estudiantes[position]

        tvNombreEstudiantes.text = "Nombre de la Actividad: ${estudiante.nombreEstudiantes}"
        tvNumeroCarnet.text = "Fecha de Inicio: ${estudiante.numeroCarnet}"
        tvPlanEstudios.text = "Fecha de Finalización: ${estudiante.planEstudios}"
        tvEmail.text = "Lugar del Evento: ${estudiante.email}"
        tvTelefono.text = "Número de Horas: ${estudiante.telefono}"

        return rowView
    }
}