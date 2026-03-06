package com.example.smartexpensetracker.data.dao

import androidx.room.*
import com.example.smartexpensetracker.data.model.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    // Add a new expense. If it already exists, replace it.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense)

    // Delete a specific expense
    @Delete
    suspend fun deleteExpense(expense: Expense)

    // Get all expenses, newest first.
    // Flow keeps your UI updated automatically when data changes!
    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    // Get total spending for a specific date range
    @Query("SELECT SUM(amount) FROM expenses WHERE date BETWEEN :startDate AND :endDate")
    fun getTotalByDateRange(startDate: Long, endDate: Long): Flow<Double?>

    // Get spending broken down by category for charts
    @Query("""
        SELECT category, SUM(amount) as total 
        FROM expenses 
        WHERE date BETWEEN :startDate AND :endDate 
        GROUP BY category
    """)
    fun getCategoryTotalsByDateRange(startDate: Long, endDate: Long): Flow<List<CategoryTotal>>
}

// A simple helper for our chart data
data class CategoryTotal(
    val category: String,
    val total: Double
)