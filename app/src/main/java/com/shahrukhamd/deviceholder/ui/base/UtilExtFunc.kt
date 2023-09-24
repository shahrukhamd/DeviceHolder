package com.shahrukhamd.deviceholder.ui.base

import androidx.lifecycle.Observer

class Event<T>(private val data: T?) {

    var isDataConsumed: Boolean = false
        private set

    fun getDataIfNotConsumed(): T? {
        return if (isDataConsumed) {
            null
        } else {
            isDataConsumed = true
            data
        }
    }

    fun peek(): T? = data
}

class EventObserver<T>(private val onEventNotConsumedObserver: (T) -> Unit) : Observer<Event<T>> {
    override fun onChanged(value: Event<T>) {
        value.getDataIfNotConsumed()?.let {
            onEventNotConsumedObserver(it)
        }
    }
}