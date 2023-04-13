package com.project.movieapp.features.movielist

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.project.movieapp.R
import com.project.movieapp.adapters.SimpleLoadMoreRecyclerAdapter
import com.project.movieapp.adapters.SimpleRecyclerAdapter
import com.project.movieapp.databinding.ActivityMovieListBinding
import com.project.movieapp.databinding.ItemGenreBinding
import com.project.movieapp.databinding.ItemMovieBinding
import com.project.movieapp.features.moviedetail.MovieDetailActivity
import com.project.movieapp.services.responses.GenresItem
import com.project.movieapp.services.responses.MovieDetailResponse
import com.project.movieapp.utils.ConnectionDetectorUtil
import com.project.movieapp.utils.DateUtils
import com.project.movieapp.utils.GlobalVariable
import com.project.movieapp.utils.UIUtils
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

class MovieListActivity : AppCompatActivity() {

    private lateinit var movieListBinding: ActivityMovieListBinding
    private val movieListViewModel: MovieListViewModel by viewModels()
    private var genreList : List<GenresItem> = ArrayList()
    private var genreAdapter: SimpleRecyclerAdapter<GenresItem>? = null
    private var selectedGenre: GenresItem = GenresItem()
    private var isAllSelected: Boolean = true
    private var movieList : MutableList<MovieDetailResponse> = ArrayList()
    private var movieAdapter: SimpleLoadMoreRecyclerAdapter<MovieDetailResponse>? = null
    private var isLoad: Boolean = false
    private var page: Int = 1
    private var totalPage: Int = 0
    private var connectionDetectorUtil = ConnectionDetectorUtil(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieListBinding = ActivityMovieListBinding.inflate(layoutInflater)
        setContentView(movieListBinding.root)

        initData()
        initView()
    }

    private fun initData() {
        if (connectionDetectorUtil.isConnectingToInternet){
            movieListViewModel.getGenreList()
            movieListViewModel.getMovieList("", 1)
        } else {
            UIUtils.newInstance(this).showDialogError(this, getString(R.string.error_title_common), getString(R.string.error_network_connection))
        }

        genreAdapter = SimpleRecyclerAdapter(genreList, R.layout.item_genre, object : SimpleRecyclerAdapter.OnViewHolder<GenresItem> {
            override fun onBindView(holder: SimpleRecyclerAdapter.SimpleViewHolder?, item: GenresItem) {
                val itemBinding : ItemGenreBinding = holder?.layoutBinding as ItemGenreBinding
                itemBinding.model = item
                itemBinding.mainLayout.setOnClickListener{
                    isAllSelected = false
                    updateSelectedGenre(item)
                    isLoad = false
                    page = 1
                    movieListBinding.refreshLayout.resetNoMoreData()
                    movieListBinding.refreshLayout.setEnableFooterFollowWhenNoMoreData(true)
                    if (connectionDetectorUtil.isConnectingToInternet) movieListViewModel.getMovieList(item.id.toString(), page) else UIUtils.newInstance(this@MovieListActivity).showDialogError(this@MovieListActivity, getString(R.string.error_title_common), getString(R.string.error_network_connection))
                }
            }
        })

        movieAdapter = SimpleLoadMoreRecyclerAdapter(movieList, R.layout.item_movie, object : SimpleLoadMoreRecyclerAdapter.OnViewHolder<MovieDetailResponse>{
            override fun onBindView(holder: SimpleLoadMoreRecyclerAdapter.SimpleViewHolder?, item: MovieDetailResponse) {
                val itemBinding : ItemMovieBinding = holder?.layoutBinding as ItemMovieBinding
                itemBinding.movieModel = item
                itemBinding.tvReleaseDate.text = DateUtils.newInstance().convertApiDateToDateTime(item.releaseDate)
                Glide.with(this@MovieListActivity).asBitmap().load(GlobalVariable.BASE_IMAGE_URL + item.backdropPath).into(itemBinding.ivMovies)

                itemBinding.mainLayout.setOnClickListener {
                    startActivity(Intent(this@MovieListActivity, MovieDetailActivity::class.java).putExtra("movie", item))
                }
            }
        })
    }

    private fun initView() {
        fetchGenreList()
        fetchMovieList()

        movieListBinding.isAllSelected = isAllSelected
        movieListBinding.genreAdapter = genreAdapter
        movieListBinding.movieAdapter = movieAdapter

        movieListBinding.btnAll.setOnClickListener {
            isAllSelected = true
            movieListBinding.isAllSelected = isAllSelected
            resetSelectedGenre()
            isLoad = false
            page = 1
            movieListBinding.refreshLayout.resetNoMoreData()
            movieListBinding.refreshLayout.setEnableFooterFollowWhenNoMoreData(true)
            if (connectionDetectorUtil.isConnectingToInternet) {
                if (genreList.isEmpty()) movieListViewModel.getGenreList()
                movieListViewModel.getMovieList("", page)
            } else {
                UIUtils.newInstance(this@MovieListActivity).showDialogError(this@MovieListActivity, getString(R.string.error_title_common), getString(R.string.error_network_connection))
            }
        }

        movieListBinding.refreshLayout.resetNoMoreData()
        movieListBinding.refreshLayout.setEnableFooterFollowWhenNoMoreData(true)
        movieListBinding.refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(@NonNull refreshLayout: RefreshLayout) {
                movieListBinding.refreshLayout.layout.postDelayed({
                    movieListBinding.refreshLayout.finishRefresh()
                    movieListBinding.refreshLayout.resetNoMoreData()
                }, 2000)
            }

            override fun onLoadMore(@NonNull refreshLayout: RefreshLayout) {
                if (page >= totalPage) {
                    movieListBinding.refreshLayout.finishLoadMoreWithNoMoreData()
                } else {
                    isLoad = true
                    page += 1
                    if (connectionDetectorUtil.isConnectingToInternet) movieListViewModel.getMovieList(selectedGenre.id.toString(), page) else UIUtils.newInstance(this@MovieListActivity).showDialogError(this@MovieListActivity, getString(R.string.error_title_common), getString(R.string.error_network_connection))
                }
            }
        })
    }

    private fun fetchGenreList(){
        movieListViewModel.genreResponse.observe(this){
            if (it.statusCode.isNullOrEmpty()){
                genreList = it.genres
                genreAdapter!!.setMainData(genreList)
            } else {
                UIUtils.newInstance(this).showDialogError(this, getString(R.string.error_title_common), getString(R.string.error_body_common))
            }
        }

        movieListViewModel.isLoadingListGenre.observe(this) {
            movieListBinding.isLoadingGenreList = it
        }
    }

    private fun fetchMovieList() {
        movieListViewModel.movieResponse.observe(this) {
            if (it.results.isNotEmpty()){
                totalPage = it.totalPages!!
                movieList = it.results as MutableList<MovieDetailResponse>
                if (isLoad){
                    movieAdapter!!.loadMore(movieList)
                    movieListBinding.refreshLayout.finishLoadMore()
                } else {
                    movieAdapter!!.setMainData(movieList)
                    movieListBinding.rvMovie.layoutManager!!.scrollToPosition(0)
                }
            } else {
                UIUtils.newInstance(this).showDialogError(this, getString(R.string.error_title_common), getString(R.string.error_body_common))
            }
        }

        movieListViewModel.isLoadingListMovie.observe(this) {
            if (!isLoad) movieListBinding.isLoadingMovieList = it
        }
    }

    private fun updateSelectedGenre(selectedItem : GenresItem){
        if (selectedGenre == selectedItem) return

        movieListBinding.isAllSelected = isAllSelected
        resetSelectedGenre()

        selectedGenre = selectedItem
        selectedGenre.isSelected = true
        val selectedGenreIdx = genreList.indexOf(selectedGenre)
        genreAdapter!!.notifyItemChanged(selectedGenreIdx, Unit)
    }

    private fun resetSelectedGenre(){
        selectedGenre.isSelected = false
        val prevSelectedIdx = genreList.indexOf(selectedGenre)
        genreAdapter!!.notifyItemChanged(prevSelectedIdx, Unit)
    }
}
