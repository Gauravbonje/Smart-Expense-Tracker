package com.example.smartexpensetracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartexpensetracker.data.model.Expense
import com.example.smartexpensetracker.data.model.ExpenseCategory
import com.example.smartexpensetracker.viewmodel.ExpenseViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddExpenseScreen(viewModel: ExpenseViewModel, onSaved: () -> Unit) {
    var amount by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(ExpenseCategory.FOOD) }
    var note by remember { mutableStateOf("") }
    var smartInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    // This block runs automatically whenever you type in "Smart Input"
    LaunchedEffect(smartInput) {
        if (smartInput.isNotEmpty()) {
            val (parsedAmount, parsedCategory) = viewModel.parseSmartInput(smartInput)
            if (parsedAmount > 0) {
                amount = parsedAmount.toString()
                selectedCategory = parsedCategory
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("Add Expense", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        // ── Smart Input Section ──
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("✨ AI Smart Input", fontWeight = FontWeight.Bold)
                Text("Try: 'Uber 250' or 'Paid Chinmay 500'", style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = smartInput,
                    onValueChange = { smartInput = it },
                    label = { Text("Describe your expense...") },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        // ── Manual Amount ──
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it; errorMessage = "" },
            label = { Text("Amount (₹)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            isError = errorMessage.isNotEmpty(),
        )

        // ── Category Selection (Manual Override) ──
        Text("Category (Manual Override)", fontWeight = FontWeight.Medium)
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            ExpenseCategory.ALL.forEach { cat ->
                FilterChip(
                    selected = selectedCategory == cat,
                    onClick = { selectedCategory = cat },
                    label = { Text("${ExpenseCategory.emoji(cat)} $cat") }
                )
            }
        }

        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            label = { Text("Note (Optional)") },
            modifier = Modifier.fillMaxWidth()
        )

        // ── Save Button ──
        Button(
            onClick = {
                val amt = amount.toDoubleOrNull()
                if (amt == null || amt <= 0) {
                    errorMessage = "Please enter a valid amount"
                } else {
                    viewModel.addExpense(Expense(
                        amount = amt,
                        category = selectedCategory,
                        note = note
                    ))
                    onSaved()
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("💾  Save Expense")
        }
    }
}
