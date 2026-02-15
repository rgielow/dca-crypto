package com.cryptofolio.domain.repository

import com.cryptofolio.domain.model.Asset
import com.cryptofolio.domain.model.Currency
import kotlinx.coroutines.flow.Flow

interface AssetRepository {
    fun getAllAssets(currency: Currency): Flow<List<Asset>>
    fun getAssetByCoinId(coinId: String, currency: Currency): Flow<Asset?>
}
