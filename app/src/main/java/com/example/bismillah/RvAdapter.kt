package com.example.bismillah

import android.content.Context
import android.graphics.BitmapFactory
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mplayer.Models.Music
import kotlinx.android.synthetic.main.music_list_rv.view.*

class RvAdapter(var context: Context, var list: List<Music>, var rvItemClick: RvItemClick)
    :RecyclerView.Adapter<RvAdapter.Vh>(){

    inner class Vh(itemView:View):RecyclerView.ViewHolder(itemView){
        fun onBind(music: Music, position:Int){
            itemView.list_art_name.text = music.audioArtist
            itemView.list_art_name.setSingleLine()
            itemView.list_art_name.ellipsize = TextUtils.TruncateAt.MARQUEE
            itemView.list_art_name.marqueeRepeatLimit = -1
            itemView.list_art_name.isSelected = true
            itemView.list_music_name.text = music.audioTitle
            itemView.list_music_name.setSingleLine()
            itemView.list_music_name.ellipsize = TextUtils.TruncateAt.MARQUEE
            itemView.list_music_name.marqueeRepeatLimit = -1
            itemView.list_music_name.isSelected = true
            if (music.image!=""){
                val bm = BitmapFactory.decodeFile(music.image)
                itemView.list_img.setImageBitmap(bm)
            }
            itemView.setOnClickListener {
                rvItemClick.itemClick(music, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(LayoutInflater.from(parent.context).inflate(R.layout.music_list_rv, parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int = list.size
}
interface RvItemClick{
    fun itemClick(music: Music, position: Int)
}