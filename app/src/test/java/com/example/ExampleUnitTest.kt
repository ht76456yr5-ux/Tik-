package com.example

import com.example.tiksave.data.model.PlatformType
import com.example.tiksave.data.network.PlatformExtractor
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testPlatformDetection() {
        assertEquals(
            PlatformType.TIKTOK,
            PlatformType.detectFromUrl("https://www.tiktok.com/@user/video/123456789")
        )
        assertEquals(
            PlatformType.INSTAGRAM,
            PlatformType.detectFromUrl("https://www.instagram.com/reel/C2-example/")
        )
        assertEquals(
            PlatformType.FACEBOOK,
            PlatformType.detectFromUrl("https://fb.watch/abCdEf123/")
        )
        assertEquals(
            PlatformType.YOUTUBE,
            PlatformType.detectFromUrl("https://youtu.be/dQw4w9WgXcQ")
        )
        assertEquals(
            PlatformType.PINTEREST,
            PlatformType.detectFromUrl("https://pin.it/1234567")
        )
        assertEquals(
            PlatformType.WHATSAPP,
            PlatformType.detectFromUrl("https://whatsapp.com/status/sample")
        )
        assertEquals(
            PlatformType.UNKNOWN,
            PlatformType.detectFromUrl("https://example.com/video.mp4")
        )
    }

    @Test
    fun testExtractorMediaResolution() = runBlocking {
        val extractor = PlatformExtractor()
        val result = extractor.resolveMedia("https://www.tiktok.com/@nature_vibes/video/728912401829")
        assertTrue(result.isSuccess)
        val info = result.getOrNull()
        assertNotNull(info)
        assertEquals(PlatformType.TIKTOK, info?.platform)
        assertTrue(info?.availableFormats?.isNotEmpty() == true)
    }
}
