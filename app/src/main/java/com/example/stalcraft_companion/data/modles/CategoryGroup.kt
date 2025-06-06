package com.example.stalcraft_companion.data.modles

data class CategoryGroup(
    val mainCategory: String,
    val subcategories: List<SubcategoryGroup>,
    val directItems: List<Item> // Предметы без подкатегории
)

data class SubcategoryGroup(
    val subcategoryName: String,
    val items: List<Item>
)