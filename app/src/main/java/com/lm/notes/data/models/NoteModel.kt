package com.lm.notes.data.models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue

data class NoteModel(
    val id: String = "",
    var text: String = "",
    var timestampChange: Long = 0,
    var timestampCreate: Long = 0,
    var header: String = "",
    var headerState: MutableState<String> = mutableStateOf(""),
    var boldMap: MutableState<String> = mutableStateOf(""),
    var underlinedMap: MutableState<String> = mutableStateOf(""),
    var italicMap: MutableState<String> = mutableStateOf(""),
    var textState: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue("")),
    var sizeXState: MutableState<Float> = mutableStateOf(200f),
    var sizeYState: MutableState<Float> = mutableStateOf(60f),
    var timestampChangeState: MutableState<Long> = mutableStateOf(0),
    var isChanged: Boolean = false
)
