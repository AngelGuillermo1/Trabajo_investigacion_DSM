package com.example.gestion_estudiantes.Estudiantes

data class Estudiante(
    var key: String? = null,
    var nombreEstudiantes: String? = null,
    var numeroCarnet: String? = null,
    var planEstudios: String? = null,
    var email: String? = null,
    var telefono: String? = null
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "nombreEstudiantes" to nombreEstudiantes,
            "numeroCarnet" to numeroCarnet,
            "planEstudios" to planEstudios,
            "email" to email,
            "telefono" to telefono
        )
    }
}