package com.kotikov.secondshelf.ui.screens.models

data class ApplicationItem(
    val id: String,
    val thingId : String,
    val thingTitle: String,
    val thingImageUrl: String?,
    val status: ApplicationStatus,
    val ownerContacts: String?,
    val createdAt: String
)