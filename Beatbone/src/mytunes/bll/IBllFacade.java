package mytunes.bll;

import java.util.List;
import mytunes.be.Playlist;
import mytunes.be.Song;
import mytunes.be.User;

/**
 * The {@code IBllFacade} interface is a facade for Business Logic Layer.
 * It is a connector between GUI and DAL and it consists of methods 
 * that are needed for all database operations.
 * 
 * @author schemabuoi
 * @author kiddo
 */
public interface IBllFacade {
    
    /**
     * Creates a song in database.
     * 
     * @param user The songs user.
     * @param title The songs title.
     * @param artist The songs artist.
     * @param genre The name of genre of song.
     * @param path The path to file with song.
     * @param time The time of the song.
     * @return Created song.
     */
    Song createSong(User user, String title, String artist, String genre, String path, int time);
    
    /**
     * Updates the song in database.
     * 
     * @param song The song to update.
     * @param newTitle The new title for song.
     * @param newArtist The new artist for song.
     * @param newGenre The new genre for song.
     * @return Updated song.
     */
    Song updateSong(Song song, String newTitle, String newArtist, String newGenre);
    
    /**
     * Deletes song from database.
     * 
     * @param song The song to delete.
     */
    void deleteSong(Song song);
    
    /**
     * Gets all songs from database for given user.
     * 
     * @param user The songs user.
     * @return List with songs.
     */
    List<Song> getAllSongs(User user);
    
    /**
     * Creates a Playlist in the database for a given user.
     * 
     * @param user The playlist user.
     * @param name The name of the playlist.
     * @return Created playlist.
     */
    Playlist createPlaylist(User user, String name);
    
    /**
     * Updates the name of the playlist in the database.
     * 
     * @param playlist The playlist to update.
     * @param newName The new name for the playlist.
     * @return Updated playlist.
     */
    Playlist updatePlaylist(Playlist playlist, String newName);
    
    /**
     * Deletes playlist from the database.
     * 
     * @param playlist The playlist to delete.
     */
    void deletePlaylist(Playlist playlist);
    
    /**
     * Gets a list with all playlists from the database for given user. All playlists
     * consist of all songs that appears on them.
     * 
     * @param user The playlists user.
     * @return List with playlists.
     */
    List<Playlist> getAllPlaylists(User user);
    
    /**
     * Switches the places of songs in database.
     * 
     * @param playlist The playlist with songs to switch.
     * @param firstSong The first song to switch.
     * @param secondSong The second song to switch.
     * @return Updated playlist with switched positions of songs.
     */
    Playlist switchSongsPlacesOnPlaylist(Playlist playlist, Song firstSong, Song secondSong);
    
    /**
     * Deletes song from given playlist in database.
     * 
     * @param playlist The playlist with song to delete.
     * @param song The song to delete.
     */
    void deleteSongFromPlaylist(Playlist playlist, Song song);
    
    /**
     * Adds a song to the playlist in database.
     * 
     * @param playlist The playlist for song.
     * @param song The song to add.
     * @return Updated playlist with added song
     */
    Playlist addSongToPlaylist(Playlist playlist, Song song);
    
    /**
     * Gets a list with names of all genres from the database.
     * 
     * @return List with genres.
     */
    List<String> getAllGenres();
    
    /**
     * Creates user in database.
     * 
     * @param email The users e-mail address.
     * @param password The users password.
     * @return Created user.
     */
    User createUser(String email, String password);
    
    /**
     * Gets the user with given e-mail address and password from database.
     * 
     * @param email The users e-mail address.
     * @param password The users password.
     * @return The user.
     */
    User getUser(String email, String password);
    
}
