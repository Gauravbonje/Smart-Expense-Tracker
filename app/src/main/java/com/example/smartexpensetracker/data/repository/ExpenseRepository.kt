package com.example.smartexpensetracker.data.repository

import com.example.smartexpensetracker.data.dao.CategoryTotal
import com.example.smartexpensetracker.data.dao.ExpenseDao
import com.example.smartexpensetracker.data.model.Expense
import kotlinx.coroutines.flow.Flow

class ExpenseRepository(private val expenseDao: ExpenseDao) {

    // READ operations (These update automatically when data changes)
    val allExpenses: Flow<List<Expense>> = expenseDao.getAllExpenses()

    fun getTotalByDateRange(startDate: Long, endDate: Long): Flow<Double?> =
        expenseDao.getTotalByDateRange(startDate, endDate)

    fun getCategoryTotals(startDate: Long, endDate: Long): Flow<List<CategoryTotal>> =
        expenseDao.getCategoryTotalsByDateRange(startDate, endDate)

    // WRITE operations (suspend = runs on a background thread so the app doesn't freeze)
    suspend fun insertExpense(expense: Expense) = expenseDao.insertExpense(expense)

    suspend fun deleteExpense(expense: Expense) = expenseDao.deleteExpense(expense)
}