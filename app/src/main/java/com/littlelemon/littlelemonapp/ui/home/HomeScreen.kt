package com.littlelemon.littlelemonapp.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.littlelemon.littlelemonapp.R
import com.littlelemon.littlelemonapp.data.MenuItemEntity
import com.littlelemon.littlelemonapp.ui.theme.LittleLemonAppTheme

@Composable
fun HomeScreen(
    searchPhrase: String = "",
    onSearchPhraseChange: (String) -> Unit = {},
    menuItems : List<MenuItemEntity> =  emptyList(),
    onMenuItemClick: (MenuItemEntity) -> Unit = {}
) {
    var selectedCategory by rememberSaveable { mutableStateOf("") }
    
    // Using LazyColumn for the entire screen to make it scrollable
    LazyColumn {
        // Header section as a single item
        item {
            Column(
                modifier = Modifier
                    .background(Color(0xFF495E57))
                    .padding(start = 12.dp, end = 12.dp, top = 16.dp, bottom = 16.dp)
            ) {
                Text(
                    text = stringResource(id =R.string.title ),
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = stringResource(id = R.string.location),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Row(
                    modifier = Modifier
                        .padding(top = 16.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.description),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(bottom = 28.dp)
                            .fillMaxWidth(0.6f)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.upperpanelimage),
                        contentDescription = "Upper Panel Image",
                        modifier = Modifier.clip(RoundedCornerShape(20.dp))
                    )
                }
                // Search bar instead of order button
                OutlinedTextField(
                    value = searchPhrase,
                    onValueChange = onSearchPhraseChange,
                    placeholder = { Text("Search menu") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search icon"
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFF4CE14),
                        unfocusedBorderColor = Color(0xFFEDEFEE),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )
            }
        }
        
        // Order for takeaway section
        item {
            Text(
                text = stringResource(id = R.string.order_for_takeaway),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }
        
        // Category section
        item {
            // Extract unique categories from menu items
            val categoriesMap = menuItems.map { it.category }.distinct()
                .associateBy ({ it.replaceFirstChar { c -> c.uppercase() } }, { it })
            val displayCategories = categoriesMap.keys.toList()
            
            LazyRow {
                items(displayCategories) { displayCategory ->
                    MenuCategory(
                        category = displayCategory,
                        isSelected = displayCategory == selectedCategory,
                        onclick = {
                            selectedCategory = if (selectedCategory != displayCategory) displayCategory else ""
                        }

                    )
                }
            }
            
            HorizontalDivider(
                modifier = Modifier.padding(8.dp),
                thickness = 1.dp,
                color = Color.Gray
            )
        }
        
        // Filter menu items based on search phrase and category
        val filteredMenuItems = menuItems.filter {menuItem ->
            //search the text filter
            (searchPhrase.isBlank() ||
                    menuItem.title.contains(searchPhrase, ignoreCase = true) ||
                    menuItem.description.contains(searchPhrase, ignoreCase = true)) &&
                    //Category filter if selected
                    (selectedCategory.isEmpty() || menuItem.category.equals(
                        menuItems.map { it.category }.distinct()
                            .associateBy ({ it.replaceFirstChar { c -> c.uppercase() } }, { it })[selectedCategory], 
                        ignoreCase = true)
                    )
        }
        
        // Menu items section
        items(filteredMenuItems) { menuItem ->
            MenuDish(
                menuItem = menuItem,
                onClick = { onMenuItemClick(menuItem) })
        }
    }
    

}




@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MenuDish(
    menuItem: MenuItemEntity,
    onClick: () -> Unit = {}) {
    // Get current device orientation
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    
    Card(
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = menuItem.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = menuItem.description,
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(top = 5.dp, bottom = 5.dp)
                        .fillMaxWidth(.75f)
                )
                Text(
                    text = "$${menuItem.price}",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Determine the image source based on menu item title
            val imageModel = when (menuItem.title) {
                "Lemon Desert" -> R.drawable.lemondessert
                "Grilled Fish" -> R.drawable.grilledfish
                else -> menuItem.imageUrl
            }

            // Use Box to constrain image dimensions while maintaining aspect ratio
            Box(
                modifier = Modifier
                    .weight(if (isLandscape) 0.7f else 1f)
                    .padding(start = 8.dp)
            ) {
                GlideImage(
                    model = imageModel,
                    contentDescription = menuItem.title,
                    modifier = Modifier
                        .size(if (isLandscape) 60.dp else 80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .align(Alignment.Center),
                    contentScale = ContentScale.FillBounds,
                    failure = placeholder(R.drawable.error),
                    loading = placeholder(R.drawable.placeholder)
                )
            }
        }
    }
    HorizontalDivider(
        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
        thickness = 1.dp,
        color = Color.LightGray
    )
}

@Composable
fun MenuCategory(
    category: String,
    isSelected: Boolean = false,
    onclick: () -> Unit = {}
) {
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.secondary else Color.LightGray
    val contentColor =  Color.Black

    Button(
        onClick = onclick,
        colors = ButtonDefaults.buttonColors(
            contentColor = contentColor,
            containerColor = backgroundColor),
        shape = RoundedCornerShape(40),
        modifier = Modifier.padding(5.dp)
    ) {
        Text(
            text = category,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}


val previewCategories = listOf(
    "Starters",
    "Mains",
    "Desserts",
    "Drinks",
    "Specials"
)






@Preview(showBackground = true)
@Composable
fun MenuCategoryPreview() {
    LittleLemonAppTheme(dynamicColor = false) {
        MenuCategory("Lunch")
    }
}

@Preview(showBackground = true)
@Composable
fun MenuDishPreview() {
    LittleLemonAppTheme(dynamicColor = false) {
        MenuDish(
            MenuItemEntity(
                1,
                "Greek Salad",
                "The famous greek salad of crispy lettuce, peppers, olives and our Chicago...",
                12,
                "",
                "Starters"
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MenuCategoriesPreview() {

    LittleLemonAppTheme(dynamicColor = false) {
        LazyRow {
            items(previewCategories) { category ->
                MenuCategory(category)
            }
        }
    }

}





@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    // Create sample menu items for the preview
    val sampleMenuItems = listOf(
        MenuItemEntity(
            id = 1,
            title = "Greek Salad",
            description = "The famous greek salad of crispy lettuce, peppers, olives and our Chicago-style feta cheese.",
            price = 12,
            imageUrl = "https://github.com/Meta-Mobile-Developer-PC/Working-With-Data-API/blob/main/images/greekSalad.jpg?raw=true",
            category = "Starters"
        ),
        MenuItemEntity(
            id = 2,
            title = "Bruschetta",
            description = "Our Bruschetta is made from grilled bread that has been smeared with garlic and seasoned with salt and olive oil.",
            price = 8,
            imageUrl = "https://github.com/Meta-Mobile-Developer-PC/Working-With-Data-API/blob/main/images/bruschetta.jpg?raw=true",
            category = "Starters"
        ),
        MenuItemEntity(
            id = 3,
            title = "Lemon Dessert",
            description = "This comes straight from grandma's recipe book, every last ingredient has been sourced and is as authentic as can be imagined.",
            price = 10,
            imageUrl = "https://github.com/Meta-Mobile-Developer-PC/Working-With-Data-API/blob/main/images/lemonDessert.jpg?raw=true",
            category = "Desserts"
        ),
        MenuItemEntity(
            id = 4,
            title = "Grilled Fish",
            description = "Our grilled fish is seasoned with olive oil and Mediterranean herbs, served with a side of roasted vegetables.",
            price = 22,
            imageUrl = "https://github.com/Meta-Mobile-Developer-PC/Working-With-Data-API/blob/main/images/grilledFish.jpg?raw=true",
            category = "Mains"
        )
    )

    LittleLemonAppTheme(dynamicColor = false) {
        HomeScreen(
            menuItems = sampleMenuItems,
            searchPhrase = "",
            onSearchPhraseChange = {}
        )
    }
}

