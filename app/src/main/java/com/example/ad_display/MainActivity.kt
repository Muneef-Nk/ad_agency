
    package com.example.ad_display

    import android.os.Bundle
    import androidx.activity.ComponentActivity
    import androidx.activity.compose.setContent
    import androidx.navigation.compose.NavHost
    import androidx.navigation.compose.composable
    import androidx.navigation.compose.rememberNavController
    import com.example.ad_display.ui.LoginScreen
    import com.example.ad_display.ui.AdsScreen

    class MainActivity : ComponentActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContent {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "login"
                ) {
                    composable("login") {
                        LoginScreen(
                            onLoginSuccess = {
                                navController.navigate("ads")
                            }
                        )
                    }

                    composable("ads") {
                        AdsScreen(
                            navController = navController
                        )
                    }
                }
            }
        }
    }
