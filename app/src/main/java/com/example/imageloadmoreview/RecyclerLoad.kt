package com.example.imageloadmoreview

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialog
import androidx.core.view.ViewCompat
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_recycler_load.*
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException

class RecyclerLoad : AppCompatActivity() {

    private var compositeDisposable = CompositeDisposable()
    private val myApiService by lazy { ProvideRetrofit.create(this) }
    private var loadingDialog: AppCompatDialog? = null

    private var isLoading: Boolean = false
    private var currentPage: Int = 1
    private lateinit var loadAdapter: LoadAdapter
    private lateinit var loadList: MutableList<LoadModel.Item>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_load)
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
        /*if ((resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE)
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
            )*/
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            this
        )
        recyclerView.isNestedScrollingEnabled = false
        ViewCompat.setNestedScrollingEnabled(recyclerView, false)
        loadList = ArrayList()
        loadAdapter = LoadAdapter(loadList, object :
            MyRecyclerViewRowClickListener {
            override fun onRowClicked(position: Int) {
            }
        })
        loadAdapter.setLoadMoreListener(object : MyAdapterOnLoadMoreListener {
            override fun onLoadMore() {
                recyclerView.post {
                    ++currentPage
                    callData()
                }
            }
        })
        recyclerView.adapter = loadAdapter
        callData()
    }

    private fun callData() {
        if (currentPage >= 2) {//load more
            //add loading progress view
            loadList.add(LoadModel.Item(isLoadingView = true))
            loadAdapter.notifyItemInserted(loadList.size - 1)
        }

        val hHashMap = LinkedHashMap<String, String>()
        hHashMap["x-rapidapi-host"] = "rakuten_webservice-rakuten-marketplace-item-ranking-v1.p.rapidapi.com"
        hHashMap["x-rapidapi-key"] = "7dbeb5e4f5msh97d67917aa9f713p105327jsnc28d1541a3e8"
        hHashMap["useQueryString"] = "true"

        val queryMap = LinkedHashMap<String, String>()
        queryMap["page"] = currentPage.toString()

        compositeDisposable.add(
            myApiService.callSearch(headerMap = hHashMap, queryMap = queryMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    if (currentPage == 1)
                        pbProgressBar.visibility = View.VISIBLE
                    isLoading = true
                }//show loading
                .doAfterTerminate {
                    //                    loadingDialog?.dismiss()
                    if (currentPage == 1)
                        pbProgressBar.visibility = View.GONE
                }//hide loading
                .subscribe({ responseData ->
                        if (currentPage == 1) {//first currentPage
                            loadList.addAll(responseData.items as List<LoadModel.Item>)
                            loadAdapter.notifyDataChanged()
                            if (responseData.items?.isEmpty()!!)
                                Toast.makeText(
                                    this, "no items",
                                    Toast.LENGTH_SHORT
                                ).show()
                        } else {//load more second currentPage
                            //remove loading view
                            if (loadList.isNotEmpty())
                                loadList.removeAt(loadList.size - 1)

                            loadList.addAll(responseData.items as List<LoadModel.Item>)
                            if (responseData.items?.isEmpty()!!) {
                                loadAdapter.setMoreDataAvailable(false)
                            }
                            loadAdapter.notifyDataChanged()
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
//                            if (error.response()?.code() == 422) {
//                                val responseBody = error.response()?.errorBody()
//                                val jsonObject = JSONObject(responseBody?.string())
//                                message = jsonObject.getString("error")
//                            }
                        }
                    }
                    this?.let {
                        Toast.makeText(
                            this, errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}
