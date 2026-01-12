package org.mycompany.myapp.utils

object DateUtils {
    fun formatearFecha(fechaIso: String?): String {
        if (fechaIso.isNullOrBlank()) return "Fecha a confirmar"

        try {
            val partes = fechaIso.split("T")
            val fechaParte = partes[0]

            val horaParte = if (partes.size > 1 && partes[1].length >= 5) {
                partes[1].substring(0, 5)
            } else {
                ""
            }
            val (anio, mesNum, dia) = fechaParte.split("-")
            val mesNombre = obtenerNombreMes(mesNum)

            return if (horaParte.isNotBlank()) {
                "$dia $mesNombre $anio - $horaParte hs"
            } else {
                "$dia $mesNombre $anio"
            }

        } catch (e: Exception) {
            return fechaIso
        }
    }

    private fun obtenerNombreMes(numero: String): String {
        return when (numero) {
            "01" -> "Ene"
            "02" -> "Feb"
            "03" -> "Mar"
            "04" -> "Abr"
            "05" -> "May"
            "06" -> "Jun"
            "07" -> "Jul"
            "08" -> "Ago"
            "09" -> "Sep"
            "10" -> "Oct"
            "11" -> "Nov"
            "12" -> "Dic"
            else -> numero
        }
    }
}