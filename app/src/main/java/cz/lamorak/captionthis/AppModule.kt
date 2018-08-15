package cz.lamorak.captionthis

import android.app.Application
import android.content.ContentResolver
import cz.lamorak.captionthis.util.FileReader
import cz.lamorak.captionthis.util.FileReaderImpl
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    fun fileReader(contentResolver: ContentResolver): FileReader = FileReaderImpl(contentResolver)

    @Provides
    fun contentResolver(application: Application): ContentResolver = application.contentResolver
}