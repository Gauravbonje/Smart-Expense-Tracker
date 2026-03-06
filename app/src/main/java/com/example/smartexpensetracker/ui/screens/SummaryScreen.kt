package com.example.smartexpensetracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smartexpensetracker.viewmodel.ExpenseViewModel

@Composable
fun SummaryScreen(viewModel: ExpenseViewModel) {
    val monthlyTotal by viewModel.monthlyTotal.collectAsStateWithLifecycle()
    val budget by viewModel.monthlyBudget.collectAsStateWithLifecycle()

    val budgetPct = if (budget > 0) (monthlyTotal / budget).toFloat() else 0f

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Text("Monthly Summary", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }

        // Budget Progress Card
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Budget Progress", fontWeight = FontWeight.Bold)
                    LinearProgressIndicator(
                        progress = { budgetPct.coerceAtMost(1f) },
                        modifier = Modifier.fillMaxWidth().height(12.dp),
                        color = if (budgetPct >= 0.8f) Color.Red else MaterialTheme.colorScheme.primary
                    )
                    Text("₹${monthlyTotal.toInt()} / ₹${budget.toInt()}", fontSize = 12.sp)
                }
            }
        }
    }
}