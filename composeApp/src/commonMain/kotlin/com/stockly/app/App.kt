package com.stockly.app

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.stockly.app.domain.MovementType
import com.stockly.app.domain.Product
import com.stockly.app.presentation.screens.*
import com.stockly.app.theme.Cream
import com.stockly.app.theme.Forest
import com.stockly.app.theme.StocklyTheme

private sealed interface Screen {
    data object Welcome : Screen
    data object Dashboard : Screen
    data object Products : Screen
    data object Movements : Screen
    data object More : Screen
    data object Categories : Screen
    data object Stats : Screen
    data class Detail(val id: String) : Screen
    data class ProductForm(val id: String? = null) : Screen
    data class MovementForm(val id: String) : Screen
    data class ProductHistory(val id: String) : Screen
}

@Composable
fun StocklyApp() {
    StocklyTheme {
        val viewModel = remember { InventoryViewModel() }
        val state by viewModel.state.collectAsState()
        var screen by remember { mutableStateOf<Screen>(Screen.Welcome) }
        var productsFilter by remember { mutableStateOf("Todos") }
        var movementsFilter by remember { mutableStateOf("Todos") }
        val showBottom = screen in listOf(Screen.Dashboard, Screen.Products, Screen.Movements, Screen.More)

        Scaffold(
            containerColor = Cream,
            bottomBar = {
                if (showBottom) {
                    NavigationBar(containerColor = MaterialTheme.colorScheme.surface, tonalElevation = 4.dp) {
                        BottomItem("Inicio", Icons.Default.Home, screen == Screen.Dashboard) { screen = Screen.Dashboard }
                        BottomItem("Productos", Icons.Default.Inventory2, screen == Screen.Products) { screen = Screen.Products }
                        NavigationBarItem(selected = false, onClick = { screen = Screen.ProductForm() }, icon = {
                            FilledIconButton(onClick = { screen = Screen.ProductForm() }, colors = IconButtonDefaults.filledIconButtonColors(containerColor = Forest)) { Icon(Icons.Default.Add, null) }
                        }, label = { Text("") })
                        BottomItem("Movimientos", Icons.Default.SwapVert, screen == Screen.Movements) { screen = Screen.Movements }
                        BottomItem("Más", Icons.Default.Menu, screen == Screen.More) { screen = Screen.More }
                    }
                }
            }
        ) { padding ->
            Box(Modifier.padding(padding).fillMaxSize()) {
                when (val s = screen) {
                    Screen.Welcome -> WelcomeScreen { screen = Screen.Dashboard }
                    Screen.Dashboard -> DashboardScreen(
                        state = state,
                        onProduct = { screen = Screen.Detail(it) },
                        onSeeProducts = { filter ->
                            productsFilter = filter
                            screen = Screen.Products
                        },
                        onSeeSales = {
                            movementsFilter = "Ventas hoy"
                            screen = Screen.Movements
                        }
                    )
                    Screen.Products -> ProductsScreen(state, { screen = Screen.Detail(it) }, { screen = Screen.ProductForm() }, initialFilter = productsFilter)
                    Screen.Movements -> MovementsScreen(state, initialFilter = movementsFilter)
                    Screen.More -> MoreScreen({ screen = Screen.Categories }, { screen = Screen.Stats })
                    Screen.Categories -> CategoriesScreen(state) { screen = Screen.More }
                    Screen.Stats -> StatsScreen(state) { screen = Screen.More }
                    is Screen.Detail -> state.products.firstOrNull { it.id == s.id }?.let { product ->
                        ProductDetailScreen(product, { screen = Screen.Products }, { screen = Screen.MovementForm(product.id) }, { screen = Screen.ProductForm(product.id) }, {
                            viewModel.deleteProduct(product.id); screen = Screen.Products
                        }, { screen = Screen.ProductHistory(product.id) })
                    } ?: run { screen = Screen.Products }
                    is Screen.ProductForm -> {
                        val existing = s.id?.let { id -> state.products.firstOrNull { it.id == id } }
                        ProductFormScreen(existing, state.categories, { screen = existing?.let { Screen.Detail(it.id) } ?: Screen.Products }) {
                            viewModel.saveProduct(it); screen = Screen.Detail(it.id)
                        }
                    }
                    is Screen.MovementForm -> state.products.firstOrNull { it.id == s.id }?.let { product ->
                        MovementFormScreen(product, { screen = Screen.Detail(product.id) }) { type, qty, reason, note ->
                            val error = viewModel.registerMovement(product.id, type, qty, reason, note)
                            if (error == null) screen = Screen.Detail(product.id)
                            error
                        }
                    } ?: run { screen = Screen.Products }
                    is Screen.ProductHistory -> Column {
                        TopBack("Historial", { screen = Screen.Detail(s.id) })
                        MovementsScreen(state, s.id)
                    }
                }
            }
        }
    }
}

@Composable
private fun RowScope.BottomItem(label: String, icon: ImageVector, selected: Boolean, onClick: () -> Unit) {
    NavigationBarItem(selected = selected, onClick = onClick, icon = { Icon(icon, null) }, label = { Text(label) })
}
