package com.example.tiksave.ui.screens

import android.os.Environment
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiksave.ui.viewmodel.Screen
import com.example.tiksave.ui.viewmodel.TikSaveViewModel
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TikSaveBackground
import com.example.ui.theme.TikSaveSurface
import com.example.ui.theme.VividMagenta

@Composable
fun SettingsScreen(
    viewModel: TikSaveViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val isProUser by viewModel.isProUser.collectAsState()
    val currentLanguage by viewModel.selectedLanguage.collectAsState()
    val autoClear by viewModel.autoClearHistory.collectAsState()

    var showLanguageDialog by remember { mutableStateOf(false) }
    var showClearHistoryDialog by remember { mutableStateOf(false) }

    val languages = listOf("English", "Español", "Français", "Deutsch", "Português", "हिन्दी", "Bahasa Indonesia", "Türkçe")

    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            containerColor = Color(0xFF161028),
            title = { Text("Choose Language", color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    languages.forEach { lang ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (lang == currentLanguage) Color(0xFF261942) else Color.Transparent)
                                .clickable {
                                    viewModel.setLanguage(lang)
                                    showLanguageDialog = false
                                }
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = lang, color = Color.White, fontSize = 14.sp)
                            if (lang == currentLanguage) {
                                Text("✓", color = VividMagenta, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showLanguageDialog = false }) {
                    Text("Close", color = TextMuted)
                }
            }
        )
    }

    if (showClearHistoryDialog) {
        AlertDialog(
            onDismissRequest = { showClearHistoryDialog = false },
            containerColor = Color(0xFF161028),
            title = { Text("Clear All History?", color = Color.White, fontWeight = FontWeight.Bold) },
            text = { Text("This will remove all downloaded media records from the database.", color = Color(0xFFCBD5E1), fontSize = 13.sp) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearAllHistory()
                        showClearHistoryDialog = false
                    }
                ) {
                    Text("Clear All", color = Color(0xFFEF4444), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearHistoryDialog = false }) {
                    Text("Cancel", color = TextMuted)
                }
            }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(TikSaveBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Preferences & App Settings",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        // Appearance
        item {
            SettingsCategoryHeader("Appearance & Display")
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = TikSaveSurface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x224B3869))
            ) {
                Column {
                    SettingsSwitchRow(
                        title = "Dark Neon Theme",
                        subtitle = "High contrast cyber dark palette",
                        icon = Icons.Default.DarkMode,
                        checked = isDarkMode,
                        onCheckedChange = { viewModel.setDarkMode(it) }
                    )

                    SettingsDivider()

                    SettingsClickableRow(
                        title = "App Language",
                        value = currentLanguage,
                        icon = Icons.Default.Language,
                        onClick = { showLanguageDialog = true }
                    )
                }
            }
        }

        // Downloads & Storage
        item {
            SettingsCategoryHeader("Storage & Downloads")
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = TikSaveSurface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x224B3869))
            ) {
                Column {
                    SettingsClickableRow(
                        title = "Download Location",
                        value = "Downloads / TikSavePro",
                        icon = Icons.Default.Folder,
                        onClick = {
                            viewModel.showToast("Saved into standard Android Downloads folder")
                        }
                    )

                    SettingsDivider()

                    SettingsSwitchRow(
                        title = "Auto-Clear Old History",
                        subtitle = "Keep download log compact over 30 days",
                        icon = Icons.Default.CleaningServices,
                        checked = autoClear,
                        onCheckedChange = { viewModel.setAutoClear(it) }
                    )

                    SettingsDivider()

                    SettingsClickableRow(
                        title = "Clear Download History",
                        value = "Tap to erase records",
                        icon = Icons.Default.DeleteSweep,
                        onClick = { showClearHistoryDialog = true }
                    )

                    SettingsDivider()

                    SettingsClickableRow(
                        title = "Clear Cache",
                        value = "18.4 MB cached",
                        icon = Icons.Default.CleaningServices,
                        onClick = {
                            viewModel.showToast("App cache cleared successfully")
                        }
                    )
                }
            }
        }

        // About, Privacy & Support
        item {
            SettingsCategoryHeader("About & Compliance")
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = TikSaveSurface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x224B3869))
            ) {
                Column {
                    SettingsClickableRow(
                        title = "About TikSave Pro",
                        value = "v2.4.0 (Build 2026)",
                        icon = Icons.Default.Info,
                        onClick = { viewModel.navigateTo(Screen.AboutUs) }
                    )

                    SettingsDivider()

                    SettingsClickableRow(
                        title = "Privacy Policy & Terms",
                        value = "DRM & data transparency",
                        icon = Icons.Default.Policy,
                        onClick = { viewModel.navigateTo(Screen.PrivacyPolicy) }
                    )

                    SettingsDivider()

                    SettingsClickableRow(
                        title = "Help & Contact Us",
                        value = "Submit feedback & support",
                        icon = Icons.Default.Mail,
                        onClick = { viewModel.navigateTo(Screen.ContactUs) }
                    )

                    SettingsDivider()

                    SettingsClickableRow(
                        title = "Pro Membership Status",
                        value = if (isProUser) "VIP Active" else "Free Tier (Upgrade)",
                        icon = Icons.Default.WorkspacePremium,
                        onClick = { viewModel.navigateTo(Screen.Pro) }
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SettingsCategoryHeader(title: String) {
    Text(
        text = title,
        color = NeonPurple,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
    )
}

@Composable
fun SettingsClickableRow(
    title: String,
    value: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Icon(imageVector = icon, contentDescription = null, tint = NeonPurple, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = title, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.5.sp)
                Text(text = value, color = TextMuted, fontSize = 11.5.sp)
            }
        }
        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted, modifier = Modifier.size(18.dp))
    }
}

@Composable
fun SettingsSwitchRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Icon(imageVector = icon, contentDescription = null, tint = NeonPurple, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = title, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.5.sp)
                Text(text = subtitle, color = TextMuted, fontSize = 11.5.sp)
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = VividMagenta,
                uncheckedThumbColor = TextMuted,
                uncheckedTrackColor = Color(0xFF231A3C)
            )
        )
    }
}

@Composable
fun SettingsDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color(0x1A4B3869))
    )
}
