package mytunes.dal.daos;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import mytunes.be.Song;
import mytunes.be.User;
import mytunes.dal.DbConnectionProvider;

/**
 * The {@code SongDAO} class is responsible for
 * operations on Songs table in our database.
 * 
 * @author schemabuoi
 * @author kiddo
 */
public class SongDAO {
    
    private DbConnectionProvider connector;
    private PlaylistSongsDAO playlistSongsDao;
    
    /**
     * Creates connector with database.
     */
    public SongDAO()
    {
        connector = new DbConnectionProvider();
        playlistSongsDao = new PlaylistSongsDAO();
    }
    
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
     * @throws SQLException if connection with database cannot be established.
     */
    public Song createSong(User user, String title, String artist, String genre, String path, int time) throws SQLException
    {
        String sqlStatement = "INSERT INTO Songs(userId, title, artist,genre,path,time) values(?,?,?,?,?,?)";
        try(Connection con = connector.getConnection();
                PreparedStatement statement = con.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS))
        {
            statement.setInt(1, user.getId());
            statement.setString(2, title);
            statement.setString(3, artist);
            statement.setString(4, genre);
            statement.setString(5, path);
            statement.setInt(6, time);
            statement.execute();
            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            int id = rs.getInt(1);
            return new Song(id, title,artist,genre,path,time);
        }
    }
    
    /**
     * Updates the song in database.
     * 
     * @param song The song to update.
     * @param newTitle The new title for song.
     * @param newArtist The new artist for song.
     * @param newGenre The new genre for song.
     * @return Updated song.
     * @throws SQLException if connection with database cannot be established.
     */
    public Song updateSong(Song song, String newTitle, String newArtist, String newGenre) throws SQLException
    {
        String sqlStatement = "UPDATE Songs SET title=?, artist=?, genre=? WHERE id=?";
        try(Connection con = connector.getConnection();
                PreparedStatement statement = con.prepareStatement(sqlStatement))
        {
            statement.setString(1, newTitle);
            statement.setString(2, newArtist);
            statement.setString(3, newGenre);
            statement.setInt(4, song.getId());
            statement.execute();
            song.setTitle(newTitle);
            song.setArtist(newArtist);
            song.setGenre(newGenre);
            return song;
        }
    }
    
    /**
     * Gets all songs from database for given user.
     * 
     * @param user The songs user.
     * @return List with songs.
     * @throws SQLException if connection with database cannot be established.
     */
    public List<Song> getAllSongs(User user) throws SQLException
    {
        String sqlStatement = "SELECT * FROM Songs WHERE userId=?";
        List<Song> allSongs = new ArrayList();
        try(Connection con = connector.getConnection();
                PreparedStatement statement = con.prepareStatement(sqlStatement))
        {
            statement.setInt(1, user.getId());
            ResultSet rs = statement.executeQuery();
            while(rs.next())
            {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String artist = rs.getString("artist");
                String genre = rs.getString("genre");
                String path = rs.getString("path");
                int time = rs.getInt("time");
                allSongs.add(new Song(id,title,artist,genre,path,time));
            }
        }
        return allSongs;
    }
    
    /**
     * Uses object of PlaylistSongsDAO class to delete song from
     * all playlists that contain this song and then completely deletes song from database.
     * 
     * @param song The song to delete.
     * @throws SQLException if connection with database cannot be established.
     */
    public void deleteSong(Song song) throws SQLException
    {
        playlistSongsDao.deleteSongFromAllPlaylists(song);
        String sqlStatement = "DELETE FROM Songs WHERE id=?";
        try(Connection con = connector.getConnection();
                PreparedStatement statement = con.prepareStatement(sqlStatement))
        {
            statement.setInt(1, song.getId());
            statement.execute();
        }
    }
    
}
