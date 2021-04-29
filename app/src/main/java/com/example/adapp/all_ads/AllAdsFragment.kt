package com.example.adapp.all_ads

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.adapp.R
import com.example.adapp.model.Ad_response
import com.example.adapp.model.Advertisement
import com.example.adapp.presenter.AdDisplayPresenter
import com.example.adapp.presenter.RetrieveAdsCallback
import com.google.firebase.database.GenericTypeIndicator
import kotlinx.android.synthetic.main.fragment_item_all_ads.*

/**
 * A fragment representing a list of Items.
 */
class AllAdsFragment : Fragment(), AdDisplayPresenter.View, RetrieveAdsCallback {

    private var columnCount = 1
    lateinit var displayPresenter: AdDisplayPresenter
    var listOfAds = listOf<Advertisement>()
    lateinit var rView : RecyclerView

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

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                /*layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }*/
                rView = view
                layoutManager=GridLayoutManager(context,2)
                //adapter = AllAdsRecyclerViewAdapter(listOfAds)
            }
        }
        return view
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
        rView.adapter = AllAdsRecyclerViewAdapter(listOfAds)
        rView.adapter?.notifyDataSetChanged()
    }
}