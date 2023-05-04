package com.nacho.balance.application

import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue

import com.nacho.balance.domain.BalanceService
import com.nacho.balance.domain.Debt

import java.math.BigDecimal

@ExecuteOn(TaskExecutors.IO)
@Controller("/balance")
class BalanceController(private val balanceService: BalanceService) {

    @Get
    fun getPersonBalance(): Map<String, BigDecimal> {
        return balanceService.getGroupBalance()
    }
    
    @Get("/accurate")
    fun getAccurateBalance(): List<Debt> {
        return balanceService.getGroupBalanceAccurate()
    }
}
