package com.nacho.expense.infraestructure

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import jakarta.inject.Inject
import com.nacho.expense.domain.ExpenseService
import com.nacho.expense.domain.ExpenseRequest
import java.time.LocalDateTime
import java.math.BigDecimal
import com.nacho.expense.domain.DeodorExpenseRequest

@MicronautTest
class ExpenseServiceImplTest {

    @Inject
    lateinit var expenseService: ExpenseService

    @Test
    fun `test getAllExpenses when there are no expenses`() {
        val expenses = expenseService.getAllExpenses()
        Assertions.assertEquals(0, expenses.size)
    }

    @Test
    fun `test createExpense when payerId is invalid`() {
        val expenseRequest = ExpenseRequest(
            description = "test expense",
            amount = BigDecimal.TEN,
            date = LocalDateTime.now(),
            payerId = 1000,
            deodorsIds = listOf(1, 2, 3)
        )
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            expenseService.createExpense(expenseRequest)
        }
    }

    @Test
    fun `test createExpense when deodorsIds contains invalid id`() {
        val expenseRequest = ExpenseRequest(
            description = "test expense",
            amount = BigDecimal.TEN,
            date = LocalDateTime.now(),
            payerId = 1,
            deodorsIds = listOf(2, 3, 1000)
        )
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            expenseService.createExpense(expenseRequest)
        }
    }

    @Test
    fun `test addDeodorExpense when expenseId is invalid`() {
        val deodorExpenseRequest = DeodorExpenseRequest(
            expenseId = 1000,
            deodorsIds = listOf(1, 2, 3)
        )
        Assertions.assertThrows(NoSuchElementException::class.java) {
            expenseService.addDeodorExpense(deodorExpenseRequest)
        }
    }

    @Test
    fun `test addDeodorExpense when deodorsIds contains invalid id`() {
        val deodorExpenseRequest = DeodorExpenseRequest(
            expenseId = 1,
            deodorsIds = listOf(2, 3, 1000)
        )
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            expenseService.addDeodorExpense(deodorExpenseRequest)
        }
    }
}
