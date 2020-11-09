package com.example.imageloadmoreview

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialog
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_scroll_load.*
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException

class ScrollLoad : AppCompatActivity() {

    private var compositeDisposable = CompositeDisposable()
    private val myApiService by lazy { ProvideRetrofit.create(this) }
    private var loadingDialog: AppCompatDialog? = null

    private var isLoading: Boolean = false
    private var currentPage: Int = 1
    private lateinit var loadAdapter: LoadAdapter
    private lateinit var loadList: MutableList<LoadModel.Item>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll_load)
        setupToolbar()
        setupRecycler()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { _ -> onBackPressed() }
    }

    private fun setupRecycler() {
        currentPage = 1
        if ((resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE)
            recyclerView.layoutManager = androidx.recyclerview.widget.GridLayoutManager(
                this,
                3,
                RecyclerView.VERTICAL,
                false
            )
        else
            recyclerView.layoutManager = androidx.recyclerview.widget.GridLayoutManager(
                this,
                2,
                RecyclerView.VERTICAL,
                false
            )
        recyclerView.isNestedScrollingEnabled = false
        ViewCompat.setNestedScrollingEnabled(recyclerView, false)
        loadList = ArrayList()
        loadAdapter = LoadAdapter(loadList, object :
            MyRecyclerViewRowClickListener {
            override fun onRowClicked(position: Int) {
            }
        })

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            nsv.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
                if (v != null) {
                    if (scrollY == (v.getChildAt(0).measuredHeight - v.measuredHeight)) {
                        if (
                            loadAdapter.isMoreDataAvailable()
                            and (!isLoading)
                        ) {
                            ++currentPage
                            callSearch()
                        }
                    }
                }
            }
        }
        recyclerView.adapter = loadAdapter
        callSearch()
    }

    private fun callSearch() {
        if (currentPage >= 2) {//load more
            //add loading progress view
            loadList.add(LoadModel.Item(isLoadingView = true))
            loadAdapter.notifyItemInserted(loadList.size - 1)
        }

        val hHashMap = LinkedHashMap<String, String>()
        val queryMap = LinkedHashMap<String, String>()
            queryMap["title"] = ""
        queryMap["page"] = currentPage.toString()

        compositeDisposable.add(
                myApiService.callSearch(headerMap = hHashMap, queryMap = queryMap)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe {
                            //                    loadingDialog?.dismiss()
//                    loadingDialog = this?.let { it1 -> MyDialogUty.showLoadingDialog(context = it1) }
                            if (currentPage == 1)
                                pbProgressBar.visibility = View.VISIBLE
                            isLoading = true
                        }//show loading
                        .doAfterTerminate {
                            //                    loadingDialog?.dismiss()
                            if (currentPage == 1)
                                pbProgressBar.visibility = View.GONE
                            isLoading = false
                        }//hide loading
                        .subscribe({ responseData ->
                            if (currentPage == 1) {//first time
                                loadList.addAll(responseData.items as List<LoadModel.Item>)
                                loadAdapter.notifyDataChanged()
                                if (responseData.items.isEmpty()){

                                } else {//load more

                                    //remove loading view
                                    if (loadList.isNotEmpty())
                                        loadList.removeAt(loadList.size - 1)

                                    loadList.addAll(responseData.items as List<LoadModel.Item>)
                                    if (responseData.items?.isEmpty()!!) {
                                        loadAdapter.setMoreDataAvailable(false)
                                    }
                                    loadAdapter.notifyDataChanged()
                                }
                            }
                        }, { error ->
                            var errorTitle = ""
                            var errorMessage = error.message.toString()
                            when (error) {
                                is SocketTimeoutException -> {
                                    errorTitle = "Socket Time Out Exception"
                                }
                                is ConnectException -> {
                                    errorTitle = "Connect Exception"
                                }
                                is HttpException -> {
                                    errorTitle = "Http Exception ${error.response()?.code().toString()}"
                                }
                            }

                        })
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

}

