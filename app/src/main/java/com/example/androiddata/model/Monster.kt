package com.example.androiddata.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.androiddata.IMAGE_BSEURL
import com.squareup.moshi.Json
@Entity(tableName = "monsters")
data class Monster(
    @PrimaryKey(autoGenerate = true)
    val monsterId:Int,
    val imageFile :String,
    val monsterName :String,
    val caption :String,
    val description :String,
    val price :Double,
    val scariness : Int
 )
{
    val imageUrl
    get() = "$IMAGE_BSEURL/$imageFile.webp"

    val thumbnailUrl
        get() = "$IMAGE_BSEURL/${imageFile}_tn.webp"
}

