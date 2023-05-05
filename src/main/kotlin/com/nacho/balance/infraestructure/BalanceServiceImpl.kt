package com.nacho.balance.infraestructure

import jakarta.inject.Singleton
import io.micronaut.transaction.annotation.ReadOnly

import java.math.BigDecimal
import java.math.RoundingMode

import com.nacho.person.infraestructure.PersonRepository
import com.nacho.expense.infraestructure.ExpenseRepository
import com.nacho.balance.domain.BalanceService
import com.nacho.person.domain.Person
import com.nacho.balance.domain.Debt

@Singleton
open class BalanceServiceImpl(
	private val personRepository: PersonRepository,
	private val expenseRepository: ExpenseRepository
) : BalanceService {

	@ReadOnly
	override fun getGroupBalance(): Map<String, BigDecimal> {
		val expenses = expenseRepository.findAll()
		val balanceMap = mutableMapOf<String, BigDecimal>()
		// map from person ID to total spent
		val totalSpentMap = mutableMapOf<Long, BigDecimal>()

		// calculate total spent by each person (as payer and debtor)
		expenses.forEach {
			totalSpentMap[it.payer.id!!] = totalSpentMap.getOrDefault(it.payer.id, BigDecimal.ZERO) + it.amount
			it.deodors.forEach { deodor ->
				totalSpentMap[deodor.id!!] =
					totalSpentMap.getOrDefault(deodor.id, BigDecimal.ZERO) - it.amount / it.deodors.size.toBigDecimal()
			}
		}

		val totalPeople = personRepository.count().toBigDecimal()
		val avgSpent = totalSpentMap.values.fold(BigDecimal.ZERO) { acc, curr -> acc + curr }
			.divide(totalPeople, RoundingMode.HALF_UP)

		totalSpentMap.forEach { (personId, totalSpent) ->
			val balance = totalSpent.subtract(avgSpent)
			val personName = personRepository.findById(personId).get().name
			balanceMap[personName] = balance
		}
		return balanceMap
	}

	@ReadOnly
	override fun getGroupBalanceAccurate(): List<Debt> {
		val expenses = expenseRepository.findAll()
		val debts = mutableMapOf<Pair<String, String>, BigDecimal>()
		expenses.forEach { expense ->
			val payer = expense.payer
			val totalAmount = expense.amount
			val numDeodors = expense.deodors.size
			val amountPerPerson = totalAmount.divide(numDeodors.toBigDecimal(), 2, RoundingMode.HALF_UP) // fixed line
			expense.deodors.forEach { deodor ->
				val debtKey = Pair(payer.name, deodor.name)
				val previousAmount = debts.getOrDefault(debtKey, BigDecimal.ZERO)
				debts[debtKey] = previousAmount.add(amountPerPerson)
			}
		}
		return debts.map { (debtKey, amount) ->
			if (amount >= BigDecimal.ZERO) {
				Debt(debtKey.first, debtKey.second, amount.setScale(2, RoundingMode.HALF_UP)) // fixed line
			} else {
				Debt(debtKey.second, debtKey.first, amount.negate().setScale(2, RoundingMode.HALF_UP)) // fixed line
			}
		}
	}
}
