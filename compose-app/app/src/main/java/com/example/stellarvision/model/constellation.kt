package com.example.stellarvision.model

data class Constellation(
    val id: String,
    val lines: List<List<String>>,
    val common_name: CommonName?
)

data class CommonName(
    val native: String,
    val english: String
)

data class ConstellationDataset(
    val id: String,
    val region: String?,
    val constellations: List<Constellation>,
)