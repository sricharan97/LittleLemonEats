package com.littlelemon.littlelemonapp.data

import android.util.Log
import com.littlelemon.littlelemonapp.network.MenuItemService
import kotlinx.coroutines.flow.Flow

private const val TAG = "MenuRepository"

class MenuRepository(
    private val menuItemService: MenuItemService,
    menuDatabase: MenuDatabase
) : MenuFetch {
    private val menuItemDao = menuDatabase.menuItemDao()

    override val menuItems: Flow<List<MenuItemEntity>> =
        menuItemDao.getAllMenuItems()

    override suspend fun refreshMenu() {
        try {
            Log.d(TAG, "Starting menu refresh from network")
            val menuResult = menuItemService.getAllMenuItems()
            menuResult.fold(
                onSuccess = { menuNetworkData ->
                    val menuEntities = menuNetworkData.menu.map { it.toMenuItemEntity() }
                    Log.d(TAG, "Network call successful, received ${menuEntities.size} items")
                    Log.d(TAG, "First few items: ${menuEntities.take(3)}")
                    menuItemDao.deleteAll()
                    menuItemDao.insertAll(menuEntities)
                    Log.d(TAG, "Menu items saved to database")
                },
                onFailure = { error ->
                    Log.e(TAG, "Network call failed", error)
                    throw error
                }
            )
        } catch (e: Exception) {
            throw e
        }
    }

    override fun getMenuItemsByCategory(category: String): Flow<List<MenuItemEntity>> =
        menuItemDao.getMenuItemsByCategory(category)


}