package com.littlelemon.littlelemonapp.network

interface MenuItemService {
    suspend fun getAllMenuItems(): Result<MenuNetworkData>
    suspend fun getMenuItemsByCategory(category: String): Result<List<MenuItemNetwork>>
}