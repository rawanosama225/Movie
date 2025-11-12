package com.example.myfinalproject.Auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfinalproject.Model.Data.AuthMode
import com.example.myfinalproject.Model.Data.AuthUiState
import com.example.myfinalproject.R



val netflixFont = FontFamily(Font(R.font.one))

@Composable
fun AuthScreenNew(
    mode: AuthMode,
    state: AuthUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onSwitchMode: () -> Unit
) {
    val isSignUp = mode == AuthMode.SIGN_UP

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color.Black, Color(0xFF0E0E0E))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            AuthHeader()

            Spacer(modifier = Modifier.height(30.dp))

            AuthTextFields(
                isSignUp = isSignUp,
                state = state,
                onEmailChange = onEmailChange,
                onPasswordChange = onPasswordChange,
                onConfirmPasswordChange = onConfirmPasswordChange,
                onUsernameChange = onUsernameChange
            )

            Spacer(modifier = Modifier.height(20.dp))

            AuthButton(
                text = if (isSignUp) "Sign Up" else "Sign In",
                onClick = onSubmit
            )

            Spacer(modifier = Modifier.height(16.dp))

            SwitchAuthMode(
                isSignUp = isSignUp,
                onSwitchMode = onSwitchMode
            )
        }

        if (state.isLoading) {
            CircularProgressIndicator(color = Color.Red)
        }

        state.errorMessage?.let {
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) { Text(it, color = Color.White) }
        }
    }
}

@Composable
fun AuthHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "MOVIE\nVIBE",
            fontFamily = netflixFont,
            fontSize = 48.sp,
            color = Color.White,
            lineHeight = 40.sp,
            textAlign = TextAlign.Center
        )
    }
}


@Composable
fun AuthTextFields(
    isSignUp: Boolean,
    state: AuthUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        if (isSignUp) {
            CustomTextField(
                value = state.userName ?: "",
                label = "Username",
                onValueChange = onUsernameChange
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        CustomTextField(
            value = state.email,
            label = "Email",
            onValueChange = onEmailChange,
            keyboardType = KeyboardType.Email
        )

        Spacer(modifier = Modifier.height(12.dp))

        CustomTextField(
            value = state.password,
            label = "Password",
            onValueChange = onPasswordChange,
            isPassword = true,
            isVisible = passwordVisible,
            onVisibilityChange = { passwordVisible = it }
        )

        if (isSignUp) {
            Spacer(modifier = Modifier.height(12.dp))
            CustomTextField(
                value = state.confirmPassword,
                label = "Confirm Password",
                onValueChange = onConfirmPasswordChange,
                isPassword = true
            )
        }
    }
}

@Composable
fun CustomTextField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    isVisible: Boolean = false,
    onVisibilityChange: ((Boolean) -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.Gray) },
        singleLine = true,
        visualTransformation = if (isPassword && !isVisible) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier
            .fillMaxWidth(0.85f),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Red,
            unfocusedBorderColor = Color.DarkGray,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            cursorColor = Color.Red
        )
    )
}

@Composable
fun AuthButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFE50914),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun SwitchAuthMode(isSignUp: Boolean, onSwitchMode: () -> Unit) {
    Row(horizontalArrangement = Arrangement.Center) {
        Text(
            text = if (isSignUp) "Already have an account? " else "Don't have an account? ",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp
        )
        Text(
            text = if (isSignUp) "Sign In" else "Sign Up",
            color = Color(0xFFE50914),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.clickable { onSwitchMode() }
        )
    }
}
