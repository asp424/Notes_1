package com.lm.notes.di.compose

import android.net.Uri
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.firebase.auth.FirebaseAuth
import com.lm.notes.core.IntentStates
import com.lm.notes.data.local_data.FilesProvider
import com.lm.notes.data.local_data.SPreferences
import com.lm.notes.presentation.MainActivity
import com.lm.notes.presentation.NotesViewModel
import com.lm.notes.presentation.ViewModelFactory
import com.lm.notes.ui.cells.view.app_widget.NoteAppWidgetController
import kotlinx.coroutines.CoroutineScope

data class MainDependencies(
    val width: Dp,
    val height: Dp,
    var iconUri: MutableState<Uri>,
    val infoVisibility: MutableState<Boolean>,
    val infoOffset: State<Dp>,
    val progressVisibility: MutableState<Boolean>,
    val coroutine: CoroutineScope,
    val notesViewModel: NotesViewModel,
    val firebaseAuth: FirebaseAuth,
    val sPreferences: SPreferences,
    val listState: LazyListState,
    val filesProvider: FilesProvider,
    val navController: NavHostController,
    val noteAppWidgetController: NoteAppWidgetController
)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreenDependencies(
    sPreferences: SPreferences,
    viewModelFactory: ViewModelFactory,
    firebaseAuth: FirebaseAuth,
    filesProvider: FilesProvider,
    noteAppWidgetController: NoteAppWidgetController,
    content: @Composable () -> Unit
) = with(LocalConfiguration.current) {
    val infoVisibility = remember { mutableStateOf(false) }
    val context = LocalContext.current as MainActivity
    val owner = LocalViewModelStoreOwner.current ?: context
    CompositionLocalProvider(
        Local provides MainDependencies(
            width = screenWidthDp.dp,
            height = screenHeightDp.dp,
            iconUri = remember { mutableStateOf(checkNotNull(sPreferences.readIconUri())) },
            progressVisibility = remember { mutableStateOf(false) },
            infoVisibility = remember { infoVisibility },
            coroutine = rememberCoroutineScope(),
            infoOffset = animateDpAsState(
                if (infoVisibility.value) 20.dp else 0.dp, tween(500)
            ),
            notesViewModel = remember {
                ViewModelProvider(owner, viewModelFactory)[NotesViewModel::class.java]
            },
            firebaseAuth = remember { firebaseAuth },
            sPreferences = remember { sPreferences },
            listState = rememberLazyListState(),
            filesProvider = remember { filesProvider },
            navController = rememberAnimatedNavController(),
            noteAppWidgetController = remember { noteAppWidgetController }
        ).apply {
                if(iconUri.toString().isNotEmpty()) {
                    progressVisibility.value = false
                    iconUri.value = sPreferences.readIconUri()?: Uri.EMPTY
                }
        }, content = content
    )
}

private val Local = staticCompositionLocalOf<MainDependencies> { error("No value provided") }

object MainDep {
    val mainDep @Composable get() = Local.current
}

