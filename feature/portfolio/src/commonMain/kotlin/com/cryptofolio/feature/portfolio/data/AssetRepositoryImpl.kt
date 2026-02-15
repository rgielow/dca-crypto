package com.cryptofolio.feature.portfolio.data

import com.cryptofolio.core.database.dao.TransactionDao
import com.cryptofolio.domain.model.Asset
import com.cryptofolio.domain.model.Currency
import com.cryptofolio.domain.repository.AssetRepository
import com.cryptofolio.feature.portfolio.data.mapper.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AssetRepositoryImpl(
    private val transactionDao: TransactionDao,
) : AssetRepository {

    override fun getAllAssets(currency: Currency): Flow<List<Asset>> =
        transactionDao.getAggregatedAssets().map { assets ->
            assets.map { it.toDomain() }
        }

    override fun getAssetByCoinId(coinId: String, currency: Currency): Flow<Asset?> =
        getAllAssets(currency).map { assets ->
            assets.find { it.coinId == coinId }
        }
}
