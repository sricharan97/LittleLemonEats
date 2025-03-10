package com.littlelemon.littlelemonapp.ui.profile


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.littlelemonapp.ui.composables.ActionButton
import com.littlelemon.littlelemonapp.ui.composables.ProfileAvatar
import com.littlelemon.littlelemonapp.ui.theme.LittleLemonAppTheme


@Composable
fun ProfileScreen(
    firstname: String,
    lastname: String,
    email: String,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        ProfileHeader(firstname, lastname)
        Spacer(modifier = Modifier.height(16.dp))
        ProfileInformation(firstname, lastname, email)
        Spacer(modifier = Modifier.height(16.dp))
        ActionButton(onContinueClicked = onLogout, label = "Log out")
    }


}

@Composable
fun HeaderUsernameText(firstName: String, lastName: String) {
    // Ensure the first character of each name is uppercase
    val capitalizedFirstName = firstName.replaceFirstChar { it.uppercase() }
    val capitalizedLastName = lastName.replaceFirstChar { it.uppercase() }
    val displayName = "$capitalizedFirstName $capitalizedLastName"

    Text(
        text = displayName,
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
    )
}

@Composable
fun ProfileHeader(
    firstName: String,
    lastName: String,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ProfileAvatar(firstName, lastName)
        Spacer(modifier = Modifier.height(16.dp))
        HeaderUsernameText(firstName, lastName)

    }

}

@Composable
fun ProfileInfoField(label: String, textValue: String, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = textValue,
        onValueChange = {},
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSecondary
            )
        },
        modifier = Modifier.fillMaxWidth(),
        enabled = false,   // Grays out the field
        readOnly = true    // Prevents editing
    )


}

@Composable
fun ProfileInformation(
    firstName: String,
    lastName: String,
    email: String,
    modifier: Modifier = Modifier
) {

    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = "Profile information",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        ProfileInfoField(label = "First Name", textValue = firstName)
        Spacer(modifier = Modifier.height(8.dp))
        ProfileInfoField(label = "Last Name", textValue = lastName)
        Spacer(modifier = Modifier.height(8.dp))
        ProfileInfoField(label = "Email", textValue = email)
    }
}


@Preview
@Composable
private fun ProfileAvatarPreview() {
    LittleLemonAppTheme(dynamicColor = false) {
        ProfileAvatar("Charan", "Konduri")
    }
}

@Preview
@Composable
private fun ProfileHeaderPreview() {
    LittleLemonAppTheme(dynamicColor = false) {
        Surface() {
            ProfileHeader("sarah", "anderson")
        }
    }

}

@Preview
@Composable
private fun ProfileInformationPreview() {
    LittleLemonAppTheme(dynamicColor = false) {

        Surface {
            ProfileInformation("Charan", "Konduri", "hakdn@ex.co")
        }
    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    LittleLemonAppTheme(dynamicColor = false) {
        Surface {
            ProfileScreen("Charan", "Konduri", "example.co", {})
        }
    }
}