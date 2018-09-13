package com.example.vasha.vasharadio.musicplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.vasha.vasharadio.R;

public class MusicPlayerHomeActivity extends AppCompatActivity {
    ListView musicList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player_home);

        musicList = (ListView)findViewById(R.id.musicList);

        
    }
}
