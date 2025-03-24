/**
 * UserManager.java
 *
 * Handles user registration, authentication, and library data storage.
 * Uses SHA-256 password hashing and saves user data in text files under /resources.
 *
 * Author: Haobin Yan
 */

package model;

import java.io.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class UserManager {
    private static final String USER_DATA_FILE = "resources/users.txt"; // Stores username,passwordHash
    private Map<String, String> users; // Map of usernames to hashed passwords

    // Constructor: loads existing users from file
    public UserManager() {
        users = new HashMap<>();
        loadUsers();
    }

    // Load users from file into memory
    private void loadUsers() {
        try {
            File file = new File(USER_DATA_FILE);
            if (!file.exists()) {
                file.createNewFile(); // Create if missing
            }
            List<String> lines = Files.readAllLines(Paths.get(USER_DATA_FILE), StandardCharsets.UTF_8);
            for (String line : lines) {
                String[] data = line.split(",");
                if (data.length == 2) {
                    users.put(data[0], data[1]); // username → password hash
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Save current users back to file
    private void saveUsers() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(USER_DATA_FILE));
            for (Map.Entry<String, String> entry : users.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Register a new user with hashed password
    public boolean registerUser(String username, String password) {
        if (users.containsKey(username)) {
            return false; // Username already exists
        }
        users.put(username, hashPassword(password));
        saveUsers(); // Persist change
        return true;
    }

    // Authenticate login using SHA-256 hashed password
    public boolean authenticateUser(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(hashPassword(password));
    }

    // Hash password using SHA-256
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    // Save the user's library (songs + albums) to a text file
    public void saveUserLibrary(String username, LibraryModel library) {
        try {
            File userLibraryFile = new File("resources/users/" + username + "_library.txt");
            if (!userLibraryFile.exists()) {
                userLibraryFile.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(userLibraryFile));

            // Save songs
            for (Song song : library.getUserSongs().values()) {
                writer.write(song.getTitle() + "," + song.getArtist() + "," + song.getAlbum() + "," + song.getGenre());
                writer.newLine();
            }

            // Save albums (only title and artist for now)
            for (Album album : library.getUserAlbums().values()) {
                writer.write("Album: " + album.getTitle() + "," + album.getArtist());
                writer.newLine();
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load user's saved library into the current session
    public void loadUserLibrary(String username, LibraryModel library) {
        try {
            File userLibraryFile = new File("resources/users/" + username + "_library.txt");
            if (userLibraryFile.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(userLibraryFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data[0].equals("Album")) {
                        // Reconstruct album (simplified)
                        String albumTitle = data[1];
                        String artist = data[2];
                        Album album = new Album(albumTitle, artist, "", 0, new ArrayList<>());
                        library.addAlbum(album);
                    } else {
                        // Reconstruct song
                        String title = data[0];
                        String artist = data[1];
                        String album = data[2];
                        String genre = data[3];
                        Song song = new Song(title, artist, album, genre);
                        library.addSong(song);
                    }
                }
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

