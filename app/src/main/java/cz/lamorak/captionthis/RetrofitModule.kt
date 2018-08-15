package cz.lamorak.captionthis

import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
class RetrofitModule {

    @Provides
    @Singleton
    fun retrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
    }

    @Provides
    fun okHttpClient(interceptors: Array<Interceptor>): OkHttpClient {
        val builder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            interceptors.forEach { builder.addInterceptor(it) }
        }
        return builder.build()
    }

    @Provides
    fun networkInterceptors(): Array<Interceptor> = arrayOf(
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY),
            Interceptor {
                val original = it.request()
                val request = original.newBuilder()
                        .header("Ocp-Apim-Subscription-Key", BuildConfig.API_KEY)
                        .method(original.method(), original.body())
                        .build()
                it.proceed(request)
            }
    )
}