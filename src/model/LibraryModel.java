/**
 * LibraryModel.java
 *
 * Manages the user's personal music library, including songs, albums,
 * playlists, ratings, play counts, and recent activity.
 *
 * Author: Haobin Yan
 */

package model;

import java.util.*;
import java.util.stream.Collectors;

public class LibraryModel implements Iterable<Song> {
    private Map<String, Song> userSongs;               // User's songs (title → song)
    private Map<String, Album> userAlbums;             // User's albums (title → album)
    private Map<String, PlayList> playlists;           // Named playlists
    private Set<String> favoriteSongs;                 // Songs marked as favorites
    private Map<String, Integer> songRatings;          // Song ratings (title → rating)
    private Map<String, Integer> songPlayCounts;       // Song play counts
    private List<String> recentSongs;                  // Recently played or top played

    public LibraryModel() {
        userSongs = new HashMap<>();
        userAlbums = new HashMap<>();
        playlists = new HashMap<>();
        favoriteSongs = new HashSet<>();
        songRatings = new HashMap<>();
        songPlayCounts = new HashMap<>();
        recentSongs = new ArrayList<>();
    }

    // Add a song to the user's library
    public void addSong(Song song) {
        userSongs.put(song.getTitle(), song);
    }

    // Add an album and include all its songs in the library
    public void addAlbum(Album album) {
        userAlbums.put(album.getTitle(), album);
        for (Song song : album.getSongs()) {
            addSong(song);
        }
    }

    // Create a new empty playlist if it doesn't already exist
    public void createPlaylist(String name) {
        if (!playlists.containsKey(name)) {
            playlists.put(name, new PlayList(name));
        }
    }

    // Add a song to an existing playlist
    public void addSongToPlaylist(String playlistName, Song song) {
        if (playlists.containsKey(playlistName)) {
            playlists.get(playlistName).addSong(song);
        }
    }

    // Rate a song (only 1–5); mark as favorite if rating is 5
    public void rateSong(String songTitle, int rating) {
        if (userSongs.containsKey(songTitle) && rating >= 1 && rating <= 5) {
            songRatings.put(songTitle, rating);
            if (rating == 5) {
                favoriteSongs.add(songTitle);
            }
        }
    }

    // Simulate playing a song; update play count and recent songs
    public void playSong(String songTitle) {
        if (userSongs.containsKey(songTitle)) {
            songPlayCounts.put(songTitle, songPlayCounts.getOrDefault(songTitle, 0) + 1);
            recentSongs.add(0, songTitle); // add to front
            if (recentSongs.size() > 10) {
                recentSongs.remove(10); // keep only 10
            }
            updateMostFrequentlyPlayedSongs();
        }
    }

    // Recalculate the 10 most frequently played songs
    private void updateMostFrequentlyPlayedSongs() {
        List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(songPlayCounts.entrySet());
        sortedList.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        recentSongs.clear();
        for (int i = 0; i < Math.min(10, sortedList.size()); i++) {
            recentSongs.add(sortedList.get(i).getKey());
        }
    }

    // Shuffle the library and print songs in random order
    public void shuffleLibrary() {
        List<Song> songList = new ArrayList<>(userSongs.values());
        Collections.shuffle(songList);
        System.out.println("Shuffled Songs:");
        for (Song song : songList) {
            System.out.println(song);
        }
    }

    // Remove a song and all related metadata
    public void removeSong(String songTitle) {
        if (userSongs.containsKey(songTitle)) {
            userSongs.remove(songTitle);
            songRatings.remove(songTitle);
            favoriteSongs.remove(songTitle);
            songPlayCounts.remove(songTitle);
            System.out.println("Song '" + songTitle + "' removed from library.");
        }
    }

    // Remove an album and its songs from the library
    public void removeAlbum(String albumTitle) {
        if (userAlbums.containsKey(albumTitle)) {
            Album album = userAlbums.get(albumTitle);
            for (Song song : album.getSongs()) {
                removeSong(song.getTitle());
            }
            userAlbums.remove(albumTitle);
            System.out.println("Album '" + albumTitle + "' removed from library.");
        }
    }

    // Automatically create playlists for genres with ≥10 songs
    public void generateGenreBasedPlaylists() {
        Map<String, List<Song>> songsByGenre = new HashMap<>();
        for (Song song : userSongs.values()) {
            songsByGenre.computeIfAbsent(song.getGenre(), k -> new ArrayList<>()).add(song);
        }

        for (String genre : songsByGenre.keySet()) {
            List<Song> genreSongs = songsByGenre.get(genre);
            if (genreSongs.size() >= 10) {
                PlayList playlist = new PlayList(genre + " Playlist");
                for (Song song : genreSongs) {
                    playlist.addSong(song);
                }
                playlists.put(playlist.getName(), playlist);
                System.out.println("Created playlist for genre: " + genre);
            }
        }
    }

    // Automatically create a playlist of songs rated 4 or 5
    public void generateTopRatedPlaylist() {
        PlayList topRated = new PlayList("Top Rated Songs");
        for (String songTitle : songRatings.keySet()) {
            int rating = songRatings.get(songTitle);
            if (rating >= 4) {
                Song song = userSongs.get(songTitle);
                topRated.addSong(song);
            }
        }
        playlists.put(topRated.getName(), topRated);
        System.out.println("Top Rated playlist created with " + topRated.getSongs().size() + " songs.");
    }

    // Return songs sorted alphabetically by title
    public List<Song> sortByTitle() {
        return userSongs.values().stream()
                .sorted(Comparator.comparing(Song::getTitle))
                .collect(Collectors.toList());
    }

    // Return songs sorted alphabetically by artist
    public List<Song> sortByArtist() {
        return userSongs.values().stream()
                .sorted(Comparator.comparing(Song::getArtist))
                .collect(Collectors.toList());
    }

    // Return songs sorted by rating (highest first)
    public List<Song> sortByRating() {
        return userSongs.values().stream()
                .sorted(Comparator.comparingInt(Song::getRating).reversed())
                .collect(Collectors.toList());
    }

    // Find songs matching a specific genre
    public List<Song> searchSongsByGenre(String genre) {
        return userSongs.values().stream()
                .filter(song -> genre.equalsIgnoreCase(song.getGenre()))
                .collect(Collectors.toList());
    }

    // Find a song by its title
    public Song searchSongByTitle(String title) {
        return userSongs.getOrDefault(title, null);
    }

    // Allow iteration over all songs in the library
    @Override
    public Iterator<Song> iterator() {
        return userSongs.values().iterator();
    }

    // Return all songs (read-only)
    public Map<String, Song> getUserSongs() {
        return Collections.unmodifiableMap(userSongs);
    }

    // Return all albums (read-only)
    public Map<String, Album> getUserAlbums() {
        return Collections.unmodifiableMap(userAlbums);
    }

    // Return all playlists (read-only)
    public Map<String, PlayList> getPlaylists() {
        return Collections.unmodifiableMap(playlists);
    }

    // Return song ratings (read-only)
    public Map<String, Integer> getSongRatings() {
        return Collections.unmodifiableMap(songRatings);
    }

    // Return favorite songs (read-only)
    public Set<String> getFavoriteSongs() {
        return Collections.unmodifiableSet(favoriteSongs);
    }

    // Return list of recently played songs or top played (read-only)
    public List<String> getRecentSongs() {
        return Collections.unmodifiableList(recentSongs);
    }

    // Return same list as above (alias for top played)
    public List<String> getFrequentlyPlayedSongs() {
        return Collections.unmodifiableList(recentSongs);
    }

    // Print all playlists and their contents
    public void displayPlaylists() {
        System.out.println("Playlists:");
        for (PlayList playlist : playlists.values()) {
            playlist.displayPlayList();
        }
    }
}

