package com.example.songplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    private ArrayList<File> mySongs;
    private Button Next;
    private Button Previous;
    private Button Pause;
    private static MediaPlayer mediaPlayer;
    private Thread updataSeekbar;
    private SeekBar seekBar;
    private int position;
    private TextView Title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        seekBar=(SeekBar)findViewById(R.id.seek_bar);
        Title=(TextView)findViewById(R.id.tv_songs);
        Next=(Button)findViewById(R.id.next);
        Previous=(Button)findViewById(R.id.previous);
        Pause=(Button)findViewById(R.id.pause);

        updataSeekbar=new Thread(){

            @Override
            public void run(){
                int currentDuration=0;
                int totalDuration=mediaPlayer.getDuration();
                while(currentDuration < totalDuration){

                    try{
                        sleep(500);
                        currentDuration=mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentDuration);
                    }
                    catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        };

        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        Intent i=getIntent();
        Bundle bundle=i.getExtras();

        mySongs=(ArrayList) bundle.getParcelableArrayList("songs");
        position=bundle.getInt("pos",0);
        String SongName=mySongs.get(position).getName().toString();

        Title.setText(SongName);
        Title.setSelected(true);

        Uri u=Uri.parse(mySongs.get(position).toString());

        mediaPlayer=MediaPlayer.create(getApplicationContext(),u);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());

        updataSeekbar.start();




        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                mediaPlayer.seekTo(seekBar.getProgress());

            }
        });

        Pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                seekBar.setMax(mediaPlayer.getDuration());
               if(mediaPlayer.isPlaying()){

                  Pause.setBackgroundResource(R.drawable.icon_playy);
                  mediaPlayer.pause();
               }
               else{
                   Pause.setBackgroundResource(R.drawable.icon_pausee);
                   mediaPlayer.start();
               }
            }
        });

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaPlayer.stop();
                mediaPlayer.release();
                position=position+1;

                String songName=mySongs.get(position).getName().toString();

                Title.setText(songName);

                Uri u=Uri.parse(mySongs.get(position).toString());

                mediaPlayer=MediaPlayer.create(getApplicationContext(),u);

                mediaPlayer.start();

            }
        });


        Previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaPlayer.stop();
                mediaPlayer.release();

                position=position-1;
                if(position<0){
                    position=0;
                }

                String songName=mySongs.get(position).getName().toString();

                Title.setText(songName);
                Title.setSelected(true);

                Uri u=Uri.parse(mySongs.get(position).toString());

                mediaPlayer=MediaPlayer.create(getApplicationContext(),u);

                mediaPlayer.start();

                seekBar.setMax(mediaPlayer.getDuration());

            }
        });















    }
}
