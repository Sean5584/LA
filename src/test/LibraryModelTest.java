package test;

import model.*;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LibraryModelTest {
	private LibraryModel library;

	@BeforeEach
	void setUp() {
		library = new LibraryModel();
	}
 
	@Test
	void testAddSong() {
		Song song = new Song("Test Song", "Test Artist", "Test Album", "Pop");
		library.addSong(song);
		assertTrue(library.getUserSongs().containsKey("Test Song"));
	}
 
	@Test
	void testAddDuplicateSong() {
		Song song = new Song("Duplicate Song", "Test Artist", "Test Album", "Pop");
		// Song song = new Song("Duplicate Song", "Test Artist", "Test Album");
		library.addSong(song);
		library.addSong(song);
		assertEquals(1, library.getUserSongs().size());
	}

	@Test
	void testAddNullSong() {
		assertThrows(NullPointerException.class, () -> library.addSong(null));
	}

	@Test
	void testAddAlbum() {
		Album album = new Album("Test Album", "Test Artist", "Pop", 2024,
				Arrays.asList(new Song("Song 1", "Test Artist", "Test Album", "Pop"),
						new Song("Song 2", "Test Artist", "Test Album", "Pop")));
		library.addAlbum(album);
		assertTrue(library.getUserAlbums().containsKey("Test Album"));
		assertEquals(2, library.getUserSongs().size());
	}

	@Test
	void testCreatePlaylist() {
		library.createPlaylist("My Playlist");
		assertTrue(library.getPlaylists().containsKey("My Playlist"));
	}

	@Test
	void testCreateDuplicatePlaylist() {
		library.createPlaylist("My Playlist");
		library.createPlaylist("My Playlist");
		assertEquals(1, library.getPlaylists().size());
	}

	@Test
	void testAddSongToPlaylist() {
		Song song = new Song("Playlist Song", "Test Artist", "Test Album", "Pop");

		// Song song = new Song("Playlist Song", "Test Artist", "Test Album");
		library.addSong(song);
		library.createPlaylist("Chill Vibes");
		library.addSongToPlaylist("Chill Vibes", song);
		assertEquals(1, library.getPlaylists().get("Chill Vibes").getSongs().size());
	}

	@Test
	void testAddSongToNonExistentPlaylist() {
		Song song = new Song("Lonely Song", "Test Artist", "Test Album", "Pop");
		library.addSong(song);
		library.addSongToPlaylist("Nonexistent Playlist", song);
		assertNull(library.getPlaylists().get("Nonexistent Playlist")); // 播放列表未创建
	}

	@Test
	void testRateSong() {
		Song song = new Song("Rated Song", "Test Artist", "Test Album", "Pop");
		library.addSong(song);
		library.rateSong("Rated Song", 5);
		assertEquals(5, library.getSongRatings().get("Rated Song"));
	}

	@Test
	void testRateSongWithInvalidValue() {
		Song song = new Song("Invalid Rated Song", "Test Artist", "Test Album", "Pop");
		library.addSong(song);
		library.rateSong("Invalid Rated Song", 6);
		assertNull(library.getSongRatings().get("Invalid Rated Song"));
	}

	@Test
	void testRateSongMinimumValue() {
		Song song = new Song("Min Rated Song", "Test Artist", "Test Album", "Pop");
		library.addSong(song);
		library.rateSong("Min Rated Song", 1);
		assertEquals(1, library.getSongRatings().get("Min Rated Song"));
	}

	@Test
	void testDisplayUserLibrary() {
		Song song1 = new Song("Song A", "Artist A", "Album A", "Pop");
		Song song2 = new Song("Song B", "Artist B", "Album B", "Rock");

		library.addSong(song1);
		library.addSong(song2);

		assertTrue(library.getUserSongs().containsKey("Song A"));
		assertTrue(library.getUserSongs().containsKey("Song B"));
		assertEquals(2, library.getUserSongs().size());

		library.getUserSongs().values().forEach(System.out::println);
	}

	@Test
	void testDisplayPlaylists() {
		library.createPlaylist("Road Trip");
		library.createPlaylist("Workout");
		library.displayPlaylists();
	}

	@Test
	void testPlaySongTracksRecentAndFrequency() {
		Song song = new Song("Play Me", "Artist", "Album", "Pop");
		library.addSong(song);
		library.playSong("Play Me");
		library.playSong("Play Me");

		List<String> recent = library.getRecentSongs();
		assertEquals("Play Me", recent.get(0));

		List<String> frequent = library.getFrequentlyPlayedSongs();
		assertEquals("Play Me", frequent.get(0));
	}

	@Test
	void testRemoveSong() {
		Song song = new Song("Delete Me", "Artist", "Album", "Pop");
		library.addSong(song);
		library.rateSong("Delete Me", 5);
		library.playSong("Delete Me");

		library.removeSong("Delete Me");

		assertFalse(library.getUserSongs().containsKey("Delete Me"));
		assertFalse(library.getFavoriteSongs().contains("Delete Me"));
		assertFalse(library.getSongRatings().containsKey("Delete Me"));
	}

	@Test
	void testRemoveAlbum() {
		Song song = new Song("A Song", "Artist", "Test Album", "Rock");
		Album album = new Album("Test Album", "Artist", "Rock", 2023, Arrays.asList(song));
		library.addAlbum(album);

		library.removeAlbum("Test Album");

		assertFalse(library.getUserAlbums().containsKey("Test Album"));
		assertFalse(library.getUserSongs().containsKey("A Song"));
	}

	@Test
	void testGenerateGenreBasedPlaylists() {
		for (int i = 0; i < 10; i++) {
			Song song = new Song("Rock Song " + i, "Band", "Rock Album", "Rock");
			library.addSong(song);
		}
		library.generateGenreBasedPlaylists();
		assertTrue(library.getPlaylists().containsKey("Rock Playlist"));
	}

	@Test
	void testGenerateTopRatedPlaylist() {
		Song good = new Song("Good Song", "Artist", "Album", "Jazz");
		library.addSong(good);
		library.rateSong("Good Song", 5);

		library.generateTopRatedPlaylist();
		PlayList topRated = library.getPlaylists().get("Top Rated Songs");
		assertNotNull(topRated);
		assertEquals(1, topRated.getSongs().size());
	}
 
	@Test
	void testSortByTitle() {
		library.addSong(new Song("B", "A", "Album", "Pop"));
		library.addSong(new Song("A", "B", "Album", "Pop"));

		List<Song> sorted = library.sortByTitle();
		assertEquals("A", sorted.get(0).getTitle());
	}

	@Test
	void testSortByArtist() {
		library.addSong(new Song("Title1", "Zebra", "Album", "Pop"));
		library.addSong(new Song("Title2", "Alpha", "Album", "Pop"));

		List<Song> sorted = library.sortByArtist();
		assertEquals("Alpha", sorted.get(0).getArtist());
	}

	@Test
	void testSortByRating() {
		Song low = new Song("Low", "Artist", "Album", "Pop", 1);
		Song high = new Song("High", "Artist", "Album", "Pop", 5);
		library.addSong(low);
		library.addSong(high);

		List<Song> sorted = library.sortByRating();
		assertEquals("High", sorted.get(0).getTitle());
	}

	@Test
	void testSearchByGenre() {
		library.addSong(new Song("Classical One", "Mozart", "Classics", "Classical"));
		List<Song> result = library.searchSongsByGenre("Classical");
		assertEquals(1, result.size());
	}

	@Test
	void testSearchByTitle() {
		Song song = new Song("Search Me", "Artist", "Album", "Pop");
		library.addSong(song);
		Song found = library.searchSongByTitle("Search Me");
		assertNotNull(found);
	}

}
