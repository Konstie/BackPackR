package com.app.backpackr.injection.modules

import com.app.backpackr.data.local.repository.RecognizedLocationsRepository
import com.app.backpackr.data.local.repository.RecognizedLocationsRepositoryImpl
import com.app.backpackr.data.local.repository.TagsRepository
import com.app.backpackr.data.local.repository.TagsRepositoryImpl
import com.app.backpackr.injection.scopes.ForApplication
import dagger.Module
import dagger.Provides
import io.realm.Realm
import io.realm.RealmConfiguration

@Module
class DatabaseModule() {
    val REALM_DATABASE_NAME = "backpackr.realm"
    val SCHEMA_VERSION: Long = 1

    @Provides
    @ForApplication
    fun provideRealm(realmConfiguration: RealmConfiguration): Realm {
        return Realm.getInstance(realmConfiguration)
    }

    @Provides
    @ForApplication
    fun provideCustomRealmConfiguration(): RealmConfiguration {
        val customRealmConfiguration = RealmConfiguration.Builder()
                .name(REALM_DATABASE_NAME)
                .schemaVersion(SCHEMA_VERSION)
                .build()
        return customRealmConfiguration
    }

    @Provides
    @ForApplication
    fun providePlacesRepository(realm: Realm): RecognizedLocationsRepository {
        return RecognizedLocationsRepositoryImpl(realm)
    }

    @Provides
    @ForApplication
    fun provideTagsRepository(realm: Realm): TagsRepository {
        return TagsRepositoryImpl(realm)
    }
}