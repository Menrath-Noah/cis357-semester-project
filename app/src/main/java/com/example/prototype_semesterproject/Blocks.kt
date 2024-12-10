package com.example.prototype_semesterproject

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

/*
internal open class BaseShape(
    var xVal: Double? = 0.0,
    var blockColor: String = "black",
    var zVal: Double = 2.5,
    open var yVal: Double = 0.0
)

{
    internal var mProgram: Int = 0

    internal val vertexShaderCode = """
        attribute vec4 vPosition;
        void main() {
            gl_Position = uMVPMatrix * vPosition;
        }
    """.trimIndent()

    internal val fragmentShaderCode = """
        precision mediump float;
        uniform vec4 vColor;
        void main() {
            gl_FragColor = vColor;
        }
    """.trimIndent()

    internal val drawOrder = shortArrayOf(
        0, 1, 2, 0, 2, 3,
        4, 5, 6, 4, 6, 7,
        0, 1, 5, 0, 5, 4,
        3, 2, 6, 3, 6, 7,
        0, 3, 7, 0, 7, 4,
        1, 2, 6, 1, 6, 5
    )

    internal val COORDS_PER_VERTEX = 3
    open val squareCoords: FloatArray = floatArrayOf(
        0.0f, 0.05f, 0.3f,
        -0.15f, 0.0f, -0.15f,
        0.15f, 0.0f, -0.15f
    )

     val vertexCount: Int = squareCoords.size / COORDS_PER_VERTEX
     val vertexStride: Int = COORDS_PER_VERTEX * 4 // 4 bytes per vertex
     val vertexBuffer: FloatBuffer =
        // (# of coordinate values * 4 bytes per float)
        ByteBuffer.allocateDirect(squareCoords.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(squareCoords)
                position(0)
            }
        }
     val drawListBuffer: ShortBuffer =
        // (# of coordinate values * 2 bytes per short)
        ByteBuffer.allocateDirect(drawOrder.size * 2).run {
            order(ByteOrder.nativeOrder())
            asShortBuffer().apply {
                put(drawOrder)
                position(0)
            }
        }

    var positionHandle: Int = 0
    var mColorHandle: Int = 0

    internal fun loadShader(type: Int, shaderCode: String): Int {
        return GLES20.glCreateShader(type).also { shader ->
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)
        }
    }


 }

internal class Square(override var yVal: Double = 0.0) : BaseShape()
{
    override val squareCoords = floatArrayOf(
        -0.25f, .55f, 0.0f,
        -0.25f, -.55f, 0.0f,
        0.25f, -.55f, 0.0f,
        0.25f, .55f, 0.0f,
        -0.25f, .55f, -0.25f,
        -0.25f, -.55f, -0.25f,
        0.25f, -.55f, -0.25f,
        0.25f, .55f, -0.25f
    )

    internal fun draw(vPMatrix: FloatArray) {
        GLES20.glUseProgram(mProgram)

        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition").also { pos ->
            GLES20.glEnableVertexAttribArray(pos)
            GLES20.glVertexAttribPointer(
                pos,
                COORDS_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                vertexStride,
                vertexBuffer
            )
        }

        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor").also { color ->
            val colorArray = if (blockColor == "blue") floatArrayOf(0f, 0f, 1f, 1f) else floatArrayOf(0f, 0f, 1f, 1f)
            GLES20.glUniform4fv(mColorHandle, 1, colorArray, 0)
        }

        GLES20.glDrawElements(
            GLES20.GL_TRIANGLES,
            drawOrder.size,
            GLES20.GL_UNSIGNED_SHORT,
            drawListBuffer
        )

        GLES20.glDisableVertexAttribArray(positionHandle)

        val vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix")
        GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, vPMatrix, 0)
    }
}

internal class Square3 : BaseShape()
{
    override val squareCoords = floatArrayOf(
        -0.15f, -.2f, 0.0f,
        -0.15f, -.55f, 0.0f,
        0.15f, -.55f, 0.0f,
        0.15f, -.2f, 0.0f,
        -0.15f, -.2f, -0.25f,
        -0.15f, -.55f, -0.25f,
        0.15f, -.55f, -0.25f,
        0.15f, -.2f, -0.25f
    )

    internal fun draw(vPMatrix: FloatArray) {
        GLES20.glUseProgram(mProgram)

        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition").also { pos ->
            GLES20.glEnableVertexAttribArray(pos)
            GLES20.glVertexAttribPointer(
                pos,
                COORDS_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                vertexStride,
                vertexBuffer
            )
        }

        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor").also { color ->
            val colorArray = if (blockColor == "blue") floatArrayOf(0f, 0f, 1f, 1f) else floatArrayOf(0f, 0f, 1f, 1f)
            GLES20.glUniform4fv(mColorHandle, 1, colorArray, 0)
        }

        GLES20.glDrawElements(
            GLES20.GL_TRIANGLES,
            drawOrder.size,
            GLES20.GL_UNSIGNED_SHORT,
            drawListBuffer
        )

        GLES20.glDisableVertexAttribArray(positionHandle)

        val vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix")
        GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, vPMatrix, 0)
    }
}

internal class Triangle : BaseShape()
{
    override val squareCoords = floatArrayOf(
        0.0f, 0.05f, 0.3f,
        -0.15f, 0.0f, -0.15f,
        0.15f, 0.0f, -0.15f
    )

    internal fun draw(vPMatrix: FloatArray) {
        GLES20.glUseProgram(mProgram)

        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition").also { pos ->
            GLES20.glEnableVertexAttribArray(pos)
            GLES20.glVertexAttribPointer(
                pos,
                COORDS_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                vertexStride,
                vertexBuffer
            )
        }

        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor").also { color ->
            val colorArray = if (blockColor == "black") floatArrayOf(0f, 0f, 0f, 1f) else floatArrayOf(0f, 0f, 0f, 1f)
            GLES20.glUniform4fv(mColorHandle, 1, colorArray, 0)
        }

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)

        GLES20.glDisableVertexAttribArray(positionHandle)
    }
}
*/


class Square2(var xVal: Double?= 0.0, var blockColor: Int = 150, var zVal: Double = 5.0, var lengthVal: Double = 0.25, var heightVal: Double = 0.55, var widthVal: Double = 0.15) {

    private var mProgram: Int = 0
    private val vertexShaderCode =
    // This matrix member variable provides a hook to manipulate
        // the coordinates of the objects that use this vertex shader
        "uniform mat4 uMVPMatrix;" +
                "attribute vec4 vPosition;" +
                "void main() {" +
                // the matrix must be included as a modifier of gl_Position
                // Note that the uMVPMatrix factor *must be first* in order
                // for the matrix multiplication product to be correct.
                "  gl_Position = uMVPMatrix * vPosition;" +
                "}"

    private var vPMatrixHandle: Int = 0
    private val fragmentShaderCode =
        "precision mediump float;" +
                "uniform vec4 vColor;" +
                "void main() {" +
                "  gl_FragColor = vColor;" +
                "}"

    private val drawOrder = shortArrayOf(
        0, 1, 2, 0, 2, 3,  // front side
        4, 5, 6, 4, 6, 7,  // back side
        0, 1, 5, 0, 5, 4,  // left side
        3, 2, 6, 3, 6, 7,  // right side
        0, 3, 7, 0, 7, 4,  // top side
        1, 2, 6, 1, 6, 5)  // Bottom face
    val COORDS_PER_VERTEX = 3
    //        var squareCoords = floatArrayOf(
//            -0.25f,  .55f, 0.0f,     // top left
//            -0.25f, -.55f, 0.0f,     // bottom left
//            0.25f, -.55f, 0.0f,      // bottom right
//            0.25f,  .55f, 0.0f,      // top right
//
//            -0.25f,  .55f, -0.25f,
//            -0.25f, -.55f, -0.25f,
//            0.25f, -.55f, -0.25f,
//            0.25f,  .55f, -0.25f
//        )
    var squareCoords = floatArrayOf(
        (-lengthVal).toFloat(), heightVal.toFloat(), 0.0f,     // top left
        (-lengthVal).toFloat(), -.55f, 0.0f,                   // bottom left
        lengthVal.toFloat(), -.55f, 0.0f,                      // bottom right
        lengthVal.toFloat(), heightVal.toFloat(), 0.0f,        // top right

        (-lengthVal).toFloat(), heightVal.toFloat(), widthVal.toFloat(),
        (-lengthVal).toFloat(), -.55f, widthVal.toFloat(),
        lengthVal.toFloat(), -.55f, widthVal.toFloat(),
        lengthVal.toFloat(), heightVal.toFloat(), widthVal.toFloat()
    )
//        var squareCoords = floatArrayOf(
//            (-lengthVal).toFloat(), .55f, 0.0f,     // top left
//            (-lengthVal).toFloat(), -.55f, 0.0f,                   // bottom left
//            lengthVal.toFloat(), -.55f, 0.0f,                      // bottom right
//            lengthVal.toFloat(), .55f, 0.0f,        // top right
//
//            (-lengthVal).toFloat(), .55f, -0.25f,
//            (-lengthVal).toFloat(), -.55f, -0.25f,
//            lengthVal.toFloat(), -.55f, -0.25f,
//            lengthVal.toFloat(), .55f, -0.25f
//        )
    //heightVal .2 to .9

    private var positionHandle: Int = 0
    private var mColorHandle: Int = 0

    private val vertexCount: Int = squareCoords.size / COORDS_PER_VERTEX
    private val vertexStride: Int = COORDS_PER_VERTEX * 4 // 4 bytes per vertex

    init {

        val vertexShader: Int = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader: Int = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram().also {

            // add the vertex shader to program
            GLES20.glAttachShader(it, vertexShader)

            // add the fragment shader to program
            GLES20.glAttachShader(it, fragmentShader)

            // creates OpenGL ES program executables
            GLES20.glLinkProgram(it)
        }
    }

    // initialize vertex byte buffer for shape coordinates
    private val vertexBuffer: FloatBuffer =
        // (# of coordinate values * 4 bytes per float)
        ByteBuffer.allocateDirect(squareCoords.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(squareCoords)
                position(0)
            }
        }

    // initialize byte buffer for the draw list
    private val drawListBuffer: ShortBuffer =
        // (# of coordinate values * 2 bytes per short)
        ByteBuffer.allocateDirect(drawOrder.size * 2).run {
            order(ByteOrder.nativeOrder())
            asShortBuffer().apply {
                put(drawOrder)
                position(0)
            }
        }
    fun draw(vPMatrix: FloatArray) {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram)

        // get handle to vertex shader's vPosition member
        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition").also {

            // Enable a handle to the triangle vertices
            GLES20.glEnableVertexAttribArray(it)

            // Prepare the triangle coordinate data
            GLES20.glVertexAttribPointer(
                it,
                COORDS_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                vertexStride,
                vertexBuffer
            )
            var blueShade = blockColor.toFloat() / 255f

            // get handle to fragment shader's vColor member
            mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor").also { colorHandle ->

                // Set color for drawing the triangle
                var color = floatArrayOf(0.0f, 0.0f, .8f, 1.0f)

                GLES20.glUniform4fv(colorHandle, 1, color, 0)
            }

            // Draw the triangle
            GLES20.glDrawElements(
                GLES20.GL_TRIANGLES,
                drawOrder.size,
                GLES20.GL_UNSIGNED_SHORT,
                drawListBuffer
            )

            // Disable vertex array
            GLES20.glDisableVertexAttribArray(it)

            // get handle to shape's transformation matrix
            vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix")

            // Pass the projection and view transformation to the shader
            GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, vPMatrix, 0)

            // Draw the triangle
            //GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)

            // Disable vertex array
            GLES20.glDisableVertexAttribArray(positionHandle)
        }
    }
    fun loadShader(type: Int, shaderCode: String): Int {

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        return GLES20.glCreateShader(type).also { shader ->

            // add the source code to the shader and compile it
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)
        }
    }

}



class Square3(var xVal: Double?= 0.0, var blockColor: String = "black", var zVal: Double = 3.0, var yVal: Double = 0.0) {

    private var mProgram: Int = 0
    var color = floatArrayOf(0.0f, 0.0f, 0.0f, 1.0f)
    private val vertexShaderCode =
    // This matrix member variable provides a hook to manipulate
        // the coordinates of the objects that use this vertex shader
        "uniform mat4 uMVPMatrix;" +
                "attribute vec4 vPosition;" +
                "void main() {" +
                // the matrix must be included as a modifier of gl_Position
                // Note that the uMVPMatrix factor *must be first* in order
                // for the matrix multiplication product to be correct.
                "  gl_Position = uMVPMatrix * vPosition;" +
                "}"
    //            "attribute vec4 vPosition;" +
//                "void main() {" +
//                "  gl_Position = vPosition;" +
//                "}"
    private var vPMatrixHandle: Int = 0
    private val fragmentShaderCode =
        "precision mediump float;" +
                "uniform vec4 vColor;" +
                "void main() {" +
                "  gl_FragColor = vColor;" +
                "}"

    private val drawOrder = shortArrayOf(
        0, 1, 2, 0, 2, 3,  // front side
        4, 5, 6, 4, 6, 7,  // back side
        0, 1, 5, 0, 5, 4,  // left side
        3, 2, 6, 3, 6, 7,  // right side
        0, 3, 7, 0, 7, 4,  // top side
        1, 2, 6, 1, 6, 5)  // Bottom face
    val COORDS_PER_VERTEX = 3
    //        var squareCoords = floatArrayOf(
//            -0.15f,  -.2f, 0.0f,     // top left
//            -0.15f, -.55f, 0.0f,     // bottom left
//            0.15f, -.55f, 0.0f,      // bottom right
//            0.15f,  -.2f, 0.0f,      // top right
//
//            -0.15f,  -.2f, -0.25f,
//            -0.15f, -.55f, -0.25f,
//            0.15f, -.55f, -0.25f,
//            0.15f,  -.2f, -0.25f
//        )
    var squareCoords = floatArrayOf(
        -0.10f,  -.35f, 0.0f,     // top left
        -0.10f, -.55f, 0.0f,     // bottom left
        0.10f, -.55f, 0.0f,      // bottom right
        0.10f,  -.35f, 0.0f,      // top right

        -0.10f,  -.35f, -0.1f,
        -0.10f, -.55f, -0.1f,
        0.10f, -.55f, -0.1f,
        0.10f,  -.35f, -0.1f
    )


    private var positionHandle: Int = 0
    private var mColorHandle: Int = 0

    private val vertexCount: Int = squareCoords.size / COORDS_PER_VERTEX
    private val vertexStride: Int = COORDS_PER_VERTEX * 4 // 4 bytes per vertex

    init {

        val vertexShader: Int = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader: Int = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram().also {

            // add the vertex shader to program
            GLES20.glAttachShader(it, vertexShader)

            // add the fragment shader to program
            GLES20.glAttachShader(it, fragmentShader)

            // creates OpenGL ES program executables
            GLES20.glLinkProgram(it)
        }
    }

    // initialize vertex byte buffer for shape coordinates
    private val vertexBuffer: FloatBuffer =
        // (# of coordinate values * 4 bytes per float)
        ByteBuffer.allocateDirect(squareCoords.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(squareCoords)
                position(0)
            }
        }

    // initialize byte buffer for the draw list
    private val drawListBuffer: ShortBuffer =
        // (# of coordinate values * 2 bytes per short)
        ByteBuffer.allocateDirect(drawOrder.size * 2).run {
            order(ByteOrder.nativeOrder())
            asShortBuffer().apply {
                put(drawOrder)
                position(0)
            }
        }
    fun draw(vPMatrix: FloatArray) {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram)

        // get handle to vertex shader's vPosition member
        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition").also {

            // Enable a handle to the triangle vertices
            GLES20.glEnableVertexAttribArray(it)

            // Prepare the triangle coordinate data
            GLES20.glVertexAttribPointer(
                it,
                COORDS_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                vertexStride,
                vertexBuffer
            )

            // get handle to fragment shader's vColor member
            mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor").also { colorHandle ->

                // Set color for drawing the triangle

//                    color = floatArrayOf(0f, 0f, 0f, 1f)
                if (blockColor == "black") {
                    color = floatArrayOf(0f, 0f, 0f, 1f)
                }
                else if (blockColor == "transparent") {
                    color = floatArrayOf(0f, 0f, 0f, .15f)
                }

                GLES20.glUniform4fv(colorHandle, 1, color, 0)
            }

            // Draw the triangle
            GLES20.glDrawElements(
                GLES20.GL_TRIANGLES,
                drawOrder.size,
                GLES20.GL_UNSIGNED_SHORT,
                drawListBuffer
            )

            // Disable vertex array
            GLES20.glDisableVertexAttribArray(it)

            // get handle to shape's transformation matrix
            vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix")

            // Pass the projection and view transformation to the shader
            GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, vPMatrix, 0)

            // Draw the triangle
            //GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)

            // Disable vertex array
            GLES20.glDisableVertexAttribArray(positionHandle)
        }
    }
    fun loadShader(type: Int, shaderCode: String): Int {

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        return GLES20.glCreateShader(type).also { shader ->

            // add the source code to the shader and compile it
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)
        }
    }

}


class Triangle(var xVal: Double?= 0.0, var blockColor: String = "black", var zVal: Double = 2.5) {

    // Set color with red, green, blue and alpha (opacity) values

    private var mProgram: Int = 0

    private val vertexShaderCode =
        "attribute vec4 vPosition;" +
                "void main() {" +
                "  gl_Position = vPosition;" +
                "}"

    private val fragmentShaderCode =
        "precision mediump float;" +
                "uniform vec4 vColor;" +
                "void main() {" +
                "  gl_FragColor = vColor;" +
                "}"

    // number of coordinates per vertex in this array
    val COORDS_PER_VERTEX = 3
    var triangleCoords = floatArrayOf(
        0.0f, 0.05f, 0.3f,
        -0.15f, 0.0f, -0.15f,
        0.15f, 0.0f, -0.15f
    )

    init {

        val vertexShader: Int = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader: Int = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram().also {

            // add the vertex shader to program
            GLES20.glAttachShader(it, vertexShader)

            // add the fragment shader to program
            GLES20.glAttachShader(it, fragmentShader)

            // creates OpenGL ES program executables
            GLES20.glLinkProgram(it)
        }
    }


    private var vertexBuffer: FloatBuffer =
        // (number of coordinate values * 4 bytes per float)
        ByteBuffer.allocateDirect(triangleCoords.size * 4).run {
            // use the device hardware's native byte order
            order(ByteOrder.nativeOrder())

            // create a floating point buffer from the ByteBuffer
            asFloatBuffer().apply {
                // add the coordinates to the FloatBuffer
                put(triangleCoords)
                // set the buffer to read the first coordinate
                position(0)
            }
        }
    fun loadShader(type: Int, shaderCode: String): Int {

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        return GLES20.glCreateShader(type).also { shader ->

            // add the source code to the shader and compile it
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)
        }
    }

    private var positionHandle: Int = 0
    private var mColorHandle: Int = 0

    private val vertexCount: Int = triangleCoords.size / COORDS_PER_VERTEX
    private val vertexStride: Int = COORDS_PER_VERTEX * 4 // 4 bytes per vertex

    fun draw(vPMatrix: FloatArray) {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram)

        // get handle to vertex shader's vPosition member
        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition").also {

            // Enable a handle to the triangle vertices
            GLES20.glEnableVertexAttribArray(it)

            // Prepare the triangle coordinate data
            GLES20.glVertexAttribPointer(
                it,
                COORDS_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                vertexStride,
                vertexBuffer
            )

            // get handle to fragment shader's vColor member
            mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor").also { colorHandle ->

                // Set color for drawing the triangle
                var color = floatArrayOf(0.0f, 0.0f, 0.0f, 1.0f)
                if (blockColor == "black") {
                    color = floatArrayOf(0.0f, 0.0f, 0.0f, 1.0f)
                }
                GLES20.glUniform4fv(colorHandle, 1, color, 0)
            }

            // Draw the triangle
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)

            // Disable vertex array
            GLES20.glDisableVertexAttribArray(it)
        }
    }
}
