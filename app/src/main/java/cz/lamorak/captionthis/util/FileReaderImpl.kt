package cz.lamorak.captionthis.util

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FileReaderImpl(private val contentResolver: ContentResolver): FileReader {

    override fun readFile(uri: String): MultipartBody.Part {
        val fileUri = Uri.parse(uri)
        val fileInfo = fileInfo(fileUri)

        val bytes = when {
            fileInfo.size < MAX_SIZE -> readBytes(fileUri)
            fileInfo.type == "image/jpeg" -> JpegUtil.compress(readBytes(fileUri), MAX_SIZE)
            else -> throw FileTooLargeException()
        }

        val body = RequestBody.create(MediaType.parse(fileInfo.type), bytes)
        return MultipartBody.Part.createFormData("file", fileInfo.name, body)
    }

    private fun readBytes(fileUri: Uri): ByteArray = contentResolver.openInputStream(fileUri).readBytes()

    private fun fileInfo(fileUri: Uri): FileInfo {
        val cursor = contentResolver.query(fileUri, null, null, null, null)
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
        cursor.moveToFirst()
        val name = cursor.getString(nameIndex)
        val size = cursor.getLong(sizeIndex)
        cursor.close()
        val type = contentResolver.getType(fileUri)
        return FileInfo(type, name, size)
    }

    data class FileInfo(val type: String,
                        val name: String,
                        val size: Long)

    companion object {
        private const val MAX_SIZE: Long = 4 * 1024 * 1024
    }
}