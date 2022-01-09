package com.example.bismillah

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.navigation.fragment.findNavController
import com.example.bismillah.MyData.list
import com.example.bismillah.MyData.loveListData
import com.example.bismillah.data.MySharedPreference
import com.example.mplayer.Models.Music
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_player.*
import kotlinx.android.synthetic.main.fragment_player.view.*


class PlayerFragment : Fragment() {

    lateinit var root: View
    lateinit var music: Music
    var position: Int = 0
    var mediaPlayer: MediaPlayer? = null
    lateinit var handler: Handler
    lateinit var handler1: Handler
    var isChecked = false
    private var loveList = ArrayList<Music>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_player, container, false)

        position = arguments?.getInt("position",-1)!!
        music = arguments?.getSerializable("music") as Music
        handler1 = Handler()

        root.pl_artist_name.isSelected = true

        return root
    }

    override fun onResume() {
        super.onResume()

        MySharedPreference.init(root.context)

        val list = MySharedPreference.adibList
        var index = -1

        for (i in list.indices){
            if (list[i].id == music.id){
                index = i
                break
            }
        }

        if (index!=-1){
            root.like_anim.setImageResource(R.drawable.like_img_red)
        }else{
            root.like_anim.setImageResource(R.drawable.like_img)
        }

        root.pl_like_btn.setOnClickListener {
            if (index != -1){
                root.like_anim.setImageResource(R.drawable.like_img_red)
                val list2 = MySharedPreference.adibList
                list2.removeAt(index)
                //LoveListga qo'shilishi
                MySharedPreference.adibList = list2
                onResume()
                /*val loveAdapter = RvLoveAdapter(root.context,loveList1,object : RvItemClickLove{
                    override fun itemClick(music: Music, position: Int) {
                        loveListData.addAll(listOf(Music()))
                    }

                })*/
            }else{
                root.like_anim.setImageResource(R.drawable.like_img)
                val list2 = MySharedPreference.adibList
                list2.add(music)
                MySharedPreference.adibList = list2
                onResume()
            }
        }

        if (position != -1){
            mediaPlayer = null
            mediaPlayer = MediaPlayer.create(context,list[position].audioUri)
            mediaPlayer?.start()

            playAnimation()
            root.pl_play_btn.speed = 1f
            root.pl_play_btn.playAnimation()
            handler1.postDelayed({root.pl_play_btn.cancelAnimation()},750)
            root.seekbar.max = mediaPlayer?.duration!!
            root.sound_progress.progressMax = mediaPlayer?.duration!!.toFloat()
            handler = Handler(activity?.mainLooper!!)

            root.all_song_size.text = list.size.toString()
            root.current_music_position.text = (position + 1).toString()

            root.pl_artist_name.text = list[position].audioArtist
            root.pl_artist_name.setSingleLine()
            root.pl_artist_name.ellipsize = TextUtils.TruncateAt.MARQUEE
            root.pl_artist_name.marqueeRepeatLimit = -1
            root.pl_artist_name.isSelected = true
            root.pl_song_name.text = list[position].audioTitle
            root.pl_song_name.setSingleLine()
            root.pl_song_name.ellipsize = TextUtils.TruncateAt.MARQUEE
            root.pl_song_name.marqueeRepeatLimit = -1
            root.pl_song_name.isSelected = true

            root.pl_music_time.text = milliSecondToTimer(mediaPlayer?.duration!!.toLong())
        }
        if (mediaPlayer?.isPlaying!!){
            handler.postDelayed(runnable,100)
        }
        var pos:Int? = null

        root.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser){
                    pos = progress
                }
                root.seekbar.progress = progress.toFloat().toInt()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                mediaPlayer?.seekTo(pos!!)
            }

        })

        root.pl_next_10.setOnClickListener {
            mediaPlayer?.seekTo(mediaPlayer?.currentPosition!!.plus(10000))
            root.pl_next_10.speed = 1f
            root.pl_next_10.playAnimation()
            playAnimation()
        }
        root.pl_prev_10.setOnClickListener {
            mediaPlayer?.seekTo(mediaPlayer?.currentPosition!!.minus(10000))
            root.pl_prev_10.speed = 1f
            root.pl_prev_10.playAnimation()
            playAnimation()
        }

        root.pl_play_btn.setOnClickListener {
            if (mediaPlayer?.isPlaying!!){
                mediaPlayer?.pause()
                root.pl_play_btn.speed = 50f
                root.pl_play_btn.playAnimation()
                pauseAnimation()
            }else{
                mediaPlayer?.start()
                root.pl_play_btn.speed = 1f
                root.pl_play_btn.playAnimation()
                handler1.postDelayed({root.pl_play_btn.cancelAnimation()},750)
                playAnimation()
            }
        }
        root.pl_next_btn.setOnClickListener {
            root.pl_next_btn.speed = 1f
            root.pl_next_btn.playAnimation()
            playAnimation()
            handler1.postDelayed({},500)
            if (++position < list.size){
                releaseMp()
                onResume()
            }else{
                position = 0
                releaseMp()
                onResume()
            }
        }
        root.pl_prev_btn.setOnClickListener {
            root.pl_prev_btn.speed = 1f
            root.pl_prev_btn.playAnimation()
            playAnimation()
            if (--position >= 0){
                releaseMp()
                onResume()
            }else{
                position = list.size -1
                releaseMp()
                onResume()
            }
        }
    }

    private fun releaseMp(){
        if (mediaPlayer != null){
            try {
                mediaPlayer?.release()
                mediaPlayer = null
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        if (mediaPlayer != null) releaseMp()
    }

    override fun onPause() {
        super.onPause()
        if (mediaPlayer != null){
            mediaPlayer!!.pause()
        }
    }


    private fun pauseAnimation(){

        root.circle_anim.cancelAnimation()
        root.circular_anim.cancelAnimation()
        root.bottom_anim.cancelAnimation()
        handler.postDelayed({
            root.circle_anim.visibility = View.GONE; root.circular_anim.visibility = View.GONE },500)
    }
    private fun playAnimation() {
        root.circle_anim.visibility = View.VISIBLE
        root.circular_anim.visibility = View.VISIBLE
        root.bottom_anim.playAnimation()
        root.circle_anim.playAnimation()
        root.circular_anim.playAnimation()
        root.circle_anim.loop(true)
        root.circular_anim.loop(true)
        root.bottom_anim.loop(true)
    }

    private var runnable = object : Runnable{
        override fun run() {
            if (mediaPlayer != null){
                root.seekbar.progress = mediaPlayer?.currentPosition!!
                root.pl_music_start_time.text =
                    milliSecondToTimer(mediaPlayer?.currentPosition!!.toLong())
                if (root.pl_music_start_time.text.toString() == root.pl_music_time.text.toString()){
                    //Musiqa tugaganda keyin musiqaga o'tib ketadi
                    if(mediaPlayer?.duration == seekbar.scrollBarFadeDuration){
                        playAnimation()
                        position++
                    }
                    releaseMp()
                    if (++position < list.size){
                        releaseMp()
                        onResume()
                    }else{
                        position = 0
                        releaseMp()
                        onResume()
                    }
                }
                handler.postDelayed(this,100)
            }
        }

    }
    fun milliSecondToTimer(millisecond:Long) : String{
        var finalTimerString = ""
        var secondsString = ""

        //Convert total duration into time
        val hours  = (millisecond / (1000*60*60)).toInt()
        val minutes = (millisecond % (1000*60*60)).toInt() / (1000*60)
        val seconds = (millisecond % (1000*60*60) % (1000*60)/1000).toInt()
        //Add hours if there
        if (hours > 0){
            finalTimerString = "$hours:"
        }

        //Prepending 0 to seconds if it one digit
        secondsString  = if (seconds < 10){
            "0$seconds"
        }else{
            "" + seconds
        }

        finalTimerString = "$finalTimerString$minutes:$secondsString"

        //return timer string
        return finalTimerString
    }
}