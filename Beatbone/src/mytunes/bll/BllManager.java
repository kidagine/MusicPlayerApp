/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.bll;

import java.util.List;
import mytunes.be.Playlist;
import mytunes.be.Song;
import mytunes.be.User;
import mytunes.dal.DalController;
import mytunes.bll.IBllFacade;
import mytunes.dal.IDalFacade;

/**
 * The {@code BllManager} class is responsible for
 * connection between GUI and DAL. It is implementing IDalFacade
 * interface.
 * 
 * @author schemabuoi
 * @author kiddo
 */
public class BllManager implements IBllFacade{
    
    private IDalFacade dalController;
    
    /**
     * Creates connection with DAL.
     */
    public BllManager()
    {
        dalController = new DalController();
    }

    @Override
    public Song createSong(User user, String title, String artist, String genre, String path, int time) {
        return dalController.createSong(user, title, artist, genre, path, time);
    }

    @Override
    public Song updateSong(Song song, String newTitle, String newArtist, String newGenre) {
        return dalController.updateSong(song, newTitle, newArtist, newGenre);
    }

    @Override
    public void deleteSong(Song song) {
        dalController.deleteSong(song);
    }

    @Override
    public List<Song> getAllSongs(User user) {
        return dalController.getAllSongs(user);
    }

    @Override
    public Playlist createPlaylist(User user, String name) {
        return dalController.createPlaylist(user, name);
    }

    @Override
    public Playlist updatePlaylist(Playlist playlist, String newName) {
        return dalController.updatePlaylist(playlist, newName);
    }

    @Override
    public void deletePlaylist(Playlist playlist) {
        dalController.deletePlaylist(playlist);
    }

    @Override
    public List<Playlist> getAllPlaylists(User user) {
        return dalController.getAllPlaylists(user);
    }

    @Override
    public Playlist switchSongsPlacesOnPlaylist(Playlist playlist, Song firstSong, Song secondSong)
    {
        return dalController.switchSongsPlacesOnPlaylist(playlist, firstSong, secondSong);
    }

    @Override
    public void deleteSongFromPlaylist(Playlist playlist, Song song) {
        dalController.deleteSongFromPlaylist(playlist, song);
    }

    @Override
    public Playlist addSongToPlaylist(Playlist playlist, Song song) {
        return dalController.addSongToPlaylist(playlist, song);
    }
    
    @Override
    public List<String> getAllGenres() {
        return dalController.getAllGenres();
    }
    
    @Override
    public User createUser(String email, String password) {
        return dalController.createUser(email,password);
    }
    
    @Override
    public User getUser(String email, String password) {
        return dalController.getUser(email, password);
    }
    
}
