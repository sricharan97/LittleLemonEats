package com.littlelemon.littlelemonapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.littlelemon.littlelemonapp.network.MenuItemNetwork

@Entity(tableName = "menu_items")
data class MenuItemEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val description: String,
    val price: Int,
    val imageUrl: String,
    val category: String
)

// Extension function to convert Network model to Entity
fun MenuItemNetwork.toMenuItemEntity() = MenuItemEntity(
    id = id,
    title = title,
    description = description,
    price = price,
    imageUrl = imageUrl,
    category = category
)

// Extension function to convert Entity to Network model
fun MenuItemEntity.toMenuItemNetwork() = MenuItemNetwork(
    id = id,
    title = title,
    description = description,
    price = price,
    imageUrl = imageUrl,
    category = category
)