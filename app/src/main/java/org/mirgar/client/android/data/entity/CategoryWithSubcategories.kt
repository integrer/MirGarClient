package org.mirgar.client.android.data.entity

import androidx.room.Embedded
import androidx.room.Relation
import org.mirgar.client.android.data.entity.Category

data class CategoryWithSubcategories(
    @Embedded
    val category: Category,

    @Relation(parentColumn = "category_id", entityColumn = "super_id")
    val subCategories: List<Category> = emptyList()
)
