package com.example.ad_display.ui

import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ad_display.model.LoginRequest
import com.example.ad_display.network.ApiClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.sin

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Multiple animations for dynamic background
    val infiniteTransition = rememberInfiniteTransition(label = "background")

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val offset1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 50f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset1"
    )

    val offset2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -40f,
        animationSpec = infiniteRepeatable(
            animation = tween(3500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset2"
    )


    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFF6B35),
                        Color(0xFFFF8C42),
                        Color(0xFFF4A261),
                        Color(0xFFE76F51)
                    ),
                    center = androidx.compose.ui.geometry.Offset(0.3f, 0.3f)
                )
            )
    ) {
        // Animated geometric shapes
        Box(
            modifier = Modifier
                .offset(x = 50.dp + offset1.dp, y = 100.dp)
                .size(300.dp)
                .rotate(rotation * 0.5f)
                .clip(CircleShape)
                .background(Color(0xFFFFB84D).copy(alpha = 0.2f))
                .blur(80.dp)
        )

        Box(
            modifier = Modifier
                .offset(x = 200.dp, y = 500.dp + offset2.dp)
                .size(250.dp)
                .rotate(-rotation * 0.3f)
                .clip(RoundedCornerShape(60.dp))
                .background(Color(0xFFFF6B35).copy(alpha = 0.25f))
                .blur(70.dp)
        )

        Box(
            modifier = Modifier
                .offset(x = (-50).dp + offset2.dp, y = 400.dp)
                .size(200.dp)
                .rotate(rotation * 0.4f)
                .clip(CircleShape)
                .background(Color(0xFFFFA94D).copy(alpha = 0.18f))
                .blur(60.dp)
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 80.dp, y = (-50).dp + offset1.dp)
                .size(180.dp)
                .rotate(rotation * 0.6f)
                .clip(RoundedCornerShape(50.dp))
                .background(Color(0xFFFF8F5C).copy(alpha = 0.22f))
                .blur(65.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo section with animated scale
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFFF6B35),
                                Color(0xFFFF8C42)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "AD",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    letterSpacing = 2.sp
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Title
            Text(
                text = "Ad Agency",
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                letterSpacing = 1.5.sp
            )

            Text(
                text = "Display System",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.85f),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 8.dp),
                letterSpacing = 3.sp
            )

            Spacer(modifier = Modifier.height(60.dp))

            // Login Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(36.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Welcome",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF6B35)
                    )

                    Text(
                        text = "Enter your username to continue",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Username Field
                    OutlinedTextField(
                        value = username,
                        onValueChange = {
                            username = it
                            errorMessage = ""
                        },
                        label = { Text("Username") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                tint = Color(0xFFFF6B35)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color(0xFF333333),
                            unfocusedTextColor = Color(0xFF666666),
                            focusedBorderColor = Color(0xFFFF6B35),
                            unfocusedBorderColor = Color(0xFFCCCCCC),
                            cursorColor = Color(0xFFFF6B35),
                            focusedLabelColor = Color(0xFFFF6B35),
                            unfocusedLabelColor = Color(0xFF999999)
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Password Field
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            errorMessage = ""
                        },
                        label = { Text("Password") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Lock,
                                contentDescription = null,
                                tint = Color(0xFFFF6B35)
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Visibility
                                    else Icons.Default.VisibilityOff,
                                    contentDescription = "Toggle password",
                                    tint = Color(0xFF999999)
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None
                        else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color(0xFF333333),
                            unfocusedTextColor = Color(0xFF666666),
                            focusedBorderColor = Color(0xFFFF6B35),
                            unfocusedBorderColor = Color(0xFFCCCCCC),
                            cursorColor = Color(0xFFFF6B35),
                            focusedLabelColor = Color(0xFFFF6B35),
                            unfocusedLabelColor = Color(0xFF999999)
                        )
                    )

                    // Error Message
                    if (errorMessage.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = errorMessage,
                            color = Color(0xFFE63946),
                            fontSize = 13.sp,
                            modifier = Modifier.align(Alignment.Start)
                        )
                    }

                    Spacer(modifier = Modifier.height(36.dp))
                    val scope = rememberCoroutineScope()
                    // Login Button
                    Button(
                        onClick = {
                            if (username.isEmpty() || password.isEmpty()) {
                                errorMessage = "Please fill all fields"
                                return@Button
                            }
                            isLoading = true
                            errorMessage = ""

                            // API call
                            scope.launch {
                                try {
                                    val response = ApiClient.apiService.login(
                                        LoginRequest(username, password)
                                    )
                                    println("Access: ${response.access}, Refresh: ${response.refresh}")
                                    println("response $response")
                                    Log.d("LoginScreen", "API response: $response")

                                    // Save tokens if needed
                                     TokenManager.saveTokens(context, response.access, response.refresh)

                                    // Navigate to Ads screen
                                    onLoginSuccess()
                                } catch (e: Exception) {
                                    errorMessage = "Login failed: ${e.message}"
                                    Log.d("LoginScreen", "Login failed: ${e.message}")
                                } finally {
                                    isLoading = false
                                }
                            }

                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        contentPadding = PaddingValues(0.dp),
                        enabled = !isLoading
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(0xFFFF6B35),
                                            Color(0xFFFF8C42),
                                            Color(0xFFFFA94D)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(28.dp),
                                    strokeWidth = 3.dp
                                )

                                LaunchedEffect(Unit) {
                                    delay(1200)
                                    onLoginSuccess()
                                }
                            } else {
                                Text(
                                    text = "LOGIN",
                                    color = Color.White,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 2.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Footer
            Text(
                text = "Ad Display Management System",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp,
                letterSpacing = 1.sp
            )
        }
    }
}