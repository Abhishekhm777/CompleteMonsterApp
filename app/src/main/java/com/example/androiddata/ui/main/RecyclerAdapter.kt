package com.example.androiddata.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androiddata.R
import com.example.androiddata.model.Monster
import com.example.androiddata.utilities.PrefsHelper
import kotlinx.android.synthetic.main.monster_gridlayout.view.*

class RecyclerAdapter(val context: Context,
                      val monster:List<Monster>,
                      val itemClickListner: ItemClickListner):
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>()

{


    override fun getItemCount() = monster.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =LayoutInflater.from(parent.context)
        val layoutStyle = PrefsHelper.getItemType(parent.context)
        val layoutId=if(layoutStyle =="grid"){
             R.layout.monster_gridlayout
        }else{
            R.layout.monster_list_item
        }
        val view = inflater.inflate(layoutId,parent,false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val monster =monster[position]
        with(holder){
            nameText?.let{
                it.text=monster.monsterName
                it.contentDescription=monster.monsterName
            }
            ratingBar?.rating=monster.scariness.toFloat()
            Glide.with(context)
                .load(monster.thumbnailUrl)
                .into(monsterImage)
            itemView.setOnClickListener {
                    itemClickListner.onItemClick(monster)
            }
        }
    }


    inner class ViewHolder(itemView : View) :
        RecyclerView.ViewHolder(itemView){
        val nameText=itemView.findViewById<TextView>(R.id.nameText)
        val monsterImage=itemView.findViewById<ImageView>(R.id.monsterImage)
        val ratingBar=itemView.findViewById<RatingBar>(R.id.ratingBar)


    }

    interface ItemClickListner {

        fun onItemClick(monster:Monster)

    }

}