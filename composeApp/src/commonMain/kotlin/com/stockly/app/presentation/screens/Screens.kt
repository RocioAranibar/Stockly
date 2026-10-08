package com.stockly.app.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stockly.app.domain.*
import com.stockly.app.presentation.components.*
import com.stockly.app.theme.*

@Composable
fun WelcomeScreen(onStart: () -> Unit) {
    Column(Modifier.fillMaxSize().background(Cream).padding(28.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.weight(.8f))
        Box(Modifier.size(104.dp).background(Forest.copy(alpha = .10f), RoundedCornerShape(28.dp)), contentAlignment = Alignment.Center) {
            Text("📦", fontSize = 58.sp)
        }
        Spacer(Modifier.height(22.dp))
        Text("Stockly", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = ForestDark)
        Text("Tu inventario, siempre con vos", fontSize = 18.sp, color = Muted)
        Spacer(Modifier.height(28.dp))
        Card(colors = CardDefaults.cardColors(containerColor = StocklyCard), shape = RoundedCornerShape(22.dp)) {
            Column(Modifier.padding(22.dp)) {
                FeatureLine("Controlá el stock en segundos")
                FeatureLine("Registrá entradas y ventas")
                FeatureLine("Detectá productos que necesitan reposición")
            }
        }
        Spacer(Modifier.weight(1f))
        Button(onClick = onStart, modifier = Modifier.fillMaxWidth().height(54.dp), shape = RoundedCornerShape(18.dp)) {
            Text("Comenzar", fontWeight = FontWeight.SemiBold)
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable private fun FeatureLine(text: String) {
    Row(Modifier.padding(vertical = 7.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.CheckCircle, null, tint = Success)
        Spacer(Modifier.width(10.dp)); Text(text)
    }
}

@Composable
fun DashboardScreen(
    state: InventoryState,
    onProduct: (String) -> Unit,
    onSeeProducts: () -> Unit
) {
    val lowStockProducts = state.products
        .filter { it.status != StockStatus.AVAILABLE }
        .take(3)

    val weeklyValues = listOf(20, 34, 25, 43, 30, 52, 41)
    val weeklyLabels = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")
    val maxValue = weeklyValues.maxOrNull() ?: 1

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(max = 520.dp)
                .padding(horizontal = 18.dp),
            contentPadding = PaddingValues(
                top = 12.dp,
                bottom = 110.dp
            )
        ) {

        item {
            // CABECERA
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Hola, Sofi 👋",
                        fontSize = 27.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Así va tu negocio hoy",
                        fontSize = 13.sp,
                        color = Muted
                    )
                }

                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Default.NotificationsNone,
                        contentDescription = "Notificaciones"
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // ESTADÍSTICAS SUPERIORES
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    title = "Productos",
                    value = state.products.size.toString(),
                    icon = Icons.Default.Inventory2,
                    tint = Forest,
                    modifier = Modifier.weight(1f)
                )

                StatCard(
                    title = "Stock bajo",
                    value = state.lowStockCount.toString(),
                    icon = Icons.Default.WarningAmber,
                    tint = Warning,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    title = "Sin stock",
                    value = state.outOfStockCount.toString(),
                    icon = Icons.Default.Cancel,
                    tint = Danger,
                    modifier = Modifier.weight(1f)
                )

                StatCard(
                    title = "Ventas hoy",
                    value = state.todayOutUnits.toString(),
                    icon = Icons.Default.BarChart,
                    tint = Success,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(24.dp))

            // VENTAS DE LA SEMANA
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ventas de esta semana",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "Ver más ›",
                    color = Forest,
                    fontSize = 12.sp
                )
            }

            Spacer(Modifier.height(10.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = StocklyCard
                ),
                shape = RoundedCornerShape(18.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(105.dp)
                        .padding(
                            start = 14.dp,
                            end = 14.dp,
                            top = 14.dp,
                            bottom = 10.dp
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    weeklyValues.forEachIndexed { index, value ->

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {

                            Spacer(
                                modifier = Modifier.weight(1f)
                            )

                            Box(
                                modifier = Modifier
                                    .width(14.dp)
                                    .height(
                                        (58f * value / maxValue)
                                            .coerceAtLeast(8f)
                                            .dp
                                    )
                                    .background(
                                        color = Forest.copy(alpha = 0.55f),
                                        shape = RoundedCornerShape(
                                            topStart = 4.dp,
                                            topEnd = 4.dp
                                        )
                                    )
                            )

                            Spacer(Modifier.height(6.dp))

                            Text(
                                text = weeklyLabels[index],
                                fontSize = 9.sp,
                                color = Muted
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // STOCK BAJO
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Productos con stock bajo",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "Ver todos ›",
                    color = Forest,
                    fontSize = 12.sp,
                    modifier = Modifier.clickable {
                        onSeeProducts()
                    }
                )
            }

            Spacer(Modifier.height(8.dp))
        }

        if (lowStockProducts.isEmpty()) {

            item {
                EmptyCard(
                    title = "Stock al día",
                    body = "No tenés productos para reponer."
                )
            }

        } else {

            items(
                items = lowStockProducts,
                key = { it.id }
            ) { product ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            onProduct(product.id)
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = StocklyCard
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(
                                    Color(0xFFF0ECE3),
                                    RoundedCornerShape(10.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = product.emoji,
                                fontSize = 23.sp
                            )
                        }

                        Spacer(Modifier.width(12.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = product.name,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )

                            Text(
                                text = if (product.stock == 0) {
                                    "Sin stock"
                                } else {
                                    "Quedan ${product.stock} unidades"
                                },
                                fontSize = 12.sp,
                                color = if (product.stock == 0) {
                                    Danger
                                } else {
                                    Muted
                                }
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(9.dp)
                                .background(
                                    color = if (product.stock == 0) {
                                        Danger
                                    } else {
                                        Warning
                                    },
                                    shape = RoundedCornerShape(50)
                                )
                        )

                        Spacer(Modifier.width(7.dp))

                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = Muted,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductsScreen(state: InventoryState, onProduct: (String) -> Unit, onAdd: () -> Unit) {
    var search by remember { mutableStateOf("") }
    var filter by remember { mutableStateOf("Todos") }
    Column(Modifier.fillMaxSize().padding(horizontal = 18.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("Productos", fontSize = 28.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            FilledIconButton(onClick = onAdd, colors = IconButtonDefaults.filledIconButtonColors(containerColor = Forest)) {
                Icon(Icons.Default.Add, null)
            }
        }
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(search, { search = it }, modifier = Modifier.fillMaxWidth(), singleLine = true,
            placeholder = { Text("Buscar productos...") }, leadingIcon = { Icon(Icons.Default.Search, null) }, shape = RoundedCornerShape(16.dp))
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Todos", "Stock bajo", "Sin stock").forEach { label ->
                FilterChip(selected = filter == label, onClick = { filter = label }, label = { Text(label) })
            }
        }
        val filtered = state.products.filter { p ->
            val matchesSearch = p.name.contains(search, true) || p.category.contains(search, true)
            val matchesFilter = when (filter) {
                "Stock bajo" -> p.status == StockStatus.LOW
                "Sin stock" -> p.status == StockStatus.OUT
                else -> true
            }
            matchesSearch && matchesFilter
        }
        Spacer(Modifier.height(6.dp))
        if (filtered.isEmpty()) EmptyCard("Sin resultados", "Probá con otro término o filtro.")
        else LazyColumn(contentPadding = PaddingValues(bottom = 100.dp)) {
            items(filtered, key = { it.id }) { product ->
                ProductRow(product) { onProduct(product.id) }
                HorizontalDivider(color = Color(0xFFEAE5DB))
            }
        }
    }
}

@Composable
fun ProductDetailScreen(product: Product, onBack: () -> Unit, onMovement: () -> Unit, onEdit: () -> Unit, onDelete: () -> Unit, onHistory: () -> Unit) {
    LazyColumn(Modifier.fillMaxSize().padding(horizontal = 20.dp), contentPadding = PaddingValues(bottom = 30.dp)) {
        item {
            TopBack("", onBack)
            Box(Modifier.fillMaxWidth().height(220.dp).background(Color(0xFFF0ECE3), RoundedCornerShape(24.dp)), contentAlignment = Alignment.Center) {
                Text(product.emoji, fontSize = 100.sp)
            }
            Spacer(Modifier.height(18.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(product.name, fontSize = 28.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                StockBadge(product.status)
            }
            Text(product.category, color = Muted)
            Text("$ ${formatMoney(product.price)}", fontSize = 25.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(18.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                InfoTile("Stock actual", product.stock.toString(), Success, Modifier.weight(1f))
                InfoTile("Stock mínimo", product.minimumStock.toString(), Danger, Modifier.weight(1f))
            }
            Spacer(Modifier.height(22.dp))
            Text("Descripción", fontWeight = FontWeight.SemiBold)
            Text(product.description.ifBlank { "Sin descripción." }, color = Muted)
            Spacer(Modifier.height(24.dp))
            ActionRow(Icons.Default.SwapVert, "Registrar movimiento", onMovement)
            ActionRow(Icons.Default.Edit, "Editar producto", onEdit)
            ActionRow(Icons.Default.History, "Ver historial", onHistory)
            ActionRow(Icons.Default.Delete, "Eliminar producto", onDelete, Danger)
        }
    }
}

@Composable
private fun InfoTile(label: String, value: String, tint: Color, modifier: Modifier) {
    Card(modifier, colors = CardDefaults.cardColors(containerColor = tint.copy(alpha=.08f)), shape = RoundedCornerShape(18.dp)) {
        Column(Modifier.padding(16.dp)) { Text(label, color = Muted, fontSize = 12.sp); Text(value, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = tint) }
    }
}

@Composable
private fun ActionRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit, tint: Color = Color.Unspecified) {
    Row(Modifier.fillMaxWidth().clickable(onClick = onClick).padding(vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = if (tint == Color.Unspecified) LocalContentColor.current else tint)
        Spacer(Modifier.width(14.dp)); Text(label, Modifier.weight(1f), color = if (tint == Color.Unspecified) LocalContentColor.current else tint)
        Icon(Icons.Default.ChevronRight, null, tint = Muted)
    }
    HorizontalDivider(color = Color(0xFFEAE5DB))
}

@Composable
fun ProductFormScreen(existing: Product?, categories: List<String>, onBack: () -> Unit, onSave: (Product) -> Unit) {
    var name by remember(existing) { mutableStateOf(existing?.name ?: "") }
    var category by remember(existing) { mutableStateOf(existing?.category ?: categories.firstOrNull() ?: "Otros") }
    var price by remember(existing) { mutableStateOf(existing?.price?.toString() ?: "") }
    var stock by remember(existing) { mutableStateOf(existing?.stock?.toString() ?: "0") }
    var minStock by remember(existing) { mutableStateOf(existing?.minimumStock?.toString() ?: "0") }
    var description by remember(existing) { mutableStateOf(existing?.description ?: "") }
    var categoryMenu by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    LazyColumn(Modifier.fillMaxSize().padding(horizontal = 18.dp), contentPadding = PaddingValues(bottom = 30.dp)) {
        item {
            TopBack(if (existing == null) "Nuevo producto" else "Editar producto", onBack)
            Box(Modifier.fillMaxWidth().height(130.dp).background(Color(0xFFF0ECE3), RoundedCornerShape(20.dp)), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) { Text(existing?.emoji ?: "📷", fontSize = 45.sp); Text("Foto del producto", color = Muted) }
            }
            Spacer(Modifier.height(16.dp))
            FormField("Nombre del producto", name, { name = it }, "Ej. Remera básica")
            Text("Categoría", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            Box {
                OutlinedTextField(category, {}, readOnly = true, modifier = Modifier.fillMaxWidth().clickable { categoryMenu = true }, trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) }, shape = RoundedCornerShape(14.dp))
                DropdownMenu(expanded = categoryMenu, onDismissRequest = { categoryMenu = false }) {
                    categories.forEach { c -> DropdownMenuItem(text = { Text(c) }, onClick = { category = c; categoryMenu = false }) }
                }
            }
            Spacer(Modifier.height(12.dp))
            FormField("Precio de venta", price, { price = it.filter(Char::isDigit) }, "0", KeyboardType.Number)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(Modifier.weight(1f)) { FormField(if (existing == null) "Stock inicial" else "Stock actual", stock, { if (existing == null) stock = it.filter(Char::isDigit) }, "0", KeyboardType.Number, enabled = existing == null) }
                Box(Modifier.weight(1f)) { FormField("Stock mínimo", minStock, { minStock = it.filter(Char::isDigit) }, "0", KeyboardType.Number) }
            }
            FormField("Descripción (opcional)", description, { description = it }, "Descripción del producto...", singleLine = false)
            error?.let { Text(it, color = Danger, fontSize = 13.sp) }
            Spacer(Modifier.height(16.dp))
            Button(onClick = {
                val p = price.toIntOrNull(); val s = stock.toIntOrNull(); val m = minStock.toIntOrNull()
                if (name.isBlank() || p == null || p < 0 || s == null || s < 0 || m == null || m < 0) {
                    error = "Revisá los campos obligatorios y los valores numéricos."
                } else {
                    onSave(Product(existing?.id ?: "p${kotlin.random.Random.nextLong()}", name.trim(), category, p, existing?.stock ?: s, m, description.trim(), existing?.emoji ?: "📦"))
                }
            }, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(16.dp)) {
                Text(if (existing == null) "Guardar producto" else "Guardar cambios")
            }
        }
    }
}

@Composable
private fun FormField(label: String, value: String, onValue: (String)->Unit, placeholder: String, keyboardType: KeyboardType = KeyboardType.Text, enabled: Boolean = true, singleLine: Boolean = true) {
    Text(label, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
    OutlinedTextField(value, onValue, enabled = enabled, modifier = Modifier.fillMaxWidth(), placeholder = { Text(placeholder) }, singleLine = singleLine, keyboardOptions = KeyboardOptions(keyboardType = keyboardType), shape = RoundedCornerShape(14.dp))
    Spacer(Modifier.height(12.dp))
}

@Composable
fun MovementFormScreen(product: Product, onBack: () -> Unit, onSave: (MovementType, Int, String, String) -> String?) {
    var type by remember { mutableStateOf(MovementType.OUT) }
    var quantity by remember { mutableStateOf("1") }
    var reason by remember { mutableStateOf("Venta") }
    var note by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    val reasons = if (type == MovementType.IN) listOf("Reposición", "Devolución", "Ajuste", "Otro") else listOf("Venta", "Daño", "Pérdida", "Ajuste", "Otro")
    var reasonMenu by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize().padding(horizontal = 18.dp)) {
        TopBack("Registrar movimiento", onBack)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = type == MovementType.IN, onClick = { type = MovementType.IN; reason = "Reposición" }, label = { Text("Entrada") }, modifier = Modifier.weight(1f))
            FilterChip(selected = type == MovementType.OUT, onClick = { type = MovementType.OUT; reason = "Venta" }, label = { Text("Salida") }, modifier = Modifier.weight(1f))
        }
        Spacer(Modifier.height(14.dp))
        Card(colors = CardDefaults.cardColors(containerColor = StocklyCard), shape = RoundedCornerShape(18.dp)) {
            Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) { Text(product.emoji, fontSize = 34.sp); Spacer(Modifier.width(12.dp)); Column { Text(product.name, fontWeight = FontWeight.SemiBold); Text("Stock actual: ${product.stock}", color = Muted) } }
        }
        Spacer(Modifier.height(18.dp))
        FormField("Cantidad", quantity, { quantity = it.filter(Char::isDigit) }, "1", KeyboardType.Number)
        Text("Motivo", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
        Box {
            OutlinedTextField(reason, {}, readOnly = true, modifier = Modifier.fillMaxWidth(), trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) }, shape = RoundedCornerShape(14.dp))
            Box(Modifier.matchParentSize().clickable { reasonMenu = true })
            DropdownMenu(expanded = reasonMenu, onDismissRequest = { reasonMenu = false }) { reasons.forEach { r -> DropdownMenuItem({ Text(r) }, onClick = { reason = r; reasonMenu = false }) } }
        }
        Spacer(Modifier.height(12.dp))
        FormField("Nota (opcional)", note, { note = it }, "Ej. Venta por Instagram", singleLine = false)
        error?.let { Text(it, color = Danger, fontSize = 13.sp) }
        Spacer(Modifier.weight(1f))
        Button(onClick = {
            val q = quantity.toIntOrNull() ?: 0
            error = onSave(type, q, reason, note)
        }, modifier = Modifier.fillMaxWidth().height(54.dp), shape = RoundedCornerShape(16.dp)) { Text("Guardar movimiento") }
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
fun MovementsScreen(state: InventoryState, productId: String? = null) {
    var filter by remember { mutableStateOf("Todos") }
    val list = state.movements.filter { (productId == null || it.productId == productId) && when(filter) { "Entradas" -> it.type == MovementType.IN; "Salidas" -> it.type == MovementType.OUT; else -> true } }
    Column(Modifier.fillMaxSize().padding(horizontal = 18.dp)) {
        Text(if (productId == null) "Movimientos" else "Historial", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { listOf("Todos", "Entradas", "Salidas").forEach { label -> FilterChip(selected = filter == label, onClick = { filter = label }, label = { Text(label) }) } }
        Spacer(Modifier.height(6.dp))
        if (list.isEmpty()) EmptyCard("Sin movimientos", "Los movimientos aparecerán acá.") else LazyColumn(contentPadding = PaddingValues(bottom = 100.dp)) { items(list) { MovementRow(it); HorizontalDivider(color = Color(0xFFEAE5DB)) } }
    }
}

@Composable
fun MovementRow(movement: StockMovement) {
    Row(Modifier.fillMaxWidth().padding(vertical = 11.dp), verticalAlignment = Alignment.CenterVertically) {
        val color = if (movement.type == MovementType.IN) Success else Danger
        Box(Modifier.size(40.dp).background(color.copy(alpha=.10f), RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) { Icon(if (movement.type == MovementType.IN) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward, null, tint = color) }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) { Text(movement.productName, fontWeight = FontWeight.SemiBold); Text("${if (movement.type == MovementType.IN) "+" else "-"}${movement.quantity} unidades · ${movement.reason}", color = color, fontSize = 12.sp) }
        Text(movement.dateLabel, color = Muted, fontSize = 12.sp)
    }
}

@Composable
fun CategoriesScreen(state: InventoryState, onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(horizontal = 18.dp)) {
        TopBack("Categorías", onBack)
        Spacer(Modifier.height(6.dp))
        state.categories.forEach { category ->
            val count = state.products.count { it.category == category }
            Row(Modifier.fillMaxWidth().padding(vertical = 13.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(44.dp).background(Forest.copy(alpha=.09f), RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) { Icon(Icons.Default.Category, null, tint = Forest) }
                Spacer(Modifier.width(12.dp)); Column(Modifier.weight(1f)) { Text(category, fontWeight = FontWeight.SemiBold); Text("$count productos", color = Muted, fontSize = 12.sp) }
                Icon(Icons.Default.ChevronRight, null, tint = Muted)
            }
            HorizontalDivider(color = Color(0xFFEAE5DB))
        }
    }
}

@Composable
fun StatsScreen(state: InventoryState, onBack: () -> Unit) {
    val outUnits = state.movements.filter { it.type == MovementType.OUT }.sumOf { it.quantity }
    val inventoryValue = state.products.sumOf { it.price * it.stock }
    LazyColumn(Modifier.fillMaxSize().padding(horizontal = 18.dp), contentPadding = PaddingValues(bottom = 100.dp)) {
        item {
            TopBack("Estadísticas", onBack)
            Spacer(Modifier.height(6.dp))
            StatCard("Unidades vendidas", outUnits.toString(), Icons.Default.ShoppingCart, Warning, Modifier.fillMaxWidth())
            Spacer(Modifier.height(10.dp))
            StatCard("Valor del inventario", "$ ${formatMoney(inventoryValue)}", Icons.Default.AccountBalanceWallet, Success, Modifier.fillMaxWidth())
            Spacer(Modifier.height(22.dp))
            Text("Stock por producto", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            state.products.take(6).forEach { p ->
                Text(p.name, fontWeight = FontWeight.Medium)
                LinearProgressIndicator(progress = { (p.stock.coerceAtMost(20) / 20f) }, modifier = Modifier.fillMaxWidth().height(8.dp), color = if (p.status == StockStatus.AVAILABLE) Forest else Warning, trackColor = Color(0xFFE7E3D9))
                Text("${p.stock} unidades", color = Muted, fontSize = 12.sp)
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun MoreScreen(onCategories: () -> Unit, onStats: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(horizontal = 18.dp)) {
        Text("Más", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(18.dp))
        ActionRow(Icons.Default.Category, "Categorías", onCategories)
        ActionRow(Icons.Default.BarChart, "Estadísticas", onStats)
        Spacer(Modifier.height(26.dp))
        Text("Stockly 1.0", color = Muted, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
    }
}

@Composable
fun EmptyCard(title: String, body: String) {
    Card(colors = CardDefaults.cardColors(containerColor = StocklyCard), shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) { Text(title, fontWeight = FontWeight.SemiBold); Text(body, color = Muted, textAlign = TextAlign.Center) }
    }
}

@Composable
fun TopBack(title: String, onBack: () -> Unit) {
    Row(Modifier.fillMaxWidth().height(58.dp), verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) }
        if (title.isNotBlank()) Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}
