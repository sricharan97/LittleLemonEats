package com.littlelemon.littlelemonapp.data

import com.littlelemon.littlelemonapp.network.MenuItemService
import kotlinx.coroutines.flow.Flow

class MenuRepository(
    private val menuItemService: MenuItemService,
    private val menuDatabase: MenuDatabase
) : MenuFetch {
    private val menuItemDao = menuDatabase.menuItemDao()

    override val menuItems: Flow<List<MenuItemEntity>> =
        menuItemDao.getAllMenuItems()

    override suspend fun refreshMenu() {
        try {
            val menuResult = menuItemService.getAllMenuItems()
            menuResult.fold(
                onSuccess = { menuNetworkData ->
                    val menuEntities = menuNetworkData.menu.map { it.toMenuItemEntity() }
                    menuItemDao.deleteAll()
                    menuItemDao.insertAll(menuEntities)
                },
                onFailure = { error ->
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