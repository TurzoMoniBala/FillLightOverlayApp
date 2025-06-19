package com.example.filllightoverlay

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button

class OverlayService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var overlayView: View
    private var brightness: Int = 180 // Initial alpha (0-255)

    override fun onCreate() {
        super.onCreate()

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        overlayView = inflater.inflate(R.layout.overlay_layout, null)

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        val topOverlay = overlayView.findViewById<View>(R.id.topOverlay)
        val bottomOverlay = overlayView.findViewById<View>(R.id.bottomOverlay)
        val leftOverlay = overlayView.findViewById<View>(R.id.leftOverlay)
        val rightOverlay = overlayView.findViewById<View>(R.id.rightOverlay)

        val overlays = listOf(topOverlay, bottomOverlay, leftOverlay, rightOverlay)
        overlays.forEach { it.background.alpha = brightness }

        overlayView.findViewById<Button>(R.id.btnIncrease).setOnClickListener {
            brightness = (brightness + 20).coerceAtMost(255)
            overlays.forEach { it.background.alpha = brightness }
        }

        overlayView.findViewById<Button>(R.id.btnDecrease).setOnClickListener {
            brightness = (brightness - 20).coerceAtLeast(0)
            overlays.forEach { it.background.alpha = brightness }
        }

        overlayView.findViewById<Button>(R.id.btnClose).setOnClickListener {
            stopSelf()
        }

        windowManager.addView(overlayView, params)
    }

    override fun onDestroy() {
        super.onDestroy()
        windowManager.removeView(overlayView)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
