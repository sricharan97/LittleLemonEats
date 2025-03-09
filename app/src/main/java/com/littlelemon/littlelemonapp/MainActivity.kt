package com.littlelemon.littlelemonapp

import OnboardingViewModel
import OnboardingViewModelFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.littlelemon.littlelemonapp.navigation.MyAppNavigation
import com.littlelemon.littlelemonapp.ui.onboarding.Header
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
                Scaffold(
                    topBar = { Header() },
                    modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyApp(
                        viewModel = viewModel,
                        modifier = Modifier.padding(
                            innerPadding
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun MyApp(
    viewModel: OnboardingViewModel,
    modifier: Modifier = Modifier) {

    val navController = rememberNavController()
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

     MyAppNavigation(
        navController = navController,
         onBoardingComplete = onBoardingComplete,
         userLoggedIn = userLoggedIn,
        firstName = firstName,
        firstNameError = firstNameError,
        onFirstNameChange = { viewModel.updateFirstName(it)},
        lastName = lastName,
        lastNameError = lastNameError,
        onLastNameChange = { viewModel.updateLastName(it) },
        email = email,
        emailError = emailError,
        onEmailChange = { viewModel.updateEmail(it) },
        isFormValid = isFormValid,
        onContinueClicked = { viewModel.validateAndRegister() },
        modifier = modifier
     )


}





@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LittleLemonAppTheme {

    }
}
