package com.example.smartexpensetracker.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smartexpensetracker.data.model.ExpenseCategory
import com.example.smartexpensetracker.viewmodel.ExpenseViewModel

@Composable
fun ChartsScreen(viewModel: ExpenseViewModel) {
    val categoryTotals by viewModel.allExpenses.collectAsStateWithLifecycle(initialValue = emptyList())

    // Grouping expenses by category for the chart
    val chartData = categoryTotals.groupBy { it.category }
        .mapValues { it.value.sumOf { expense -> expense.amount }.toFloat() }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Text("Spending Analysis", fontSize = 24.sp, fontWeight = FontWeight.Bold) }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    if (chartData.isEmpty()) {
                        Text("No data to show yet", color = Color.Gray)
                    } else {
                        // Custom Pie Chart drawing
                        PieChart(data = chartData.toList())
                    }
                }
            }
        }
    }
}

@Composable
fun PieChart(data: List<Pair<String, Float>>) {
    val total = data.sumOf { it.second.toDouble() }.toFloat()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.size(200.dp)) {
                var startAngle = -90f
                data.forEach { (category, value) ->
                    val sweep = (value / total) * 360f
                    drawArc(
                        color = Color(ExpenseCategory.color(category)),
                        startAngle = startAngle,
                        sweepAngle = sweep,
                        useCenter = true,
                        size = Size(size.width, size.height)
                    )
                    startAngle += sweep
                }
            }
        }
        Spacer(Modifier.height(24.dp))
        // Legend
        data.forEach { (cat, _) ->
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(Color(ExpenseCategory.color(cat))))
                Spacer(Modifier.width(8.dp))
                Text(cat, fontSize = 14.sp)
            }
        }
    }
}
