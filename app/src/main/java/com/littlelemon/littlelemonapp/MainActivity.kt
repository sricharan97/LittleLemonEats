package com.littlelemon.littlelemonapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.littlelemon.littlelemonapp.navigation.MyAppNavigation
import com.littlelemon.littlelemonapp.ui.composables.Header
import com.littlelemon.littlelemonapp.ui.theme.LittleLemonAppTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels{
        MainViewModelFactory(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LittleLemonAppTheme(dynamicColor = false) {
                    MyApp(
                        viewModel = viewModel
                    )
                }
            }
        }

}

@Composable
fun MyApp(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier) {

    val navController = rememberNavController()
    // Save the current route in rememberSaveable
    var currentRoute by rememberSaveable { mutableStateOf<String?>(null) }
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route
    val onBoardingComplete by viewModel.onboardingComplete.collectAsStateWithLifecycle()
    val userLoggedIn by viewModel.userLoggedIn.collectAsStateWithLifecycle()
    val firstName by viewModel.firstName.collectAsStateWithLifecycle()
    val firstNameError by viewModel.firstNameError.collectAsStateWithLifecycle()
    val lastName by viewModel.lastName.collectAsStateWithLifecycle()
    val lastNameError by viewModel.lastNameError.collectAsStateWithLifecycle()
    val email by viewModel.email.collectAsStateWithLifecycle()
    val emailError by viewModel.emailError.collectAsStateWithLifecycle()
    val isFormValid by viewModel.isFormValid.collectAsStateWithLifecycle()
    val menuItems by viewModel.menuItems.collectAsStateWithLifecycle()
    val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()
    val cartItemCount = cartItems.sumOf { it.quantity }
    val searchPhrase by viewModel.searchPhrase.collectAsStateWithLifecycle()

    // Navigate when onBoardingComplete changes
    LaunchedEffect(onBoardingComplete) {
        if (onBoardingComplete) {
            navController.navigate(Home.route) {
                popUpTo(Onboarding.route) { inclusive = true }
            }
        }
    }

    // Update saved route when destination changes
    LaunchedEffect(currentDestination) {
        currentDestination?.let {
            currentRoute = it
        }
    }

    // Restore navigation on configuration change
    LaunchedEffect(currentRoute) {
        currentRoute?.let { route ->
            if (currentDestination != route) {
                navController.navigate(route) {
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
    }

    Scaffold(
        topBar = {
            Header(
                showProfile = currentDestination == Home.route,
                firstName = firstName,
                lastName = lastName,
                onProfileClick = { navController.navigate(Profile.route) { launchSingleTop = true } },
                modifier = Modifier.windowInsetsPadding(
                    WindowInsets.statusBars.only(WindowInsetsSides.Top)
                )
            )
        },
        floatingActionButton = {
            if (currentDestination != Checkout.route) {  // Don't show on checkout screen
                FloatingActionButton(
                    onClick = { navController.navigate(Checkout.route) },
                    modifier = Modifier.padding(bottom = 16.dp)
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
            }
        },
        contentWindowInsets =  WindowInsets(0,0,0,0) // Reset default window insets
    ) {scaffoldPadding ->
        MyAppNavigation(
            navController = navController,
            onBoardingComplete = onBoardingComplete,
            userLoggedIn = userLoggedIn,
            firstName = firstName,
            firstNameError = firstNameError,
            onFirstNameChange = { viewModel.updateFirstName(it) },
            lastName = lastName,
            lastNameError = lastNameError,
            onLastNameChange = { viewModel.updateLastName(it) },
            email = email,
            emailError = emailError,
            onEmailChange = { viewModel.updateEmail(it) },
            isFormValid = isFormValid,
            onContinueClicked = { viewModel.validateAndRegister() },
            onLogout = { viewModel.logout() },
            menuItems = menuItems,
            searchPhrase = searchPhrase,
            onSearchPhraseChange = { phrase -> viewModel.updateSearchPhrase(phrase) },
            cartItems = cartItems,
            // Updated onAddToOrder sends items to the cart
            onAddToCart = { menuItem, quantity -> viewModel.addToCart(menuItem, quantity) },
            onClearCart = { viewModel.clearCart() },
            modifier = modifier.padding(
                top = scaffoldPadding.calculateTopPadding(),
                bottom = 0.dp)
        )

    }
}



