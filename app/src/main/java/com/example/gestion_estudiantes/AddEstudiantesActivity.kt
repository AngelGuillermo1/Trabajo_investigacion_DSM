package com.example.gestion_estudiantes

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.gestion_estudiantes.Estudiantes.Estudiante
import java.util.*

class AddEstudiantesActivity : AppCompatActivity() {
    private var edtNombreEstudiantes: EditText? = null
    private var edtNumeroCarnet: EditText? = null
    private var edtPlanEstudio: EditText? = null
    private var edtEmail: EditText? = null
    private var edtTelefono: EditText? = null
    private lateinit var database: DatabaseReference
    private var EstudiantesKey: String? = null
    private var btnEliminar: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_estudiantes)
        inicializar()
        checkIntentData()
    }

    private fun inicializar() {
        edtNombreEstudiantes = findViewById(R.id.edtNombreEstudiantes)
        edtNumeroCarnet = findViewById(R.id.edtNumeroCarnet)
        edtPlanEstudio = findViewById(R.id.edtPlanEstudio)
        edtEmail = findViewById(R.id.edtEmail)
        edtTelefono = findViewById(R.id.edtTelefono)
        btnEliminar = findViewById(R.id.btnEliminar)


        // Configurar botón de eliminar
        btnEliminar?.setOnClickListener { eliminar() }
    }

    private fun checkIntentData() {
        val intent = intent
        if (intent != null && intent.hasExtra("accion") && intent.getStringExtra("accion") == "e") {
            EstudiantesKey = intent.getStringExtra("key")
            edtNombreEstudiantes?.setText(intent.getStringExtra("nombreActividad"))
            edtNumeroCarnet?.setText(intent.getStringExtra("fechaInicio"))
            edtPlanEstudio?.setText(intent.getStringExtra("fechaFin"))
            edtEmail?.setText(intent.getStringExtra("lugarEvento"))
            edtTelefono?.setText(intent.getStringExtra("horasFormacion"))
            btnEliminar?.visibility = View.VISIBLE // Set the delete button to visible
        }
    }


    fun guardar(v: View?) {
        val nombreEstudiante = edtNombreEstudiantes?.text.toString()
        val numeroCarnet = edtNumeroCarnet?.text.toString()
        val planEduction = edtPlanEstudio?.text.toString()
        val email = edtEmail?.text.toString()
        val telefono = edtTelefono?.text.toString().toInt()

        database = FirebaseDatabase.getInstance().getReference("estudiantes")

        val estudiante = Estudiante(nombreEstudiante, numeroCarnet, planEduction, email, telefono)

        if (EstudiantesKey != null) {
            // Actualizar registro existente
            database.child(EstudiantesKey!!).setValue(estudiante).addOnSuccessListener {
                Toast.makeText(this, "Se actualizó con éxito", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Agregar nuevo registro
            val newKey = database.push().key // Generar una nueva clave
            if (newKey != null) {
                database.child(newKey).setValue(estudiante).addOnSuccessListener {
                    Toast.makeText(this, "Se guardó con éxito", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "No se pudo generar una clave", Toast.LENGTH_SHORT).show()
            }
        }
        finish()
    }

    private fun eliminar() {
        if (EstudiantesKey != null) {
            database = FirebaseDatabase.getInstance().getReference("estudiantes")
            database.child(EstudiantesKey!!).removeValue().addOnSuccessListener {
                Toast.makeText(this, "Registro de estudiante eliminado con éxito", Toast.LENGTH_SHORT).show()
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, "Error al eliminar el registro", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun cancelar(v: View?) {
        finish()
    }
}
