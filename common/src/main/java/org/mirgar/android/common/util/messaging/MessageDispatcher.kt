package org.mirgar.android.common.util.messaging

import android.content.Context
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MessageDispatcher {
    private val _event = MutableLiveData<SnackbarSuite>()
    val event: LiveData<SnackbarSuite> = _event

    fun show(msgGen: ContextTextGenerator, action: SnackbarActionSuite? = null) {
        _event.value =
            SnackbarSuite(
                msgGen,
                action
            )
    }

    fun show(text: CharSequence, action: SnackbarActionSuite? = null) {
        show(generatorOf(text), action)
    }

    fun show(stringId: Int, action: SnackbarActionSuite? = null) {
        show(generatorOf(stringId), action)
    }

    fun show(messageFactory: Context.() -> CharSequence, action: SnackbarActionSuite? = null) {
        show(generatorOf(messageFactory), action)
    }

    fun show(
        msgGen: ContextTextGenerator,
        actionTitle: ContextTextGenerator,
        action: (View) -> Unit
    ) {
        show(msgGen, actionSuiteOf(actionTitle, action))
    }

    fun show(msgGen: ContextTextGenerator, actionTitle: CharSequence, action: (View) -> Unit) {
        show(msgGen, actionSuiteOf(actionTitle, action))
    }

    fun show(msgGen: ContextTextGenerator, actionTitle: Int, action: (View) -> Unit) {
        show(msgGen, actionSuiteOf(actionTitle, action))
    }

    fun show(
        msgGen: ContextTextGenerator,
        actionTitle: Context.() -> CharSequence,
        action: (View) -> Unit
    ) {
        show(msgGen, actionSuiteOf(actionTitle, action))
    }

    fun show(text: CharSequence, actionTitle: ContextTextGenerator, action: (View) -> Unit) {
        show(text, actionSuiteOf(actionTitle, action))
    }

    fun show(text: CharSequence, actionTitle: CharSequence, action: (View) -> Unit) {
        show(text, actionSuiteOf(actionTitle, action))
    }

    fun show(text: CharSequence, actionTitle: Int, action: (View) -> Unit) {
        show(text, actionSuiteOf(actionTitle, action))
    }

    fun show(text: CharSequence, actionTitle: Context.() -> CharSequence, action: (View) -> Unit) {
        show(text, actionSuiteOf(actionTitle, action))
    }

    fun show(stringId: Int, actionTitle: ContextTextGenerator, action: (View) -> Unit) {
        show(stringId, actionSuiteOf(actionTitle, action))
    }

    fun show(stringId: Int, actionTitle: CharSequence, action: (View) -> Unit) {
        show(stringId, actionSuiteOf(actionTitle, action))
    }

    fun show(stringId: Int, actionTitle: Int, action: (View) -> Unit) {
        show(stringId, actionSuiteOf(actionTitle, action))
    }

    fun show(stringId: Int, actionTitle: Context.() -> CharSequence, action: (View) -> Unit) {
        show(stringId, actionSuiteOf(actionTitle, action))
    }

    fun show(
        messageFactory: Context.() -> CharSequence,
        actionTitle: ContextTextGenerator,
        action: (View) -> Unit
    ) {
        show(messageFactory, actionSuiteOf(actionTitle, action))
    }

    fun show(
        messageFactory: Context.() -> CharSequence,
        actionTitle: CharSequence,
        action: (View) -> Unit
    ) {
        show(messageFactory, actionSuiteOf(actionTitle, action))
    }

    fun show(
        messageFactory: Context.() -> CharSequence,
        actionTitle: Int,
        action: (View) -> Unit
    ) {
        show(messageFactory, actionSuiteOf(actionTitle, action))
    }

    fun show(
        messageFactory: Context.() -> CharSequence,
        actionTitle: Context.() -> CharSequence,
        action: (View) -> Unit
    ) {
        show(messageFactory, actionSuiteOf(actionTitle, action))
    }


    private fun generatorOf(text: CharSequence) =
        ContextTextGeneratorText(
            text
        )

    private fun generatorOf(responseId: Int) =
        ContextTextGeneratorResId(
            responseId
        )

    private fun generatorOf(lambda: Context.() -> CharSequence) =
        ContextTextGeneratorLambda(
            lambda
        )


    private fun actionSuiteOf(gen: ContextTextGenerator, action: (View) -> Unit) =
        SnackbarActionSuite(gen, action)

    private fun actionSuiteOf(text: CharSequence, action: (View) -> Unit) =
        SnackbarActionSuite(
            generatorOf(text),
            action
        )

    private fun actionSuiteOf(responseId: Int, action: (View) -> Unit) =
        SnackbarActionSuite(
            generatorOf(responseId),
            action
        )

    private fun actionSuiteOf(lambda: Context.() -> CharSequence, action: (View) -> Unit) =
        SnackbarActionSuite(
            generatorOf(lambda),
            action
        )


    private class ContextTextGeneratorLambda(
        val factory: Context.() -> CharSequence
    ) : ContextTextGenerator {
        override fun getText(context: Context) = context.factory()
    }

    private class ContextTextGeneratorResId(
        @StringRes val responseId: Int
    ) : ContextTextGenerator {
        override fun getText(context: Context) = context.getString(responseId)
    }

    private class ContextTextGeneratorText(val text: CharSequence) :
        ContextTextGenerator {
        override fun getText(context: Context) = text
    }
}