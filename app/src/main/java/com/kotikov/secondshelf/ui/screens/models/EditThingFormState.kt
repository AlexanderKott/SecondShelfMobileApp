package com.kotikov.secondshelf.ui.screens.models


data class EditThingFormState(
    val id: String,
    val title: String,
    val imageUrls: List<String?>,
    val city: String,
    val category: String,
    val description: String
)