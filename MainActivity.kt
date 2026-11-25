package com.bellai.app

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    private val siteUrl = "https://amazing-cendol-b808f4.netlify.app"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(0xFF090909.toInt())
        }
        val header = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(12, 8, 12, 8)
            setBackgroundColor(0xFF111827.toInt())
        }
        val title = TextView(this).apply {
            text = "BELL AI"
            textSize = 18f
            setTextColor(0xFFFFFFFF.toInt())
        }
        val spacer = TextView(this).apply { text = "" }
        val permissionButton = Button(this).apply {
            text = "Kayan pencere izni"
            setOnClickListener {
                if (!Settings.canDrawOverlays(this@MainActivity)) {
                    startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")))
                } else {
                    Toast.makeText(this@MainActivity, "İzin zaten açık. Kayan pencereyi başlatabilirsin.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        val startButton = Button(this).apply {
            text = "Kayan pencereyi başlat"
            setOnClickListener {
                if (!Settings.canDrawOverlays(this@MainActivity)) {
                    Toast.makeText(this@MainActivity, "Önce kayan pencere iznini aç.", Toast.LENGTH_LONG).show()
                    startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")))
                } else {
                    val intent = Intent(this@MainActivity, OverlayService::class.java)
                    startForegroundService(intent)
                    Toast.makeText(this@MainActivity, "BELL AI kayan simgesi başlatıldı. Oyuna geçebilirsin.", Toast.LENGTH_LONG).show()
                }
            }
        }
        header.addView(title, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
        header.addView(permissionButton)
        root.addView(header)
        root.addView(startButton, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        val web = WebView(this).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.loadsImagesAutomatically = true
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
            loadUrl(siteUrl)
        }
        root.addView(web, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f))
        setContentView(root)
    }

    override fun onBackPressed() {
        val web = findWebView(window.decorView)
        if (web?.canGoBack() == true) web.goBack() else super.onBackPressed()
    }

    private fun findWebView(view: android.view.View): WebView? {
        if (view is WebView) return view
        if (view is ViewGroup) for (i in 0 until view.childCount) findWebView(view.getChildAt(i))?.let { return it }
        return null
    }
}
