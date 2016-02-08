package pl.mareklangiewicz.myhub.data

import io.realm.RealmObject
import io.realm.annotations.Required

open class Note : RealmObject {

    @Required open var label: String? = null
    open var text: String? = null

    constructor(label: String, text: String?) {
        this.label = label
        this.text = text
    }

    constructor() {
    }
}
