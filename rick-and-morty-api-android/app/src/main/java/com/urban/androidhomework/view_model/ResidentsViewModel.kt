package com.urban.androidhomework.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.urban.androidhomework.repository.NetworkClient
import com.urban.androidhomework.R
import com.urban.androidhomework.model.CharacterData

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

import retrofit2.Response


class ResidentsViewModel(val context: Application, val residents: ArrayList<Int>) : AndroidViewModel(context) {

    var liveData: MutableLiveData<Response<List<CharacterData>>>
    var throwableLiveData: MutableLiveData<Throwable>
    private val compositeDisposable = CompositeDisposable()

    //initialize the data
    init {
        liveData = MutableLiveData()
        throwableLiveData = MutableLiveData()
        loadResidents(residents)
    }

    /**
     * Method used to fetch the data
     */
    fun loadResidents(ids: ArrayList<Int>) {
        compositeDisposable.add(
                NetworkClient.get()
                        .service
                        .getLocationResidents(ids)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { result -> onSuccess(result) },
                                { error -> onFailure(error) }
                        )
        )
    }

    private fun onSuccess(response: Response<List<CharacterData>>) {
        if(!response.isSuccessful) {
            throwableLiveData.value = Throwable(context.resources.getString(R.string.error))

        } else if(response.body() != null) {
            liveData.value = response
            throwableLiveData.value = null
        }
    }

    private fun onFailure(error: Throwable?) {
        throwableLiveData.value = error
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}