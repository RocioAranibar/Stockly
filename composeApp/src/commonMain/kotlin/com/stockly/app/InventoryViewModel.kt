package com.stockly.app

import com.stockly.app.domain.InventoryState
import com.stockly.app.domain.MovementType
import com.stockly.app.domain.Product
import com.stockly.app.repository.DemoInventoryRepository
import com.stockly.app.repository.InventoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class InventoryViewModel(
    private val repository: InventoryRepository = DemoInventoryRepository()
) {
    private val _state = MutableStateFlow(repository.initialState())
    val state: StateFlow<InventoryState> = _state.asStateFlow()

    fun saveProduct(product: Product) {
        _state.value = repository.saveProduct(_state.value, product)
    }

    fun deleteProduct(productId: String) {
        _state.value = repository.deleteProduct(_state.value, productId)
    }

    fun registerMovement(
        productId: String,
        type: MovementType,
        quantity: Int,
        reason: String,
        note: String
    ): String? {
        val result = repository.registerMovement(_state.value, productId, type, quantity, reason, note)
        return result.fold(
            onSuccess = { _state.value = it; null },
            onFailure = { it.message ?: "No se pudo registrar el movimiento." }
        )
    }
}
