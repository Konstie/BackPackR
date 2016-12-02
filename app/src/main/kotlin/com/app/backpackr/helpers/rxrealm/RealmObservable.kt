package com.app.backpackr.helpers.rxrealm

import android.content.Context
import io.realm.Realm
import io.realm.RealmObject
import rx.Observable
import rx.functions.Func1

/**
 * Created by kmikhailovskiy on 02.12.2016.
 */
class RealmObservable private constructor() {
    companion object {
        fun <T : RealmObject> getObject(context: Context, function: Func1<Realm, T>): Observable<T> {
            return Observable.create(object : OnSubscribeRealm<T>(context) {
                override fun get(realm: Realm): T {
                    return function.call(realm)
                }
            })
        }
    }
}