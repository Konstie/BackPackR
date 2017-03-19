package com.app.backpackr.data.local.repository

import io.reactivex.Completable
import io.realm.Realm

class TagsRepositoryImpl(var realm: Realm) : TagsRepository {
    override fun createTagsRepository(): Completable {
        return Completable.fromCallable {

        }
    }

    override fun addSpotType(): Completable {
        return Completable.fromCallable {

        }
    }

    override fun addHotDate(): Completable {
        return Completable.fromCallable {

        }
    }

    override fun addKeyword(): Completable {
        return Completable.fromCallable {

        }
    }

    override fun addLocationTag(): Completable {
        return Completable.fromCallable {

        }
    }

}