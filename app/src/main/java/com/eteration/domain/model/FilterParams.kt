package com.eteration.domain.model

data class FilterParams(
    val nameFilter: String? = null,
    val brandFilter: List<String?> = emptyList(),
    val categoryFilter: String? = null,
)