package com.littlelemon.littlelemonapp

import OnboardingViewModel
import OnboardingViewModelFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.littlelemon.littlelemonapp.navigation.MyAppNavigation
import com.littlelemon.littlelemonapp.ui.composables.Header
import com.littlelemon.littlelemonapp.ui.theme.LittleLemonAppTheme

class MainActivity : ComponentActivity() {

    private val viewModel: OnboardingViewModel by viewModels{
        OnboardingViewModelFactory(applicationContext)
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
    viewModel: OnboardingViewModel,
    modifier: Modifier = Modifier) {

    val navController = rememberNavController()
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

    // Navigate when onBoardingComplete changes
    LaunchedEffect(onBoardingComplete) {
        if (onBoardingComplete) {
            navController.navigate(Home.route) {
                popUpTo(Onboarding.route) { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            Header(
                showProfile = currentDestination == Home.route,
                firstName = firstName,
                lastName = lastName,
                onProfileClick = { navController.navigate(Profile.route) { launchSingleTop } }
            )
        },
    ) {

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
            modifier = modifier.padding(it)
        )

    }
}





@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LittleLemonAppTheme {

    }
}
