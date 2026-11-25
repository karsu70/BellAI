package com.bellai.app

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class OverlayService : Service() {
    private lateinit var wm: WindowManager
    private var bubble: View? = null
    private var panel: View? = null
    private val siteUrl = "https://amazing-cendol-b808f4.netlify.app"
    private val channelId = "bell_ai_overlay"

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        val notification: Notification = if (Build.VERSION.SDK_INT >= 26) {
            Notification.Builder(this, channelId)
                .setContentTitle("BELL AI çalışıyor")
                .setContentText("Kayan pencereyi kapatmak için bildirime dokunup uygulamayı durdurabilirsin.")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setOngoing(true)
                .build()
        } else {
            @Suppress("DEPRECATION")
            Notification.Builder(this).setContentTitle("BELL AI çalışıyor")
                .setSmallIcon(android.R.drawable.ic_dialog_info).build()
        }
        if (Build.VERSION.SDK_INT >= 34) {
            startForeground(41, notification, android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
        } else {
            startForeground(41, notification)
        }
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "BELL AI için diğer uygulamaların üzerinde gösterme izni gerekli.", Toast.LENGTH_LONG).show()
            stopSelf()
            return
        }
        wm = getSystemService(WINDOW_SERVICE) as WindowManager
        showBubble()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel(channelId, "BELL AI kayan pencere", NotificationManager.IMPORTANCE_LOW)
            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)
        }
    }

    private fun overlayType(): Int = if (Build.VERSION.SDK_INT >= 26) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else @Suppress("DEPRECATION") WindowManager.LayoutParams.TYPE_PHONE

    private fun showBubble() {
        if (bubble != null) return
        val b = TextView(this).apply {
            text = "B"
            textSize = 22f
            gravity = Gravity.CENTER
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.rgb(20, 75, 180))
            elevation = 12f
            setOnClickListener { showPanel() }
        }
        val size = dp(58)
        val params = WindowManager.LayoutParams(size, size, overlayType(), WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT).apply {
            gravity = Gravity.TOP or Gravity.START
            x = dp(18); y = dp(220)
        }
        b.setOnTouchListener(object : View.OnTouchListener {
            var downX = 0f; var downY = 0f; var startX = 0; var startY = 0; var moved = false
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> { downX = event.rawX; downY = event.rawY; startX = params.x; startY = params.y; moved = false; return true }
                    MotionEvent.ACTION_MOVE -> {
                        val dx = (event.rawX - downX).toInt(); val dy = (event.rawY - downY).toInt()
                        if (kotlin.math.abs(dx) + kotlin.math.abs(dy) > dp(5)) moved = true
                        params.x = startX + dx; params.y = startY + dy
                        wm.updateViewLayout(b, params); return true
                    }
                    MotionEvent.ACTION_UP -> { if (!moved) showPanel(); return true }
                }
                return false
            }
        })
        bubble = b
        wm.addView(b, params)
    }

    private fun showPanel() {
        if (panel != null) { panel?.visibility = View.VISIBLE; return }
        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.rgb(18, 18, 22))
            setPadding(dp(8), dp(8), dp(8), dp(8))
            elevation = 18f
        }
        val bar = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL }
        val label = TextView(this).apply { text = "BELL AI"; textSize = 16f; setTextColor(Color.WHITE) }
        val close = Button(this).apply { text = "Kapat"; setOnClickListener { hidePanel() } }
        bar.addView(label, LinearLayout.LayoutParams(0, dp(44), 1f)); bar.addView(close)
        val web = WebView(this).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
            loadUrl(siteUrl)
        }
        box.addView(bar)
        box.addView(web, LinearLayout.LayoutParams(-1, 0, 1f))
        val p = WindowManager.LayoutParams(dp(340), dp(480), overlayType(), WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, PixelFormat.TRANSLUCENT).apply {
            gravity = Gravity.TOP or Gravity.END
            x = dp(10); y = dp(90)
        }
        panel = box
        wm.addView(box, p)
    }

    private fun hidePanel() {
        panel?.let { try { wm.removeView(it) } catch (_: Exception) {} }
        panel = null
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_STICKY

    override fun onDestroy() {
        hidePanel()
        bubble?.let { try { wm.removeView(it) } catch (_: Exception) {} }
        bubble = null
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
