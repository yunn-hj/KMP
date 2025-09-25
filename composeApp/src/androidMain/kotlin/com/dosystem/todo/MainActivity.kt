package com.dosystem.todo

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.dosystem.todo.navigation.Destination
import com.dosystem.todo.navigation.TodoNavHost

class MainActivity : ComponentActivity() {
    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permission ->
            if (permission.isEmpty()) {
                return@registerForActivityResult
            }

            if (permission.containsValue(false)) {
                Toast.makeText(this, "Permission required", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPermission()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.White.hashCode(),
                Color.Black.hashCode()
            ),
        )
        setContent {
            TodoApp()
        }
    }

    private fun initPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return

        permissionLauncher.launch(
            arrayOf(Manifest.permission.POST_NOTIFICATIONS)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoApp(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val startDestination = Destination.Weather
    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }

    Scaffold(modifier = modifier) { contentPadding ->
        Column(modifier = modifier.padding(contentPadding)) {
            PrimaryTabRow(
                contentColor = Color.Gray,
                containerColor = Color.White,
                indicator = {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(selectedDestination),
                        color = Color.Gray
                    )
                },
                selectedTabIndex = selectedDestination
            ) {
                Destination.entries.forEachIndexed { index, destination ->
                    Tab(
                        selected = selectedDestination == index,
                        onClick = {
                            navController.navigate(route = destination.route)
                            selectedDestination = index
                        },
                        text = {
                            Text(text = destination.label)
                        }
                    )
                }
            }
            TodoNavHost(navController, startDestination)
        }
    }
}