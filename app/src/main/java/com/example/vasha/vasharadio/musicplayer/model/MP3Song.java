package com.example.vasha.vasharadio.musicplayer.model;

public class MP3Song {

    private String songTitle;
    private String songArtist;

    public MP3Song(String songTitle, String songArtist)
    {
        this.songTitle = songTitle;
        this.songArtist = songArtist;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public String getSongArtist() {
        return songArtist;
    }
}
