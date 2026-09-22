package com.christhperalta.activosfijos.core.quotation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update



data class InvoiceTotals(
    val subtotal: Long,
    val tax: Long,
    val total: Long
)

/**
 * Holds product lines shared across quotation-related screens (in-memory singleton).
 */
class QuotationCart {

    private val _lines = MutableStateFlow<List<Product>>(emptyList())
    val lines: StateFlow<List<Product>> = _lines.asStateFlow()

    private val _invoiceTotals = MutableStateFlow<InvoiceTotals?>(null)
    val invoiceTotals: StateFlow<InvoiceTotals?> = _invoiceTotals

    fun addOrMerge(product: Product) {
        _lines.update { existing ->
            val found = existing.indexOfFirst { it.itemCode == product.itemCode }
            if (found >= 0) {
                existing.mapIndexed { index, item ->
                    if (index == found)
                        item.copy(quantity = (item.quantity ?: 1) + (product.quantity ?: 1))
                    else item
                }
            } else {
                existing + product.copy(quantity = product.quantity ?: 1)
            }
        }
        recalculateTotals()
    }

    fun updateQuantity(itemCode: String, quantity: Int) {
        if (quantity <= 0) { remove(itemCode); return }
        _lines.update { list ->
            list.map { product ->
                if (product.itemCode == itemCode)
                    product.copy(quantity = quantity)
                else product
            }
        }
        recalculateTotals()
    }

    fun remove(itemCode: String) {
        _lines.update { list -> list.filterNot { it.itemCode == itemCode } }
        recalculateTotals()
    }

    private fun recalculateTotals() {
        val subtotal = _lines.value.sumOf {
            (it.unitPrice ?: 0L) * (it.quantity ?: 0).toLong()
        }
        val tax = subtotal * 18 / 100
        _invoiceTotals.update { InvoiceTotals(subtotal, tax, subtotal + tax) }
    }

    fun reset() {
        _lines.update { emptyList() }
        _invoiceTotals.update { null }
    }
}