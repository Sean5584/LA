/**
 * User.java
 *
 * Represents a user in the music library system. Each user has a personal library
 * of songs and albums, keeps track of recent plays, favorites, top-rated songs,
 * and genre-based playlists.
 *
 * Author: Haobin Yan
 */

package model;

import java.util.*;
import java.util.stream.Collectors;

public class User {
    private String username;                       // User's username
    private String passwordHash;                   // User's hashed password
    private List<Song> library;                    // All songs added by the user
    private List<Album> albums;                    // Albums formed from added songs
    private List<Song> recentPlays;                // Recently played songs (max 10)
    private Map<Song, Integer> playCountMap;       // Song play counts
    private List<Song> favoriteSongs;              // Favorite songs (manually marked)
    private List<Song> topRatedSongs;              // Top played songs (auto-updated)
    private Map<String, List<Song>> genrePlaylists;// Genre-based auto playlists

    // Constructor: initializes a new user with empty data structures
    public User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.library = new ArrayList<>();
        this.albums = new ArrayList<>();
        this.recentPlays = new LinkedList<>();
        this.playCountMap = new HashMap<>();
        this.favoriteSongs = new ArrayList<>();
        this.topRatedSongs = new ArrayList<>();
        this.genrePlaylists = new HashMap<>();
    }

    public String getUsername() {
        return username;
    }

    public List<Song> getLibrary() {
        return new ArrayList<>(library);
    }

    public List<Song> getRecentPlays() {
        return new ArrayList<>(recentPlays);
    }

    public List<Song> getFavoriteSongs() {
        return new ArrayList<>(favoriteSongs);
    }

    public List<Song> getTopRatedSongs() {
        return new ArrayList<>(topRatedSongs);
    }

    public Map<String, List<Song>> getGenrePlaylists() {
        return genrePlaylists;
    }

    // Add a song to the user's library if it's not already present
    public void addSongToLibrary(Song song) {
        if (!library.contains(song)) {
            library.add(song);
        }
    }

    // Add a song and automatically handle album grouping
    public void addSongWithAlbum(Song song) {
        addSongToLibrary(song);
        boolean albumExists = false;
        for (Album album : albums) {
            if (album.getTitle().equalsIgnoreCase(song.getAlbum())) {
                album.addSong(song);
                albumExists = true;
                break;
            }
        }
        if (!albumExists) {
            Album newAlbum = new Album(song.getAlbum(), song.getArtist(), song.getGenre());
            newAlbum.addSong(song);
            albums.add(newAlbum);
        }
    }

    // Check if an album is in the user's library
    public boolean isAlbumInLibrary(String albumTitle) {
        for (Album album : albums) {
            if (album.getTitle().equalsIgnoreCase(albumTitle)) {
                return true;
            }
        }
        return false;
    }

    // Find all songs in the library that match a genre
    public List<Song> searchSongsByGenre(String genre) {
        return library.stream()
                .filter(song -> genre.equalsIgnoreCase(song.getGenre()))
                .collect(Collectors.toList());
    }

    // Simulate playing a song: record in recent plays and update play count
    public void playSong(Song song) {
        if (!library.contains(song)) return;

        recentPlays.add(0, song); // Add to top
        if (recentPlays.size() > 10) {
            recentPlays.remove(10); // Keep most recent 10
        }

        playCountMap.put(song, playCountMap.getOrDefault(song, 0) + 1);
        updateTopPlayedSongs();
    }

    // Update topRatedSongs list based on play counts
    private void updateTopPlayedSongs() {
        List<Map.Entry<Song, Integer>> sorted = new ArrayList<>(playCountMap.entrySet());
        sorted.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        topRatedSongs.clear();
        for (int i = 0; i < Math.min(10, sorted.size()); i++) {
            topRatedSongs.add(sorted.get(i).getKey());
        }
    }

    // Mark a song as favorite manually
    public void markSongAsFavorite(Song song) {
        if (!favoriteSongs.contains(song)) {
            favoriteSongs.add(song);
        }
    }

    // Mark a song as top rated manually (if needed)
    public void markSongAsTopRated(Song song) {
        if (!topRatedSongs.contains(song)) {
            topRatedSongs.add(song);
        }
    }

    // Generate a playlist of songs of a given genre
    public void createGenrePlaylist(String genre) {
        genrePlaylists.putIfAbsent(genre, new ArrayList<>());
        for (Song song : library) {
            if (song.getGenre().equalsIgnoreCase(genre)) {
                genrePlaylists.get(genre).add(song);
            }
        }
    }

    // Shuffle the user's song library
    public void shuffleLibrary() {
        Collections.shuffle(library);
    }

    // Check if the user library contains a specific song
    public boolean hasSong(Song song) {
        return library.contains(song);
    }
}

