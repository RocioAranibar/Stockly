package com.stockly.app.domain

enum class StockStatus { AVAILABLE, LOW, OUT }
enum class MovementType { IN, OUT }

data class Product(
    val id: String,
    val name: String,
    val category: String,
    val price: Int,
    val stock: Int,
    val minimumStock: Int,
    val description: String = "",
    val emoji: String = "📦"
) {
    val status: StockStatus
        get() = when {
            stock == 0 -> StockStatus.OUT
            stock <= minimumStock -> StockStatus.LOW
            else -> StockStatus.AVAILABLE
        }
}

data class StockMovement(
    val id: String,
    val productId: String,
    val productName: String,
    val type: MovementType,
    val quantity: Int,
    val reason: String,
    val note: String = "",
    val dateLabel: String = "Hoy"
)

data class InventoryState(
    val products: List<Product> = emptyList(),
    val movements: List<StockMovement> = emptyList(),
    val categories: List<String> = emptyList()
) {
    val lowStockCount: Int get() = products.count { it.status == StockStatus.LOW }
    val outOfStockCount: Int get() = products.count { it.status == StockStatus.OUT }
    val todaySalesUnits: Int get() = movements.filter { it.type == MovementType.OUT && it.reason.equals("Venta", ignoreCase = true) && it.dateLabel == "Hoy" }.sumOf { it.quantity }
    val todayOutUnits: Int get() = movements.filter { it.type == MovementType.OUT && it.dateLabel == "Hoy" }.sumOf { it.quantity }
}
