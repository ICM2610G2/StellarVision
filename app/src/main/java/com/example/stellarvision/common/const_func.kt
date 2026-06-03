package com.example.stellarvision.common

import android.content.Context
import android.util.Log
import com.example.stellarvision.R
import com.example.stellarvision.model.ConstellationDataset
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

data class ConstellationLine(
    val starHipId1: String,
    val starHipId2: String,
    val constellationName: String
)

fun loadConstellationLines(context: Context): List<ConstellationLine>{
    val linesList = mutableListOf<ConstellationLine>()

    try {
        val inputStream = context.resources.openRawResource(R.raw.constindex)
        val reader = InputStreamReader(inputStream)

        val datasetType = object: TypeToken<ConstellationDataset>() {}.type
        val dataset: ConstellationDataset = Gson().fromJson(reader, datasetType)

        if (dataset == null || dataset.constellations.isNullOrEmpty()) {
            Log.e("ConstellationParser", "El dataset está vacío o no se pudo parsear la raíz 'asterisms'")
            reader.close()
            return emptyList()
        }

        dataset.constellations.forEach { constellation ->

            val constellationName = constellation.common_name?.native
            ?: constellation.common_name?.english
            ?: constellation.id.replace("CON modern ", "")

            constellation.lines?.forEach { path ->
                if(path.size > 1){
                    for (i in 0 until path.size - 1){
                        val id1 = path[i]
                        val id2 = path[i + 1]
                        val line = ConstellationLine(id1, id2, constellationName)
                        linesList.add(line)
                    }
                }
            }
        }
        reader.close()

    } catch (e: Exception){
        Log.e("ConstellationParser", "Error durante el parseo del JSON: ${e.message}")
        e.printStackTrace()
    }
    Log.d("ConstellationParser", "Total de líneas individuales cargadas con éxito: ${linesList.size}")
    return linesList
}