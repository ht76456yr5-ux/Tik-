package com.example.tiksave.ui.screens

import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.tiksave.data.model.DownloadItem
import com.example.tiksave.data.model.DownloadStatus
import com.example.tiksave.data.model.MediaFormatType
import com.example.tiksave.data.model.PlatformType
import com.example.tiksave.data.model.StreamFormatOption
import com.example.tiksave.ui.components.GlowButton
import com.example.tiksave.ui.components.PlatformBadge
import com.example.tiksave.ui.viewmodel.Screen
import com.example.tiksave.ui.viewmodel.TikSaveViewModel
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.NeonPurpleGlow
import com.example.ui.theme.PrimaryGradient
import com.example.ui.theme.ProGoldGradient
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TikSaveBackground
import com.example.ui.theme.TikSaveSurface
import com.example.ui.theme.TikSaveSurfaceVariant
import com.example.ui.theme.VividMagenta

@Composable
fun HomeScreen(
    viewModel: TikSaveViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val urlInput by viewModel.urlInput.collectAsState()
    val detectedPlatform by viewModel.detectedPlatform.collectAsState()
    val isResolving by viewModel.isResolving.collectAsState()
    val resolvedMedia by viewModel.resolvedMedia.collectAsState()
    val selectedFormat by viewModel.selectedFormat.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val activeDownloads by viewModel.activeDownloads.collectAsState()
    val recentDownloads by viewModel.recentDownloads.collectAsState()
    val isProUser by viewModel.isProUser.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(TikSaveBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Card with Branding Banner
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF130E26))
                    .border(1.dp, Color(0x33A855F7), RoundedCornerShape(20.dp))
            ) {
                // Background Banner Image if available
                Image(
                    painter = painterResource(id = R.drawable.tiksave_banner),
                    contentDescription = "TikSave Banner",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    alpha = 0.5f
                )

                // Dark gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xE60A0713), Color(0x99180E30))
                            )
                        )
                )

                // Content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    Brush.horizontalGradient(listOf(NeonPurple, VividMagenta))
                                )
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "TIKSAVE PRO ENGINE",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        if (isProUser) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ProGoldGradient)
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "VIP SPEED ⚡",
                                    color = Color.Black,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Download Videos in HD",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp,
                        letterSpacing = (-0.3).sp
                    )
                    Text(
                        text = "Paste link from TikTok, Instagram, YouTube & more",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Link Input & Paste Box
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = TikSaveSurface),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x44A855F7))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Video or Post URL",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )

                        if (detectedPlatform != PlatformType.UNKNOWN) {
                            PlatformBadge(platform = detectedPlatform, isSelected = true)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Text Field with Paste & Clear actions
                    OutlinedTextField(
                        value = urlInput,
                        onValueChange = { viewModel.onUrlChanged(it) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                text = "https://www.tiktok.com/@... or instagram...",
                                color = TextMuted,
                                fontSize = 13.sp,
                                maxLines = 1
                            )
                        },
                        trailingIcon = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (urlInput.isNotBlank()) {
                                    IconButton(onClick = { viewModel.onClearUrl() }) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "Clear Link",
                                            tint = TextMuted,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFF231A3E))
                                        .clickable {
                                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                            val clip = clipboard.primaryClip
                                            if (clip != null && clip.itemCount > 0) {
                                                val pastedText = clip.getItemAt(0).text?.toString() ?: ""
                                                if (pastedText.isNotBlank()) {
                                                    viewModel.onUrlChanged(pastedText)
                                                    viewModel.fetchMediaInfo()
                                                }
                                            }
                                        }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.ContentPaste,
                                            contentDescription = "Paste",
                                            tint = NeonPurple,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Paste",
                                            color = Color.White,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = NeonPurple,
                            unfocusedBorderColor = Color(0x33A855F7),
                            focusedContainerColor = Color(0xFF0D091B),
                            unfocusedContainerColor = Color(0xFF0D091B)
                        ),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true
                    )

                    // Error Message
                    AnimatedVisibility(visible = errorMessage != null) {
                        errorMessage?.let { err ->
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0x33EF4444))
                                    .border(1.dp, Color(0x66EF4444), RoundedCornerShape(10.dp))
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Error, contentDescription = null, tint = Color(0xFFEF4444), modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = err, color = Color(0xFFFCA5A5), fontSize = 12.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Big Action Button: Fetch or Download
                    GlowButton(
                        text = if (resolvedMedia == null) "Fetch Video Details" else "Download Now (${selectedFormat?.label ?: "Video"})",
                        icon = if (resolvedMedia == null) Icons.Default.Bolt else Icons.Default.Download,
                        isLoading = isResolving,
                        onClick = {
                            if (resolvedMedia == null) {
                                viewModel.fetchMediaInfo()
                            } else {
                                viewModel.startDownload()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Resolved Media Card (When video is fetched)
        item {
            AnimatedVisibility(
                visible = resolvedMedia != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                resolvedMedia?.let { media ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = TikSaveSurface),
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x66EC4899))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Top Media Header
                            Row(modifier = Modifier.fillMaxWidth()) {
                                AsyncImage(
                                    model = media.thumbnailUrl,
                                    contentDescription = media.title,
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color.Black),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    PlatformBadge(platform = media.platform, isSelected = true)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = media.title,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "${media.author} • ${media.durationText}",
                                        color = TextMuted,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            // Policy notice if DRM or restricted
                            if (media.isPrivateOrDrm && media.policyNotice != null) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0x33F59E0B))
                                        .border(1.dp, Color(0x66F59E0B), RoundedCornerShape(12.dp))
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Warning,
                                        contentDescription = null,
                                        tint = Color(0xFFF59E0B),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = media.policyNotice,
                                        color = Color(0xFFFDE68A),
                                        fontSize = 12.sp,
                                        lineHeight = 16.sp
                                    )
                                }
                            }

                            // Available Formats Selection
                            if (media.availableFormats.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(14.dp))
                                Text(
                                    text = "Choose Download Format:",
                                    color = TextSecondary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    media.availableFormats.forEach { format ->
                                        val isChosen = selectedFormat == format
                                        FormatSelectionRow(
                                            format = format,
                                            isSelected = isChosen,
                                            isProUser = isProUser,
                                            onSelect = { viewModel.selectFormat(format) },
                                            onDownloadDirect = {
                                                viewModel.selectFormat(format)
                                                viewModel.startDownload(format)
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Active Downloads Live Tracker
        if (activeDownloads.isNotEmpty()) {
            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Active Downloads (${activeDownloads.size})",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0x33A855F7))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "LIVE",
                                color = NeonPurple,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    activeDownloads.forEach { item ->
                        ActiveDownloadCard(
                            item = item,
                            onCancel = { viewModel.cancelDownload(item.id) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

        // Supported Platforms Quick Launch & Samples
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Supported Platforms",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Tap to test sample",
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                val platforms = listOf(
                    PlatformType.TIKTOK,
                    PlatformType.INSTAGRAM,
                    PlatformType.FACEBOOK,
                    PlatformType.YOUTUBE,
                    PlatformType.PINTEREST,
                    PlatformType.WHATSAPP
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(platforms) { platform ->
                        PlatformBadge(
                            platform = platform,
                            isSelected = detectedPlatform == platform,
                            onClick = { viewModel.loadSampleUrl(platform) }
                        )
                    }
                }
            }
        }

        // Recent Downloads Section
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Downloads",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    if (recentDownloads.isNotEmpty()) {
                        Text(
                            text = "View All",
                            color = NeonPurple,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            modifier = Modifier.clickable { viewModel.navigateTo(Screen.Downloads) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (recentDownloads.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(TikSaveSurface)
                            .border(1.dp, Color(0x224B3869), RoundedCornerShape(16.dp))
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "📥", fontSize = 32.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No downloads yet",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Paste a link above or tap any platform badge to test!",
                                color = TextMuted,
                                fontSize = 12.sp
                            )
                        }
                    }
                } else {
                    recentDownloads.take(4).forEach { download ->
                        RecentDownloadItem(
                            item = download,
                            onPlay = { viewModel.openVideoPreview(download.localFilePath, download.title) },
                            onToggleFavorite = { viewModel.toggleFavorite(download) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

        // Batch Download Promo Banner for Pro
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF221538), Color(0xFF140D26))
                        )
                    )
                    .border(1.dp, Color(0x33EC4899), RoundedCornerShape(16.dp))
                    .clickable { viewModel.openBatchDialog() }
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0x33EC4899)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Layers,
                                contentDescription = null,
                                tint = VividMagenta,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Batch Downloader",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color(0xFFF59E0B))
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text("PRO", color = Color.Black, fontSize = 9.sp, fontWeight = FontWeight.Black)
                                }
                            }
                            Text(
                                text = "Queue multiple links & playlist items together",
                                color = TextMuted,
                                fontSize = 11.5.sp
                            )
                        }
                    }

                    Text(
                        text = "Open →",
                        color = VividMagenta,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun FormatSelectionRow(
    format: StreamFormatOption,
    isSelected: Boolean,
    isProUser: Boolean,
    onSelect: () -> Unit,
    onDownloadDirect: () -> Unit
) {
    val borderColor = if (isSelected) NeonPurple else Color(0x334B3869)
    val bg = if (isSelected) Color(0xFF21163C) else Color(0xFF130E22)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable { onSelect() }
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = format.label,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.5.sp
                    )
                    if (format.isProRequired) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFFF59E0B))
                                .padding(horizontal = 4.dp, vertical = 1.dp)
                        ) {
                            Text("PRO", color = Color.Black, fontSize = 9.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }
                Text(
                    text = "${format.resolution} • ~${format.estimatedSize}",
                    color = TextMuted,
                    fontSize = 11.5.sp
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .then(
                        if (isSelected) {
                            Modifier.background(Brush.horizontalGradient(listOf(NeonPurple, VividMagenta)))
                        } else {
                            Modifier.background(Color(0xFF291E44))
                        }
                    )
                    .clickable { onDownloadDirect() }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Download",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun ActiveDownloadCard(
    item: DownloadItem,
    onCancel: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(TikSaveSurface)
            .border(1.dp, Color(0x66A855F7), RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.title,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${item.platform.displayName} • ${item.speedText}",
                        color = NeonPurple,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                IconButton(onClick = onCancel, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Cancel",
                        tint = TextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { item.progress / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = VividMagenta,
                trackColor = Color(0xFF2A1F45)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${item.progress}%",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = item.quality,
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
fun RecentDownloadItem(
    item: DownloadItem,
    onPlay: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(TikSaveSurface)
            .border(1.dp, Color(0x224B3869), RoundedCornerShape(14.dp))
            .clickable { onPlay() }
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF251A40)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = VividMagenta,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${item.platform.displayName} • ${item.quality}",
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }

            IconButton(onClick = onToggleFavorite, modifier = Modifier.size(36.dp)) {
                Icon(
                    imageVector = if (item.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (item.isFavorite) VividMagenta else TextMuted,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
