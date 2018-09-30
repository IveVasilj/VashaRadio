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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.vasha.vasharadio.R;
import com.example.vasha.vasharadio.musicplayer.adapter.MP3SongAdapter;
import com.example.vasha.vasharadio.musicplayer.model.MP3Song;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MusicPlayerHomeActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    private RecyclerView musicList;
    private ImageButton playAndPauseButton;
    private ImageButton forwardButton;
    private ImageButton rewindButton;

    private MP3SongAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private int mediaPlayerCurrentPosition;
    private ArrayList<MP3Song> mp3SongArrayList = new ArrayList<>();
    private Random randomSong = new Random();
    private String dataSource;
    private int rememberPreviousSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player_home);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        initialize();
    }



    private void initialize()
    {
        //Connecting variables with views
        musicList = (RecyclerView)findViewById(R.id.musicList);
        playAndPauseButton = (ImageButton)findViewById(R.id.playAndPauseButton);
        forwardButton = (ImageButton)findViewById(R.id.forwardButton);
        rewindButton = (ImageButton)findViewById(R.id.rewindButton);

        //this setting improves perfomance if layout size of recyclerview is not changed
        musicList.setHasFixedSize(true);

        checkIfSongIsPlaying();
        //so that recyclerview has a similar behavior as listview
        mLayoutManager = new LinearLayoutManager(this);
        musicList.setLayoutManager(mLayoutManager);

        checkUserPermission();

        mAdapter = new MP3SongAdapter(mp3SongArrayList);
        musicList.setAdapter(mAdapter);

        //Listeners
        mAdapter.setOnItemClickListener(new MP3SongAdapter.OnItemClickListener() {
           @Override
           public void onItemClick(int position, ArrayList<MP3Song> songs) {
               playSelectedMP3File(position, songs);
           }
        });

        playAndPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){

                    if(playAndPauseButton.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.ic_pause).getConstantState()){
                        pauseSelectedMP3File();
                    }
                    else{
                        resumeSelectedMP3File();
                    }
                }
                else{
                    shuffleMP3Files();
                }

            }
        });

        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSource = mp3SongArrayList.get(rememberPreviousSong).getSongLocation().toString();
                shuffleMP3Files();
            }
        });

        rewindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPreviousMP3File(dataSource);
            }
        });
    }

    private void checkIfSongIsPlaying()
    {
        if(mediaPlayer.isPlaying()){
            playAndPauseButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
        }
        else{
            playAndPauseButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
        }
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
                    String currentSongLocation = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    mp3SongArrayList.add(new MP3Song(currentSongTitle,currentSongArtist,currentSongLocation));

                } while(songCursor.moveToNext());
            }
        }
    }

    private void playSelectedMP3File(int id, ArrayList<MP3Song> songs)
    {
        checkIfMP3isPlaying();

        try {
            mediaPlayer.setDataSource(songs.get(id).getSongLocation().toString());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.start();
        checkIfSongIsPlaying();
    }

    private void pauseSelectedMP3File()
    {
        mediaPlayer.pause();
        mediaPlayerCurrentPosition= mediaPlayer.getCurrentPosition();
        checkIfSongIsPlaying();
    }

    private void resumeSelectedMP3File() {
        mediaPlayer.seekTo(mediaPlayerCurrentPosition);
        mediaPlayer.start();
        checkIfSongIsPlaying();
    }

    private void goToPreviousMP3File(String dataSource){
        checkIfMP3isPlaying();

        try {
            mediaPlayer.setDataSource(dataSource);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.start();
        checkIfSongIsPlaying();
    }

    private void shuffleMP3Files(){
        int id = randomSong.nextInt(mp3SongArrayList.size() - 1) + 0;
        rememberPreviousSong = id;
        checkIfMP3isPlaying();

        try {
            mediaPlayer.setDataSource(mp3SongArrayList.get(id).getSongLocation().toString());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.start();
        checkIfSongIsPlaying();
    }

    private void checkIfMP3isPlaying(){
        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }

        mediaPlayer.reset();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar,menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }
}
