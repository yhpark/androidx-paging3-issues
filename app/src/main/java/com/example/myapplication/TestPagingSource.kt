package com.example.myapplication

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.delay

class TestPagingSource : PagingSource<Int, TestData>() {
    companion object {
        const val COUNT = 1000
        const val TAG = "TestPagingSource"
    }

    override val jumpingSupported: Boolean
        get() = Issue.CurrentDemo == Issue.C

    @ExperimentalPagingApi
    override fun getRefreshKey(state: PagingState<Int, TestData>): Int? =
        state.anchorPosition.also {
            Log.i(
                TAG,
                "getRefreshKey ${hashCode()}: $it"
            )
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TestData> {

        Log.i(
            TAG,
            "load ${hashCode()}: ${params.javaClass.simpleName} ${params.key} ${params.loadSize}"
        )

        val numbers = when (params) {
            is LoadParams.Prepend ->
                (params.key downTo (params.key - params.loadSize + 1)).toList()
            is LoadParams.Append ->
                (params.key until (params.key + params.loadSize)).toList()
            is LoadParams.Refresh -> {
                val key = params.key
                val halfSize = params.loadSize / 2
                if (key != null) {
                    ((key - halfSize) until (key + halfSize)).toList()
                } else
                    (0 until params.loadSize).toList()
            }
        }
        val data = numbers
            .filter { it in 0 until COUNT }
            .map { TestData(it) }

        val firstId = data.firstOrNull()?.id
        val lastId = data.lastOrNull()?.id

        delay(2000)

        return LoadResult.Page(
            data = data,
            prevKey = firstId?.minus(1)?.takeIf { it >= 0 },
            nextKey = lastId?.plus(1)?.takeIf { it < COUNT },
            itemsBefore = firstId ?: LoadResult.Page.COUNT_UNDEFINED,
            itemsAfter = lastId?.let { COUNT - it } ?: LoadResult.Page.COUNT_UNDEFINED
        ).also {
            Log.i(
                TAG,
                "loaded ${hashCode()}: ${data.firstOrNull()?.id} to ${data.lastOrNull()?.id}, prevKey: ${it.prevKey}, nextKey: ${it.nextKey}, itemsBefore: ${it.itemsBefore}, itemsAfter: ${it.itemsAfter}"
            )
        }
    }
}