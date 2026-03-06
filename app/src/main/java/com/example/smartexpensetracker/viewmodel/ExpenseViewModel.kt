package com.example.smartexpensetracker.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartexpensetracker.data.database.ExpenseDatabase
import com.example.smartexpensetracker.data.model.Expense
import com.example.smartexpensetracker.data.model.ExpenseCategory
import com.example.smartexpensetracker.data.repository.ExpenseRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ExpenseRepository
    private val sharedPreferences = application.getSharedPreferences("budget_prefs", Context.MODE_PRIVATE)

    val allExpenses: Flow<List<Expense>>

    private val _monthlyBudget = MutableStateFlow(sharedPreferences.getFloat("monthly_budget", 5000f).toDouble())
    val monthlyBudget: StateFlow<Double> = _monthlyBudget.asStateFlow()

    private val _dailyBudget = MutableStateFlow(sharedPreferences.getFloat("daily_budget", 500f).toDouble())
    val dailyBudget: StateFlow<Double> = _dailyBudget.asStateFlow()

    val monthlyTotal: StateFlow<Double>
    val todayTotal: StateFlow<Double>
    
    val filteredExpenses: StateFlow<List<Expense>>
    
    private val _isDailyBudgetExceeded = MutableStateFlow(false)
    val isDailyBudgetExceeded: StateFlow<Boolean> = _isDailyBudgetExceeded.asStateFlow()

    init {
        val expenseDao = ExpenseDatabase.getDatabase(application).expenseDao()
        repository = ExpenseRepository(expenseDao)
        allExpenses = repository.allExpenses

        val calendar = Calendar.getInstance()
        
        // Monthly logic
        val startOfMonth = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
        
        val endOfMonth = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }.timeInMillis

        // Daily logic
        val startOfToday = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        filteredExpenses = allExpenses.map { list ->
            list.filter { it.date in startOfMonth..endOfMonth }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        monthlyTotal = repository.getTotalByDateRange(startOfMonth, endOfMonth)
            .map { it ?: 0.0 }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

        todayTotal = repository.getTotalByDateRange(startOfToday, System.currentTimeMillis())
            .map { it ?: 0.0 }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

        // Monitor daily budget
        combine(todayTotal, _dailyBudget) { total, budget ->
            total > budget
        }.onEach { _isDailyBudgetExceeded.value = it }
         .launchIn(viewModelScope)
    }

    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            repository.insertExpense(expense)
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            repository.deleteExpense(expense)
        }
    }

    fun setMonthlyBudget(budget: Double) {
        _monthlyBudget.value = budget
        sharedPreferences.edit().putFloat("monthly_budget", budget.toFloat()).apply()
    }

    fun setDailyBudget(budget: Double) {
        _dailyBudget.value = budget
        sharedPreferences.edit().putFloat("daily_budget", budget.toFloat()).apply()
    }

    fun parseSmartInput(input: String): Pair<Double, String> {
        val cleanInput = input.trim().lowercase()
        val amountRegex = Regex("(\\d+(\\.\\d+)?)")
        val amountMatch = amountRegex.findAll(cleanInput).lastOrNull()
        val amount = amountMatch?.value?.toDoubleOrNull() ?: 0.0
        
        val categoryPart = if (amountMatch != null) {
            cleanInput.replace(amountMatch.value, "").trim()
        } else cleanInput

        val category = when {
            categoryPart.matches(Regex(".*(rapido|uber|ola|auto|metro|bike|taxi|fuel|petrol).*")) -> ExpenseCategory.TRAVEL
            categoryPart.matches(Regex(".*(swiggy|instamart|blinkit|zomato|food|dinner|lunch|burger|pizza).*")) -> ExpenseCategory.ONLINE_DELIVERY
            categoryPart.matches(Regex(".*(chinmay|abibhav|akagra|aryan|[a-z]+ paid [a-z]+|transfer|sent to).*")) -> ExpenseCategory.ONLINE_TRANSACTION
            categoryPart.contains("food") || categoryPart.contains("eat") -> ExpenseCategory.FOOD
            else -> ExpenseCategory.OTHER
        }
        
        return Pair(amount, category)
    }
}
