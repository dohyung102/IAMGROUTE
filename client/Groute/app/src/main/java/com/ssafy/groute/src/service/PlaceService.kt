package com.ssafy.groute.src.service

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ssafy.groute.src.dto.Place
import com.ssafy.groute.src.response.PlaceLikeResponse
//import com.ssafy.groute.src.main.home.Place
import com.ssafy.groute.util.RetrofitCallback
import com.ssafy.groute.util.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Query

private const val TAG = "PlaceService"
class PlaceService {

    fun getAllPlaces() : MutableLiveData<List<Place>> {
        var responseLiveData : MutableLiveData<List<Place>> = MutableLiveData()
        val placeListRequest: Call<List<Place>> = RetrofitUtil.placeService.listPlace()
        placeListRequest.enqueue(object : Callback<List<Place>> {
            override fun onResponse(call: Call<List<Place>>, response: Response<List<Place>>) {
                var res = response.body()
                if(response.code() == 200){
                    if( res!=null){
                        responseLiveData.postValue(res)
                    }
                }else{
                    Log.d(TAG, "onResponse: ")
                }
            }

            override fun onFailure(call: Call<List<Place>>, t: Throwable) {
                responseLiveData.postValue(null)
            }
        })
        return responseLiveData
    }

    fun getPlaces(callback: RetrofitCallback<List<Place>>){
        val placeRequest : Call<List<Place>> = RetrofitUtil.placeService.listPlace()
        placeRequest.enqueue(object : Callback<List<Place>> {
            override fun onResponse(call: Call<List<Place>>, response: Response<List<Place>>) {
                val res = response.body()
                if(response.code() == 200){
                    if(res!=null){
                        callback.onSuccess(response.code(),res)
                    }
                }else{
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<List<Place>>, t: Throwable) {
                callback.onError(t)
            }

        })
    }

    fun getPlace(placeId: Int) : LiveData<Place> {
        val responseLiveData : MutableLiveData<Place> = MutableLiveData()
        val placeRequest : Call<Place> = RetrofitUtil.placeService.getPlace(placeId)
        placeRequest.enqueue(object : Callback<Place>{
            override fun onResponse(call: Call<Place>, response: Response<Place>) {
                val res = response.body()
                if(response.code() == 200){
                    if(res != null){
                        Log.d(TAG, "onResponse: ")
                        responseLiveData.value = res
                    }
                }else{
                    Log.d(TAG, "onResponse: ")
                }
            }

            override fun onFailure(call: Call<Place>, t: Throwable) {
                Log.d(TAG, "onFailure: ")
            }

        })
        return responseLiveData
    }
    
    fun updatePlace(place: Place, callback:RetrofitCallback<Boolean>){
        RetrofitUtil.placeService.updatePlace(place).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                val res = response.body()
                if(response.code() == 200){
                    Log.d(TAG, "onResponse: UpdateSuccess!!")
                }else{
                    Log.d(TAG, "onResponse: FAIL")
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Log.d(TAG, "onFailure: $t")
            }

        })
    }



    // place Coroutine Call
    suspend fun getPlaceList() = RetrofitUtil.placeService.getPlaceList()

    //place Coroutine Call
    suspend fun getPlaces(id:Int): Response<Place> {
        return RetrofitUtil.placeService.getPlacebyId(id)
    }

    //place bestList Corouting Call
    suspend fun getPlaceBestList() = RetrofitUtil.placeService.getPlaceBestList()

    //placeLike  Coroutine Call
    fun placeLike(placelike:PlaceLikeResponse, callback: RetrofitCallback<Boolean>){
        RetrofitUtil.placeService.placeLike(placelike).enqueue(object :Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                val res = response.body()
                if(response.code() == 200){
                    Log.d(TAG, "onResponse: UpdateSuccess22!!")
                }else{
                    Log.d(TAG, "onResponse: FAIL")
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Log.d(TAG, "onFailure: $t")
            }

        })
    }

    //place like check
    fun placeIsLike(placelike: PlaceLikeResponse ,callback: RetrofitCallback<Boolean>){
        RetrofitUtil.placeService.placeIsLike(placelike).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                val res = response.body()
                if(response.code() == 200){
                    Log.d(TAG, "onResponse: UpdateSuccess!!")
                }else{
                    Log.d(TAG, "onResponse: FAIL")
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Log.d(TAG, "onFailure: $t")
            }

        })
    }
}