package com.app.backpackr.helpers

class ActivitiesTracker {
    private @Volatile var activitiesInForeground = 0

    fun onActivityWentToBackground() {
        activitiesInForeground--
    }

    fun onActivityWentToForeground() {
        activitiesInForeground++
    }

    fun isApplicationInBackground(): Boolean {
        return activitiesInForeground == 0
    }

    fun getCurrentActivitiesInForeground(): Int {
        return activitiesInForeground
    }
}