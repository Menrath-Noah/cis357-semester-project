package com.example.prototype_semesterproject

import android.content.Context
import android.opengl.GLSurfaceView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.prototype_semesterproject.MyGLSurfaceView


@Composable
fun GameScreen() {
    val context = LocalContext.current

    // Embed MyGLSurfaceView in the Compose UI
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { MyGLSurfaceView(context) }
    )
}
