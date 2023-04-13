package com.project.movieapp.features.moviedetail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.project.movieapp.R
import com.project.movieapp.adapters.SimpleLoadMoreRecyclerAdapter
import com.project.movieapp.databinding.ActivityMovieDetailBinding
import com.project.movieapp.databinding.ItemMovieBinding
import com.project.movieapp.databinding.ItemReviewBinding
import com.project.movieapp.services.responses.MovieDetailResponse
import com.project.movieapp.services.responses.ResultsReviewItem
import com.project.movieapp.utils.ConnectionDetectorUtil
import com.project.movieapp.utils.DateUtils
import com.project.movieapp.utils.GlobalVariable
import com.project.movieapp.utils.UIUtils
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

class MovieDetailActivity : AppCompatActivity(){

    private lateinit var movieDetailBinding: ActivityMovieDetailBinding
    private lateinit var movieDetail: MovieDetailResponse
    private val movieDetailViewModel : MovieDetailViewModel by viewModels()
    private var reviewList : MutableList<ResultsReviewItem> = ArrayList()
    private var reviewAdapter: SimpleLoadMoreRecyclerAdapter<ResultsReviewItem>? = null
    private var isLoad: Boolean = false
    private var page: Int = 1
    private var totalPage: Int = 0
    private var connectionDetectorUtil = ConnectionDetectorUtil(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieDetailBinding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(movieDetailBinding.root)

        initData()
        initView()
    }

    private fun initData() {
        movieDetail = intent.getParcelableExtra("movie")!!
        if (connectionDetectorUtil.isConnectingToInternet) {
            movieDetailViewModel.getVideoUrl(movieDetail.id.toString())
            movieDetailViewModel.getReviews(movieDetail.id.toString(), page)
        } else {
            UIUtils.newInstance(this).showDialogError(this, getString(R.string.error_title_common), getString(R.string.error_network_connection))
        }

        reviewAdapter = SimpleLoadMoreRecyclerAdapter(reviewList, R.layout.item_review, object : SimpleLoadMoreRecyclerAdapter.OnViewHolder<ResultsReviewItem>{
            override fun onBindView(holder: SimpleLoadMoreRecyclerAdapter.SimpleViewHolder?, item: ResultsReviewItem) {
                val itemBinding : ItemReviewBinding = holder?.layoutBinding as ItemReviewBinding
                itemBinding.model = item
                itemBinding.tvRating.text = getString(R.string.tag_rating, item.authorDetails!!.rating.toString())
                Glide.with(this@MovieDetailActivity).asBitmap().load(GlobalVariable.BASE_IMAGE_URL + item.authorDetails!!.avatarPath).into(itemBinding.ivUserPhoto)
            }
        })
    }

    private fun initView() {
        fetchTrailer()
        fetchReview()

        movieDetailBinding.reviewAdapter = reviewAdapter

        Glide.with(this@MovieDetailActivity)
            .load("${GlobalVariable.BASE_IMAGE_URL}${movieDetail.backdropPath}")
            .into(movieDetailBinding.ivBackdrop)

        Glide.with(this@MovieDetailActivity)
            .load("${GlobalVariable.BASE_IMAGE_URL}${movieDetail.posterPath}")
            .into(movieDetailBinding.ivPosterPath)

        movieDetailBinding.txtTitle.text = movieDetail.title
        movieDetailBinding.txtOverview.text = movieDetail.overview
        movieDetailBinding.txtRating.text = getString(R.string.tag_rating, movieDetail.voteAverage.toString())
        movieDetailBinding.txtReleaseDate.text = getString(R.string.tag_release_date, DateUtils.newInstance().convertApiDateToDateTime(movieDetail.releaseDate))

        movieDetailBinding.refreshLayout.resetNoMoreData()
        movieDetailBinding.refreshLayout.setEnableFooterFollowWhenNoMoreData(true)
        movieDetailBinding.refreshLayout.setOnRefreshLoadMoreListener(object :
            OnRefreshLoadMoreListener {
            override fun onRefresh(@NonNull refreshLayout: RefreshLayout) {
                movieDetailBinding.refreshLayout.layout.postDelayed({
                    movieDetailBinding.refreshLayout.finishRefresh()
                    movieDetailBinding.refreshLayout.resetNoMoreData()
                }, 2000)
            }

            override fun onLoadMore(@NonNull refreshLayout: RefreshLayout) {
                if (page >= totalPage) {
                    movieDetailBinding.refreshLayout.finishLoadMoreWithNoMoreData()
                } else {
                    isLoad = true
                    page += 1
                    if (connectionDetectorUtil.isConnectingToInternet) movieDetailViewModel.getReviews(movieDetail.id.toString(), page) else UIUtils.newInstance(this@MovieDetailActivity).showDialogError(this@MovieDetailActivity, getString(R.string.error_title_common), getString(R.string.error_network_connection))
                }
            }
        })
    }

    private fun fetchTrailer() {
        movieDetailViewModel.videoUrl.observe(this@MovieDetailActivity){
            movieDetailBinding.videoView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo(it[0].key!!, 0f)
                }
            })
        }
    }

    private fun fetchReview(){
        movieDetailViewModel.reviews.observe(this) {
            if (it.isEmpty()){
                movieDetailBinding.fieldEmptyState.visibility = View.VISIBLE
            } else {
                reviewList = it as MutableList<ResultsReviewItem>
                if (isLoad){
                    reviewAdapter!!.loadMore(reviewList)
                    movieDetailBinding.refreshLayout.finishLoadMore()
                } else {
                    reviewAdapter!!.setMainData(reviewList)
                }
            }
        }
    }
}
