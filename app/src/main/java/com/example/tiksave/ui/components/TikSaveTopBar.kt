package com.example.tiksave.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiksave.ui.viewmodel.Screen
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.ProGoldGradient
import com.example.ui.theme.TextMuted
import com.example.ui.theme.VividMagenta

@Composable
fun TikSaveTopBar(
    currentScreen: Screen,
    isProUser: Boolean,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF0D091A))
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    listOf(Color(0x33A855F7), Color(0x00000000))
                ),
                shape = androidx.compose.ui.graphics.RectangleShape
            )
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Logo & Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onNavigate(Screen.Home) }
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            Brush.linearGradient(listOf(NeonPurple, VividMagenta))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "⚡",
                        fontSize = 20.sp
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "TikSave",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 19.sp,
                            letterSpacing = (-0.3).sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(
                                    if (isProUser) ProGoldGradient
                                    else Brush.horizontalGradient(listOf(NeonPurple, VividMagenta))
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (isProUser) "VIP" else "PRO",
                                color = if (isProUser) Color.Black else Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 10.sp
                            )
                        }
                    }
                    Text(
                        text = if (isProUser) "Ad-Free • Ultra HD Active" else "Social Media Downloader",
                        color = TextMuted,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Action Icons
            Row(verticalAlignment = Alignment.CenterVertically) {
                // WhatsApp Status Saver shortcut
                IconButton(
                    onClick = { onNavigate(Screen.WhatsAppStatus) },
                    modifier = Modifier.size(38.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Chat,
                        contentDescription = "WhatsApp Status",
                        tint = Color(0xFF25D366),
                        modifier = Modifier.size(20.dp)
                    )
                }

                // History shortcut
                IconButton(
                    onClick = { onNavigate(Screen.History) },
                    modifier = Modifier.size(38.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.History,
                        contentDescription = "History",
                        tint = Color(0xFFCBD5E1),
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Pro Membership button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            if (isProUser) Color(0x33F59E0B)
                            else Color(0x33A855F7)
                        )
                        .border(
                            width = 1.dp,
                            color = if (isProUser) Color(0xFFF59E0B) else NeonPurple,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable { onNavigate(Screen.Pro) }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.WorkspacePremium,
                            contentDescription = "Pro",
                            tint = if (isProUser) Color(0xFFFCD34D) else NeonPurple,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isProUser) "Pro Active" else "Get Pro",
                            color = if (isProUser) Color(0xFFFCD34D) else Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
