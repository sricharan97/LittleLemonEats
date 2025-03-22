package com.littlelemon.littlelemonapp.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a menu item as it is received from the network.
 *
 * This data class is used to model the structure of a menu item
 * as provided by a remote server, typically in a JSON format. It
 * includes essential details about the item such as its ID, title,
 * description, price, image URL, and category.
 *
 * @property id The unique identifier of the menu item.
 * @property title The name or title of the menu item.
 * @property description A detailed description of the menu item's contents or features.
 * @property price The price of the menu item, represented as an integer (e.g., in cents).
 * @property imageUrl The URL of the image associated with the menu item.
 * @property category The category to which the menu item belongs (e.g., "Appetizers", "Main Courses").
 */
@Serializable
data class MenuItemNetwork(
    val id: Int,
    val title: String,
    val description: String,
    val price: Int,
    @SerialName("image") val imageUrl: String,
    val category: String
)

/**
 * Represents the network data structure for a menu.
 *
 * This data class encapsulates a list of menu items received from a network source.
 * It is designed to be used with the Kotlin Serialization library for easy serialization and deserialization
 * of JSON or other supported formats.
 *
 * @property menu A list of [MenuItemNetwork] objects representing the items in the menu.
 */
@Serializable
data class MenuNetworkData(
    val menu: List<MenuItemNetwork>
)