// OnboardingViewModel.kt
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.littlelemon.littlelemonapp.data.UserPreferences
import com.littlelemon.littlelemonapp.data.UserPreferencesRepository
import com.littlelemon.littlelemonapp.utils.Validator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class OnboardingViewModel(
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val validator = Validator()

    private val _onboardingComplete = MutableStateFlow(false)
    val onboardingComplete: StateFlow<Boolean> = _onboardingComplete.asStateFlow()

    private val _firstName = MutableStateFlow("")
    val firstName: StateFlow<String> = _firstName.asStateFlow()

    private val _lastName = MutableStateFlow("")
    val lastName: StateFlow<String> = _lastName.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _firstNameError = MutableStateFlow<String?>(null)
    val firstNameError: StateFlow<String?> = _firstNameError.asStateFlow()

    private val _lastNameError = MutableStateFlow<String?>(null)
    val lastNameError: StateFlow<String?> = _lastNameError.asStateFlow()

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError.asStateFlow()

    private val _isFormValid = MutableStateFlow(false)
    val isFormValid: StateFlow<Boolean> = _isFormValid.asStateFlow()

    private val _userLoggedIn = MutableStateFlow(userPreferences.isLoggedIn())
    val userLoggedIn: StateFlow<Boolean> = _userLoggedIn.asStateFlow()

    init {
        // Load saved user data when ViewModel is created
        userPreferences.getUserData()?.let { userData ->
            _firstName.value = userData.firstName
            _lastName.value = userData.lastName
            _email.value = userData.email
        }
    }

    fun updateFirstName(name: String) {
        _firstName.value = name
        validateField(name, validator::validateName, _firstNameError)
        checkFormValidity()
    }

    fun updateLastName(name: String) {
        _lastName.value = name
        validateField(name, validator::validateName, _lastNameError)
        checkFormValidity()
    }

    fun updateEmail(email: String) {
        _email.value = email
        validateField(email, validator::validateEmail, _emailError)
        checkFormValidity()
    }

    private fun <T> validateField(
        value: T,
        validator: (T) -> Result<T>,
        errorFlow: MutableStateFlow<String?>
    ) {
        validator(value).fold(
            onSuccess = { errorFlow.value = null },
            onFailure = { errorFlow.value = it.message }
        )
    }

    private fun checkFormValidity() {
        val hasNoErrors = _firstNameError.value == null &&
                _lastNameError.value == null &&
                _emailError.value == null

        val allFieldsFilled = _firstName.value.isNotEmpty() &&
                _lastName.value.isNotEmpty() &&
                _email.value.isNotEmpty()

        _isFormValid.value = hasNoErrors && allFieldsFilled
    }

    fun validateAndRegister(): Boolean {
        val validationResults = listOf(
            validator.validateName(_firstName.value),
            validator.validateName(_lastName.value),
            validator.validateEmail(_email.value)
        )

        val allFieldsValid = validationResults.all { it.isSuccess }

        if (allFieldsValid) {
            saveUserData()
        }

        return allFieldsValid
    }

    private fun saveUserData() {
        userPreferences.saveUserData(
            firstName = _firstName.value,
            lastName = _lastName.value,
            email = _email.value
        )

        _onboardingComplete.value = true
    }

    fun logout() {
        userPreferences.clearUserData()
        _userLoggedIn.value = false
        _firstName.value = ""
        _lastName.value = ""
        _email.value = ""
        _onboardingComplete.value = false
    }

}

class OnboardingViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OnboardingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OnboardingViewModel(UserPreferencesRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}