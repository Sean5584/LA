package view;

import model.LibraryModel;
import model.MusicStore;
import model.Song;
import model.PlayList;

import java.util.Scanner;

public class LibraryView {
    private LibraryModel library;        // Reference to the user's personal library
    private MusicStore musicStore;       // Reference to the shared music store

    public LibraryView(LibraryModel library, MusicStore musicStore) {
        this.library = library;
        this.musicStore = musicStore;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            // Display menu options
            System.out.println("\nMusic Library Menu:");
            System.out.println("1. View Library");
            System.out.println("2. Add Song to Library");
            System.out.println("3. View Most Played Songs");
            System.out.println("4. Create Genre-Based Playlist");
            System.out.println("5. View Top-Rated Songs");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            // Handle user choice
            switch (choice) {
                case 1:
                    displayUserLibrary();         // Show user's library
                    break;
                case 2:
                    addSongToLibrary(scanner);    // Let user add a new song
                    break;
                case 3:
                    displayMostPlayedSongs();     // Show most frequently played songs
                    break;
                case 4:
                    createGenrePlaylist(scanner); // Generate a playlist by genre
                    break;
                case 5:
                    displayTopRatedSongs();       // Show favorite (top-rated) songs
                    break;
                case 6:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
    }

    // Print all songs currently in the user's library
    private void displayUserLibrary() {
        System.out.println("\nYour Library:");
        for (Song song : library.getUserSongs().values()) {
            System.out.println(song);
        }
    }

    // Let user input song info and add it to their library
    private void addSongToLibrary(Scanner scanner) {
        System.out.print("Enter song title: ");
        String songTitle = scanner.nextLine();
        System.out.print("Enter artist name: ");
        String artist = scanner.nextLine();
        System.out.print("Enter album name: ");
        String albumTitle = scanner.nextLine();
        System.out.print("Enter genre: ");
        String genre = scanner.nextLine();

        Song newSong = new Song(songTitle, artist, albumTitle, genre);
        library.addSong(newSong);
        System.out.println("Song added to your library!");
    }

    // Print most frequently played songs (by title)
    private void displayMostPlayedSongs() {
        System.out.println("\nMost Played Songs:");
        for (String songTitle : library.getFrequentlyPlayedSongs()) {
            System.out.println(songTitle);
        }
    }

    // Prompt for a genre and create a genre-based playlist
    private void createGenrePlaylist(Scanner scanner) {
        System.out.print("Enter genre for the playlist: ");
        String genre = scanner.nextLine();
        library.generateGenreBasedPlaylists();
        System.out.println("Playlist created for genre: " + genre);
    }

    // Print songs that are rated 5 (favorite songs)
    private void displayTopRatedSongs() {
        System.out.println("\nTop-Rated Songs:");
        for (String songTitle : library.getFavoriteSongs()) {
            System.out.println(songTitle);
        }
    }
}

