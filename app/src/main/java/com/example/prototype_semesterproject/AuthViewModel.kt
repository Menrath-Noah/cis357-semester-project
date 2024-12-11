package com.example.prototype_semesterproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class AuthViewModel: ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    private val _authSuccess = MutableLiveData<Boolean>()
    val authSuccess: LiveData<Boolean?> get() = _authSuccess

    private val _authError = MutableLiveData<String?>()
    val authError: LiveData<String?> get() = _authError

    fun login(email:String, password:String, onAuthenticated: (String) -> Unit) {
        if(email.isBlank() || password.isBlank()){
            _authError.postValue("Email or password cannot be empty")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {

                    _authSuccess.postValue(true)
                    _authError.postValue(null)
                    onAuthenticated(it.user!!.uid)
                }
                .addOnFailureListener { exception ->
                    _authSuccess.postValue(false)
                    _authError.postValue(exception.message)
                }
        }
    }

    private val _createAccountSuccess = MutableLiveData<Boolean?>()
    val createAccountSuccess: LiveData<Boolean?> = _createAccountSuccess

    private val _createAccountError = MutableLiveData<String?>()
    val createAccountError: LiveData<String?> = _createAccountError

    fun createAccount(email: String, name: String, password: String, onComplete: (String?) -> Unit) {
        _createAccountSuccess.value = null
        _createAccountError.value = null

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _createAccountSuccess.postValue(true)
                _createAccountError.postValue(null)
                var changeName = userProfileChangeRequest { displayName = name }
                auth.currentUser?.updateProfile(changeName)
                onComplete(it.user!!.uid)
            }
            .addOnFailureListener { exception ->
                _createAccountSuccess.postValue(false)
                _createAccountError.postValue(exception.message)
            }
    }


}