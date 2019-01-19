package mytunes.be;

import java.util.ArrayList;
import java.util.List;
import mytunes.bll.util.TimeConverter;

/**
 * The {@code Playlist} class is representing 
 * playlist in our application. It consist of basic
 * informations about playlist and stores songs that
 * appear on it.
 * 
 * @author schemabuoi
 * @author kiddo
 */

public class Playlist {
    
    private int id;
    private String name;
    private int time;
    private int numberOfSongs;
    private List<Song> tracklist;
    
    /**
     * Constructs a new playlist without any songs.
     * 
     * @param id The id of playlist.
     * @param name The name of playlist.
     */
    public Playlist(int id, String name)
    {
        this.id = id;
        this.name = name;
        time = 0;
        numberOfSongs = 0;
        tracklist = new ArrayList();
    }
    
    /**
     * Adds song to playlist.
     * 
     * @param song The song to add.
     */
    public void addSong(Song song)
    {
        tracklist.add(song);
        time += song.getTime();
        numberOfSongs++;
    }
    
    /**
     * Removes song from playlist.
     * 
     * @param song The song to remove.
     */
    public void removeSong(Song song)
    {
        tracklist.remove(song);
        time -= song.getTime();
        numberOfSongs--;
    }
    
    /**
     * Sets playlists name.
     * 
     * @param name The name to set.
     */
    public void setName(String name) 
    {
        this.name = name;
    }
    
    /**
     * Returns playlists name.
     * 
     * @return The name of playlist.
     */
    public String getName() 
    {
        return name;
    }
    
    /**
     * Returns playlists ID.
     * 
     * @return The ID of playlist.
     */
    public int getId()
    {
        return id;
    }
    
    /**
     * Returns the list of songs on playlist.
     * 
     * @return The list of songs.
     */
    public List<Song> getTracklist() 
    {
        return tracklist;
    }
    
    /**
     * Sets the list of songs on playlist to 
     * given list of songs.
     * 
     * @param songs The list of songs to set.
     */
    public void setTracklist(List<Song> songs) 
    {
        tracklist.clear();
        tracklist.addAll(songs);
    }
    
    /**
     * Returns total amount of time of all songs on playlist.
     * 
     * @return The time.
     */
    public int getTime() 
    {
        return time;
    }
    
    /**
     * Returns number of all songs on playlist.
     * 
     * @return The numebr of songs.
     */
    public int getNumberOfSongs() 
    {
        return numberOfSongs;
    }
    
    /**
     * Returns total amount of time of all songs on playlist
     * in format specified by {@code TimeConverter} class.
     * 
     * @return The formatted time.
     */
    public String getTimeInString()
    {
        return TimeConverter.convertToString(time);
    }
    
    /**
     * Returns the position of song on playlist. If
     * given song is not on playlist, returns -1.
     * 
     * @param song The song from playlist.
     * @return The position of song on playlist.
     */
    public int getPositionOfSong(Song song)
    {
        return tracklist.indexOf(song);
    }
    
    /**
     * Returns true if given song is on the playlist.
     * 
     * @param song The song to check.
     * @return true if song is on playlist.
     */
    public boolean isSongOnTracklist(Song song)
    {
        for(Song s : tracklist)
        {
            if(s.getId() == song.getId())
            {
                return true;
            }
        }
        return false;
    }
}
