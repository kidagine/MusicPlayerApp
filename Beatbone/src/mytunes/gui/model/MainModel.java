/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mytunes.gui.model;

import java.util.Collections;
import java.util.Stack;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mytunes.be.Playlist;
import mytunes.be.Song;
import mytunes.be.User;
import mytunes.bll.BllManager;
import mytunes.bll.util.MusicSearcher;
import mytunes.bll.util.TimeConverter;
import mytunes.gui.PlayingMode;
import mytunes.bll.IBllFacade;
import mytunes.bll.util.SongChooser;

/**
 * The {@code MainModel} class is responsible for 
 * getting and passing all informations about playlist and songs
 * to Business Logic Layer. It is using singleton design pattern. 
 * Its main responsibility is also storing temporary state of data with all 
 * songs and all playlists and list with songs on currently selected playlist on view.
 * It also consist of all the informations that are reflecting state of main view
 * in application.
 * 
 * @author schemabuoi
 * @author kiddo
 */

public class MainModel {
    
    private static MainModel instance;
    private ObservableList<Song> songList;
    private ObservableList<Playlist> playlists;
    private ObservableList<Song> playlistSongs;
    private static User loggedUser;
    private PlayingMode mode;
    private Song currentlyPlayingSong;
    private Playlist currentlyPlayingPlaylist;
    private boolean shuffle;
    private Stack<Song> previouslyPlayedRandomSongs;
    private IBllFacade bllManager;
    private SongChooser songChooser;
       
    /**
     * Creates a connection with BLL, fetches data and sets initial values.
     */
    private MainModel()
    {
        //instantiate BLL classes.
        bllManager = new BllManager();
        songChooser = new SongChooser();
        
        //load data
        songList = FXCollections.observableArrayList(bllManager.getAllSongs(loggedUser));
        playlists = FXCollections.observableArrayList(bllManager.getAllPlaylists(loggedUser));
        playlistSongs = FXCollections.observableArrayList();
        
        //set initial values
        shuffle = false;  
        previouslyPlayedRandomSongs = new Stack();
        
    }
    
    /**
     * Sets the logged in user.
     * 
     * @param user The user.
     */
    public static void setUser(User user)
    {
        loggedUser = user;
    }
    
    /**
     * Returns single instance of MainModel class.
     * 
     * @return The instance of MainModel class.
     */
    public static MainModel createInstance()
    {
        if(instance == null)
        {
            instance = new MainModel();
        }
        return instance;
    }
    
    /**
     * Returns list with all songs.
     * 
     * @return The list of songs.
     */
    public ObservableList<Song> getSongs()
    {
        return songList;
    }
    
    /**
     * Deletes song from the list of songs.
     * It makes a request for BLL to delete song 
     * from database and deletes song from 
     * temporary state of data. It is also deleting
     * song from all playlists that containt this song.
     * 
     * @param song The song to delete.
     */
    public void deleteSong(Song song)
    {
        bllManager.deleteSong(song);
        deleteSongFromAllPlaylists(song);
        songList.remove(song);
    }
    
    /**
     * Creates song.
     * It makes a request for BLL to create song
     * in database and then adds created song that BLL returns
     * to the list of all songs.
     * 
     * @param title The songs title.
     * @param artist The songs artist.
     * @param genre The genre of song.
     * @param path The path of song.
     * @param time The time of song.
     */
    public void createSong(String title, String artist, String genre, String path, int time)
    {
        Song song = bllManager.createSong(loggedUser, title, artist, genre, path, time);
        songList.add(song);
    }
    
    /**
     * Updates song.
     * It makes a request for BLL to update song
     * in database and then updates the song that BLL returns
     * in temporary state of data.
     * 
     * @param song The song to update.
     * @param newTitle The new title for song.
     * @param newArtist The new artist for song.
     * @param newGenre The new genre for song.
     */
    public void updateSong(Song song, String newTitle, String newArtist, String newGenre)
    {
        updateSongOnAllPlaylists(song, newTitle, newArtist, newGenre);
        Song updatedSong = bllManager.updateSong(song, newTitle, newArtist, newGenre);
        updateListOfSongs(updatedSong);
    }
    
    /**
     * Updates the given song in list with
     * temporary state of all songs.
     * 
     * @param song The song to update.
     */
    private void updateListOfSongs(Song song)
    {
        int index = songList.indexOf(song);
        songList.set(index, song);
    }
    
    /**
     * Returns the list with all playlists.
     * 
     * @return The list of playlists.
     */
    public ObservableList<Playlist> getPlaylists()
    {
        return playlists;
    }
    
    /**
     * Deletes playlist from the list of all playlists.
     * It makes a request for BLL to delete playlist 
     * from database and deletes playlist from 
     * temporary state of data.
     * 
     * @param playlist The playlist to delete.
     */
    public void deletePlaylist(Playlist playlist)
    {
        bllManager.deletePlaylist(playlist);
        playlists.remove(playlist);
    }
    
    /**
     * Creates playlist.
     * It makes a request for BLL to create playlist
     * in database and then adds created playlist that BLL returns
     * to the list of all playlists..
     * 
     * @param name The name of playlist.
     */
    public void createPlaylist(String name)
    {
        Playlist playlist = bllManager.createPlaylist(loggedUser, name);
        playlists.add(playlist);
    }
    
    /**
     * Updates playlist.
     * It makes a request for BLL to update playlist
     * in database and then updates playlist that BLL returns
     * in temporary state of data.
     * 
     * @param playlist The playlist to update.
     * @param newName The new name or playlist.
     */
    public void updatePlaylist(Playlist playlist, String newName)
    {
        Playlist updatedPlaylist = bllManager.updatePlaylist(playlist, newName);
        updateListOfPlaylists(playlist);
    }

    /**
     * Moves song up on the playlist.
     * It makes a request for BLL to move song up
     * in database and then moves this song in temporary state of data.
     * 
     * @param playlist The playlist with given song.
     * @param song The song to move.
     */
    public void moveSongUpOnPlaylist(Playlist playlist, Song song)
    {
        int id = playlistSongs.indexOf(song);
        if(id!=0)
        {
            Song secondSong = playlistSongs.get(id-1);
            bllManager.switchSongsPlacesOnPlaylist(playlist, song, secondSong);
            Collections.swap(playlistSongs, id, id-1);
            Collections.swap(playlist.getTracklist(), id, id-1);
        }
    }
    
    /**
     * Moves song down on the playlist.
     * It makes a request for BLL to move song down
     * in database and then moves this song in temporary state of data.
     * 
     * @param playlist The playlist with given song.
     * @param song The song to move.
     */
    public void moveSongDownOnPlaylist(Playlist playlist, Song song)
    {
        int id = playlistSongs.indexOf(song);
        if(id!= playlist.getNumberOfSongs()-1)
        {
            Song secondSong = playlistSongs.get(id+1);
            bllManager.switchSongsPlacesOnPlaylist(playlist, song, secondSong);
            Collections.swap(playlistSongs, id, id+1);
            Collections.swap(playlist.getTracklist(), id, id+1);
        }
    }
    
    /**
     * Deletes song from playlist.
     * It makes a request for BLL to delete song from playlist
     * in database and then deletes song from playlist in temporary
     * state of data.
     * 
     * @param playlist The playlist with song to delete.
     * @param song The song to delete.
     */
    public void deleteSongFromPlaylist(Playlist playlist, Song song)
    {
        bllManager.deleteSongFromPlaylist(playlist, song);
        playlist.removeSong(song);
        updateListOfPlaylists(playlist);     
    }
    
    /**
     * Returns the list with songs on selected playlist.
     * 
     * @return The list of songs.
     */
    public ObservableList<Song> getPlaylistSongs()
    {
        return playlistSongs;
    }
    
    /**
     * Sets the list with songs on selected playlist
     * to the tracklist from given playlist.
     * 
     * @param playlist The selected playlist.
     */
    public void setPlaylistSongs(Playlist playlist)
    {
        playlistSongs.setAll(playlist.getTracklist());
    }
    
    /**
     * Clears the list with songs on selected playlist.
     */
    public void clearPlaylistSongs()
    {
        playlistSongs.clear();
    }
    
    /**
     * Adds a song to playlist.
     * 
     * @param playlist The playlist to which song should be added.
     * @param song The song to add.
     */
    public void addSongToPlaylist(Playlist playlist, Song song)
    {
        Playlist updatedPlaylist = bllManager.addSongToPlaylist(playlist, song);       
        updateListOfPlaylists(updatedPlaylist);
    }
    
    /**
     * Deletes the song from all playlists that contain this song.
     * 
     * @param song The song to delete.
     */
    private void deleteSongFromAllPlaylists(Song song)
    {
        for(int i = 0; i < playlists.size(); i++)
        {
            for(int j = 0; j < playlists.get(i).getTracklist().size(); j++)
            {
                if(song.getId() == playlists.get(i).getTracklist().get(j).getId())
                {
                    playlists.get(i).removeSong(playlists.get(i).getTracklist().get(j));
                    updateListOfPlaylists(playlists.get(i));
                }
            }
        }
    }
    
    /**
     * Updates the song from all playlists that contain this song.
     * 
     * @param song The song to update.
     * @param newTitle The new title for song.
     * @param newArtist The new artist for song.
     * @param newGenre The new genre for song.
     */
    private void updateSongOnAllPlaylists(Song song, String newTitle, String newArtist, String newGenre)
    {
        for(int i = 0; i < playlists.size(); i++)
        {
            for(int j = 0; j < playlists.get(i).getTracklist().size(); j++)
            {
                if(song.getId() == playlists.get(i).getTracklist().get(j).getId())
                {
                    Song songToUpdate = playlists.get(i).getTracklist().get(j);
                    songToUpdate.setTitle(newTitle);
                    songToUpdate.setArtist(newArtist);
                    songToUpdate.setGenre(newGenre);
                    updateListOfPlaylists(playlists.get(i));
                }
            }
        }
    }
    
    /**
     * Updates the given playlist on list of all playlists.
     * 
     * @param playlist The playlist to update.
     */
    private void updateListOfPlaylists(Playlist playlist)
    {
        int index = playlists.indexOf(playlist);
        playlists.set(index, playlist);
        setPlaylistSongs(playlist);
    }
    
    /**
     * Sets currently playing song and currently playing mode.
     * If playing mode is switching to different it invokes method
     * on {@code SongChooser} instance to clear previously played random
     * songs.
     * 
     * @param playedSong The song to set.
     * @param mode The mode to set.
     */
    public void setCurrentlyPlayingSong(Song playedSong, PlayingMode mode)
    {
        if(mode!=this.mode)
        {
            songChooser.clearPreviousRandomSongs();
        }
        this.mode = mode;
        currentlyPlayingSong = playedSong;
    }
    
    /**
     * Sets the variable which is storing information
     * about currently playing playlist to given playlist.
     * 
     * @param playlist The currently playing playlist.
     */
    public void setCurrentlyPlayingPlaylist(Playlist playlist)
    {
        currentlyPlayingPlaylist = playlist;
    }
    
    /**
     * Switches the shuffling and invokes method
     * on {@code SongChooser} instance to clear previously played random 
     * songs.
     */
    public void switchShuffling()
    {
        songChooser.clearPreviousRandomSongs();
        shuffle = !shuffle;
    }
    
    /**
     * Uses instance of {@code SongChooser} class to return
     * song from list of all songs. If {@code shuffle} is true it returns
     * random song, otherwise it returns first song from list of all songs.
     * 
     * @return The song from list of songs.
     */
    public Song getSong()
    {
        if(shuffle)
        {
            return songChooser.getRandomSong(songList);
        }
        else
        {
            return songChooser.getFirstSong(songList);
        }
    }
    
    /**
     * Uses instance of {@code SongChooser} class to return
     * song from given playlist. If {@code shuffle} is true it returns
     * random song from playlist, otherwise it returns first song from playlist.
     * 
     * @param playlist The playlist to get the song from.
     * @return The first song from playlist.
     */
    public Song getSongFromPlaylist(Playlist playlist)
    {
        if(shuffle)
        {
            return songChooser.getRandomSong(playlist.getTracklist());
        }
        else
        {
            return songChooser.getFirstSong(playlist.getTracklist());
        }
    }
      
    /**
     * Uses instance of {@code SongChooser} class to return
     * next song. If current playing mode is setted to PLAYLIST it will return next
     * song from {@code currentlyPlayingPlaylist}, otherwise it will return next song from
     * list of all songs. If {@code shuffle} is true it returns
     * random song from the proper source, otherwise it returns first song from the proper source.
     * 
     * @return The next song.
     */
    public Song getNextSong()
    {
        if(shuffle)
        {
            if(mode == PlayingMode.PLAYLIST)
            {
                return songChooser.getNextRandomSong(currentlyPlayingPlaylist.getTracklist(), currentlyPlayingSong);
            }
            else
            {
                return songChooser.getNextRandomSong(songList, currentlyPlayingSong);
            }
        }
        else
        {
            if(mode == PlayingMode.PLAYLIST)
            {
                return songChooser.getNextSong(currentlyPlayingPlaylist.getTracklist(), currentlyPlayingSong);
            }
            else
            {
                return songChooser.getNextSong(songList, currentlyPlayingSong);
            }
        }
    }
    
    /**
     * Returns current playing mode.
     * 
     * @return The playing mode.
     */
    public PlayingMode getCurrentPlayingMode()
    {
        return mode;
    }
    
    /**
     * Uses instance of {@code SongChooser} class to return
     * previous song. If current playing mode is setted to PLAYLIST it will return previous
     * song from {@code currentlyPlayingPlaylist}, otherwise it will return previous song from
     * list of all songs. If {@code shuffle} is true it returns previously played
     * random song, otherwise it returns previous song from the proper source.
     * 
     * @return The previous song.
     */
    public Song getPreviousSong()
    {
        if(shuffle)
        {
            return songChooser.getPreviousRandomSong(currentlyPlayingSong);
        }
        else
        {
            if(mode == PlayingMode.PLAYLIST)
            {
                return songChooser.getPreviousSong(currentlyPlayingPlaylist.getTracklist(), currentlyPlayingSong);
            }
            else
            {
                return songChooser.getPreviousSong(songList, currentlyPlayingSong);
            }
        }
    }
    
    /**
     * Returns list of songs from {@code songList} that matches to given filter.
     * 
     * @param filter The filter for songs.
     * @return The list of songs.
     */
    public ObservableList<Song> getFilteredSongs(String filter)
    {
        ObservableList<Song> filteredList = FXCollections.observableArrayList(MusicSearcher.searchSongs(songList, filter));
        return filteredList;
    }
    
    /**
     * Returns list of playlists from {@code playlists} that matches to given filter.
     * 
     * @param filter The filter for playlists.
     * @return The list of playlists.
     */
    public ObservableList<Playlist> getFilteredPlaylists(String filter)
    {
        ObservableList<Playlist> filteredList = FXCollections.observableArrayList(MusicSearcher.searchPlaylists(playlists, filter));
        return filteredList;
    }
    
    /**
     * Converts the time from seconds (int)
     * to format specified by {@code TimeConverter} class (String).
     * 
     * @param timeInSeconds The time to convert.
     * @return Time in proper format.
     */
    public String getTimeInString(int timeInSeconds)
    {
        return TimeConverter.convertToString(timeInSeconds);
    }
    
    /**
     * Converts the time from format specified by 
     * {@code TimeConverter} class to seconds (int)
     * 
     * @param timeInString The time to convert.
     * @return Time in seconds.
     */
    public int getTimeInInt(String timeInString)
    {
        return TimeConverter.convertToInt(timeInString);
    }
    
}
