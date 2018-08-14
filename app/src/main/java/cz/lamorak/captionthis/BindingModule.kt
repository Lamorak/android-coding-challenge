package cz.lamorak.captionthis

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BindingModule {

    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity
}