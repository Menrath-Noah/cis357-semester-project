package com.example.prototype_semesterproject

import android.content.Context
import android.opengl.GLSurfaceView
import androidx.lifecycle.MutableLiveData

class MyGLSurfaceView(context: Context) : GLSurfaceView(context) {

    private val renderer: MyGLRenderer

    init {

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2)

        renderer = MyGLRenderer()

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer)

        renderMode = RENDERMODE_CONTINUOUSLY

    }
}