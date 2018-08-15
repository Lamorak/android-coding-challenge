package cz.lamorak.captionthis.util

import okhttp3.MultipartBody

interface FileReader {

    fun readFile(uri: String): MultipartBody.Part
}