package com.example.aplicacionmovileva2.data.remote

import com.example.aplicacionmovileva2.data.model.CredentialAuthRequest
import com.example.aplicacionmovileva2.data.model.CredentialAuthResponse
import com.example.aplicacionmovileva2.data.model.DoorData
import com.example.aplicacionmovileva2.data.model.FireSensorData
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface FireSafetyApi {

    /**
     * Consulta la telemetría en tiempo real del sensor de flama (Arduino ESP8266/ESP32).
     */
    @GET("api/v1/sensor/flame")
    suspend fun getFireSensorData(): Response<FireSensorData>

    /**
     * Obtiene el listado de puertas registradas y su estado.
     */
    @GET("api/v1/doors")
    suspend fun getDoors(
        @Query("planta") planta: String? = null
    ): Response<List<DoorData>>

    /**
     * Envía la solicitud de apertura de puerta con credencial de seguridad.
     */
    @POST("api/v1/doors/auth-unlock")
    suspend fun requestDoorUnlock(
        @Body request: CredentialAuthRequest
    ): Response<CredentialAuthResponse>
}

object RetrofitClient {
    // URL base configurable (por defecto localhost/IP local del ESP32 o servidor)
    private const val BASE_URL = "https://192.168.1.100:8443/"

    fun create(
        baseUrl: String = BASE_URL,
        tokenProvider: () -> String? = { "mock_bearer_token" }
    ): FireSafetyApi {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenProvider))
            .addInterceptor(loggingInterceptor)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FireSafetyApi::class.java)
    }
}
