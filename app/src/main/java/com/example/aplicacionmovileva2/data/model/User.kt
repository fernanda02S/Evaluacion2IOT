package com.example.aplicacionmovileva2.data.model

data class User(
    val id: Long = 0,
    val email: String,
    val rut: String,
    val nombreApellido: String,
    val usuario: String,
    val contrasena: String,
    val fechaRegistro: String = ""
)
