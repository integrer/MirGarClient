package org.mirgar.client.android.data

import androidx.room.Embedded
import androidx.room.Relation

class CategoryRelations(
    @Embedded
    val category: Category,

    @Relation(parentColumn = "id", entityColumn = "super_id")
    val subCategories: List<Category> = emptyList()
)
