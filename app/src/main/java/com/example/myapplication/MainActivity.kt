package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.*
import androidx.paging.PagingSource.LoadResult.Page.Companion.COUNT_UNDEFINED
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class TestData(val id: Int)

enum class Issue {
    // A: PagingSource refresh when only placeholders are visible
    // -> wrong scroll position when data is loaded (scroll position follows the placeholder at the top of the screen)
    A,

    // B: Pagingsource is invalidated before initial load
    // -> refresh key is set to null
    B,

    // C: jumpingSupported
    // -> does not work properly (anchor position goes negative) and often crashes (IndexOutOfBoundsException)
    C;

    companion object {
        val CurrentDemo: Issue = B
    }
}

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    private var pagingSource: PagingSource<Int, TestData>? = null
    private val flow = Pager(
        config = PagingConfig(
            20,
            10,
            jumpThreshold = if (Issue.CurrentDemo == Issue.C) 10 else COUNT_UNDEFINED
        ),
        pagingSourceFactory = { TestPagingSource().also { pagingSource = it } },
    ).flow.cachedIn(lifecycleScope)

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.textView).text = Issue.CurrentDemo.name

        recyclerView = findViewById(R.id.recycler)
        val adapter = TestAdapter()
        recyclerView.adapter = adapter

        var once = false

        lifecycleScope.launch {
            flow.collectLatest {
                adapter.submitData(it)
            }
        }

        if (Issue.CurrentDemo == Issue.A) {
            lifecycleScope.launch {
                adapter.loadStateFlow.collect {
                    if (it.refresh is LoadState.Loading) return@collect
                    once = !once
                    Log.i(TAG, it.toString())

                    if (once) {
                        delay(500)
                        Log.i(TAG, "invalidate!!")
                        pagingSource?.invalidate()
                    }
                }
            }
        }

        if (Issue.CurrentDemo == Issue.B) {
            lifecycleScope.launch {
                while (true) {
                    delay(3500)
                    Log.i(TAG, "invalidate!")
                    pagingSource?.invalidate()
                    delay(200)
                    Log.i(TAG, "invalidate again!!")
                    pagingSource?.invalidate()
                }
            }
        }

    }
}