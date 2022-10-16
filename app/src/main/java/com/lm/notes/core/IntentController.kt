package com.lm.notes.core

import android.content.Intent
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.lm.notes.R
import com.lm.notes.presentation.BaseActivity
import com.lm.notes.presentation.NotesViewModel
import com.lm.notes.ui.cells.view.app_widget.ToastCreator
import com.lm.notes.utils.log
import javax.inject.Inject


interface IntentController {

    fun checkForIntentAction(
        intent: Intent?,
        notesViewModel: NotesViewModel,
        lifecycleScope: LifecycleCoroutineScope
    )

    class Base @Inject constructor(
        private val toastCreator: ToastCreator
    ) : IntentController {

        override fun checkForIntentAction(
            intent: Intent?,
            notesViewModel: NotesViewModel,
            lifecycleScope: LifecycleCoroutineScope
        ) {

            intent?.apply {
                when (action) {
                    Intent.ACTION_SEND ->
                        if ("text/plain" == type) {
                            getStringExtra(Intent.EXTRA_TEXT)
                            toastCreator.invoke(R.string.text_plain)
                        }
                    Intent.ACTION_VIEW -> {
                        if ("text/plain" == type) {
                            getStringExtra(Intent.EXTRA_TEXT)
                            toastCreator.invoke(R.string.text_all)
                        }
                        if ("application/msword" == type) {
                            toastCreator.invoke(R.string.application_msword)
                        }
                    }
                    BaseActivity.IS_AUTH_ACTION -> notesViewModel.synchronize(lifecycleScope)
                }
            }
        }
    }
}