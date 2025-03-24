package com.littlelemon.littlelemonapp.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.littlelemon.littlelemonapp.Checkout
import com.littlelemon.littlelemonapp.Home
import com.littlelemon.littlelemonapp.MenuItem
import com.littlelemon.littlelemonapp.Onboarding
import com.littlelemon.littlelemonapp.Profile
import com.littlelemon.littlelemonapp.checkout.CheckoutScreen
import com.littlelemon.littlelemonapp.data.CartItem
import com.littlelemon.littlelemonapp.data.MenuItemEntity
import com.littlelemon.littlelemonapp.ui.home.HomeScreen
import com.littlelemon.littlelemonapp.ui.menu.MenuItemScreen
import com.littlelemon.littlelemonapp.ui.onboarding.OnboardingScreen
import com.littlelemon.littlelemonapp.ui.profile.ProfileScreen
import kotlinx.coroutines.launch

@Composable
fun MyAppNavigation(
    navController: NavHostController,
    onBoardingComplete: Boolean = false,
    userLoggedIn: Boolean = false,
    firstName: String,
    firstNameError: String?,
    onFirstNameChange: (String) -> Unit,
    lastName: String,
    lastNameError: String?,
    onLastNameChange: (String) -> Unit,
    email: String,
    emailError: String?,
    onEmailChange: (String) -> Unit,
    isFormValid: Boolean,
    onContinueClicked: () -> Unit,
    onLogout: () -> Unit,
    menuItems: List<MenuItemEntity> = emptyList(),
    searchPhrase: String = "",
    onSearchPhraseChange: (String) -> Unit = {},
    cartItems: List<CartItem>,
    onAddToCart: (MenuItemEntity, Int) -> Unit,
    onClearCart: () -> Unit,
    onRemoveFromCart: (CartItem) -> Unit = {},
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    // Save the initial route to handle configuration changes
    val startDestination = rememberSaveable {
        if (userLoggedIn) Home.route else Onboarding.route
    }

    // Create navigation callback functions
    val navigateToOnboarding = { navController.navigateSingleTopTo(Onboarding.route) }
    val navigateToMenuItem = { itemId: String -> navController.navigateToMenuItem(itemId) }
    val navigateToHomeAndClearBackStack = {
        // Modified to preserve state better
        navController.navigateToHomeFromCheckout()
    }

    // Handle navigation when onBoardingComplete changes
    LaunchedEffect(onBoardingComplete) {
        if (onBoardingComplete && currentRoute == Onboarding.route) {
            navController.navigate(Home.route) {
                popUpTo(Onboarding.route) { inclusive = true }
            }
        }
    }

    // Configuration changes monitor
    val currentOrientation = LocalConfiguration.current.orientation
    
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier.padding(
                bottom = padding.calculateBottomPadding(),
                start = padding.calculateStartPadding(LayoutDirection.Ltr),
                end = padding.calculateEndPadding(LayoutDirection.Ltr),
                top = 8.dp
            )
        ) {
            composable(route = Home.route) {
                HomeScreen(
                    searchPhrase = searchPhrase,
                    onSearchPhraseChange = onSearchPhraseChange,
                    menuItems = menuItems,
                    onMenuItemClick = { menuItem ->
                        navigateToMenuItem(menuItem.id.toString())
                    }
                )
            }

            composable(route = Onboarding.route) {
                OnboardingScreen(
                    firstName = firstName,
                    firstNameError = firstNameError,
                    onFirstNameChange = { onFirstNameChange(it) },
                    lastName = lastName,
                    lastNameError = lastNameError,
                    onLastNameChange = { onLastNameChange(it) },
                    email = email,
                    emailError = emailError,
                    onEmailChange = { onEmailChange(it) },
                    isFormValid = isFormValid,
                    onContinueClicked = {
                        onContinueClicked()
                    },
                    onShowMessage = { message ->
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = message,
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                )
            }

            composable(route = Profile.route) {
                ProfileScreen(
                    firstname = firstName,
                    lastname = lastName,
                    email = email,
                    onLogout = {
                        onLogout()
                        navigateToOnboarding()
                    }
                )
            }

            composable(route = Checkout.route) {
                CheckoutScreen(
                    cartItems = cartItems,
                    onPlaceOrder = {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Order placed successfully! The restaurant will prepare your food.",
                                duration = SnackbarDuration.Long
                            )
                        }
                        onClearCart()
                        navigateToHomeAndClearBackStack()
                    },
                    onRemoveItem = onRemoveFromCart
                )
            }

            composable(
                route = MenuItem.routeWithArgs,
                arguments = MenuItem.arguments
            ) { backStackEntry ->
                val itemId = backStackEntry.arguments?.getString(MenuItem.itemIdArg)
                MenuItemScreen(
                    itemId = itemId,
                    menuItems = menuItems,
                    onAddToOrder = { menuItem, quantity ->
                        onAddToCart(menuItem, quantity)
                        // Add success feedback
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "${menuItem.title} added to cart",
                                duration = SnackbarDuration.Short
                            )
                        }
                        // Navigate back to home after adding to cart
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

// Changed from private to public to be accessible from MainActivity
fun NavHostController.navigateSingleTopTo(route: String) {
    this.navigate(route) {
        popUpTo(this@navigateSingleTopTo.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

// New navigation function that clears the entire back stack
fun NavHostController.navigateAndClearBackStack(route: String) {
    this.navigate(route) {
        popUpTo(0) { inclusive = true }
        launchSingleTop = true
    }
}

// Changed from private to public to be accessible from MainActivity
fun NavHostController.navigateToMenuItem(itemId: String) {
    this.navigate("${MenuItem.route}/$itemId") {
        launchSingleTop = true
        // Save and restore state to handle configuration changes
        restoreState = true
    }
}

// New navigation function that properly navigates to Home from Checkout
fun NavHostController.navigateToHomeFromCheckout() {
    this.navigate(Home.route) {
        // Pop up to Home only if it exists in the back stack
        // This preserves the proper navigation state
        popUpTo(Home.route) {
            // Save the state of the Home screen
            saveState = true
            // Only include if Home is already in the back stack
            inclusive = false
        }
        // Make sure we don't create multiple copies of Home
        launchSingleTop = true
        // Restore the saved state if it exists
        restoreState = true
    }
}

