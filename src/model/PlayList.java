/**
 * PlayList.java
 *
 * Represents a user-created or system-generated playlist.
 * A playlist has a name and a list of songs, and supports adding, removing,
 * shuffling, and iterating through songs.
 *
 * Author:Haobin Yan
 */

package model;

import java.util.*;

public class PlayList implements Iterable<Song> {
    private String name;           // Name of the playlist
    private List<Song> songs;      // List of songs in the playlist

    // Constructor: creates a playlist with the given name
    public PlayList(String name) {
        this.name = name;
        this.songs = new ArrayList<>();
    }

    // Returns the name of the playlist
    public String getName() {
        return name;
    }

    // Adds a song if it's not already in the playlist
    public void addSong(Song song) {
        if (!songs.contains(song)) {
            songs.add(song);
        }
    }

    // Removes the song from the playlist
    public void removeSong(Song song) {
        songs.remove(song);
    }

    // Returns a copy of the song list
    public List<Song> getSongs() {
        return new ArrayList<>(songs);
    }

    // Shuffles the order of songs in the playlist
    public void shuffle() {
        Collections.shuffle(songs);
    }

    // Displays the playlist name and its songs to the console
    public void displayPlayList() {
        System.out.println("Playlist: " + name);
        for (Song song : songs) {
            System.out.println(" - " + song);
        }
    }

    // Allows iteration over the songs using for-each
    @Override
    public Iterator<Song> iterator() {
        return songs.iterator();
    }
}

