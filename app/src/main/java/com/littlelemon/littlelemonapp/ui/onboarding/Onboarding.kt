package com.littlelemon.littlelemonapp.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.littlelemonapp.R
import com.littlelemon.littlelemonapp.ui.theme.LittleLemonAppTheme

@Composable
fun OnboardingScreen(
    firstName: String,
    firstNameError: String? ,
    onFirstNameChange: (String) -> Unit,
    lastName: String,
    lastNameError: String? ,
    onLastNameChange: (String) -> Unit,
    email: String,
    emailError: String? ,
    onEmailChange: (String) -> Unit,
    isFormValid: Boolean,
    onContinueClicked: () -> Unit ,
    onShowMessage : (String) -> Unit,
    modifier: Modifier = Modifier) {

    var validationMessage by remember { mutableStateOf<String?>(null) }



        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
        ) {
            //Header()
            Title()
            Text(
                text = "Personal information", style = MaterialTheme.typography.titleMedium,
                modifier = modifier.padding(top = 40.dp, bottom = 24.dp, start = 16.dp)
            )
            InputSection(
                firstName = firstName,
                firstNameError = firstNameError,
                onFirstNameChange = onFirstNameChange,
                lastName = lastName,
                lastNameError = lastNameError,
                onLastNameChange = onLastNameChange,
                email = email,
                emailError = emailError,
                onEmailChange = onEmailChange
            )
            RegisterButton(onContinueClicked = {
                validationMessage = if (isFormValid) {
                    "Registration successful!"

                } else {
                    "Registration unsuccessful. Please enter all data."
                }
                onShowMessage(validationMessage!!)
                onContinueClicked()
            })
        }

    }




@Composable
fun Header(modifier: Modifier = Modifier) {
    Surface (
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.little_lemon_logo),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.padding(24.dp)
        )
    }
}


@Composable
fun Title(modifier: Modifier = Modifier) {
    Surface (
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp),
        color = MaterialTheme.colorScheme.primary
    ) {
        Box (
            modifier = modifier,
            contentAlignment = Alignment.Center)
        {
            Text(
                text = "Let's get to know you",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
    
}

@Composable
fun InputField(inputName: String,
               inputHint: String,
               value: String,
               onValueChange: (String) -> Unit,
               errorMessage: String? ,
               modifier: Modifier =Modifier) {

        Column(modifier = modifier
            .padding(start = 16.dp, end = 32.dp, top = 16.dp, bottom = 16.dp)
            .background(Color.White)) {
            Text(
                text = inputName,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = { Text(text = inputHint) },
                shape = RoundedCornerShape(8.dp),
                isError = errorMessage != null,
                supportingText = {
                    errorMessage?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth())
        }

}

@Composable
fun RegisterButton(onContinueClicked: () -> Unit, modifier: Modifier = Modifier) {
    Button(onClick = onContinueClicked , modifier = modifier
        .fillMaxWidth()
        .padding(24.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = Color.Black)){
        Text(text = "Register", style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(8.dp))
    }
}

@Composable
fun InputSection(
    firstName: String,
    firstNameError: String? ,
    onFirstNameChange: (String) -> Unit,
    lastName: String,
    lastNameError: String? ,
    onLastNameChange: (String) -> Unit,
    email: String,
    emailError: String? ,
    onEmailChange: (String) -> Unit,

) {
    Surface {
        Column {
            InputField(
                inputName = "First name",
                inputHint = "Enter your first name",
                value = firstName,
                onValueChange = onFirstNameChange,
                errorMessage = firstNameError)
            InputField(
                inputName = "Last name",
                inputHint = "Enter your last name",
                value = lastName,
                onValueChange = onLastNameChange,
                errorMessage = lastNameError)
            InputField(
                inputName = "Email",
                inputHint = "Enter your email",
                value = email,
                onValueChange = onEmailChange,
                errorMessage = emailError)

        }
    }
    
}

@Preview
@Composable
private fun HeaderPreview() {
   LittleLemonAppTheme(dynamicColor = false) {
       Header()
   }

}


@Preview
@Composable
private fun TitlePreview() {
    LittleLemonAppTheme(dynamicColor = false) {
        Title()
    }
}

@Preview
@Composable
private fun InputFieldPreview() {
    InputField("First name", "Enter your first name",
        "example", {}, errorMessage = null)
}

@Preview
@Composable
private fun ButtonPreview() {
  LittleLemonAppTheme(dynamicColor = false) {
      RegisterButton(onContinueClicked = {})
  }

}

@Preview
@Composable
private fun InputSectionPreview() {
    LittleLemonAppTheme(dynamicColor = false) {
        InputSection(firstName = "firstName",
            firstNameError = null,
            onFirstNameChange = {  },
            lastName = "lastName",
            lastNameError = null,
            onLastNameChange = {  },
            email = "email",
            emailError = null,
            onEmailChange = {  }
            )
    }
    
}

@Preview(showBackground = true)
@Composable
private fun OnboardingPreview() {
    LittleLemonAppTheme(dynamicColor = false) {
        OnboardingScreen(firstName = "firstName",
            firstNameError = null,
            onFirstNameChange = { },
            lastName = "lastName",
            lastNameError = null,
            onLastNameChange = { },
            email = "email",
            emailError = null,
            onEmailChange = { },
            isFormValid = true,
            onContinueClicked = {},
            onShowMessage = {})
    }
}