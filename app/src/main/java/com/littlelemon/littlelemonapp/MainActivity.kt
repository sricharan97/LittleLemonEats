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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.littlelemon.littlelemonapp.navigation.MyAppNavigation
import com.littlelemon.littlelemonapp.navigation.navigateSingleTopTo
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
                MyApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MyApp(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier) {

    val navController = rememberNavController()
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route

    // Collect states
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
    // Use the cart item count from ViewModel instead of calculating it here
    val cartItemCount by viewModel.cartItemCount.collectAsStateWithLifecycle()
    val searchPhrase by viewModel.searchPhrase.collectAsStateWithLifecycle()

    // Updated showProfile logic - show on all screens except Profile and Onboarding
    val showProfile = currentDestination != Profile.route && currentDestination != Onboarding.route
    // Determine when to show the cart (unchanged as requested)
    val showCart = currentDestination != Checkout.route && currentDestination != Onboarding.route

    Scaffold(
        topBar = {
            Header(
                showProfile = showProfile,
                showCart = showCart,
                cartItemCount = cartItemCount,
                firstName = firstName,
                lastName = lastName,
                onProfileClick = { 
                    navController.navigateSingleTopTo(Profile.route)
                },
                onCartClick = {
                    // Same navigation logic as was previously in the FAB
                    navController.navigateSingleTopTo(Checkout.route)
                },
                modifier = Modifier.windowInsetsPadding(
                    WindowInsets.statusBars.only(WindowInsetsSides.Top)
                )
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0) // Reset default window insets
    ) { scaffoldPadding ->
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
            onAddToCart = { menuItem, quantity -> viewModel.addToCart(menuItem, quantity) },
            onClearCart = { viewModel.clearCart() },
            onRemoveFromCart = { cartItem -> viewModel.removeFromCart(cartItem) },
            modifier = modifier.padding(
                top = scaffoldPadding.calculateTopPadding(),
                bottom = 0.dp
            )
        )
    }
}
