package org.d3if0104.assessment1.ui.objek_non_ppn

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if0104.assessment1.MainActivity
import org.d3if0104.assessment1.model.ObjekNonPpn
import org.d3if0104.assessment1.network.ApiStatus
import org.d3if0104.assessment1.network.ObjekNonPpnApi
import org.d3if0104.assessment1.network.UpdateWorker
import java.util.concurrent.TimeUnit

class ObjekNonPpnViewModel: ViewModel() {
    private val data = MutableLiveData<List<ObjekNonPpn>>()
    private val status = MutableLiveData<ApiStatus>()
    init {
        retrieveData()
    }

    private fun retrieveData() {
        viewModelScope.launch (Dispatchers.IO) {
            status.postValue(ApiStatus.LOADING)
            try {
                data.postValue(ObjekNonPpnApi.service.getObjekNonPpn())
                status.postValue(ApiStatus.SUCCESS)
            } catch (e: Exception) {
                status.postValue(ApiStatus.FAILED)
            }
        }
    }
    fun getData(): LiveData<List<ObjekNonPpn>> = data

    fun getStatus(): LiveData<ApiStatus> = status

    fun scheduleUpdater(app: Application) {
        val request = OneTimeWorkRequestBuilder<UpdateWorker>()
            .setInitialDelay(1, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(app).enqueueUniqueWork(
            MainActivity.CHANNEL_ID,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }
}