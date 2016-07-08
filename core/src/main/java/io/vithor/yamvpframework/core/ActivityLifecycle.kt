package io.vithor.yamvpframework.core

/**
 * Created by Hazer on 4/11/16.
 */
enum class ActivityLifecycleEvent {
    None,
    Started,
    Stopped,
    Resumed,
    SaveInstanceState,
    Destroyed,
    Created,
    Paused
}