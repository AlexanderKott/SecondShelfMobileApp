package com.kotikov.secondshelf.ui.screens.models


data class FeedThing(
    val id: String,
    val title: String,
    val city: String,
    val description: String,
    val imageUrl: String?,
    val category: String?,
)