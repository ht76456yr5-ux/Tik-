package com.example.tiksave.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiksave.data.preferences.AppAccentTheme
import com.example.tiksave.ui.components.GlowButton
import com.example.tiksave.ui.viewmodel.TikSaveViewModel
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.PrimaryGradient
import com.example.ui.theme.ProGoldGradient
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TikSaveBackground
import com.example.ui.theme.TikSaveSurface
import com.example.ui.theme.VividMagenta

data class ProFeatureItem(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val iconColor: Color
)

@Composable
fun ProScreen(
    viewModel: TikSaveViewModel,
    modifier: Modifier = Modifier
) {
    val isProUser by viewModel.isProUser.collectAsState()
    val proPlan by viewModel.proTierPlan.collectAsState()
    val currentAccent by viewModel.accentTheme.collectAsState()

    var selectedPlanIndex by remember { mutableStateOf(1) } // Default to Yearly VIP
    val plans = listOf(
        Triple("Monthly Pass", "$4.99/mo", "Billed monthly"),
        Triple("Yearly VIP", "$29.99/yr", "Best Value • 50% OFF ($2.49/mo)"),
        Triple("Lifetime Access", "$49.99", "One-time payment • Forever VIP")
    )

    val proFeatures = listOf(
        ProFeatureItem("100% Ad-Free Experience", "Zero banners, zero interruptions, uninterrupted downloads", Icons.Default.Block, VividMagenta),
        ProFeatureItem("Ultra HD & 4K Resolution", "Extract the highest available quality without compression", Icons.Default.HighQuality, NeonPurple),
        ProFeatureItem("Concurrent Batch Downloads", "Queue dozens of video links simultaneously", Icons.Default.Layers, Color(0xFF06B6D4)),
        ProFeatureItem("Turbo Speed Engine", "Multi-thread CDN acceleration with priority servers", Icons.Default.Speed, Color(0xFF10B981)),
        ProFeatureItem("Exclusive Cyber Themes", "Unlock Neon Purple, Sunset Magenta, and Matrix Cyan themes", Icons.Default.Palette, Color(0xFFF59E0B)),
        ProFeatureItem("Unlimited Offline Archiving", "No daily download limits subject to fair-use policies", Icons.Default.Bolt, Color(0xFFE1306C))
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(TikSaveBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Pro Status Header
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        if (isProUser) {
                            Brush.verticalGradient(
                                listOf(Color(0xFF2C1E08), Color(0xFF181005))
                            )
                        } else {
                            Brush.verticalGradient(
                                listOf(Color(0xFF2A1544), Color(0xFF150C28))
                            )
                        }
                    )
                    .border(
                        1.5.dp,
                        if (isProUser) Color(0xFFF59E0B) else Color(0x66A855F7),
                        RoundedCornerShape(20.dp)
                    )
                    .padding(20.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(
                                if (isProUser) ProGoldGradient else PrimaryGradient
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.WorkspacePremium,
                            contentDescription = null,
                            tint = if (isProUser) Color.Black else Color.White,
                            modifier = Modifier.size(34.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (isProUser) "TikSave Pro VIP Active" else "Upgrade to TikSave Pro",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (isProUser) "Current Plan: $proPlan" else "Unleash max download speed, zero ads & batch queues",
                        color = if (isProUser) Color(0xFFFCD34D) else TextSecondary,
                        fontSize = 12.5.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    if (isProUser) {
                        OutlinedButton(
                            onClick = { viewModel.cancelProSubscription() },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFCA5A5))
                        ) {
                            Text("Switch Back to Free Tier", fontSize = 12.sp)
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0x33A855F7))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Fair-Use Verified • 3-Day Free Trial",
                                color = NeonPurple,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Plan Selection Cards
        if (!isProUser) {
            item {
                Text(
                    text = "Select Your Plan",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    plans.forEachIndexed { index, (title, price, subtitle) ->
                        val isSelected = selectedPlanIndex == index
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedPlanIndex = index },
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) Color(0xFF261942) else TikSaveSurface
                            ),
                            shape = RoundedCornerShape(16.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                if (isSelected) 1.5.dp else 1.dp,
                                if (isSelected) VividMagenta else Color(0x334B3869)
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(22.dp)
                                            .clip(CircleShape)
                                            .background(if (isSelected) VividMagenta else Color.Transparent)
                                            .border(1.5.dp, if (isSelected) VividMagenta else Color(0x66FFFFFF), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (isSelected) {
                                            Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column {
                                        Text(text = title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Text(text = subtitle, color = if (isSelected) Color(0xFFF472B6) else TextMuted, fontSize = 11.5.sp)
                                    }
                                }

                                Text(text = price, color = Color.White, fontWeight = FontWeight.Black, fontSize = 15.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Activate button
                GlowButton(
                    text = "Unlock Pro with ${plans[selectedPlanIndex].first}",
                    icon = Icons.Default.WorkspacePremium,
                    onClick = {
                        val chosen = plans[selectedPlanIndex]
                        viewModel.upgradeToPro("${chosen.first} (${chosen.second})")
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Exclusive Theme Selector (for Pro or testing)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = TikSaveSurface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x334B3869))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Exclusive Pro Neon Themes",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .then(
                                    if (isProUser) Modifier.background(ProGoldGradient)
                                    else Modifier.background(Color(0x33A855F7))
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (isProUser) "UNLOCKED" else "VIP ONLY",
                                color = if (isProUser) Color.Black else Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        AppAccentTheme.entries.forEach { theme ->
                            val isSelected = currentAccent == theme
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(Color(theme.primaryHex), Color(theme.secondaryHex))
                                        )
                                    )
                                    .border(
                                        2.dp,
                                        if (isSelected) Color.White else Color.Transparent,
                                        RoundedCornerShape(12.dp)
                                    )
                                    .clickable {
                                        if (isProUser) {
                                            viewModel.setAccentTheme(theme)
                                            viewModel.showToast("Applied ${theme.title} theme!")
                                        } else {
                                            viewModel.showToast("Unlock Pro to apply exclusive themes!")
                                        }
                                    }
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = theme.title,
                                    color = Color.White,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Pro Features List
        item {
            Text(
                text = "Everything in Pro VIP",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                proFeatures.forEach { feat ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = TikSaveSurface),
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x224B3869))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(feat.iconColor.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = feat.icon,
                                    contentDescription = null,
                                    tint = feat.iconColor,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = feat.title,
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.5.sp
                                )
                                Text(
                                    text = feat.description,
                                    color = TextMuted,
                                    fontSize = 11.5.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
