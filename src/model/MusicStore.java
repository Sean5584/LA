/**
 * MusicStore.java
 *
 * Loads and manages a shared music database of albums and songs.
 * This class is part of the static "store" (not user-specific) in the Music Library system.
 *
 * Author: Haobin yan
 */

package model;

import java.io.*; 
import java.util.*;

public class MusicStore {
    private Map<String, Album> albumsByTitle;         // All albums indexed by title
    private Map<String, List<Song>> songsByArtist;    // Songs grouped by artist
    private static final String ALBUMS_DIRECTORY = "resources/albums/"; // Folder for album .txt files

    // Constructor: loads albums from the given file path
    public MusicStore(String albumsFilePath) {
        albumsByTitle = new HashMap<>();
        songsByArtist = new HashMap<>();
        loadAlbums(albumsFilePath);
    } 

    // Loads album metadata from a CSV-like file (e.g., albums.txt)
    private void loadAlbums(String albumsFilePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(albumsFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 2) continue; // Skip invalid lines

                String albumTitle = parts[0].trim();
                String artist = parts[1].trim();
                String albumFileName = albumTitle + "_" + artist + ".txt";

                // Load full album info from its dedicated .txt file
                Album album = readAlbumFromFile(ALBUMS_DIRECTORY + albumFileName);
                if (album != null) {
                    albumsByTitle.put(albumTitle, album);
                    songsByArtist.putIfAbsent(artist, new ArrayList<>());
                    songsByArtist.get(artist).addAll(album.getSongs());
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading albums: " + e.getMessage());
        }
    }

    // Reads a single album's full metadata and song list from its .txt file
    private Album readAlbumFromFile(String albumFilePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(albumFilePath))) {
            String header = br.readLine();
            if (header == null) return null;

            String[] metadata = header.split(",");
            if (metadata.length < 4) return null;

            String albumTitle = metadata[0].trim();
            String artist = metadata[1].trim();
            String genre = metadata[2].trim();
            int year = Integer.parseInt(metadata[3].trim());

            List<Song> songs = new ArrayList<>();
            String songTitle;
            while ((songTitle = br.readLine()) != null) {
                songs.add(new Song(songTitle.trim(), artist, albumTitle, genre));
            }

            return new Album(albumTitle, artist, genre, year, songs);
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error reading album file: " + albumFilePath);
            return null;
        }
    }

    // Get a specific album by its title
    public Album getAlbum(String title) {
        return albumsByTitle.getOrDefault(title, null);
    }

    // Get all songs by a specific artist
    public List<Song> getSongsByArtist(String artist) {
        return songsByArtist.getOrDefault(artist, new ArrayList<>());
    }

    // Print all album titles and artists
    public void displayAllAlbums() {
        for (String title : albumsByTitle.keySet()) {
            System.out.println(title + " by " + albumsByTitle.get(title).getArtist());
        }
    }

    // Return all albums as a collection
    public Collection<Album> getAllAlbums() {
        return albumsByTitle.values();
    }
}

