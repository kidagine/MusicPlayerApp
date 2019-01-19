package mytunes.bll.util;

import java.util.ArrayList;
import java.util.List;
import mytunes.be.Playlist;
import mytunes.be.Song;

/**
 * The {@code MusicSearcher} class is responsible for
 * filtering lists of songs and playlists.  
 * 
 * @author schemabuoi
 * @author kiddo
 */
public class MusicSearcher {
    
    /**
     * Takes the list with songs and filter and
     * returns filtered list.
     * 
     * @param songList The List with songs to filter.
     * @param filter The filter for list.
     * @return Filtered list of songs.
     */
    public static List<Song> searchSongs(List<Song> songList, String filter)
    {
        filter = filter.toLowerCase();
        List<Song> filteredSongs = new ArrayList();
        for(Song s : songList)
        {
            if(filter.length() <= s.getTitle().length() && filter.equals(s.getTitle().toLowerCase().substring(0, filter.length())))
            {
                filteredSongs.add(s);
            }
            else if(filter.length() <= s.getArtist().length() && filter.equals(s.getArtist().toLowerCase().substring(0, filter.length())))
            {
                filteredSongs.add(s);
            }
        }
        return filteredSongs;
    }
    
    /**
     * Takes the list with playlists and filter and
     * returns filtered list.
     * 
     * @param playlists The List with playlists to filter.
     * @param filter The filter for list.
     * @return Filtered list of playlists.
     */
    public static List<Playlist> searchPlaylists(List<Playlist> playlists, String filter)
    {
        filter = filter.toLowerCase();
        List<Playlist> filteredPlaylists = new ArrayList();
        for(Playlist p : playlists)
        {
            if(filter.length() <= p.getName().length() && filter.equals(p.getName().toLowerCase().substring(0, filter.length())))
            {
                filteredPlaylists.add(p);
            }
        }
        return filteredPlaylists;
        
    }
    
}
