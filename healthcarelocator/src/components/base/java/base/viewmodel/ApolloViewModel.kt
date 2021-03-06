package base.viewmodel

import android.content.Context
import android.content.SharedPreferences
import base.extensions.runOnUiThread
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.Query
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.rx2.Rx2Apollo
import com.healthcarelocator.extensions.ApolloConnector
import com.healthcarelocator.extensions.removeConsultedProfile
import com.healthcarelocator.extensions.storeConsultedProfiles
import com.healthcarelocator.extensions.storeLastSearch
import com.healthcarelocator.model.SearchObject
import com.healthcarelocator.model.activity.ActivityObject
import com.healthcarelocator.state.HealthCareLocatorSDK
import com.google.gson.Gson
import io.reactivex.Flowable

abstract class ApolloViewModel<T> : AppViewModel<T>() {

    protected fun <D : Operation.Data, T, V : Operation.Variables>
            query(
            query: () -> Query<D, T, V>, success: (response: Response<T>) -> Unit,
            error: (e: Exception) -> Unit, runOnThread: Boolean = false, context: Context? = null) {
        ApolloConnector.getInstance()
                .getApolloClient((HealthCareLocatorSDK.getInstance() as HealthCareLocatorSDK).getClientUrl())
                .query(query())
                .enqueue(object : ApolloCall.Callback<T>() {
                    override fun onFailure(e: ApolloException) {
                        runOnUiThread(Runnable { error(e) })
                    }

                    override fun onResponse(response: Response<T>) {
                        if (!runOnThread)
                            runOnUiThread(Runnable {
                                if (response.hasErrors()) error(Exception(response.errors?.toString()))
                                else success(response)
                            })
                        else {
                            if (response.hasErrors()) error(Exception(response.errors?.toString()))
                            else success(response)
                        }
                    }
                })
    }

    protected fun <D : Operation.Data, T, V : Operation.Variables, K>
            rxQuery(
            query: () -> Query<D, T, V>, map: (response: Response<T>) -> K,
            onSuccess: (data: K) -> Unit, onError: (e: Throwable) -> Unit, context: Context? = null) {
        disposable?.add(Rx2Apollo.from(
                ApolloConnector.getInstance()
                        .getApolloClient((HealthCareLocatorSDK.getInstance() as HealthCareLocatorSDK).getClientUrl())
                        .query(query())
        ).map { map(it) }.compose(composeObservable()).subscribe({
            onSuccess(it)
        }, {
            onError(it)
        })
        )
    }

    fun storeConsultedProfile(pref: SharedPreferences, activity: ActivityObject) {
        disposable?.add(Flowable.just(0)
                .map {
                    activity.createdAt = System.currentTimeMillis()
                    pref.storeConsultedProfiles(Gson(), activity)
                }
                .compose(compose())
                .subscribe({}, {})
        )
    }

    fun storeSearch(pref: SharedPreferences, obj: SearchObject) {
        disposable?.add(Flowable.just(0)
                .map {
                    obj.createdAt = System.currentTimeMillis()
                    pref.storeLastSearch(Gson(), obj)
                }
                .compose(compose())
                .subscribe({}, {})
        )
    }

    fun removeConsultedProfile(pref: SharedPreferences, activity: ActivityObject) {
        disposable?.add(Flowable.just(activity)
                .map {
                    pref.removeConsultedProfile(Gson(), activity)
                }
                .compose(compose())
                .subscribe({}, {})
        )
    }
}