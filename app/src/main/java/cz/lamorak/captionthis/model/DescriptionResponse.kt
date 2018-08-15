package cz.lamorak.captionthis.model

data class DescriptionResponse(val requestId: String,
                               val description: ImageDescription,
                               val metadata: ImageMetadata)