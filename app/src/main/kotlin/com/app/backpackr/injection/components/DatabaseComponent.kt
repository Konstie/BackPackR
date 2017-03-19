package com.app.backpackr.injection.components

import com.app.backpackr.data.local.repository.RecognizedLocationsRepository
import com.app.backpackr.data.local.repository.TagsRepository
import com.app.backpackr.injection.modules.DatabaseModule
import com.app.backpackr.injection.scopes.ForApplication
import com.app.backpackr.presenters.home.HomePresenter
import dagger.Component
import io.realm.Realm

@Component(modules = arrayOf(DatabaseModule::class))
@ForApplication
interface DatabaseComponent {
    fun inject(homePresenter: HomePresenter)
    fun realm(): Realm
    fun placesRepository(): RecognizedLocationsRepository
    fun tagsRepository(): TagsRepository
}
