package inc.pneuma.mybaby.ui.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import inc.pneuma.mybaby.ui.theme.MyBabyTheme

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<ClientViewModel>()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            viewModel.startSplash(this@MainActivity)
            setKeepOnScreenCondition {
                !viewModel.navigateToLogin.value
            }
        }

        setContent {
            MyBabyTheme {
                MainScreen(this, viewModel)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyBabyTheme {
        //SignInScreen()
    }
}