package com.littlelemon.littlelemonapp.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    menuItems : List<MenuItemEntity> =  emptyList()
) {

    var selectedCategory by rememberSaveable { mutableStateOf("") }

    Column {
        Column(
            modifier = Modifier
                .background(Color(0xFF495E57))
                .padding(start = 12.dp, end = 12.dp, top = 16.dp, bottom = 16.dp)
        ) {
            Text(
                text = stringResource(id =R.string.title ),
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFF4CE14)
            )
            Text(
                text = stringResource(id = R.string.location),
                fontSize = 24.sp,
                color = Color(0xFFEDEFEE)
            )
            Row(
                modifier = Modifier
                    .padding(top = 18.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.description),
                    color = Color(0xFFEDEFEE),
                    fontSize = 18.sp,
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
                    .padding(top =24.dp),
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
        LowerPanel(
            menuItems = menuItems,
            searchPhrase = searchPhrase,
            selectedCategory = selectedCategory,
            onCategorySelected = { category ->
                selectedCategory = if (selectedCategory != category) category
                else ""
            })
    }
}

@Composable
private fun LowerPanel(
    menuItems: List<MenuItemEntity> = emptyList(),
    searchPhrase: String = "",
    selectedCategory: String = "",
    onCategorySelected: (String) -> Unit = {}
) {

    // Extract unique categories from menu items
    val categoriesMap = menuItems.map { it.category }.distinct()
        .associateBy ({ it.replaceFirstChar { c -> c.uppercase() } }, { it })
    val displayCategories = categoriesMap.keys.toList()

    // Filter menu items based on search phrase
    val filteredMenuItems = menuItems.filter {menuItem ->
        //search the text filter
        (searchPhrase.isBlank() ||
                menuItem.title.contains(searchPhrase, ignoreCase = true) ||
                menuItem.description.contains(searchPhrase, ignoreCase = true)) &&
                //Category filter if selected
                (selectedCategory.isEmpty() || menuItem.category.equals(categoriesMap[selectedCategory], ignoreCase = true))

    }

    Column {

        Text(
            text = stringResource(id = R.string.order_for_takeaway),
            fontSize = 24.sp,
            fontWeight = Bold,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        LazyRow {

            items(displayCategories) { displayCategory ->
                MenuCategory(
                    category = displayCategory,
                    isSelected = displayCategory == selectedCategory,
                    onclick = { onCategorySelected(displayCategory) })
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(8.dp),
            thickness = 1.dp,
            color = Color.Gray
        )
        LazyColumn {
            items(filteredMenuItems) { menuItem ->
                MenuDish(menuItem)
            }
        }
    }
}






@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MenuDish(menuItem: MenuItemEntity) {
    Card {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column {
                Text(
                    text = menuItem.title, fontSize = 18.sp, fontWeight = Bold
                )
                Text(
                    text = menuItem.description,
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(top = 5.dp, bottom = 5.dp)
                        .fillMaxWidth(.75f)
                )
                Text(
                    text = "$${menuItem.price}",
                    color = Color.Gray,
                    fontWeight = Bold
                )
            }
            // Use GlideImage to load from URL
            GlideImage(
                model = menuItem.imageUrl,
                contentDescription = menuItem.title,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .size(80.dp),
                contentScale = ContentScale.Crop,
                failure = placeholder(R.drawable.error),
                loading = placeholder(R.drawable.placeholder)
            )
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
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray
    val contentColor = if (isSelected) Color.White else Color.Black

    Button(
        onClick = onclick,
        colors = ButtonDefaults.buttonColors(
            contentColor = contentColor,
            containerColor = backgroundColor),
        shape = RoundedCornerShape(40),
        modifier = Modifier.padding(5.dp)
    ) {
        Text(
            text = category
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
fun LowerPanelPreview() {
    LittleLemonAppTheme(dynamicColor = false)  {
        LowerPanel()
    }
}

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