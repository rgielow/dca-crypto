package com.cryptofolio.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow

@Composable
fun <T> CollectAsEffect(
    flow: Flow<T>,
    onEvent: suspend (T) -> Unit,
) {
    LaunchedEffect(flow) {
        flow.collect { event ->
            onEvent(event)
        }
    }
}
