package com.stockly.app

import com.stockly.app.domain.MovementType
import com.stockly.app.domain.Product
import com.stockly.app.domain.StockStatus
import com.stockly.app.repository.DemoInventoryRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class InventoryRulesTest {
    @Test
    fun stockStatusIsCalculatedFromStockAndMinimum() {
        assertEquals(StockStatus.OUT, Product("1","A","Cat",100,0,5).status)
        assertEquals(StockStatus.LOW, Product("2","B","Cat",100,5,5).status)
        assertEquals(StockStatus.AVAILABLE, Product("3","C","Cat",100,6,5).status)
    }

    @Test
    fun outputCannotCreateNegativeStock() {
        val repo = DemoInventoryRepository()
        val state = repo.initialState()
        val result = repo.registerMovement(state, "p2", MovementType.OUT, 99, "Venta", "")
        assertTrue(result.isFailure)
    }
}
