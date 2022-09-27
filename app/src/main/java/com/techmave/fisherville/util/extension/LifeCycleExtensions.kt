package com.techmave.fisherville.util.extension

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController

object LifeCycleExtensions {

    fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {

        observe(lifecycleOwner, object : Observer<T> {

            override fun onChanged(t: T?) {

                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }

    fun <T> Fragment.setNavigationResult(key: String, value: T) {

        findNavController().previousBackStackEntry?.savedStateHandle?.set(key, value)
    }

    fun <T> Fragment.getNavigationResult(@IdRes id: Int, key: String, onResult: (result: T) -> Unit) {

        val navBackStackEntry = findNavController().getBackStackEntry(id)

        val observer = LifecycleEventObserver { _, event ->

            if (event == Lifecycle.Event.ON_RESUME && navBackStackEntry.savedStateHandle.contains(key)) {

                navBackStackEntry.savedStateHandle.get<T>(key)?.let(onResult)
                navBackStackEntry.savedStateHandle.remove<T>(key)
            }
        }

        navBackStackEntry.lifecycle.addObserver(observer)

        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->

            if (event == Lifecycle.Event.ON_DESTROY) {

                navBackStackEntry.lifecycle.removeObserver(observer)
            }
        })
    }
}