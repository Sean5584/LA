/**
 * Album.java
 *
 * Represents a music album that contains metadata (title, artist, genre, year)
 * and a list of songs. This class is used in the model layer of the music library system.
 *
 * Author: Haobin yan
 */

package model;

import java.util.ArrayList;
import java.util.List;

public class Album {
    private String title;           // Album title
    private String artist;          // Artist name
    private String genre;           // Music genre
    private int year;               // Release year
    private List<Song> songs;       // List of songs in the album

    // Constructor with all album details
    public Album(String title, String artist, String genre, int year, List<Song> songs) {
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.year = year;
        this.songs = songs != null ? songs : new ArrayList<>();
    }

    // Constructor used when the song list is not yet available
    public Album(String title, String artist, String genre) {
        this(title, artist, genre, 0, new ArrayList<>());
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getGenre() {
        return genre;
    }

    public int getYear() {
        return year;
    }

    public List<Song> getSongs() {
        return songs;
    }

    // Adds a song to the album if it's not already included
    public void addSong(Song song) {
        if (!songs.contains(song)) {
            songs.add(song);
        }
    }

    // Prints album details and the titles of all included songs
    public void displayAlbumDetails() {
        System.out.println("Album: " + title);
        System.out.println("Artist: " + artist);
        System.out.println("Genre: " + genre);
        System.out.println("Year: " + year);
        System.out.println("Songs:");
        for (Song song : songs) {
            System.out.println(" - " + song.getTitle());
        }
    }
}

