package com.example.gestion_estudiantes

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.gestion_estudiantes.Estudiantes.Estudiante
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private var ListaEstudiantes: ListView? = null
    private var Estudiantes: ArrayList<Estudiante>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        inicializar()
    }

    private fun inicializar() {
        val fabAgregar: FloatingActionButton = findViewById(R.id.fab_agregar)
        ListaEstudiantes = findViewById(R.id.ListaEstudiantes)

        ListaEstudiantes!!.setOnItemClickListener { _, _, i, _ ->
            val intent = Intent(this, AddEstudiantesActivity::class.java)
            intent.putExtra("accion", "e") // Editar
            val Estudiantes = Estudiantes!![i]
            intent.putExtra("key", Estudiantes.key)
            intent.putExtra("nombreEstudiantes", Estudiantes.nombreEstudiantes)
            intent.putExtra("numeroCarnet", Estudiantes.numeroCarnet)
            intent.putExtra("planEstudios", Estudiantes.planEstudios)
            intent.putExtra("email", Estudiantes.email)
            intent.putExtra("telefono", Estudiantes.telefono)
            startActivity(intent)
        }

        ListaEstudiantes!!.onItemLongClickListener = AdapterView.OnItemLongClickListener { _, _, position, _  ->

            val ad = AlertDialog.Builder(this@MainActivity)
            ad.setMessage("¿Está seguro de eliminar el registro de estudiante?")
                .setTitle("Confirmación")
            ad.setPositiveButton("Sí") { _, _ ->
                Estudiantes!![position].key?.let {
                    refEstudiantes.child(it).removeValue()
                }
                Toast.makeText(
                    this@MainActivity,
                    "Registro borrado!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            ad.setNegativeButton("No") { _, _ ->
                Toast.makeText(
                    this@MainActivity,
                    "Operación de borrado cancelada!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            ad.show()
            true
        }

        fabAgregar.setOnClickListener {
            val intent = Intent(this, AddEstudiantesActivity::class.java)
            intent.putExtra("accion", "a") // Agregar
            intent.putExtra("key", "")
            intent.putExtra("nombreEstudiantes", "")
            intent.putExtra("numeroCarnet", "")
            intent.putExtra("planEstudios", "")
            intent.putExtra("email", "")
            intent.putExtra("telefono", "")
            startActivity(intent)
        }

        Estudiantes = ArrayList()

        refEstudiantes.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Estudiantes!!.clear()
                for (dato in dataSnapshot.children) {
                    val estudiante: Estudiante? = dato.getValue(Estudiante::class.java)
                    estudiante?.key = dato.key
                    estudiante?.let { Estudiantes!!.add(it) }
                }
                val adapter = AdaptadorEstudiantes(
                    this@MainActivity,
                    Estudiantes as ArrayList<Estudiante>
                )
                ListaEstudiantes!!.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    companion object {
        private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        private val refEstudiantes: DatabaseReference = database.getReference("estudiantes")
    }
}