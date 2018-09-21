package com.example.vasha.vasharadio.musicplayer.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.vasha.vasharadio.R;
import com.example.vasha.vasharadio.musicplayer.adapter.MP3SongAdapter;
import com.example.vasha.vasharadio.musicplayer.model.MP3Song;

import java.io.IOException;
import java.util.ArrayList;

public class MusicPlayerHomeActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private RecyclerView musicList;
    private MP3SongAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private SearchView searchView;

    private ArrayList<MP3Song> mp3SongArrayList = new ArrayList<>();
    private ArrayList<String> musicLocation = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player_home);

        initialize();
    }



    private void initialize()
    {
        musicList = (RecyclerView)findViewById(R.id.musicList);
        //this setting improves perfomance if layout size of recyclerview is not changed
        musicList.setHasFixedSize(true);

        //so that recyclerview has a similar behavior as listview
        mLayoutManager = new LinearLayoutManager(this);
        musicList.setLayoutManager(mLayoutManager);

        checkUserPermission();

        mAdapter = new MP3SongAdapter(mp3SongArrayList);
        musicList.setAdapter(mAdapter);

        //searchView = (SearchView)findViewById(R.id.searchView);

//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//           @Override
//           public boolean onQueryTextSubmit(String query) {
//               return false;
//           }
//
//           @Override
//           public boolean onQueryTextChange(String newText) {
//               mAdapter.getFilter().filter(newText);
//               return false;
//           }
//       });
        mAdapter.setOnItemClickListener(new MP3SongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                playSelectedMP3File(position);
            }
        });
    }

    private void checkUserPermission(){
        if(ContextCompat.checkSelfPermission(MusicPlayerHomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(MusicPlayerHomeActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE))
            {
                ActivityCompat.requestPermissions(MusicPlayerHomeActivity.this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
            else
            {
                ActivityCompat.requestPermissions(MusicPlayerHomeActivity.this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        }
        else
        {
            fetchMP3Files();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    fetchMP3Files();
                }
                else
                {
                    Toast.makeText(MusicPlayerHomeActivity.this, "Permission denied!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }
        }
    }



    private void fetchMP3Files()
    {
        ContentResolver contentResolver =  getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor songCursor = contentResolver.query(uri,null,null,null,MediaStore.Audio.Media.TITLE + " ASC");

        if(songCursor != null)
        {
            if(songCursor.moveToFirst())
            {
                do{

                    String currentSongTitle = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                    String currentSongArtist = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));

                    musicLocation.add(songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
                    mp3SongArrayList.add(new MP3Song(currentSongTitle,currentSongArtist));

                } while(songCursor.moveToNext());
            }
        }
    }

    private void playSelectedMP3File(int id)
    {
        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }

        mediaPlayer.reset();

        try {
            mediaPlayer.setDataSource(musicLocation.get(id).toString());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.start();
    }
}
