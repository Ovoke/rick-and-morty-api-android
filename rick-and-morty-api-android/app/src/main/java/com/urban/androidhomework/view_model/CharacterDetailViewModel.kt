package com.urban.androidhomework.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.urban.androidhomework.repository.NetworkClient
import com.urban.androidhomework.R
import com.urban.androidhomework.utils.Utils
import com.urban.androidhomework.model.CharacterData
import com.urban.androidhomework.model.CharacterDetails

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class CharacterDetailViewModel(val context: Application, val characterID: Int) : AndroidViewModel(context) {

    var liveData: MutableLiveData<CharacterDetails>
    var throwableLiveData: MutableLiveData<Throwable>
    private val compositeDisposable = CompositeDisposable()

    //initialize the data
    init {
        liveData = MutableLiveData()
        throwableLiveData = MutableLiveData()
        loadCharacterDetails(characterID)
    }

    /**
     * Method used to fetch the data
     */
    fun loadCharacterDetails(id: Int) {
        compositeDisposable.add(
                NetworkClient.get()
                        .service
                        .getCharacter(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { result -> run {
                                    if(!result.isSuccessful) {
                                        throwableLiveData.value = Throwable(context.resources.getString(R.string.error))

                                    } else if(result.body() != null) {
                                        val characterData = result.body()!!
                                        val locationID = Utils.getIDFromUrl(result.body()!!.location.url)
                                        loadLocationDetails(locationID, characterData)
                                    }
                                }},
                                { error -> onFailure(error) }
                        )
        )
    }

    private fun loadLocationDetails(locationID: Int, characterData: CharacterData){
        compositeDisposable.add(
                NetworkClient.get()
                        .service
                        .getLocation(locationID)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { result -> run{
                                    if(!result.isSuccessful) {
                                        throwableLiveData.value = Throwable(context.resources.getString(R.string.error))

                                    } else if(result.body() != null) {
                                        val location = result.body()!!
                                        onSuccess(CharacterDetails(characterData, location))
                                    }
                                }},
                                { error -> onFailure(error) }
                        )
        )
    }

    private fun onSuccess(characterDetails: CharacterDetails) {
        liveData.value = characterDetails
        throwableLiveData.value = null
    }

    private fun onFailure(error: Throwable?) {
        throwableLiveData.value = error
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}