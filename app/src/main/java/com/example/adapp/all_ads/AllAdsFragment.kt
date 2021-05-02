package com.example.adapp.all_ads

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.replace
import com.example.adapp.R
import com.example.adapp.model.Ad_response
import com.example.adapp.model.Advertisement
import com.example.adapp.presenter.AdDisplayPresenter
import com.example.adapp.presenter.RetrieveAdsCallback
import com.example.adapp.view.AddDetailsFragment
import com.google.firebase.database.GenericTypeIndicator
import kotlinx.android.synthetic.main.fragment_item_all_ads.*

/**
 * A fragment representing a list of Items.
 */
class AllAdsFragment : Fragment(), AdDisplayPresenter.View, RetrieveAdsCallback, SearchView.OnQueryTextListener {

    private var columnCount = 1
    lateinit var displayPresenter: AdDisplayPresenter
    var listOfAds = listOf<Advertisement>()
    lateinit var rView : RecyclerView
    var selectedFilter = "all"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
        displayPresenter = AdDisplayPresenter(this)
        displayPresenter.getAllAds(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_all_ads, container, false)

        rView = view.findViewById(R.id.myAdsList)
        //rView.layoutManager = LinearLayoutManager(context)
        rView.layoutManager = GridLayoutManager(context,2)
        // Set the adapter
//        if (view is RecyclerView) {
//            with(view) {
//                /*layoutManager = when {
//                    columnCount <= 1 -> LinearLayoutManager(context)
//                    else -> GridLayoutManager(context, columnCount)
//                }*/
//                rView = view
//                layoutManager=GridLayoutManager(context,2)
//                //adapter = AllAdsRecyclerViewAdapter(listOfAds)
//            }
//        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchView.setOnQueryTextListener(this)

        mobB.setOnClickListener {
            filterList("mobile")
        }

        vehicleB.setOnClickListener {
            filterList("vehicle")
        }

        propertyB.setOnClickListener {
            filterList("property")
        }

        electronicB.setOnClickListener {
            filterList("electronic")
        }

        furnitureB.setOnClickListener {
            filterList("furniture")
        }

        allB.setOnClickListener {
            filterList("all")
        }
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            AllAdsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }

    override fun sendToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun onResponse(response: Ad_response) {
        listOfAds = response.listOfAds!!
        rView.adapter = AllAdsRecyclerViewAdapter(listOfAds){
            val adFragment = AddDetailsFragment.newInstance(it)
            activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment, adFragment)
                    ?.addToBackStack(null)
                    ?.commit()
        }
        rView.adapter?.notifyDataSetChanged()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        var filteredAdList = mutableListOf<Advertisement>()

        for(ad: Advertisement in listOfAds){
            if (newText != null) {
                if(ad.location?.toLowerCase()?.contains(newText.toLowerCase())!!){
                    filteredAdList.add(ad)
                }
            }
        }
        Log.d("AllAds", "$filteredAdList")
        rView.adapter = AllAdsRecyclerViewAdapter(filteredAdList){
            val adFragment = AddDetailsFragment.newInstance(it)
            activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment, adFragment)
                    ?.addToBackStack(null)
                    ?.commit()
        }
        return false
    }

    fun filterList(status: String){
        selectedFilter = status
        var filteredAdList = mutableListOf<Advertisement>()

        for(ad: Advertisement in listOfAds){
            if (selectedFilter != null && selectedFilter != "all") {
                if(ad.category?.toLowerCase()?.contains(selectedFilter.toLowerCase())!!){
                    filteredAdList.add(ad)
                }
            }
            else if(selectedFilter == "all"){
                filteredAdList = listOfAds.toMutableList()
            }
        }
        rView.adapter = AllAdsRecyclerViewAdapter(filteredAdList){
            val adFragment = AddDetailsFragment.newInstance(it)
            activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment, adFragment)
                    ?.addToBackStack(null)
                    ?.commit()
        }
    }
}