package mytunes.dal.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import mytunes.be.Playlist;
import mytunes.be.User;
import mytunes.dal.DbConnectionProvider;

/**
 * The {@code PlaylistDAO} class is responsible for
 * operations on Playlists table in our database.
 * It is also using {@code PlaylistSongsDAO} class
 * for operations on PlaylistSongs table because both
 * tables are connected with each other.
 * 
 * @author schemabuoi
 * @author kiddo
 */
public class PlaylistDAO {
    
    private DbConnectionProvider connector;
    private PlaylistSongsDAO psDao;
        
    /**
     * Creates connector with database and DAO for PlaylistSongs.
     */
    public PlaylistDAO()
    {
        connector = new DbConnectionProvider();
        psDao = new PlaylistSongsDAO();
    }
    
    /**
     * Creates a Playlist in the database for a given user.
     * 
     * @param user The playlist user.
     * @param name The name of the playlist.
     * @return Created playlist.
     * @throws SQLException if connection with database cannot be established.
     */
    public Playlist createPlaylist(User user, String name) throws SQLException
    {
        String sqlStatement = "INSERT INTO Playlists(userId, name) values(?, ?)";
        try(Connection con = connector.getConnection();
                PreparedStatement statement = con.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS))
        {
            statement.setInt(1, user.getId());
            statement.setString(2, name);
            statement.execute();
            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            int id = rs.getInt(1);
            return new Playlist(id, name);
        }
    }
    
    /**
     * Updates the name of the playlist in the database.
     * 
     * @param playlist The playlist to update.
     * @param newName The new name for the playlist.
     * @return Updated playlist.
     * @throws SQLException if connection with database cannot be established.
     */
    public Playlist updatePlaylist(Playlist playlist, String newName) throws SQLException
    {
        String sqlStatement = "UPDATE Playlists SET name=? WHERE id=?";
        try(Connection con = connector.getConnection();
                PreparedStatement statement = con.prepareStatement(sqlStatement))
        {
            statement.setString(1, newName);
            statement.setInt(2, playlist.getId());
            statement.execute();
            playlist.setName(newName);
            return playlist;
        }
    }
    
    /**
     * Gets a list with all playlists from the database for given user. After adding all playlists
     * to the list,it uses object of PlaylistSongsDAO class to add
     * all songs for all playlist.
     * 
     * @param user The playlists user.
     * @return List with playlists.
     * @throws SQLException if connection with database cannot be established. 
     */
    public List<Playlist> getAllPlaylists(User user) throws SQLException
    {
        String sqlStatement = "SELECT * FROM Playlists WHERE userId=?";
        List<Playlist> allPlaylists = new ArrayList();
        try(Connection con = connector.getConnection();
                PreparedStatement statement = con.prepareStatement(sqlStatement))
        {
            statement.setInt(1, user.getId());
            ResultSet rs = statement.executeQuery();
            while(rs.next())
            {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                allPlaylists.add(new Playlist(id, name));
            }
            psDao.addAllSongsToPlaylists(user, allPlaylists);
        }
        return allPlaylists;
    }
    
    /**
     * Uses object of PlaylistSongsDAO class to delete all songs 
     * from the given playlist and deletes playlist from the database.
     * 
     * @param playlist The playlist to delete.
     * @throws SQLException if connection with database cannot be established. 
     */
    public void deletePlaylist(Playlist playlist) throws SQLException
    {
        psDao.deleteAllSongsFromPlaylist(playlist);
        String sqlStatement = "DELETE FROM Playlists WHERE id=?";
        try(Connection con = connector.getConnection();
                PreparedStatement statement = con.prepareStatement(sqlStatement))
        {
            statement.setInt(1, playlist.getId());
            statement.execute();
        }
    }
    
}
