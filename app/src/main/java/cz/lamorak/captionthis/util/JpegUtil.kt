package cz.lamorak.captionthis.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.IOException


object JpegUtil {

    private const val MAX_PIXELS = 3145728

    fun compress(bytes: ByteArray, maxSize: Long): ByteArray {
        val options = readOptions(bytes)
        options.inSampleSize = determineSampleSize(options.outWidth, options.outHeight)
        val bitmap = readBitmap(bytes, options)
        return compressBitmap(bitmap, maxSize)
    }

    private fun readOptions(bytes: ByteArray): BitmapFactory.Options {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
        return options
    }

    private fun determineSampleSize(width: Int, height: Int): Int {
        log("Original size: $width x $height")
        var sample = 1
        while ((width / sample) * (height / sample) > MAX_PIXELS) {
            sample *= 2
        }
        log("Output size: ${width / sample} x ${height / sample}, sample: $sample")
        return sample
    }

    private fun readBitmap(bytes: ByteArray, options: BitmapFactory.Options): Bitmap {
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
    }

    private fun compressBitmap(bitmap: Bitmap, maxSize: Long): ByteArray {
        var size = bitmap.byteCount
        log("Uncompressed size: $size")
        var quality = 100
        val outputStream = ByteArrayOutputStream()
        do {
            try {
                outputStream.flush()
                outputStream.reset()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            size = outputStream.size()
            quality -= 5
        } while (size >= maxSize)

        log("Compressed size $size, JPEG quality: $quality")
        return outputStream.toByteArray()
    }

    private fun log(message: String) {
        Log.i("JPEG compression", message)
    }
}