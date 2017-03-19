package com.app.backpackr.data.local.models

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Tags(open var spotTypes: RealmList<SpotType> = RealmList(),
        open var hotDates: RealmList<RequestingDate> = RealmList(),
        open var spotKeywords: RealmList<SpotKeyword> = RealmList(),
        open var locationTags: RealmList<SpotLocationTag> = RealmList()) : RealmObject() {
}

open class SpotType(@PrimaryKey open var spotType: String) : RealmObject() {}

open class RequestingDate(open var date: Long = 0L) : RealmObject() {}

open class SpotKeyword(open var keyword: String = "") : RealmObject() {}

open class SpotLocationTag(open var locationTag: String = "") : RealmObject() {}