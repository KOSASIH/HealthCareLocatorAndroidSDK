package com.healthcarelocator.fragments.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import base.fragments.IFragment
import com.healthcarelocator.R
import com.healthcarelocator.adapter.search.SearchAdapter
import com.healthcarelocator.state.HealthCareLocatorSDK
import com.healthcarelocator.extensions.getColor
import com.healthcarelocator.extensions.isNotNullAndEmpty
import com.healthcarelocator.model.activity.ActivityObject
import com.healthcarelocator.model.config.HealthCareLocatorCustomObject
import kotlinx.android.synthetic.main.fragment_one_key_list_result.*

class HCLListResultFragment : IFragment() {
    companion object {
        fun newInstance() = HCLListResultFragment().apply {}
    }

    private var healthCareLocatorCustomObject: HealthCareLocatorCustomObject = HealthCareLocatorSDK.getInstance().getConfiguration()
    private var activities: ArrayList<ActivityObject> = arrayListOf()
    private val searchAdapter by lazy { SearchAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_one_key_list_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listContainer.setBackgroundColor(healthCareLocatorCustomObject.colorListBackground.getColor())
        getAbsFragment()?.getActivities()?.also {
            this.activities = it
        }
        rvResult.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchAdapter
            showDistance()
            searchAdapter.setData(activities)
        }
        searchAdapter.onHCPCardClickedListener = { oneKeyLocation ->
            if (parentFragment is FullMapFragment) (parentFragment as FullMapFragment).navigateToHCPProfile(oneKeyLocation)
            else if (parentFragment is HCLNearMeFragment) (parentFragment as HCLNearMeFragment).navigateToHCPProfile(oneKeyLocation)
        }
    }

    fun updateActivities(activities: ArrayList<ActivityObject>) {
        showDistance()
        this.activities = activities
        searchAdapter.setData(activities)
    }

    private fun showDistance(){
        searchAdapter.isPlaceAvailable = getAbsFragment()?.getPlaceDetail()?.placeId?.isNotNullAndEmpty()?:false
    }

    private fun getAbsFragment(): AbsMapFragment<*, *>? = parentFragment as? AbsMapFragment<*, *>
}