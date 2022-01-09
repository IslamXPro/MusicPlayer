package com.example.bismillah

import android.content.Context
import android.graphics.BitmapFactory
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mplayer.Models.Music
import kotlinx.android.synthetic.main.music_like_rv.view.*
import kotlinx.android.synthetic.main.music_list_rv.view.*

class RvLoveAdapter(var context: Context, var list: List<Music>, var rvItemClickLove: RvItemClickLove)
    : RecyclerView.Adapter<RvLoveAdapter.Vh>(){

    inner class Vh(itemView: View): RecyclerView.ViewHolder(itemView){
        fun onBind(music: Music, position:Int){
            itemView.like_art_name.text = music.audioArtist
            itemView.like_art_name.setSingleLine()
            itemView.like_art_name.ellipsize = TextUtils.TruncateAt.MARQUEE
            itemView.like_art_name.marqueeRepeatLimit = -1
            itemView.like_art_name.isSelected = true
            itemView.like_song_name.text = music.audioTitle
            itemView.like_song_name.setSingleLine()
            itemView.like_song_name.ellipsize = TextUtils.TruncateAt.MARQUEE
            itemView.like_song_name.marqueeRepeatLimit = -1
            itemView.like_song_name.isSelected = true
            if (music.image!=""){
                val bm = BitmapFactory.decodeFile(music.image)
                itemView.list_img.setImageBitmap(bm)
            }
            itemView.setOnClickListener {
                rvItemClickLove.itemClick(music, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(LayoutInflater.from(parent.context).inflate(R.layout.music_like_rv, parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int = list.size
}
interface RvItemClickLove{
    fun itemClick(music: Music, position: Int)
}