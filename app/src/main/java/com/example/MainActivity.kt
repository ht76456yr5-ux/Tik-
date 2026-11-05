package com.example

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.tiksave.ui.components.BatchDownloadDialog
import com.example.tiksave.ui.components.TikSaveBottomBar
import com.example.tiksave.ui.components.TikSaveTopBar
import com.example.tiksave.ui.components.VideoPlayerDialog
import com.example.tiksave.ui.screens.AboutUsScreen
import com.example.tiksave.ui.screens.ContactUsScreen
import com.example.tiksave.ui.screens.DownloadsScreen
import com.example.tiksave.ui.screens.FavoritesScreen
import com.example.tiksave.ui.screens.HistoryScreen
import com.example.tiksave.ui.screens.HomeScreen
import com.example.tiksave.ui.screens.PrivacyPolicyScreen
import com.example.tiksave.ui.screens.ProScreen
import com.example.tiksave.ui.screens.SettingsScreen
import com.example.tiksave.ui.screens.WhatsAppStatusScreen
import com.example.tiksave.ui.viewmodel.Screen
import com.example.tiksave.ui.viewmodel.TikSaveViewModel
import com.example.ui.theme.TikSaveBackground
import com.example.ui.theme.TikSaveProTheme

class MainActivity : ComponentActivity() {

    private val viewModel: TikSaveViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Handle shared URL via Android Share Sheet
        handleShareIntent(intent)

        setContent {
            val isDarkMode by viewModel.isDarkMode.collectAsState()

            TikSaveProTheme(darkTheme = isDarkMode) {
                TikSaveApp(viewModel = viewModel)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleShareIntent(intent)
    }

    private fun handleShareIntent(intent: Intent?) {
        if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain") {
            val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
            if (!sharedText.isNullOrBlank()) {
                viewModel.onUrlChanged(sharedText)
                viewModel.fetchMediaInfo()
                viewModel.navigateTo(Screen.Home)
            }
        }
    }
}

@Composable
fun TikSaveApp(viewModel: TikSaveViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val isProUser by viewModel.isProUser.collectAsState()
    val activeDownloads by viewModel.activeDownloads.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()

    val previewPath by viewModel.previewVideoPath.collectAsState()
    val previewTitle by viewModel.previewVideoTitle.collectAsState()
    val showBatchDialog by viewModel.showBatchDialog.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearToast()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(TikSaveBackground),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            TikSaveTopBar(
                currentScreen = currentScreen,
                isProUser = isProUser,
                onNavigate = { viewModel.navigateTo(it) }
            )
        },
        bottomBar = {
            TikSaveBottomBar(
                currentScreen = currentScreen,
                activeDownloadCount = activeDownloads.size,
                onSelectScreen = { viewModel.navigateTo(it) }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(TikSaveBackground)
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "ScreenTransition"
            ) { screen ->
                when (screen) {
                    Screen.Home -> HomeScreen(viewModel = viewModel)
                    Screen.Downloads -> DownloadsScreen(viewModel = viewModel)
                    Screen.History -> HistoryScreen(viewModel = viewModel)
                    Screen.Favorites -> FavoritesScreen(viewModel = viewModel)
                    Screen.Pro -> ProScreen(viewModel = viewModel)
                    Screen.Settings -> SettingsScreen(viewModel = viewModel)
                    Screen.WhatsAppStatus -> WhatsAppStatusScreen(viewModel = viewModel)
                    Screen.AboutUs -> AboutUsScreen(viewModel = viewModel)
                    Screen.PrivacyPolicy -> PrivacyPolicyScreen(viewModel = viewModel)
                    Screen.ContactUs -> ContactUsScreen(viewModel = viewModel)
                }
            }

            // Video Player Dialog
            previewPath?.let { path ->
                VideoPlayerDialog(
                    filePath = path,
                    title = previewTitle ?: "Media Player",
                    onDismiss = { viewModel.closeVideoPreview() }
                )
            }

            // Batch Download Dialog
            if (showBatchDialog) {
                BatchDownloadDialog(
                    onDismiss = { viewModel.closeBatchDialog() },
                    onSubmit = { urls -> viewModel.executeBatchDownload(urls) }
                )
            }
        }
    }
}
