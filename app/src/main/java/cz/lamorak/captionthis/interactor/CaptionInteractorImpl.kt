package cz.lamorak.captionthis.interactor

import cz.lamorak.captionthis.CaptionApi
import cz.lamorak.captionthis.model.ImageDescription.Caption
import cz.lamorak.captionthis.util.FileReader
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class CaptionInteractorImpl(private val api: CaptionApi,
                            private val fileReader: FileReader) : CaptionInteractor {

    override fun caption(imageUri: String): Single<String> {
        val imageFile = fileReader.readFile(imageUri)
        return api.describeImage(imageFile)
                .map({ mostLikelyCaption(it.description.captions) })
                .subscribeOn(Schedulers.io())
    }

    private fun mostLikelyCaption(captions: Array<Caption>): String {
        return captions.sortedWith(Comparator{ left, right -> left.confidence.compareTo(right.confidence) })
                .first().text
    }
}