package com.kl3jvi.insightoid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.kl3jvi.insightoid.ui.theme.InsightoidTheme
import com.kl3jvi.insightoid_api.sdk.Insightoid
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Insightoid.initialize(this)

        // launch a coroutine and wait for three seconds then throw a runtime error
        // this will be caught by the exception handler
        // and the crash data will be sent to the server
        lifecycleScope.launch {
            delay(3000)
//            throw RuntimeException("Test Crash")
        }

        val a = 1
        println(1 / 0)

        setContent {
            InsightoidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    InsightoidTheme {
        Greeting("Android")
    }
}
