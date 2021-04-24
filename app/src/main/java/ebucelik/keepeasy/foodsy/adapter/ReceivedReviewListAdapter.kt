package ebucelik.keepeasy.foodsy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.databinding.RowReviewBinding
import ebucelik.keepeasy.foodsy.entitiy.ReceivedReviewList
import ebucelik.keepeasy.foodsy.entitiy.Review

class ReceivedReviewListAdapter(context: Context, _receivedReviewList: ReceivedReviewList):BaseAdapter() {

    private val receivedReviewContext = context
    private val reviewList = _receivedReviewList
    private lateinit var binding: RowReviewBinding

    override fun getCount(): Int {
        return reviewList.reviewList.size
    }

    override fun getItem(p0: Int): Review {
        return reviewList.reviewList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(receivedReviewContext)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.row_review, p2, false)

        binding.review = getItem(p0)

        binding.reviewPoints.text = "${getItem(p0).reviewPoints} points"

        if (getItem(p0).reviewText != null)
            binding.reviewText.text = "\"${getItem(p0).reviewText}\""

        return binding.root
    }
}