package edu.bupt.smart_refresh

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.MutatorMutex
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect

class SmartSwipeRefreshState {
    private val mutatorMutex = MutatorMutex()
    private val indicatorOffsetAnimatable = Animatable(0.dp, Dp.VectorConverter)
    val indicatorOffset get() = indicatorOffsetAnimatable.value
    private val _indicatorOffsetFlow  = MutableStateFlow(0f)
    val indicatorOffsetFlow: Flow<Float> get() = _indicatorOffsetFlow
    val isSwipeInProgress by derivedStateOf { indicatorOffset != 0.dp }

    var isRefreshing: Boolean by mutableStateOf(false)

    fun updateOffsetDelta(value: Float) {
        _indicatorOffsetFlow.value = value
    }

    suspend fun snapToOffset(value: Dp) {
        mutatorMutex.mutate(MutatePriority.UserInput) {
            indicatorOffsetAnimatable.snapTo(value)
        }
    }

    suspend fun animateToOffset(value: Dp) {
        mutatorMutex.mutate {
            indicatorOffsetAnimatable.animateTo(value, tween(1000))
        }
    }
}

class SmartSwipeRefreshNestedScrollConnection(
    val state: SmartSwipeRefreshState
): NestedScrollConnection {
    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        Log.d("gzz", "onPreScroll")
        if (source == NestedScrollSource.Drag && available.y < 0) {
            state.updateOffsetDelta(available.y)
            return if (state.isSwipeInProgress) Offset(x = 0f, y = available.y) else Offset.Zero
        } else {
            return Offset.Zero
        }
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        Log.d("gzz", "onPostScroll")
        if (source == NestedScrollSource.Drag && available.y > 0) {
            state.updateOffsetDelta(available.y)
            return Offset(x = 0f, y = available.y)
        } else {
            return Offset.Zero
        }
    }

    override suspend fun onPreFling(available: Velocity): Velocity {
        if (state.indicatorOffset > 30.dp) {
            state.animateToOffset(60.dp)
            state.isRefreshing = true
        } else {
            state.animateToOffset(0.dp)
        }
        return super.onPreFling(available)
    }

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        Log.d("gzz", "onPostFling")
        return super.onPostFling(consumed, available)
    }
}

@Composable
fun SmartSwipeRefresh(
    onRefresh: suspend () -> Unit,
    state:SmartSwipeRefreshState = remember {
        SmartSwipeRefreshState()
    },
    content: @Composable () -> Unit
) {
    val smartSwipeRefreshNestedScrollConnection = remember(state) {
        SmartSwipeRefreshNestedScrollConnection(state)
    }

    Column(
        Modifier
            .nestedScroll(smartSwipeRefreshNestedScrollConnection)
            .offset(y = (-40).dp + state.indicatorOffset),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        content()
    }

    var density = LocalDensity.current
    LaunchedEffect(Unit) {
        state.indicatorOffsetFlow.collect {
            var currentOffset = with(density) { state.indicatorOffset + it.toDp() }
            state.snapToOffset(currentOffset.coerceAtLeast(0.dp).coerceAtMost(60.dp))
        }
    }

    LaunchedEffect(state.isRefreshing) {
        if (state.isRefreshing) {
            onRefresh()
            state.animateToOffset(0.dp)
            state.isRefreshing = false
        }
    }
}
