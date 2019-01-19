package mytunes.dal;

import java.sql.SQLException;
import java.util.List;
import mytunes.be.Playlist;
import mytunes.be.Song;
import mytunes.be.User;
import mytunes.dal.daos.GenreDAO;
import mytunes.dal.daos.PlaylistDAO;
import mytunes.dal.daos.PlaylistSongsDAO;
import mytunes.dal.daos.SongDAO;
import mytunes.dal.daos.UserDAO;

/**
 * The {@code DalController} class is responsible for
 * all operations on database. It is implementing IDalFacade
 * interface.
 * 
 * @author schemabuoi
 * @author kiddo
 */
public class DalController implements IDalFacade{
    
    private SongDAO songDao;
    private PlaylistDAO playlistDao;
    private PlaylistSongsDAO playlistSongsDao;
    private GenreDAO genreDao;
    private UserDAO userDao;
    
    /**
     * Creates all Data Access Objects.
     */
    public DalController()
    {
        songDao = new SongDAO();
        playlistDao = new PlaylistDAO();
        playlistSongsDao = new PlaylistSongsDAO();
        genreDao = new GenreDAO();
        userDao = new UserDAO();
    }
    
    @Override
    public Song createSong(User user, String title, String artist, String genre, String path, int time)
    {
        Song createdSong = null;
        try
        {
            createdSong = songDao.createSong(user, title,artist,genre,path,time);
        }
        catch(SQLException e)
        {
            //TO DO
        }
        return createdSong;
    }

    @Override
    public Song updateSong(Song song, String newTitle, String newArtist, String newGenre) {
        Song updatedSong = null;
        try
        {
            updatedSong = songDao.updateSong(song, newTitle, newArtist, newGenre);
        }
        catch(SQLException e)
        {
            //TO DO
        }
        return updatedSong;
    }

    @Override
    public void deleteSong(Song song) {
        try
        {
            songDao.deleteSong(song);
        }
        catch(SQLException e)
        {
            //TO DO
        }
    }

    @Override
    public List<Song> getAllSongs(User user) {
        List<Song> allSongs = null;
        try
        {
            allSongs = songDao.getAllSongs(user);
        }
        catch(SQLException e)
        {
            //TO DO
        }
        return allSongs;
    }

    @Override
    public Playlist createPlaylist(User user, String name) {
        Playlist createdPlaylist = null;
        try
        {
            createdPlaylist = playlistDao.createPlaylist(user, name);
        }
        catch(SQLException e)
        {
            //TO DO
        }
        return createdPlaylist;
    }

    @Override
    public Playlist updatePlaylist(Playlist playlist, String newName) {
        Playlist updatedPlaylist = null;
        try
        {
            updatedPlaylist = playlistDao.updatePlaylist(playlist, newName);
        }
        catch(SQLException e)
        {
            //TO DO
        }
        return updatedPlaylist;
    }

    @Override
    public void deletePlaylist(Playlist playlist) {
        try
        {
            playlistDao.deletePlaylist(playlist);
        }
        catch(SQLException e)
        {
            //TO DO
        }
    }

    @Override
    public List<Playlist> getAllPlaylists(User user) {
        List<Playlist> allPlaylists = null;
        try
        {
            allPlaylists = playlistDao.getAllPlaylists(user);
        }
        catch(SQLException e)
        {
            //TO DO
        }
        return allPlaylists;
    }

    @Override
    public Playlist switchSongsPlacesOnPlaylist(Playlist playlist, Song firstSong, Song secondSong)
    {
        Playlist updatedPlaylist = null;
        try
        {
            updatedPlaylist = playlistSongsDao.switchSongPlacesOnPlaylist(playlist, firstSong, secondSong);
        }
        catch(SQLException e)
        {
            //TO DO
        }
        return updatedPlaylist;
    }

    @Override
    public void deleteSongFromPlaylist(Playlist playlist, Song song) {
        try
        {
            playlistSongsDao.deleteSongFromPlaylist(playlist, song);
        }
        catch(SQLException e)
        {
            //TO DO
        }
    }

    @Override
    public Playlist addSongToPlaylist(Playlist playlist, Song song) {
        Playlist updatedPlaylist = null;
        try
        {
            updatedPlaylist = playlistSongsDao.addSongToPlaylist(playlist, song);
        }
        catch(SQLException e)
        {
            //TO DO
        }
        return updatedPlaylist;
    }
    
    @Override
    public List<String> getAllGenres()
    {
        List<String> allGenres = null;
        try
        {
            allGenres = genreDao.getAllGenres();
        }
        catch(SQLException e)
        {
            //TO DO
        }
        return allGenres;
    }
    
    @Override
    public User createUser(String email, String password)
    {
        User user = null;
        try
        {
            user = userDao.createUser(email, password);
        }
        catch(SQLException e)
        {
            //TO DO
        }
        return user;
    }
    
    @Override
    public User getUser(String email, String password)
    {
        User user = null;
        try
        {
            user = userDao.getUser(email, password);
        }
        catch(SQLException e)
        {
            //TO DO
        }
        return user;
    }

}
