package com.amir.musicvideosample.viewModel
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amir.musicvideosample.data.repository.AppRepository
import com.amir.musicvideosample.model.VideosModel
import com.amir.musicvideosample.utils.JsonConverter
import com.amir.musicvideosample.utils.lazyDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VideosViewModel(application: Application,private val repository : AppRepository) : AndroidViewModel(application){

    val shouldFetchNewVideos = MutableLiveData(false)

    init {
        loadNewVideos(application)
    }

    fun loadNewVideos(context: Context){
        val videos = JsonConverter().getVideos(context)
        videos?.forEach {
            insertVideo(it)
        }
    }

    private fun insertVideo(video : VideosModel){
        CoroutineScope(Dispatchers.Main).launch {
            repository.insertVideo(video)
        }
    }

    fun deleteVideos(){
        CoroutineScope(Dispatchers.Main).launch {
            repository.deleteVideos()
        }
    }

    fun getAllVideosAsync() : Deferred<LiveData<List<VideosModel>>>{
        val videos by lazyDeferred {
            repository.getAllVideos()
        }
        return videos
    }
}