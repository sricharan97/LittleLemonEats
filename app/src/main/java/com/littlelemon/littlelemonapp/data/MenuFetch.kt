package com.littlelemon.littlelemonapp.data

import kotlinx.coroutines.flow.Flow

interface MenuFetch {
    /**
     * Get all menu items as a Flow
     */
    val menuItems: Flow<List<MenuItemEntity>>

    /**
     * Refresh menu data from the network source
     * @throws Exception if the refresh operation fails
     */
    suspend fun refreshMenu()

    /**
     * Get menu items filtered by category
     * @param category The category to filter by
     * @return Flow of menu items for the specified category
     */
    fun getMenuItemsByCategory(category: String): Flow<List<MenuItemEntity>>
}