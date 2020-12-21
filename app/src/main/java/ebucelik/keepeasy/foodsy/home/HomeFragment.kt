package ebucelik.keepeasy.foodsy.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private var names: Array<String> = arrayOf("Ebu", "Gabriel", "Ahmedin", "Turgut", "Leon", "Max", "Abdi", "Haki", "Maxi", "Taxi", "Susi", "Sushi", "Taki", "Maki", "Saki", "Laki", "Fifa", "Fortnite", "CoD");

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)

        binding.homeListView.adapter = HomeAdapter(activity?.baseContext!!)
    }

    private class HomeAdapter(context: Context) : BaseAdapter(){

        private val homeContext: Context

        init {
            homeContext = context
        }

        override fun getCount(): Int {
            return 35
        }

        override fun getItem(position: Int): Any {
            return "Any"
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
            val textView  = TextView(homeContext)
            textView.text = "Helo Moto"

            return textView
        }
    }

}