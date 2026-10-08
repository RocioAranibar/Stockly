package com.stockly.app.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stockly.app.domain.*

private val Dark = Color(0xFF102E31)
private val Green = Color(0xFF08755D)
private val Background = Color(0xFFF2F8F5)
private val Muted = Color(0xFF617078)
private val Border = Color(0xFFEAF0ED)

@Composable
fun DashboardScreen(
    state: InventoryState,
    onProduct: (String) -> Unit,
    onSeeProducts: (String) -> Unit,
    onSeeSales: () -> Unit,
    onAddProduct: () -> Unit,
    onSeeMovements: () -> Unit,
    onSeeStats: () -> Unit
) {
    val low = state.products.filter { it.status != StockStatus.AVAILABLE }
    val ranking = state.movements
        .filter { it.type == MovementType.OUT && it.reason.equals("Venta", ignoreCase = true) }
        .groupBy { it.productId }
        .mapValues { (_, items) -> items.sumOf { it.quantity } }
        .toList()
        .sortedByDescending { it.second }
        .mapNotNull { (id, units) -> state.products.firstOrNull { it.id == id }?.let { it to units } }
        .take(4)

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Background),
        contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 16.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Inventory2, contentDescription = null, tint = Green, modifier = Modifier.size(30.dp))
                Spacer(Modifier.width(8.dp))
                Text("Stockly", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Dark, modifier = Modifier.weight(1f))
                // Solo un icono decorativo: no se simula una sesión ni notificaciones existentes.
                Surface(color = Color(0xFFD9F0E6), shape = RoundedCornerShape(50)) {
                    Icon(Icons.Default.Storefront, contentDescription = null, tint = Green, modifier = Modifier.padding(10.dp).size(20.dp))
                }
            }
            Spacer(Modifier.height(20.dp))
            Text("¡Hola! 👋", fontSize = 29.sp, fontWeight = FontWeight.Bold, color = Dark)
            Text("Así va tu negocio hoy", fontSize = 14.sp, color = Muted)
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                MetricCard("Productos", state.products.size.toString(), "Ver inventario", Icons.Default.Inventory2,
                    Green, Color(0xFFE0F8EC), Modifier.weight(1f)) { onSeeProducts("Todos") }
                MetricCard("Stock bajo", state.lowStockCount.toString(), "Revisar stock", Icons.Default.WarningAmber,
                    Color(0xFFE68A10), Color(0xFFFFF2DD), Modifier.weight(1f)) { onSeeProducts("Stock bajo") }
            }
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                MetricCard("Sin stock", state.outOfStockCount.toString(), "Ver agotados", Icons.Default.RemoveShoppingCart,
                    Color(0xFFCF4B4B), Color(0xFFFFEBED), Modifier.weight(1f)) { onSeeProducts("Sin stock") }
                MetricCard("Ventas hoy", state.todaySalesUnits.toString(), "Unidades vendidas", Icons.Default.ShoppingCart,
                    Color(0xFF087F92), Color(0xFFE3F5FB), Modifier.weight(1f), onSeeSales)
            }
        }
        item {
            Text("Acciones rápidas", fontWeight = FontWeight.Bold, color = Dark, fontSize = 19.sp)
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                ActionCard("Nuevo producto", Icons.Default.AddBox, Green, Modifier.weight(1f), onAddProduct)
                ActionCard("Registrar venta", Icons.Default.ShoppingCart, Color(0xFF087F92), Modifier.weight(1f)) { onSeeProducts("Todos") }
            }
            Spacer(Modifier.height(7.dp))
            Text("Para registrar una venta, elegí un producto y seleccioná Registrar movimiento.", color = Muted, fontSize = 11.sp)
        }
        item {
            SectionPanel("Productos con stock bajo", "Ver todos", { onSeeProducts("Stock bajo") }) {
                if (low.isEmpty()) EmptyInfo("¡Todo al día! No hay productos para reponer.")
                low.take(4).forEachIndexed { index, product ->
                    if (index > 0) HorizontalDivider(color = Border)
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable { onProduct(product.id) }.padding(vertical = 11.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        EmojiIcon(product.emoji)
                        Spacer(Modifier.width(10.dp))
                        Column(Modifier.weight(1f)) {
                            Text(product.name, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Dark, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text(product.category, fontSize = 12.sp, color = Muted)
                            Text(if (product.stock == 0) "Sin unidades" else "${product.stock} unidades restantes", fontSize = 12.sp, color = Color(0xFFC44848))
                        }
                        Text(if (product.stock == 0) "Sin stock" else "Stock bajo", fontSize = 10.sp,
                            color = Color(0xFFAA6010), modifier = Modifier.background(Color(0xFFFFEED6), RoundedCornerShape(50)).padding(horizontal = 7.dp, vertical = 5.dp))
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Muted, modifier = Modifier.size(19.dp))
                    }
                }
            }
        }
        item {
            SectionPanel("Productos más vendidos", "Ver ranking", onSeeStats) {
                if (ranking.isEmpty()) EmptyInfo("Todavía no hay ventas registradas.")
                ranking.forEachIndexed { index, pair ->
                    val (product, units) = pair
                    if (index > 0) HorizontalDivider(color = Border)
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable { onProduct(product.id) }.padding(vertical = 11.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(Modifier.size(28.dp).background(Color(0xFFFFEACB), RoundedCornerShape(50)), contentAlignment = Alignment.Center) {
                            Text("${index + 1}", color = Color(0xFFC5770B), fontWeight = FontWeight.Bold)
                        }
                        Spacer(Modifier.width(8.dp))
                        EmojiIcon(product.emoji)
                        Spacer(Modifier.width(9.dp))
                        Column(Modifier.weight(1f)) {
                            Text(product.name, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Dark, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text(product.category, fontSize = 12.sp, color = Muted)
                            Text("$units unidades vendidas", fontSize = 12.sp, color = Muted)
                        }
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Muted, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
        item {
            SectionPanel("Últimos movimientos", "Ver todos", onSeeMovements) {
                if (state.movements.isEmpty()) EmptyInfo("Todavía no hay movimientos.")
                state.movements.take(5).forEachIndexed { index, movement ->
                    if (index > 0) HorizontalDivider(color = Border)
                    Row(Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                        val incoming = movement.type == MovementType.IN
                        Box(Modifier.size(38.dp).background(if (incoming) Color(0xFFE6F3FD) else Color(0xFFE1F6EC), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                            Icon(if (incoming) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward, contentDescription = null,
                                tint = if (incoming) Color(0xFF177AA7) else Green, modifier = Modifier.size(20.dp))
                        }
                        Spacer(Modifier.width(10.dp))
                        Column(Modifier.weight(1f)) {
                            Text("${movement.reason} · ${movement.productName}", color = Dark, fontWeight = FontWeight.SemiBold, fontSize = 13.sp,
                                maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text("${if (incoming) "+" else "-"}${movement.quantity} unidades", color = Muted, fontSize = 12.sp)
                        }
                        Text(movement.dateLabel, fontSize = 11.sp, color = Muted)
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricCard(title: String, value: String, note: String, icon: ImageVector, tint: Color, background: Color, modifier: Modifier, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = modifier.height(142.dp), shape = RoundedCornerShape(23.dp), colors = CardDefaults.cardColors(containerColor = background)) {
        Column(Modifier.fillMaxSize().padding(13.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(29.dp))
                Spacer(Modifier.weight(1f))
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Dark, modifier = Modifier.size(18.dp))
            }
            Text(value, fontWeight = FontWeight.ExtraBold, fontSize = 27.sp, color = Dark)
            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Dark)
            Text(note, fontSize = 11.sp, color = Muted, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
private fun ActionCard(label: String, icon: ImageVector, tint: Color, modifier: Modifier, onClick: () -> Unit) {
    Surface(modifier = modifier.clickable(onClick = onClick), shape = RoundedCornerShape(17.dp), color = Color.White) {
        Row(Modifier.padding(horizontal = 12.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(24.dp))
            Spacer(Modifier.width(7.dp))
            Text(label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Dark, lineHeight = 15.sp)
        }
    }
}

@Composable
private fun SectionPanel(title: String, action: String, onAction: () -> Unit, content: @Composable ColumnScope.() -> Unit) {
    Card(shape = RoundedCornerShape(21.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(Modifier.fillMaxWidth().padding(horizontal = 13.dp, vertical = 15.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Dark, modifier = Modifier.weight(1f))
                Spacer(Modifier.width(6.dp))
                Text(action, fontSize = 11.sp, color = Green, fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable(onClick = onAction).padding(vertical = 7.dp))
            }
            Spacer(Modifier.height(5.dp))
            HorizontalDivider(color = Border)
            content()
        }
    }
}

@Composable
private fun EmojiIcon(emoji: String) {
    Box(Modifier.size(43.dp).background(Background, RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
        Text(emoji, fontSize = 23.sp)
    }
}

@Composable
private fun EmptyInfo(message: String) {
    Text(message, fontSize = 13.sp, color = Muted, modifier = Modifier.padding(vertical = 14.dp))
}
