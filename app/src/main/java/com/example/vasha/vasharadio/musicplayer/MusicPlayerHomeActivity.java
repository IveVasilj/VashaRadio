package com.example.vasha.vasharadio.musicplayer;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.vasha.vasharadio.R;

import java.util.ArrayList;

public class MusicPlayerHomeActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    ListView musicList;
    ArrayList<String> songsArrayList;
    ArrayAdapter<String> songsArrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player_home);
        musicList = (ListView)findViewById(R.id.musicList);
        songsArrayList = new ArrayList<>();
        checkUserPermission();
        songsArrayAdapter = new ArrayAdapter<String>(MusicPlayerHomeActivity.this,android.R.layout.simple_list_item_1, songsArrayList);
        musicList.setAdapter(songsArrayAdapter);
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
        Cursor songCursor = contentResolver.query(uri,null,null,null,null);

        if(songCursor != null)
        {
            if(songCursor.moveToFirst())
            {
                do{
                    String currentSongTitle = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                    String currentSongArtist = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    songsArrayList.add(currentSongTitle + "\n" + currentSongArtist);
                } while(songCursor.moveToNext());
            }
        }
    }
}
