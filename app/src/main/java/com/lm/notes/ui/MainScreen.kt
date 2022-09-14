package com.lm.notes.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.lm.notes.R
import com.lm.notes.di.compose.MainDep.mainDep
import com.lm.notes.presentation.MainActivity
import com.lm.notes.ui.bars.BottomBar
import com.lm.notes.ui.bars.TopBar
import com.lm.notes.ui.cells.NavHost
import com.lm.notes.utils.backPressHandle

@Composable
fun MainScreen() {

    var isFullScreen by remember { mutableStateOf(false) }

    Image(
        painter = painterResource(id = R.drawable.notebook_list), null,
        modifier = Modifier
            .fillMaxSize()
            .alpha(0.5f), contentScale = ContentScale.Crop
    )

    with(mainDep) {
        val isFormatMode by editTextProvider.longClickState.collectAsState()

        notesViewModel.noteModelFullScreen.value.also { noteModel ->

            Column {
                TopBar(isFullScreen)
                NavHost(noteModel, isFormatMode) { isFullScreen = it }
            }

            BottomBar(isFullScreen)

            (LocalContext.current as MainActivity).apply {
                BackHandler {
                    backPressHandle(navController, notesViewModel, coroutine, noteModel)
                }
            }
        }
    }
}
