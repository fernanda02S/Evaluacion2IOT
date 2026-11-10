package com.example.aplicacionmovileva2.data.remote

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor de seguridad para comunicaciones HTTPS con el backend IoT (ESP32 / Servidor REST).
 * Incorpora autenticación mediante Bearer Token y cabeceras de seguridad requeridas
 * en reemplazo del protocolo inseguro HTTP sin cifrar original.
 */
class AuthInterceptor(private val tokenProvider: () -> String? = { "mock_jwt_token_secure_iot_2026" }) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .header("X-Client-Device", "Android-Safety-Client")

        val token = tokenProvider()
        if (!token.isNullOrBlank()) {
            requestBuilder.header("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}
