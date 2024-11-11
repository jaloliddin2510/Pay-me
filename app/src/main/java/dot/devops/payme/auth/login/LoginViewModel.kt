package dot.devops.payme.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dot.devops.payme.base.BaseViewModel
import dot.devops.payme.home.main.MainApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel : BaseViewModel() {

    private val _logInEvent = MutableLiveData<LoginUiEvent>()
    val logInEvent: LiveData<LoginUiEvent> get() = _logInEvent

    fun logIn(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            MainApplication.fbAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        _logInEvent.value = LoginUiEvent.Success("Success", it.result.user?.uid)
                    } else {
                        _logInEvent.value = LoginUiEvent.Error(it.exception?.message.toString())
                    }
                }.addOnFailureListener {
                    _logInEvent.value = LoginUiEvent.Error(it.message.toString())
                }
        }
    }
}

sealed class LoginUiEvent {
    data object Loading : LoginUiEvent()
    data class Success(val message: String, val token: String?) : LoginUiEvent()
    data class Error(val massage: String?) : LoginUiEvent()
}