package test;

import model.*;
import org.junit.jupiter.api.*;

import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MusicStoreTest {
    private MusicStore musicStore;
    private static final String TEST_ALBUMS_FILE = "resources/albums/albums.txt";
 
    @BeforeEach
    void setUp() {
        musicStore = new MusicStore(TEST_ALBUMS_FILE);
    }

    @Test 
    void testGetAlbum() {
        Album album = musicStore.getAlbum("Old Ideas");
        assertNotNull(album, "Album should exist");
        assertEquals("Leonard Cohen", album.getArtist(), "Artist should be Leonard Cohen");
    } 

    @Test
    void testGetAlbumNotFound() {
        Album album = musicStore.getAlbum("Nonexistent Album");
        assertNull(album, "Album should not exist");
    }

    @Test
    void testGetSongsByArtist() {
        List<Song> songs = musicStore.getSongsByArtist("Leonard Cohen");
        assertNotNull(songs, "Songs list should not be null");
        assertFalse(songs.isEmpty(), "Songs list should not be empty");
    }

    @Test
    void testGetSongsByUnknownArtist() {
        List<Song> songs = musicStore.getSongsByArtist("Unknown Artist");
        assertNotNull(songs, "Songs list should not be null");
        assertTrue(songs.isEmpty(), "Songs list should be empty");
    }

    @Test
    void testDisplayAllAlbums() {
        musicStore.displayAllAlbums();
        
    }

    @Test
    void testLoadAlbums_EmptyFile() throws IOException {
        String emptyFile = "resources/empty_albums.txt";
        createEmptyFile(emptyFile);
        MusicStore emptyStore = new MusicStore(emptyFile);
        assertTrue(emptyStore.getAllAlbums().isEmpty(), "Albums list should be empty");
    }

    @Test
    void testLoadAlbums_InvalidFormat() throws IOException {
        String invalidFile = "resources/invalid_albums.txt";
        createFileWithContent(invalidFile, "InvalidDataWithoutComma\n");
        MusicStore invalidStore = new MusicStore(invalidFile);
        assertTrue(invalidStore.getAllAlbums().isEmpty(), "Albums list should be empty due to format error");
    }

    @Test
    void testReadAlbumFromFile_FileNotFound() {
        Album album = musicStore.getAlbum("Fake Album");
        assertNull(album, "Album should not exist");
    }

    @Test
    void testReadAlbumFromFile_InvalidYearFormat() throws IOException {
        String invalidYearFile = "resources/invalid_year_album.txt";
        createFileWithContent(invalidYearFile, "Test Album,Test Artist,Pop,NotAYear\nSong A\nSong B\n");
        MusicStore storeWithInvalidYear = new MusicStore(invalidYearFile);
        assertTrue(storeWithInvalidYear.getAllAlbums().isEmpty(), "Albums should not be loaded if year is invalid");
    }

    /**
     *Make a empty file
     */
    private void createEmptyFile(String filePath) throws IOException {
        File file = new File(filePath);
        file.getParentFile().mkdirs(); 
        new FileWriter(file).close(); 
    }

  
    private void createFileWithContent(String filePath, String content) throws IOException {
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
    }
}
