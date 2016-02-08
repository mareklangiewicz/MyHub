package pl.mareklangiewicz.myhub.data

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.Required

open class Repo : RealmObject {

    @Required open var name: String? = null
    open var description: String? = null
    open var forks: Long = 0
    open var watchers: Long = 0
    open var stars: Long = 0
    open var notes = RealmList<Note>()

    constructor(name: String, description: String?, forks: Long, watchers: Long, stars: Long) {
        this.name = name
        this.description = description
        this.forks = forks
        this.watchers = watchers
        this.stars = stars
    }

    constructor() {
    }
}
