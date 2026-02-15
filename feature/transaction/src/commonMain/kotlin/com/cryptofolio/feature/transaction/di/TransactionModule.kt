package com.cryptofolio.feature.transaction.di

import com.cryptofolio.domain.repository.TransactionRepository
import com.cryptofolio.feature.transaction.add.AddTransactionViewModel
import com.cryptofolio.feature.transaction.data.TransactionRepositoryImpl
import com.cryptofolio.feature.transaction.detail.TransactionDetailViewModel
import com.cryptofolio.feature.transaction.list.TransactionListViewModel
import com.cryptofolio.feature.transaction.usecase.AddTransactionUseCase
import com.cryptofolio.feature.transaction.usecase.DeleteTransactionUseCase
import com.cryptofolio.feature.transaction.usecase.GetTransactionByIdUseCase
import com.cryptofolio.feature.transaction.usecase.GetTransactionsUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val transactionModule = module {
    singleOf(::TransactionRepositoryImpl) bind TransactionRepository::class
    factoryOf(::AddTransactionUseCase)
    factoryOf(::GetTransactionsUseCase)
    factoryOf(::DeleteTransactionUseCase)
    factoryOf(::GetTransactionByIdUseCase)
    viewModelOf(::TransactionListViewModel)
    viewModelOf(::AddTransactionViewModel)
    viewModelOf(::TransactionDetailViewModel)
}
