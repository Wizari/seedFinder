package com.gmail.wizaripost.seedFinder.utils

import org.springframework.stereotype.Component
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import java.util.concurrent.atomic.AtomicLong

interface BalanceManager {
    fun increase(amount: Long): Long
    fun getCurrentBalance(): Long
    fun reset()
}

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
class BalanceCounter : BalanceManager {

    private val balance = AtomicLong(0)

    @Synchronized
    override fun increase(amount: Long): Long {
//        require(amount > 0) { "Amount must be positive" }
        val newBalance = balance.addAndGet(amount)
        // Логирование или дополнительная логика при необходимости
        return newBalance
    }

    override fun getCurrentBalance(): Long {
        return balance.get()
    }

    @Synchronized
    override fun reset() {
        balance.set(0)
    }
}