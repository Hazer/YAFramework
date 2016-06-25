package io.vithor.yamvpframework.core

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.util.Base64
import android.util.Base64InputStream
import android.util.TypedValue
import android.view.WindowManager
import io.vithor.yamvpframework.core.extensions.debugLog
import java.io.*
import java.nio.ByteBuffer

/**
 * Created by Vithorio Polten on 1/4/16.
 */
object BitmapUtils {

    fun pxToDip(context: Context, px: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, context.resources.displayMetrics)
    }

    fun dipToPx(context: Context, dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, dp, context.resources.displayMetrics)
    }

    @Suppress("deprecation")
    fun loadImage(resourceId: Int, context: Context): Bitmap? {
        val defaultDisplay = (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val point = Point()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            point.x = defaultDisplay.width
            point.y = defaultDisplay.height
        } else {
            defaultDisplay.getSize(point)
        }

        if (isLargeScreen(context)) {
            point.y *= 2
        }

        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(context.resources, resourceId, options)
        var sampleSize = (options.outHeight.toFloat() / point.y).toInt()
        if (sampleSize <= 0) {
            sampleSize = 1
        }

        val options1 = BitmapFactory.Options()
        options1.inSampleSize = sampleSize
        options1.inPurgeable = true

        var bitmap: Bitmap? = null
        try {
            bitmap = BitmapFactory.decodeResource(context.resources, resourceId, options1)
        } catch (e: OutOfMemoryError) {
            e.debugLog { "OOM load image" }
        }

        return bitmap
    }

    fun isLargeScreen(context: Context): Boolean {
        val type = getScreenType(context)
        return type == 3 || type == 4
    }

    fun getScreenType(context: Context): Int {
        return 15 and context.resources.configuration.screenLayout
    }

    fun fromAssets(context: Context, filePath: String): Bitmap? {
        val assetManager = context.assets

        val istr: InputStream
        var bitmap: Bitmap? = null
        try {
            istr = assetManager.open(filePath)
            bitmap = BitmapFactory.decodeStream(istr)
        } catch (e: IOException) {
            // handle exception
            e.debugLog { "io in assets loading" }
        }

        return bitmap
    }

    // Scale and keep aspect ratio
    fun scaleToFitWidth(b: Bitmap, width: Int): Bitmap {
        val factor = width / b.width.toFloat()
        return Bitmap.createScaledBitmap(b, width, (b.height * factor).toInt(), false)
    }

    // Scale and keep aspect ratio
    fun scaleToFitHeight(b: Bitmap, height: Int): Bitmap {
        val factor = height / b.height.toFloat()
        return Bitmap.createScaledBitmap(b, (b.width * factor).toInt(), height, false)
    }

    fun scaleDownToFit(b: Bitmap, maxSize: Int): Bitmap {
        val width = b.width
        val height = b.height

        if (width > height) {
            // landscape
            return scaleToFitHeight(b, maxSize)
        } else if (height > width) {
            // portrait
            return scaleToFitWidth(b, maxSize)
        }
        return b
    }

    @Throws(FileNotFoundException::class)
    fun decodeAndResizeUri(context: Context, imageUri: Uri, imageMaxSize: Int): Bitmap? {
        var b: Bitmap? = null

        //Decode image size
        val o = BitmapFactory.Options()
        o.inJustDecodeBounds = true

        var inputStream: InputStream = context.contentResolver.openInputStream(imageUri)
        BitmapFactory.decodeStream(inputStream, null, o)
        try {
            inputStream.close()
            var scale = 1
            if (o.outHeight > imageMaxSize || o.outWidth > imageMaxSize) {
                scale = Math.pow(2.0, Math.ceil(Math.log(imageMaxSize / Math.max(o.outHeight, o.outWidth).toDouble()) / Math.log(0.5)).toInt().toDouble()).toInt()
            }

            //Decode with inSampleSize
            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            inputStream = context.contentResolver.openInputStream(imageUri)
            b = BitmapFactory.decodeStream(inputStream, null, o2)
            inputStream.close()
        } catch (e: IOException) {
            e.debugLog { "Failed" }
        }

        return b
    }

    @Throws(FileNotFoundException::class)
    fun decodeAndResize(context: Context, imageFile: File, newFile: File, imageMaxSize: Int): File? {
        val b: Bitmap?

        //Decode image size
        val o = BitmapFactory.Options()
        o.inJustDecodeBounds = true

        var inputStream = context.contentResolver.openInputStream(Uri.fromFile(imageFile))
        BitmapFactory.decodeStream(inputStream, null, o)
        try {
            inputStream!!.close()
            var scale = 1
            if (o.outHeight > imageMaxSize || o.outWidth > imageMaxSize) {
                scale = Math.pow(2.0, Math.ceil(Math.log(imageMaxSize / Math.max(o.outHeight, o.outWidth).toDouble()) / Math.log(0.5)).toInt().toDouble()).toInt()
            }

            //Decode with inSampleSize
            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            inputStream = context.contentResolver.openInputStream(Uri.fromFile(imageFile))
            b = BitmapFactory.decodeStream(inputStream, null, o2)

            b.debugLog { "Bitmap Size: " + b!!.width + "x" + b.height }

            val os: OutputStream

            os = FileOutputStream(newFile)
            b.compress(Bitmap.CompressFormat.JPEG, 70, os)
            os.flush()
            os.close()

            //            int size = b.getRowBytes() * b.getHeight();
            //            ByteBuffer byteBuffer = ByteBuffer.allocate(size);
            //            writeByteBuffer(byteBuffer, newFile);

            return newFile
        } catch (e: IOException) {
            e.printStackTrace()
            e.debugLog { "Failed resizing" }
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close()
                } catch (ignored: IOException) {

                }

            }
        }
        return null
    }

    @Throws(IOException::class)
    fun writeByteBuffer(byteBuffer: ByteBuffer, newFile: File) {
        // Set to true if the bytes should be appended to the file;
        // set to false if the bytes should replace current bytes
        // (if the file exists)
        val append = false

        // Create a writable file channel
        val wChannel = FileOutputStream(newFile, append).channel

        // Write the ByteBuffer contents; the bytes between the ByteBuffer's
        // position and the limit is written to the file
        wChannel.write(byteBuffer)

        // Close the file
        wChannel.close()
    }

    /**
     * Returns Width or Height of the picture, depending on which size is smaller. Doesn't actually
     * decode the picture, so it is pretty efficient to run.
     */
    fun getSmallerExtentFromBytes(bytes: ByteArray): Int {
        val options = BitmapFactory.Options()

        // don't actually decode the picture, just return its bounds
        options.inJustDecodeBounds = true
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)

        // test what the best sample size is
        return Math.min(options.outWidth, options.outHeight)
    }

    /**
     * Returns Width or Height of the picture, depending on which size is smaller. Doesn't actually
     * decode the picture, so it is pretty efficient to run.
     */
    fun getLargerExtent(inputStream: InputStream): Int {
        val options = BitmapFactory.Options()

        // don't actually decode the picture, just return its bounds
        options.inJustDecodeBounds = true
        BitmapFactory.decodeStream(inputStream, null, options)

        // test what the best sample size is
        return Math.max(options.outWidth, options.outHeight)
    }

    fun bitmapToBase64(bitmap: Bitmap, recycle: Boolean): String {
        var bao: ByteArrayOutputStream? = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao)
        if (recycle) {
            recycleBitmap(bitmap)
            System.gc()
        }
        val ba = bao!!.toByteArray()
        try {
            bao.close()
        } catch (e: IOException) {
            // Ignore
            e.debugLog { "io in base64" }
        } finally {
            bao = null
        }

        return "data:image/jpeg;base64," + Base64.encodeToString(ba, Base64.NO_WRAP)
    }


    fun inputStreamToBase64(inputStream: InputStream): String {
        val base64InputStream = Base64InputStream(inputStream, Base64.NO_WRAP)

        //Now that we have the InputStream, we can read it and put it into the String
        val writer = StringWriter()
        try {
            YAIOUtils.copy(base64InputStream, writer, null)
        } catch (e: IOException) {
            e.debugLog { "io in base64" }
        }

        return "data:image/jpeg;base64," + writer.toString()
    }

    @Throws(FileNotFoundException::class)
    fun base64FromUri(context: Context, imageUri: Uri, newSize: Int): String {
        if (newSize > 0) {
            return bitmapToBase64(decodeAndResizeUri(context, imageUri, newSize)!!, true)
        }
        return inputStreamToBase64(context.contentResolver.openInputStream(imageUri))
    }

    private fun recycleBitmap(bitmap: Bitmap?) {
        if (bitmap != null && !bitmap.isRecycled) {
            bitmap.recycle()
        }
    }
}
