// MainViewModel.kt
package com.littlelemon.littlelemonapp
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.littlelemon.littlelemonapp.data.CartItem
import com.littlelemon.littlelemonapp.data.MenuDatabase
import com.littlelemon.littlelemonapp.data.MenuFetch
import com.littlelemon.littlelemonapp.data.MenuItemEntity
import com.littlelemon.littlelemonapp.data.MenuRepository
import com.littlelemon.littlelemonapp.data.UserPreferences
import com.littlelemon.littlelemonapp.data.UserPreferencesRepository
import com.littlelemon.littlelemonapp.network.MenuItemServiceImpl
import com.littlelemon.littlelemonapp.utils.Validator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val TAG = "MainViewModel"


class MainViewModel(
    private val userPreferences: UserPreferences,
    private val menuFetch: MenuFetch
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

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _searchPhrase = MutableStateFlow("")
    val searchPhrase: StateFlow<String> = _searchPhrase.asStateFlow()

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems = _cartItems.asStateFlow()

    val menuItems = menuFetch.menuItems.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    init {
        // Load saved user data when ViewModel is created
        userPreferences.getUserData()?.let { userData ->
            _firstName.value = userData.firstName
            _lastName.value = userData.lastName
            _email.value = userData.email
        }

        refreshMenuItems()
    }

    private fun refreshMenuItems() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                Log.d("OnboardingViewModel", "Starting menu refresh")
                menuFetch.refreshMenu()
                Log.d("OnboardingViewModel", "Menu refresh completed successfully")
            } catch (e: Exception) {
                // Handle error
                Log.e("OnboardingViewModel", "Menu refresh failed", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateSearchPhrase(phrase: String) {
        _searchPhrase.value = phrase
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

    fun addToCart(menuItem: MenuItemEntity, quantity: Int) {
        val currentCart = _cartItems.value.toMutableList()
        val existingItem = currentCart.find { it.menuItem.id == menuItem.id }
        if (existingItem != null) {
            // Update quantity
            existingItem.quantity += quantity
        } else {
            currentCart.add(CartItem(menuItem, quantity))
        }
        _cartItems.value = currentCart
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

}

class MainViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            val menuService = MenuItemServiceImpl()
            val database = MenuDatabase.getDatabase(context)
            val menuRepository = MenuRepository(menuService, database)
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(
                UserPreferencesRepository(context),
                menuFetch = menuRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
