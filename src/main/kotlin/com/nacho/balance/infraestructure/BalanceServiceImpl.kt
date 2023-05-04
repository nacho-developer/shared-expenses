package com.nacho.balance.infraestructure

import jakarta.inject.Singleton
import javax.transaction.Transactional
import io.micronaut.transaction.annotation.ReadOnly

import java.math.BigDecimal
import java.math.RoundingMode

import com.nacho.person.infraestructure.PersonRepository
import com.nacho.expense.infraestructure.ExpenseRepository
import com.nacho.balance.domain.BalanceService
import com.nacho.person.domain.Person
import com.nacho.balance.domain.Debt

@Singleton
@Transactional
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
		val balanceMap = mutableMapOf<String, BigDecimal>()
		expenses.groupBy { it.payer.id }
			.mapValues { (_, expenses) -> expenses.map { it.amount }.fold(BigDecimal.ZERO) { acc, curr -> acc + curr } }
			.forEach { (personId, totalSpent) ->
				val personName = personRepository.findById(personId).get().name
				balanceMap[personName] = totalSpent
			}

		val debts = mutableListOf<Debt>()
		balanceMap.forEach { (from, fromBalance) ->
			balanceMap.filter { it.key != from }
				.forEach { (to, toBalance) ->
					val debtAmount = fromBalance.minus(toBalance).abs().setScale(2, RoundingMode.HALF_EVEN)
					if (debtAmount > BigDecimal.ZERO) {
						debts.add(Debt(from, to, debtAmount))
					}
				}
		}
		debts.sortByDescending { it.amount }

		val transactions = mutableListOf<Debt>()
		while (debts.isNotEmpty()) {
			val debt = debts.first()
			val from = balanceMap[debt.from]!!
			val to = balanceMap[debt.to]!!
			if (from >= debt.amount && to >= debt.amount) {
				balanceMap[debt.from] = from.minus(debt.amount)
				balanceMap[debt.to] = to.plus(debt.amount)
				transactions.add(debt)
				debts.remove(debt)
			} else {
				debts.remove(debt)
				debts.add(Debt(debt.from, debt.to, debt.amount.minus(from)))
				debts.add(Debt(debt.to, debt.from, debt.amount.minus(to)))
			}
		}
		return transactions
	}

}
