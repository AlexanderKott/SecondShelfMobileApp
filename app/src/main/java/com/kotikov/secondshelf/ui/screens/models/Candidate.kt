package com.kotikov.secondshelf.ui.screens.models

data class Candidate(
    val id: String,
    val firstName: String,
    val lastName: String,
    val photoUrl: String?,
    val profileLink: String,
    val thingTitle: String,
    val applicationText: String
)