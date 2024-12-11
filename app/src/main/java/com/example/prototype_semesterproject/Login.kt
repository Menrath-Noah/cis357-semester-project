package com.example.prototype_semesterproject


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun Login(
    modifier: Modifier = Modifier,
    navController: NavController,
    vm: AuthViewModel = AuthViewModel(),
    onLoginSuccess: (String) -> Unit
)
{
    var username by remember { mutableStateOf("test@test.com") }
    var password by remember { mutableStateOf("123456") }
    val authSuccess = vm.authSuccess.observeAsState()
    val authError = vm.authError.observeAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Welcome to Cubeshake",
            fontSize = 30.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Email") },
            isError = authSuccess.value == false,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            isError = authSuccess.value == false,
            modifier = Modifier.fillMaxWidth()
        )

        if (authSuccess.value == false) {
            Text(
                text = authError.value ?: "Login failed, please try again.",
                color = androidx.compose.ui.graphics.Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                vm.login(username, password) { uid ->
                    if (uid != null) {
                        onLoginSuccess(uid)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                navController.navigate("signup")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign Up")
        }
    }
}


@Composable
fun CreateAccount(
    modifier: Modifier = Modifier,
    navController: NavController,
    vm: AuthViewModel = AuthViewModel(),
) {
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val createAccountSuccess = vm.createAccountSuccess.observeAsState()
    val createAccountError = vm.createAccountError.observeAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        if (createAccountSuccess.value == false) {
            Text(
                text = createAccountError.value ?: "Account creation failed, please try again.",
                color = androidx.compose.ui.graphics.Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                vm.createAccount(email, name, password) { uid ->
                    if (uid != null) {
                        navController.navigate("game_config/$uid")
                    }
                }
            },
            enabled = email.isNotBlank() && name.isNotBlank() && password.isNotBlank() && confirmPassword == password,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Account")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancel")
        }
    }
}
