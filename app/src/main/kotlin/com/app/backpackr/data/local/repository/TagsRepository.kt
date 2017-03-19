package com.app.backpackr.data.local.repository

import io.reactivex.Completable

interface TagsRepository {
    fun createTagsRepository(): Completable
    fun addSpotType(): Completable
    fun addHotDate(): Completable
    fun addKeyword(): Completable
    fun addLocationTag(): Completable
}
