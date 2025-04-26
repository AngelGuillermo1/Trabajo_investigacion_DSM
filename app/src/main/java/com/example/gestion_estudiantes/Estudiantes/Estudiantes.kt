package com.example.gestion_estudiantes.Estudiantes

data class Estudiante(
    var nombreEstudiantes: String? = null,
    var numeroCarnet: String? = null,
    var planEstudios: String? = null,
    var email: String? = null,
    var telefono: Int? = null,
    var key: String? = null
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