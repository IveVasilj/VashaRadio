package com.example.vasha.vasharadio.musicplayer.model;

public class MP3Song {

    private String songTitle;
    private String songArtist;
    private String songLocation;

    public MP3Song(String songTitle, String songArtist, String songLocation)
    {
        this.songTitle = songTitle;
        this.songArtist = songArtist;
        this.songLocation = songLocation;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public String getSongArtist() {
        return songArtist;
    }

    public String getSongLocation() {
        return songLocation;
    }
}
