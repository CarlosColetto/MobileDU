package utils

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class FragmentLifeCycleSaver(private val onPauseAction: () -> Unit) : DefaultLifecycleObserver {
    override fun onPause(owner: LifecycleOwner) {
        onPauseAction()
    }
}