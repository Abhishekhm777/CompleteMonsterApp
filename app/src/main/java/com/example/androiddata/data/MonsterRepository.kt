package com.example.androiddata.data

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast
import androidx.annotation.WorkerThread
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.example.androiddata.LOG_TAG
import com.example.androiddata.WEB_SERVICE_URL
import com.example.androiddata.model.Monster
import com.example.androiddata.utilities.FileHelper
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MonsterRepository(val app : Application) {


   val monsterData =MutableLiveData<List<Monster>>()
    private val monsterDao =MonsterDatabase.getDatabaseInstance(app)
        .monsterDao()

    init {
        //To perform Database operation here using Coroutines
        CoroutineScope(Dispatchers.IO).launch {
            val data =monsterDao.getAll()
            if(data.isEmpty()){
                callWebService()
            }else{
                monsterData.postValue(data)

                //ui operation cannot be done here becs corotines running on background
                //thread meaning Dispatchers.IO
                //In order to call ui operation use withcontext like below
                withContext(Dispatchers.Main){
                    Toast.makeText(app,"LOCAL DATA",Toast.LENGTH_LONG).show()

                }
            }
        }



     /*
     To read and display from the file stored in the external or internal storage
     val data =readDataFromCache()
        if(data.isEmpty()){
            refreshDataFromWeb()
        }
        else{
            Log.i(LOG_TAG,"Using local data")
            monsterData.value=data
        }*/
    }

    @WorkerThread
    private suspend fun callWebService(){
        if(networkAvailable()){
            withContext(Dispatchers.Main){
                Toast.makeText(app,"NetworkData",Toast.LENGTH_LONG).show()

            }
            Log.i(LOG_TAG,"Calling web service")
            val retrofit = Retrofit.Builder()
            .baseUrl(WEB_SERVICE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
            val service=retrofit.create(MonsterService::class.java)
            val serviceData = service.getMonsterdata().body() ?: emptyList()
            monsterData.postValue(serviceData)


            //To store int structured data base /first delte all and then add new data
            monsterDao.deleteAll()
            monsterDao.insertMonsters(serviceData)
           /* Save to  internal storage as afile
           saveDataToCache(serviceData)*/
        }

    }

    @Suppress("DEPRECATION")
    private fun networkAvailable():Boolean{
        val connectivityManager = app.getSystemService(Context.CONNECTIVITY_SERVICE)
        as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo?.isConnectedOrConnecting ?: false

    }

    fun refreshDataFromWeb() {
        CoroutineScope(Dispatchers.IO).launch {
            callWebService()
        }
    }

    private fun saveDataToCache(monsterData:List<Monster>){

        if(ContextCompat.checkSelfPermission(
             app,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED) {
            val moshi = Moshi.Builder().build()
            val listType = Types.newParameterizedType(List::class.java, Monster::class.java)
            val adapter: JsonAdapter<List<Monster>> = moshi.adapter(listType)
            val json = adapter.toJson(monsterData)
            FileHelper.saveTextToFile(app, json)
        }
    }

    private fun readDataFromCache() : List<Monster>{
        val json = FileHelper.readTextFile(app) ?: return emptyList()
        val moshi=Moshi.Builder().build()
        val listType=Types.newParameterizedType(List::class.java,Monster::class.java)
        val adapter:JsonAdapter<List<Monster>> =moshi.adapter(listType)
       return  adapter.fromJson(json) ?: return  emptyList()

    }
}