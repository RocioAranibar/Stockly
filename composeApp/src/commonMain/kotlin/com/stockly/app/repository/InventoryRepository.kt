package com.stockly.app.repository

import com.stockly.app.domain.InventoryState
import com.stockly.app.domain.MovementType
import com.stockly.app.domain.Product
import com.stockly.app.domain.StockMovement

interface InventoryRepository {
    fun initialState(): InventoryState
    fun saveProduct(state: InventoryState, product: Product): InventoryState
    fun deleteProduct(state: InventoryState, productId: String): InventoryState
    fun registerMovement(
        state: InventoryState,
        productId: String,
        type: MovementType,
        quantity: Int,
        reason: String,
        note: String
    ): Result<InventoryState>
}

class DemoInventoryRepository : InventoryRepository {
    override fun initialState(): InventoryState = InventoryState(
        products = listOf(
            Product("p1", "Remera negra", "Ropa", 18500, 12, 5, "Remera de algodón, color negro. Talle único.", "👕"),
            Product("p2", "Remera blanca", "Ropa", 18500, 3, 5, "Remera básica blanca.", "👚"),
            Product("p3", "Perfume X", "Perfumes", 42000, 2, 5, "Fragancia 100 ml.", "🧴"),
            Product("p4", "Crema facial", "Cosmética", 12000, 0, 4, "Crema hidratante 50 ml.", "🧴"),
            Product("p5", "Gorra negra", "Accesorios", 11000, 15, 4, "Gorra clásica regulable.", "🧢"),
            Product("p6", "Labial rojo", "Cosmética", 9500, 1, 3, "Tono rojo intenso.", "💄")
        ),
        movements = listOf(
            StockMovement("m1", "p1", "Remera negra", MovementType.OUT, 2, "Venta", "Venta por Instagram", "Hoy"),
            StockMovement("m2", "p3", "Perfume X", MovementType.IN, 5, "Reposición", "", "Ayer"),
            StockMovement("m3", "p6", "Labial rojo", MovementType.OUT, 1, "Daño", "", "Ayer")
        ),
        categories = listOf("Ropa", "Cosmética", "Perfumes", "Accesorios", "Otros")
    )

    override fun saveProduct(state: InventoryState, product: Product): InventoryState {
        val exists = state.products.any { it.id == product.id }
        val products = if (exists) state.products.map { if (it.id == product.id) product else it } else state.products + product
        val categories = (state.categories + product.category).distinct()
        return state.copy(products = products, categories = categories)
    }

    override fun deleteProduct(state: InventoryState, productId: String): InventoryState =
        state.copy(products = state.products.filterNot { it.id == productId })

    override fun registerMovement(
        state: InventoryState,
        productId: String,
        type: MovementType,
        quantity: Int,
        reason: String,
        note: String
    ): Result<InventoryState> {
        if (quantity <= 0) return Result.failure(IllegalArgumentException("La cantidad debe ser mayor que cero."))
        val product = state.products.firstOrNull { it.id == productId }
            ?: return Result.failure(IllegalArgumentException("Producto no encontrado."))
        if (type == MovementType.OUT && quantity > product.stock) {
            return Result.failure(IllegalArgumentException("No hay stock suficiente. Disponible: ${product.stock}."))
        }
        val newStock = if (type == MovementType.IN) product.stock + quantity else product.stock - quantity
        val updatedProduct = product.copy(stock = newStock)
        val movement = StockMovement(
            id = "m${state.movements.size + 1}",
            productId = product.id,
            productName = product.name,
            type = type,
            quantity = quantity,
            reason = reason,
            note = note,
            dateLabel = "Hoy"
        )
        return Result.success(
            state.copy(
                products = state.products.map { if (it.id == product.id) updatedProduct else it },
                movements = listOf(movement) + state.movements
            )
        )
    }
}
