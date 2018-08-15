package cz.lamorak.captionthis.model

import java.util.*

data class ImageDescription(val tags: Array<String>,
                            val captions: Array<Caption>) {

    data class Caption(val text: String,
                       val confidence: Double)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ImageDescription) return false

        if (!Arrays.equals(tags, other.tags)) return false
        if (!Arrays.equals(captions, other.captions)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = Arrays.hashCode(tags)
        result = 31 * result + Arrays.hashCode(captions)
        return result
    }
}