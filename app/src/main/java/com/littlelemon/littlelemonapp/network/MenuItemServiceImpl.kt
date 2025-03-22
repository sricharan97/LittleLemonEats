package com.littlelemon.littlelemonapp.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.serialization.ContentConverter
import io.ktor.serialization.kotlinx.json.KotlinxSerializationJsonExtensionProvider
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.reflect.TypeInfo
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.charsets.Charset
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

class MenuItemServiceImpl : MenuItemService {

    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true  // Ignore fields we don’t need.
                isLenient = true
                ContentType("text", "plain")
            })
            register(ContentType.Text.Plain, object : ContentConverter{
                private val json = Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                }

                override suspend fun deserialize(
                    charset: Charset,
                    typeInfo: TypeInfo,
                    content: ByteReadChannel
                ): Any? {
                    val text = content.readRemaining().readText()
                    val serializer = typeInfo.kotlinType?.let { serializer(it) }
                    return json.decodeFromString(serializer!!, text)
                }

            }
            )

        }
    }

    private val baseUrl =
        "https://raw.githubusercontent.com/Meta-Mobile-Developer-PC/Working-With-Data-API/main/menu.json"

    override suspend fun getAllMenuItems(): Result<MenuNetworkData> = try {
        val response: MenuNetworkData = client.get(baseUrl).body()
        Result.success(response)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getMenuItemsByCategory(category: String): Result<List<MenuItemNetwork>> =
        try {
            val allItems = getAllMenuItems().getOrThrow()
            val filteredItems =
                allItems.menu.filter { it.category.equals(category, ignoreCase = true) }
            Result.success(filteredItems)
        } catch (e: Exception) {
            Result.failure(e)
        }
}