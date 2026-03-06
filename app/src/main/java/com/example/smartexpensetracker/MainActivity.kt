package com.example.smartexpensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.smartexpensetracker.ui.screens.*
import com.example.smartexpensetracker.ui.theme.SmartExpenseTrackerTheme
import com.example.smartexpensetracker.viewmodel.ExpenseViewModel

// Navigation routes as constants — avoids typos
object Routes {
    const val HOME = "home"
    const val ADD_EXPENSE = "add_expense"
    const val SUMMARY = "summary"
    const val CHARTS = "charts"
    const val BUDGET = "budget"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartExpenseTrackerTheme {
                ExpenseTrackerApp()
            }
        }
    }
}

@Composable
fun ExpenseTrackerApp() {
    val navController = rememberNavController()
    val viewModel: ExpenseViewModel = viewModel()

    // Bottom nav items: (Route, Label, Icon)
    val navItems = listOf(
        Triple(Routes.HOME, "Home", Icons.Filled.Home),
        Triple(Routes.ADD_EXPENSE, "Add", Icons.Filled.Add),
        Triple(Routes.SUMMARY, "Summary", Icons.Filled.BarChart),
        Triple(Routes.CHARTS, "Charts", Icons.Filled.PieChart),
        Triple(Routes.BUDGET, "Budget", Icons.Filled.AccountBalanceWallet)
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                navItems.forEach { (route, label, icon) ->
                    NavigationBarItem(
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label) },
                        selected = currentDestination?.hierarchy?.any { it.route == route } == true,
                        onClick = {
                            navController.navigate(route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.HOME) { HomeScreen(viewModel) }
            composable(Routes.ADD_EXPENSE) { AddExpenseScreen(viewModel) { navController.popBackStack() } }
            composable(Routes.SUMMARY) { SummaryScreen(viewModel) }
            composable(Routes.CHARTS) { ChartsScreen(viewModel) }
            composable(Routes.BUDGET) { BudgetScreen(viewModel) }
        }
    }
}