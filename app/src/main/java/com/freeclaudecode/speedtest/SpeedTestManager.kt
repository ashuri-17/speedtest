package com.freeclaudecode.speedtest

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.freeclaudecode.speedtest.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit
import java.text.DecimalFormat

class SpeedTestManager(private val context: Context) {

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    private val testServers = listOf(
        "https://speedtest.tele2.net/1MB.zip",
        "https://speedtestselect.tele2.net/5MB.zip",
        "https://sandbox.tele2.net/speedtest/10MB.zip"
    )

    fun runTest(binding: ActivityMainBinding) {
        binding.txtStatus.text = "Testing..."
        binding.txtDownloadSpeed.text = "0.00 Mbps"
        binding.txtUploadSpeed.text = "0.00 Mbps"
        binding.txtPing.text = "..."

        android.widget.Toast.makeText(context, "Starting speed test...", android.widget.Toast.LENGTH_SHORT).show()

        (context as? MainActivity)?.lifecycleScope?.launch {
            testPing(binding)
            testDownloadSpeed(binding)
            testUploadSpeed(binding)
            binding.txtStatus.text = "Test Complete"
        }
    }

    private suspend fun testPing(binding: ActivityMainBinding) {
        withContext(Dispatchers.IO) {
            try {
                val startTime = System.nanoTime()
                val request = Request.Builder()
                    .url("https://www.google.com/generate_204")
                    .build()
                client.newCall(request).execute().use { response ->
                    val endTime = System.nanoTime()
                    val pingMs = (endTime - startTime) / 1_000_000.0
                    withContext(Dispatchers.Main) {
                        binding.txtPing.text = "${DecimalFormat("0.0").format(pingMs)} ms"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.txtPing.text = "Error"
                }
            }
        }
    }

    private suspend fun testDownloadSpeed(binding: ActivityMainBinding) {
        withContext(Dispatchers.IO) {
            try {
                val testUrl = testServers[0]
                val request = Request.Builder().url(testUrl).build()
                val startTime = System.nanoTime()

                client.newCall(request).execute().use { response ->
                    val body = response.body
                    if (body != null) {
                        val bytes = body.bytes()
                        val endTime = System.nanoTime()
                        val durationSec = (endTime - startTime) / 1_000_000_000.0
                        val bits = bytes.size * 8.0
                        val mbps = (bits / durationSec) / 1_000_000.0

                        withContext(Dispatchers.Main) {
                            binding.txtDownloadSpeed.text = "${DecimalFormat("0.00").format(mbps)} Mbps"
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.txtDownloadSpeed.text = "Error"
                }
            }
        }
    }

    private suspend fun testUploadSpeed(binding: ActivityMainBinding) {
        withContext(Dispatchers.IO) {
            try {
                val testUrl = "https://postman-echo.com/post"
                val uploadData = ByteArray(1024 * 1024) // 1MB
                val requestBody = okhttp3.RequestBody.create(null, uploadData)
                val request = Request.Builder()
                    .url(testUrl)
                    .post(requestBody)
                    .build()

                val startTime = System.nanoTime()
                client.newCall(request).execute().use { response ->
                    val endTime = System.nanoTime()
                    val durationSec = (endTime - startTime) / 1_000_000_000.0
                    val bits = uploadData.size * 8.0
                    val mbps = (bits / durationSec) / 1_000_000.0

                    withContext(Dispatchers.Main) {
                        binding.txtUploadSpeed.text = "${DecimalFormat("0.00").format(mbps)} Mbps"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.txtUploadSpeed.text = "Error"
                }
            }
        }
    }
}
