package cz.lamorak.captionthis

import android.content.Intent
import android.provider.MediaStore
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.*
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.runner.AndroidJUnit4
import cz.lamorak.captionthis.viewmodel.CaptionAction.OpenCamera
import cz.lamorak.captionthis.viewmodel.CaptionAction.OpenGallery
import cz.lamorak.captionthis.viewmodel.CaptionState
import cz.lamorak.captionthis.viewmodel.CaptionState.*
import org.hamcrest.Matchers.not
import org.hamcrest.core.AllOf.allOf
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    val intentsTestRule: IntentsTestRule<MainActivity> = IntentsTestRule(MainActivity::class.java)

    @Test
    @Ignore // TODO: Tests need cleanup to allow other tests running after
    fun openGallery() {
        intentsTestRule.activity.handle(OpenGallery)

        intended(allOf(
                hasAction(Intent.ACTION_PICK),
                hasData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
                hasFlag(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        ))
    }

    @Test
    @Ignore // TODO: Tests need cleanup to allow other tests running after
    fun openCamera() {
        intentsTestRule.activity.handle(OpenCamera)

        intended(allOf(
                hasAction(MediaStore.ACTION_IMAGE_CAPTURE),
                hasFlag(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        ))
    }

    @Test
    fun selectImage() {
        intentsTestRule.activity.let {
            it.runOnUiThread { it.display(SelectImage) }
        }

        onView(withId(R.id.select_image)).check(matches(isDisplayed()))
        onView(withId(R.id.image_preview)).check(matches(not(isDisplayed())))
        onView(withId(R.id.processing_label)).check(matches(not(isDisplayed())))
        onView(withId(R.id.caption)).check(matches(not(isDisplayed())))
    }

    @Test
    fun processing() {
        intentsTestRule.activity.let {
            it.runOnUiThread { it.display(Processing("imageUri")) }
        }

        onView(withId(R.id.select_image)).check(matches(not(isDisplayed())))
        onView(withId(R.id.image_preview)).check(matches(isDisplayed()))
        onView(withId(R.id.processing_label)).check(matches(isDisplayed()))
        onView(withId(R.id.caption)).check(matches(not(isDisplayed())))
    }

    @Test
    fun captioned() {
        val caption = "caption"
        intentsTestRule.activity.let {
            it.runOnUiThread { it.display(CaptionedImage("imageUri", caption)) }
        }

        onView(withId(R.id.select_image)).check(matches(not(isDisplayed())))
        onView(withId(R.id.image_preview)).check(matches(isDisplayed()))
        onView(withId(R.id.processing_label)).check(matches(not(isDisplayed())))
        onView(withId(R.id.caption)).check(matches(allOf(isDisplayed(), withText(caption))))
    }
}
