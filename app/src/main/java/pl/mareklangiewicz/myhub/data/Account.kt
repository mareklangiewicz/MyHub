package pl.mareklangiewicz.myhub.data

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

/**
 * Created by Marek Langiewicz on 03.01.16.
 * TODO SOMEDAY: try to use concise kotlin data classes as RealmObjects - I tried once and had some problems - try again someday

 */
open class Account : RealmObject {

    /**
     * When it was fetched from github - in miliseconds from 1.1.1970
     */
    @Index open var time: Long = 0
    @PrimaryKey open var login: String? = null
    @Required @Index open var name: String? = null
    open var avatar: String? = null
    open var description: String? = null
    open var notes = RealmList<Note>()
    open var repos = RealmList<Repo>()

    constructor() {
    }

    constructor(time: Long, login: String, name: String, avatar: String?, description: String?) {
        this.time = time
        this.login = login
        this.avatar = avatar
        this.name = name
        this.description = description
    }
}

