package org.mirgar.android.mgclient.data.net.models

import kotlin.properties.Delegates

class CategoryContainer {
    lateinit var template: String
    lateinit var groups: List<CategoryGroup>
}

class CategoryGroup {
    var parentId by Delegates.notNull<Long>()
    lateinit var items: List<Category>
}

class Category {
    var id by Delegates.notNull<Long>()
    lateinit var name: String
    var hasIcon by Delegates.notNull<Boolean>()
}
