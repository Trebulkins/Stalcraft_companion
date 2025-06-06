package com.example.stalcraft_companion.data.modles

data class CategoryGroup(
    val categoryName: String,
    val subcategories: List<SubcategoryGroup>,
    val items: List<Item>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as CategoryGroup
        return categoryName == other.categoryName && subcategories == other.subcategories
    }

    override fun hashCode(): Int {
        return categoryName.hashCode()
    }
}

data class SubcategoryGroup(
    val subcategoryName: String,
    val items: List<Item>
)