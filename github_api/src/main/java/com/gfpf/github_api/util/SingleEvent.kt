package com.gfpf.github_api.util

/**
 * Wrapper for data that is exposed via a LiveData that represents an event.
 *
 * LiveData is designed to hold and re-emit the last value it was set with when an observer is attached.
 * This behavior ensures that observers always receive the most recent data, which is particularly useful
 * for UI components that need to display data consistently across configuration changes or when the user
 * navigates back to a previous screen.
 *
 * However, there are scenarios where you might want to handle a piece of data only once, such as navigation
 * events or showing toast messages. Using this Event class, you can ensure that the data is consumed only
 * once.
 *
 * @param <T> The type of content held by this event.
 */
open class SingleEvent<out T>(private val content: T) {

    // Indicates whether the event has been handled.
    private var hasBeenHandled = false

    /**
     * Returns the content if it has not been handled yet, and marks it as handled.
     *
     * @return The content if not handled, otherwise null.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it has already been handled.
     *
     * @return The content.
     */
    fun peekContent(): T = content
}
