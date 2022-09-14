package com.lm.notes.ui.cells

import android.graphics.Typeface
import android.text.Editable
import android.text.Html
import android.text.Html.FROM_HTML_MODE_LEGACY
import android.text.Spanned
import android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE
import android.text.style.StyleSpan
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.lm.notes.data.local_data.NoteData
import com.lm.notes.utils.log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

interface EditTextProvider {

    val longClickState: StateFlow<Boolean>

    fun hideFormatPanel()

    fun setText(text: String)

    fun EditText.initEditText()

    fun toHtml(text: Spanned): String

    fun fromHtml(text: String): Spanned?

    fun setSpan(formatType: Any)

    val editText: EditText

    class Base @Inject constructor(
        private val noteData: NoteData,
        private val editTextInstance: EditText,
        private val inputMethodManager: InputMethodManager
    ) : EditTextProvider {

        private val _longClickState = MutableStateFlow(false)

        override val longClickState get() = _longClickState.asStateFlow()

        override fun hideFormatPanel() {
            _longClickState.value = false
            editText.showSoftInputOnFocus = true
            editText.isCursorVisible = true
        }

        override fun setText(text: String) = editText.setText(
            Html.fromHtml(text, FROM_HTML_MODE_LEGACY)
        )

        override fun EditText.initEditText() {
            setOnLongClickListener {
                inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
                showSoftInputOnFocus = false
                isCursorVisible = false
                _longClickState.value = true
                noteData.noteModelFullScreen.value
                false
            }
            setOnClickListener {
                if (_longClickState.value) hideFormatPanel()
                //inputMethodManager.showSoftInput(it, 0)
            }
        }

        override fun toHtml(text: Spanned) = Html.toHtml(text, flagHtml).toString()

        override fun fromHtml(text: String): Spanned? = Html.fromHtml(text, flagHtml)

        override fun setSpan(formatType: Any) {
            editText.apply {
                text.setSpan(formatType, selectionStart, selectionEnd, SPAN_EXCLUSIVE_EXCLUSIVE)
                text.updateSpan
            }
        }

        private val Editable.removeSpan get() = clearSpans()

        private val Spanned.updateSpan get() = noteData.noteModelFullScreen.value.also { noteModel ->
                noteModel.text = toHtml(this)
                noteModel.isChanged = true
            }

        override val editText get() = editTextInstance.apply { initEditText() }

        private val flagHtml by lazy { Html.FROM_HTML_MODE_LEGACY }
    }
}