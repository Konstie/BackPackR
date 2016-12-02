package com.app.backpackr.helpers.rxrealm

import android.content.Context
import com.app.backpackr.dagger.components.AppComponent
import com.app.backpackr.dagger.components.DaggerAppComponent
import io.realm.Realm
import io.realm.RealmObject
import rx.Observable
import rx.Subscriber
import rx.subscriptions.Subscriptions
import javax.inject.Inject

/**
 * Created by kmikhailovskiy on 02.12.2016.
 */

abstract class OnSubscribeRealm<T : RealmObject>(context: Context) : Observable.OnSubscribe<T> {
    @Inject var realmComponent: AppComponent? = null
    var realmDatabase: Realm? = null

    init {
        realmComponent = DaggerAppComponent.builder().build()
        realmDatabase = realmComponent?.realm()
    }

    override fun call(subscriber: Subscriber<in T>?) {
        subscriber?.add(Subscriptions.create {
            realmDatabase?.close()
        })
        val objectToWrite: T
        realmDatabase?.beginTransaction()
        try {
            objectToWrite = get(realmDatabase!!)
            realmDatabase?.commitTransaction()
        } catch (runtimeException: RuntimeException) {
            realmDatabase?.cancelTransaction()
            subscriber?.onError(Throwable(runtimeException.message))
            return
        } catch (error: Error) {
            realmDatabase?.cancelTransaction()
            subscriber?.onError(Throwable(error.message))
            return
        }
        subscriber?.onNext(objectToWrite)
        subscriber?.onCompleted()
    }

    abstract fun get(realm: Realm): T
}