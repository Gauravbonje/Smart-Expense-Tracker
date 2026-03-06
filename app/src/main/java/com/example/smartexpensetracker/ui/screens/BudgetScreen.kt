package com.example.smartexpensetracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smartexpensetracker.viewmodel.ExpenseViewModel

@Composable
fun BudgetScreen(viewModel: ExpenseViewModel) {
    val monthlyBudget by viewModel.monthlyBudget.collectAsStateWithLifecycle()
    val dailyBudget by viewModel.dailyBudget.collectAsStateWithLifecycle()
    
    var monthlyInput by remember(monthlyBudget) { mutableStateOf(monthlyBudget.toInt().toString()) }
    var dailyInput by remember(dailyBudget) { mutableStateOf(dailyBudget.toInt().toString()) }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text("Budget Settings", fontSize = 28.sp, fontWeight = FontWeight.Bold)

        // Monthly Budget Section
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Monthly Budget", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                OutlinedTextField(
                    value = monthlyInput,
                    onValueChange = { monthlyInput = it },
                    label = { Text("Monthly Limit (INR)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = {
                        monthlyInput.toDoubleOrNull()?.let { viewModel.setMonthlyBudget(it) }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Monthly Budget")
                }
            }
        }

        // Daily Budget Section
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Daily Budget", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                OutlinedTextField(
                    value = dailyInput,
                    onValueChange = { dailyInput = it },
                    label = { Text("Daily Limit (INR)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = {
                        dailyInput.toDoubleOrNull()?.let { viewModel.setDailyBudget(it) }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Daily Budget")
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Developer Credit Section
        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
            Text(
                text = "Developed by Gaurav Yadav | B.Tech CSE Student",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
