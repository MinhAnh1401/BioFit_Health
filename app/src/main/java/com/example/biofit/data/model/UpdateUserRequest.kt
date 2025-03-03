package com.example.biofit.data.model

data class UpdateUserRequest(
    val fullName: String?,
    val gender: Int?,
    val dateOfBirth: String?,
    val height: Float?,
    val weight: Float?,
    val targetWeight: Float?,
    val avatar: String?
)