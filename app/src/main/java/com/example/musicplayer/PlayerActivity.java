package com.example.musicplayer;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity
{
    Button btn_next,btn_previous,btn_pause;
    TextView songTextLabel;
    SeekBar songseekbar;

    static MediaPlayer mymediaplayer;
    int position;
    String sname;
    ArrayList<File> mySongs;
    Thread updateseekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        btn_next=(Button)findViewById(R.id.next);
        btn_previous=(Button)findViewById(R.id.previous);
        btn_pause=(Button)findViewById(R.id.pause);
        songTextLabel = (TextView) findViewById(R.id.songLabel);
        songseekbar = (SeekBar) findViewById(R.id.seekbar);

        getSupportActionBar().setTitle("Now Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);;

        updateseekbar = new Thread()
        {

            @Override
            public void run()
            {

                int totalduration = mymediaplayer.getDuration();
                int currentposition = 0;

                while (currentposition<totalduration)
                {

                    try
                    {
                        sleep(500);
                        currentposition = mymediaplayer.getCurrentPosition();
                        songseekbar.setProgress(currentposition);
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }

                }

            }
        };


        if (mymediaplayer!=null)
        {
            mymediaplayer.stop();
            mymediaplayer.release();

        }

        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        mySongs=(ArrayList) bundle.getParcelableArrayList("songs");
        sname = mySongs.get(position).getName().toString();

        String songname = i.getStringExtra("songname");

        songTextLabel.setText(songname);
        songTextLabel.setSelected(true);

        position = bundle.getInt("pos",0);

        Uri u = Uri.parse(mySongs.get(position).toString());

        mymediaplayer = MediaPlayer.create(getApplicationContext(),u);

        mymediaplayer.start();
        songseekbar.setMax(mymediaplayer.getDuration());

        updateseekbar.start();

        songseekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

                mymediaplayer.seekTo(seekBar.getProgress());

            }
        });


         btn_pause.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 songseekbar.setMax(mymediaplayer.getDuration());

                 if (mymediaplayer.isPlaying()){

                     btn_pause.setBackgroundResource(R.drawable.icon_play);
                      mymediaplayer.pause();
                 }

                 else
                 {
                     btn_pause.setBackgroundResource(R.drawable.icon_pause);
                     mymediaplayer.start();
                 }



             }
         });


         btn_next.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 mymediaplayer.stop();
                 mymediaplayer.release();

                 position = ((position+1)%mySongs.size());
                 Uri u = Uri.parse(mySongs.get(position).toString());
                 mymediaplayer = MediaPlayer.create(getApplicationContext(),u);

                 sname = mySongs.get(position).getName().toString();
                 songTextLabel.setText(sname);

                 mymediaplayer.start();
             }
         }

         );

          btn_previous.setOnClickListener(new View.OnClickListener()
          {
              @Override
              public void onClick(View v) {
                mymediaplayer.stop();
                mymediaplayer.release();

                  position = ((position-1)<0)?(mySongs.size()-1):(position-1);
                  Uri u = Uri.parse(mySongs.get(position).toString());
                  mymediaplayer = MediaPlayer.create(getApplicationContext(),u);

                  sname = mySongs.get(position).getName().toString();
                  songTextLabel.setText(sname);

                  mymediaplayer.start();
              }
          }
          );
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        if(item.getItemId() == android.R.id.home);{
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}

