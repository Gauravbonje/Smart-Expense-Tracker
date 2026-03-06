package com.example.smartexpensetracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val amount: Double,
    val category: String,
    val date: Long = System.currentTimeMillis(),
    val note: String = ""
)

object ExpenseCategory {
    const val FOOD = "Food"
    const val TRAVEL = "Travel"
    const val SHOPPING = "Shopping"
    const val ONLINE_TRANSACTION = "Online Transaction"
    const val ONLINE_DELIVERY = "Online Delivery"
    const val OTHER = "Other"

    val ALL = listOf(FOOD, TRAVEL, SHOPPING, ONLINE_TRANSACTION, ONLINE_DELIVERY, OTHER)

    fun color(category: String): Long = when (category) {
        FOOD -> 0xFFFF9800
        TRAVEL -> 0xFF2196F3
        SHOPPING -> 0xFFE91E63
        ONLINE_TRANSACTION -> 0xFF9C27B0
        ONLINE_DELIVERY -> 0xFF4CAF50
        else -> 0xFF9E9E9E
    }

    fun emoji(category: String): String = when (category) {
        FOOD -> "🍔"
        TRAVEL -> "🚗"
        SHOPPING -> "🛍️"
        ONLINE_TRANSACTION -> "💳"
        ONLINE_DELIVERY -> "🛵"
        else -> "💰"
    }
}
