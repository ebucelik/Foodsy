package ebucelik.keepeasy.foodsy.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.databinding.FragmentAccountBinding
import kotlin.math.round

class AccountFragment : Fragment(R.layout.fragment_account) {

    private lateinit var binding: FragmentAccountBinding
    private val review = 3.4

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAccountBinding.bind(view)

        setReview()
    }

    private fun setReview(){
        val reviewInt = round(review).toInt()

        if(reviewInt >= 1){
            for (i in 0 until reviewInt){
                when(i){
                    0 -> binding.rate1.setImageResource(R.drawable.ic_round_star_rate_24_gold)
                    1 -> binding.rate2.setImageResource(R.drawable.ic_round_star_rate_24_gold)
                    2 -> binding.rate3.setImageResource(R.drawable.ic_round_star_rate_24_gold)
                    3 -> binding.rate4.setImageResource(R.drawable.ic_round_star_rate_24_gold)
                    4 -> binding.rate5.setImageResource(R.drawable.ic_round_star_rate_24_gold)
                }
            }
        }
    }

}