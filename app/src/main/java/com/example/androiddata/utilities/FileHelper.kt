package com.example.androiddata.utilities

import android.app.Application
import android.content.Context
import java.io.File
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets.UTF_8
import kotlin.text.Charsets.UTF_8

class FileHelper {
    companion object{
        fun getTextFromResources(context: Context,resourceId:Int):String{
            return context.resources.openRawResource(resourceId).use {
                it.bufferedReader().use {
                     it.readText()
                }
            }
        }
        fun getTextFromAssets(context: Context,filename:String):String{
            return context.assets.open(filename).use {
                it.bufferedReader().use {
                    it.readText()
                }
            }
        }

        fun saveTextToFile(app: Application, json: String?) {
           val file = File(app.getExternalFilesDir("monsters"),"monster.json")
            file.writeText(json ?:"",Charsets.UTF_8)
        }

        fun readTextFile(app:Application):String?{
            val file = File(app.getExternalFilesDir("monsters"),"monster.json")
            return if(file.exists()){
                file.readText()
            } else null
        }
    }
}
