package com.littlelemon.littlelemonapp.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.littlelemon.MenuListScreen
import com.littlelemon.littlelemonapp.Home
import com.littlelemon.littlelemonapp.MenuItem
import com.littlelemon.littlelemonapp.MenuList
import com.littlelemon.littlelemonapp.Onboarding
import com.littlelemon.littlelemonapp.Profile
import com.littlelemon.littlelemonapp.ui.home.HomeScreen
import com.littlelemon.littlelemonapp.ui.menu.MenuItemScreen
import com.littlelemon.littlelemonapp.ui.onboarding.OnboardingScreen
import com.littlelemon.littlelemonapp.ui.profile.ProfileScreen
import kotlinx.coroutines.launch

@Composable
fun MyAppNavigation(
    navController: NavHostController,
    onBoardingComplete : Boolean = false,
    userLoggedIn : Boolean = false,
    firstName:String,
    firstNameError:String?,
    onFirstNameChange:(String) -> Unit,
    lastName:String,
    lastNameError:String?,
    onLastNameChange:(String) -> Unit,
    email:String,
    emailError:String?,
    onEmailChange:(String) -> Unit,
    isFormValid:Boolean,
    onContinueClicked:() -> Unit,
    onLogout: () -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier) {

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle navigation when onBoardingComplete changes
    LaunchedEffect(onBoardingComplete) {
        if (onBoardingComplete) {
            navController.navigateSingleTopTo(Home.route)
        }
    }

  Scaffold (
      snackbarHost = {SnackbarHost(hostState = snackbarHostState)}

      ){ padding ->
      NavHost(
          navController = navController,
          startDestination = if (userLoggedIn) Home.route else Onboarding.route,
          modifier = modifier.padding(padding)
      ) {
          composable(route = Home.route) {
              HomeScreen(onOrderTakeAway = {
                  navController.navigate(MenuList.route)
              })
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
                            snackbarHostState.showSnackbar(message = message,
                                duration = SnackbarDuration.Short)
                      }

                  }
              )
          }

          composable(route = MenuList.route) {
              MenuListScreen()
          }

          composable(route = Profile.route) {
              ProfileScreen(
                  firstname = firstName,
                  lastname = lastName,
                  email = email,
                  onLogout = {
                      onLogout()
                      navController.navigateSingleTopTo(Onboarding.route)
                  }
              )
          }

          composable(
              route = MenuItem.routeWithArgs,
              arguments = MenuItem.arguments
          ) { backStackEntry ->
              val itemId = backStackEntry.arguments?.getString(MenuItem.itemIdArg)
              MenuItemScreen(itemId)
          }
      }
  }
}

private fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        popUpTo (
            this@navigateSingleTopTo.graph.findStartDestination().id
        ){
            saveState = true
        }

        launchSingleTop = true
        restoreState = true
    }


private fun NavHostController.navigateToMenuItem(itemId: String) {
    this.navigateSingleTopTo("${MenuItem.route}/$itemId")
}