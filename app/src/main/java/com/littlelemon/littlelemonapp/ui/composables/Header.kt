package com.littlelemon.littlelemonapp.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.littlelemon.littlelemonapp.R

@Composable
fun Header(
    modifier: Modifier = Modifier,
    showProfile: Boolean = false,
    showCart: Boolean = false,
    cartItemCount: Int = 0,
    firstName: String = "",
    lastName: String = "",
    onProfileClick: () -> Unit = {},
    onCartClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(top = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left side - Profile avatar
            if (showProfile) {
                Box(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(0.2f)
                ) {
                    ProfileAvatar(
                        firstName = firstName,
                        lastName = lastName,
                        modifier = Modifier
                            .clickable { onProfileClick() },
                        size = 40,
                        displayInitialAt = "home"
                    )
                }
            } else {
                Box(modifier = Modifier.weight(0.2f))
            }

            // Center - Logo
            Box(
                modifier = Modifier
                    .weight(0.6f)
                    .padding(horizontal = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.little_lemon_logo),
                    contentDescription = "Little Lemon Logo",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(vertical = 16.dp)
                )
            }

            // Right side - Cart icon
            if (showCart) {
                Box(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .weight(0.2f)
                        .clickable { onCartClick() },
                    contentAlignment = Alignment.Center
                ) {
                    BadgedBox(
                        badge = {
                            if (cartItemCount > 0) {
                                Badge { Text("$cartItemCount") }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ShoppingCart,
                            contentDescription = "Cart"
                        )
                    }
                }
            } else {
                Box(modifier = Modifier.weight(0.2f))
            }
        }
    }
}
