package com.lm.notes.ui.bars

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.lm.notes.di.compose.MainDep.mainDep
import com.lm.notes.utils.animScale
import com.lm.notes.utils.noRippleClickable

@Composable
fun TopBar() {
    with(mainDep) {
        with(notesViewModel) {
            with(uiStates) {
                noteModelFullScreen.collectAsState().value.textState.value.apply {
                    TopAppBar(backgroundColor = getMainColor, modifier = Modifier.fillMaxWidth()
                        .noRippleClickable { false.setSettingsVisible }) {
                        DefaultBar(animScale(getIsMainMode))
                        FullScreenBar(
                            animScale(
                                getIsFullscreenMode && spansProvider.fromHtml(
                                    replace(" ", "", false)
                                )?.isNotEmpty() ?: false
                            )
                        )
                        DeleteBar(animScale(getIsDeleteMode))
                    }
                }
            }
        }
    }
}
