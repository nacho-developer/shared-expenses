package com.nacho.balance.domain

import java.math.BigDecimal

interface BalanceService {
	fun getGroupBalance(): Map<String, BigDecimal>
	fun getGroupBalanceAccurate(): List<Debt>
}
