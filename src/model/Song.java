/**
 * Song.java
 *
 * Represents a single song with metadata such as title, artist, album, genre, and rating.
 * Used as a core data type throughout the user's music library and playlists.
 *
 * Author: Haobin Yan
 */

package model;

public class Song {
    private String title;     // Song title
    private String artist;    // Artist name
    private String album;     // Album the song belongs to
    private String genre;     // Genre of the song
    private int rating;       // User rating (1–5)

    // Full constructor with all metadata including rating
    public Song(String title, String artist, String album, String genre, int rating) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.genre = genre;
        this.rating = rating;
    }

    // Constructor 
    public Song(String title, String artist, String album, String genre) {
        this(title, artist, album, genre, 3);
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getGenre() {
        return genre;
    }

    public int getRating() {
        return rating;
    }

    // Allows updating the rating later
    public void setRating(int rating) {
        this.rating = rating;
    }

    // String representation of the song for display
    @Override
    public String toString() {
        return title + " - " + artist + " (" + album + ", Genre: " + genre + ", Rating: " + rating + ")";
    }
}

