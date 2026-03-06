package com.example.smartexpensetracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smartexpensetracker.data.model.Expense
import com.example.smartexpensetracker.data.model.ExpenseCategory
import com.example.smartexpensetracker.viewmodel.ExpenseViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(viewModel: ExpenseViewModel) {
    val expenses by viewModel.filteredExpenses.collectAsStateWithLifecycle()
    val monthlyTotal by viewModel.monthlyTotal.collectAsStateWithLifecycle()
    val todayTotal by viewModel.todayTotal.collectAsStateWithLifecycle()
    val dailyBudget by viewModel.dailyBudget.collectAsStateWithLifecycle()
    val isExceeded by viewModel.isDailyBudgetExceeded.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        // ── Header Card ──
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("This Month", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                        Text("₹${String.format("%.2f", monthlyTotal)}",
                            color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Today", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                        Text("₹${String.format("%.2f", todayTotal)}",
                            color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Daily Budget Progress
                val progress = if (dailyBudget > 0) (todayTotal / dailyBudget).toFloat().coerceIn(0f, 1f) else 0f
                Text(
                    text = if (isExceeded) "Daily Budget Exceeded" else "Daily Budget Progress",
                    color = if (isExceeded) Color.Red else Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                    color = if (isExceeded) Color.Red else MaterialTheme.colorScheme.secondary,
                    trackColor = Color.White.copy(alpha = 0.3f)
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Spent: ₹${String.format("%.0f", todayTotal)}", color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
                    Text("Budget: ₹${String.format("%.0f", dailyBudget)}", color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
                }
            }
        }

        // ── Expense List ──
        Text(
            "Expenses (Current Month)",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            fontWeight = FontWeight.Bold, fontSize = 18.sp
        )

        if (expenses.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No expenses this month! Tap + to add one.", color = Color.Gray)
            }
        } else {
            LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
                items(expenses) { expense ->
                    ExpenseItem(expense = expense, onDelete = { viewModel.deleteExpense(expense) })
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun ExpenseItem(expense: Expense, onDelete: () -> Unit) {
    val categoryColor = Color(ExpenseCategory.color(expense.category))
    val dateFormat = remember { SimpleDateFormat("dd MMM, EEE", Locale.getDefault()) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(40.dp).background(categoryColor.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = expense.category.take(1),
                    color = categoryColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(expense.category, fontWeight = FontWeight.SemiBold)
                Text(dateFormat.format(Date(expense.date)), color = Color.Gray, fontSize = 12.sp)
                if (expense.note.isNotEmpty()) {
                    Text(expense.note, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
            }
            Text("₹${expense.amount}", fontWeight = FontWeight.Bold, color = categoryColor)
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Gray)
            }
        }
    }
}
