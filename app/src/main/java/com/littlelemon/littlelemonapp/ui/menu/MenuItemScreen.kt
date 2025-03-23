package com.littlelemon.littlelemonapp.ui.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.littlelemon.littlelemonapp.R
import com.littlelemon.littlelemonapp.data.MenuItemEntity
import com.littlelemon.littlelemonapp.ui.composables.ActionButton
import com.littlelemon.littlelemonapp.ui.theme.LittleLemonAppTheme
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MenuItemScreen(
    itemId:String?,
    menuItems: List<MenuItemEntity> = emptyList(),
    onAddToOrder :(MenuItemEntity, Int) -> Unit,
    ) {
    
    // Save the itemId across configuration changes
    val savedItemId = rememberSaveable { mutableStateOf(itemId) }

    val menuItem = savedItemId.value?.toIntOrNull()?.let{ id ->
        menuItems.find{it.id == id}
    }

    var quantity by rememberSaveable { mutableIntStateOf(1) }

    if(menuItem == null){
        Text(
            text = "Menu item not found",
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            textAlign = TextAlign.Center
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Scrollable content area
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Item details section
            // Image at the top
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                GlideImage(
                    model = menuItem.imageUrl,
                    contentDescription = menuItem.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop,
                    failure = placeholder(R.drawable.error),
                    loading = placeholder(R.drawable.placeholder)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            //Title below the image
            Text(
                text = menuItem.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = formatPrice(menuItem.price.toDouble()),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Description",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = menuItem.description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Add a spacer to separate content sections
            Spacer(modifier = Modifier.height(40.dp))

            // Quantity section now in the middle
            Text(
                text = "Quantity",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { if (quantity > 1) quantity-- }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Decrease quantity"
                        )
                    }

                    Text(
                        text = quantity.toString(),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(
                        onClick = { quantity++ }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Increase quantity"
                        )
                    }
                }
            }
            
            // Add padding at the bottom of scrollable content
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // Add to Order button outside of scrollable area, at the bottom
        Spacer(modifier = Modifier.height(16.dp))
        
        ActionButton(
            onContinueClicked = { onAddToOrder(menuItem, quantity) },
            label = "ADD TO ORDER - ${formatPrice(menuItem.price.toDouble() * quantity)}",
            modifier = Modifier.padding(0.dp)
        )
    }
    
}

private fun formatPrice(price: Double): String {
    return NumberFormat.getCurrencyInstance(Locale.US).format(price)
}

@Preview(showBackground = true)
@Composable
fun MenuItemScreenPreview() {
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
        )
    )

    LittleLemonAppTheme(dynamicColor = false) {
        MenuItemScreen(
            itemId = "1",
            menuItems = sampleMenuItems,
            onAddToOrder = { menuItem, quantity ->
                // In a real implementation, this would add the item to an order
                // For preview, this is empty
            }
        )
    }
}

