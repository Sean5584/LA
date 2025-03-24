package main;

import java.io.*;
import java.util.Scanner;
import view.LibraryView;

import model.*;

public class Main {
    private static UserManager userManager = new UserManager();
    private static User currentUser;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Music store setup
        //test
        String albumsFilePath = "resources/albums/albums.txt"; // Ensure this file exists
        MusicStore musicStore = new MusicStore(albumsFilePath);
        LibraryModel library = new LibraryModel();
        LibraryView view = new LibraryView(library, musicStore);
        System.out.println("Looking for albums at: " + new File(albumsFilePath).getAbsolutePath());

        // Login or Registration Menu
        while (true) {
            System.out.println("\nWelcome to the Music Library!");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    register(scanner);
                    break;
                case 2:
                    login(scanner);
                    break;
                case 3:
                    System.out.println("Exiting... Goodbye!");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private static void register(Scanner scanner) {
        System.out.print("Enter a username: ");
        String username = scanner.nextLine();
        System.out.print("Enter a password: ");
        String password = scanner.nextLine();

        if (userManager.registerUser(username, password)) {
            System.out.println("Registration successful! You can now log in.");
        } else {
            System.out.println("Username already exists. Try again.");
        }
    }

    private static void login(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (userManager.authenticateUser(username, password)) {
            System.out.println("Login successful! Welcome, " + username);
            currentUser = new User(username, password);
            loadUserLibrary(username);
            userMenu(scanner);
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    private static void loadUserLibrary(String username) {
        // Load user-specific library data from a file if it exists
        String filePath = "resources/users/" + username + "_library.txt"; // File path for the user's library
        File userLibraryFile = new File(filePath);

        if (userLibraryFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(userLibraryFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // Each line is "title, artist, album, genre"
                    String[] parts = line.split(",");
                    if (parts.length == 4) {
                        String title = parts[0].trim();
                        String artist = parts[1].trim();
                        String album = parts[2].trim();
                        String genre = parts[3].trim();

                        Song song = new Song(title, artist, album, genre);
                        currentUser.addSongToLibrary(song);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No previous library found for " + username + ".");
        }
    }

    private static void saveUserLibrary(String username) {
        // Save the current user's library to a file
        String filePath = "resources/users/" + username + "_library.txt"; // File path for saving the user's library
        File userLibraryFile = new File(filePath);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userLibraryFile))) {
            for (Song song : currentUser.getLibrary()) {
                writer.write(song.getTitle() + "," + song.getArtist() + "," + song.getAlbum() + "," + song.getGenre());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void userMenu(Scanner scanner) {
        while (true) {
            System.out.println("\nUser Menu:");
            System.out.println("1. View Library");
            System.out.println("2. Add Song to Library");
            System.out.println("3. Logout");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    System.out.println("Your Library:");
                    for (Song song : currentUser.getLibrary()) {
                        System.out.println(song);
                    }
                    break;
                case 2:
                    addSongToLibrary(scanner);
                    break;
                case 3:
                    System.out.println("Logging out...");
                    saveUserLibrary(currentUser.getUsername()); // Save the user's library before logout
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private static void addSongToLibrary(Scanner scanner) {
        System.out.print("Enter song title: ");
        String title = scanner.nextLine();
        System.out.print("Enter artist name: ");
        String artist = scanner.nextLine();
        System.out.print("Enter album name: ");
        String album = scanner.nextLine();
        System.out.print("Enter genre: ");
        String genre = scanner.nextLine();

        Song newSong = new Song(title, artist, album, genre);
        currentUser.addSongToLibrary(newSong);
        System.out.println("Song added to your library!");
    }
}
