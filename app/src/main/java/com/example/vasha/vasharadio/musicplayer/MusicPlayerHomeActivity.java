package com.example.vasha.vasharadio.musicplayer;

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
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.vasha.vasharadio.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicPlayerHomeActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    ListView musicList;
    ArrayList<String> songsArrayList;
    ArrayAdapter<String> songsArrayAdapter;
    ArrayList<String> musicLocation = new ArrayList<>();
    MediaPlayer mediaPlayer = new MediaPlayer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player_home);
        musicList = (ListView)findViewById(R.id.musicList);
        songsArrayList = new ArrayList<>();
        checkUserPermission();
        songsArrayAdapter = new ArrayAdapter<String>(MusicPlayerHomeActivity.this,android.R.layout.simple_list_item_1, songsArrayList);
        musicList.setAdapter(songsArrayAdapter);

        musicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
                    String currentSongDuration = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                    musicLocation.add(songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
                    songsArrayList.add(currentSongTitle + "\n" + currentSongArtist + "\n" + currentSongDuration);
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
