package mytunes.bll.util;

import java.util.List;
import java.util.Random;
import java.util.Stack;
import mytunes.be.Song;

/**
 * The {@code SongChooser} class is responsible for
 * picking appropriate for the given request.  
 * 
 * @author schemabuoi
 * @author kiddo
 */
public class SongChooser {
    
    private Stack<Song> previouslyPlayedRandomSongs = new Stack();
    
    /**
     * Returns first song from the given list of songs. If
     * list of songs is empty returns null.
     * 
     * @param songList The list of songs.
     * @return First song from the list.
     */
    public Song getFirstSong(List<Song> songList)
    {
        if(songList.isEmpty())
        {
            return null;
        }
        return songList.get(0);
    }
    
    /**
     * Returns random song from the given list of songs. If
     * list of songs is empty returns null.
     * 
     * @param songList The list of songs.
     * @return Random song from the list.
     */
    public Song getRandomSong(List<Song> songList)
    {
        if(songList.isEmpty())
        {
            return null;
        }
        Random rand = new Random();
        int songId = rand.nextInt(songList.size());
        return songList.get(songId);
    }
    
    /**
     * Returns next song in reference to the given one. If given song
     * is the last song on the list the first song from the list is returned.
     * If list of songs is empty returns null.
     * 
     * @param songList The list of songs.
     * @param currentSong The song that relates to which song will be next.
     * @return Next song from list.
     */
    public Song getNextSong(List<Song> songList, Song currentSong)
    {
        if(songList.isEmpty())
        {
            return null;
        }
        int index = songList.indexOf(currentSong);
        if(index == songList.size()-1)
        {
            return getFirstSong(songList);
        }
        else
        {
            return songList.get(index+1);
        }
    }
    
    /**
     * Returns previous song in reference to the given one. If given song
     * is the first song on the list, the method is returning this given song.
     * If list of songs is empty returns null.
     * 
     * @param songList The list of songs.
     * @param currentSong The song that relates to which song will be previous.
     * @return Previous song from the list.
     */
    public Song getPreviousSong(List<Song> songList, Song currentSong)
    {
        if(songList.isEmpty())
        {
            return null;
        }
        int index = songList.indexOf(currentSong);
        if(index == 0)
        {
            return currentSong;
        }
        else
        {
            return songList.get(index-1);
        }
    }
    
    /**
     * Returns next random songs which has to be different than
     * given current song. If list of songs is empty returns null.
     * If list of songs has only 1 song then this song is returned.
     * 
     * @param songList The list of songs.
     * @param currentSong The song that is referenced.
     * @return Next random song from the list.
     */
    public Song getNextRandomSong(List<Song> songList, Song currentSong)
    {
        if(songList.isEmpty())
        {
            return null;
        }
        if(songList.size() == 1)
        {
            return currentSong;
        }
        Random rand = new Random();
        int currentSongId = songList.indexOf(currentSong);
        int nextSongId = currentSongId;
        while(currentSongId == nextSongId)
        {
            nextSongId = rand.nextInt(songList.size());
        }
        previouslyPlayedRandomSongs.add(currentSong);
        return songList.get(nextSongId);
    }
    
    /**
     * Returns previously played random song. If there 
     * was no previous random song, {@code currentSong}
     * is returned.
     * 
     * @param currentSong The song that is referenced.
     * @return Previously played random song.
     */
    public Song getPreviousRandomSong(Song currentSong)
    {
        if(previouslyPlayedRandomSongs.isEmpty())
        {
            return currentSong;
        }
        else
        {
            return previouslyPlayedRandomSongs.pop();
        }
    }  
    
    /**
     * Clears previously played random songs.
     */
    public void clearPreviousRandomSongs()
    {
        previouslyPlayedRandomSongs = new Stack();
    }
}
