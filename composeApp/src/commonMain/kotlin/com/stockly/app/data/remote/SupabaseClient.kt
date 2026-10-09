package com.stockly.app.data.remote

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.postgrest.from
object StocklySupabase {
    suspend fun testConnection(): Boolean {
        return try {
            client.from("products").select()
            println("STOCKLY: Conexión con Supabase exitosa")
            true
        } catch (e: Exception) {
            println("STOCKLY: Error al conectar con Supabase: ${e.message}")
            false
        }
    }
    private const val SUPABASE_URL =
        "https://afosjrxndkppcfxkypin.supabase.co"

    private const val SUPABASE_KEY =
        "sb_publishable_Fs-C637m2qTNY7vtiiL4hg_R9NIY3sw"

    val client by lazy {
        createSupabaseClient(
            supabaseUrl = SUPABASE_URL,
            supabaseKey = SUPABASE_KEY
        ) {
            install(Auth)
            install(Postgrest)
            install(Storage)
        }
    }
}
