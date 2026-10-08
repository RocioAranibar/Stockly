package com.stockly.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stockly.app.domain.Product
import com.stockly.app.domain.StockStatus
import com.stockly.app.theme.*

@Composable
fun StatCard(title: String, value: String, icon: ImageVector, tint: Color, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Card(onClick = onClick, modifier = modifier, colors = CardDefaults.cardColors(containerColor = StocklyCard), shape = RoundedCornerShape(18.dp)) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(40.dp).background(tint.copy(alpha = .12f), RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = tint)
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(value, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(title, fontSize = 12.sp, color = Muted)
            }
        }
    }
}

@Composable
fun ProductRow(product: Product, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().clickable(onClick = onClick).padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(52.dp).background(Color(0xFFF0ECE3), RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
            ProductPhoto(product.photoUri, product.emoji, Modifier.size(52.dp), 26)
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(product.name, fontWeight = FontWeight.SemiBold)
            Text(product.category, fontSize = 12.sp, color = Muted)
            Text("$ ${formatMoney(product.price)}", fontWeight = FontWeight.Medium)
        }
        StockBadge(product.status)
        Spacer(Modifier.width(8.dp))
        Icon(Icons.Default.ChevronRight, null, tint = Muted)
    }
}

@Composable
fun StockBadge(status: StockStatus) {
    val (text, color) = when (status) {
        StockStatus.AVAILABLE -> "Disponible" to Success
        StockStatus.LOW -> "Stock bajo" to Warning
        StockStatus.OUT -> "Sin stock" to Danger
    }
    Surface(color = color.copy(alpha = .12f), shape = RoundedCornerShape(50)) {
        Text(text, Modifier.padding(horizontal = 10.dp, vertical = 5.dp), color = color, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
    }
}

fun formatMoney(value: Int): String = value.toString().reversed().chunked(3).joinToString(".").reversed()
