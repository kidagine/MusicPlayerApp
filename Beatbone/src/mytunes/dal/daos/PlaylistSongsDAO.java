package mytunes.dal.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import mytunes.be.Playlist;
import mytunes.be.Song;
import mytunes.be.User;
import mytunes.dal.DbConnectionProvider;

/**
 * The {@code PlaylistSongsDAO} class is responsible for
 * operations on PlaylistSongs table in our database.
 * 
 * @author schemabuoi
 * @author kiddo
 */
public class PlaylistSongsDAO {
    
    private DbConnectionProvider connector;
    
    /**
     * Creates connector with database.
     */
    public PlaylistSongsDAO()
    {
        connector = new DbConnectionProvider();
    }
    
    /**
     * Adds a song to the playlist in database.
     * 
     * @param playlist The playlist for song.
     * @param song The song to add.
     * @return Updated playlist with added song
     * @throws SQLException if connection with database cannot be established.
     */
    public Playlist addSongToPlaylist(Playlist playlist, Song song) throws SQLException
    {
        String sqlStatement = "INSERT INTO PlaylistSongs(playlistId, songId) values(?,?)";
        try(Connection con = connector.getConnection();
                PreparedStatement statement = con.prepareStatement(sqlStatement))
        {
            statement.setInt(1, playlist.getId());
            statement.setInt(2, song.getId());
            statement.execute();
            playlist.addSong(song);
            return playlist;
        }
    }
    
    /**
     * Adds all songs from database to playlists from given list.
     * 
     * @param playlists The list of a playlists.
     * @throws SQLException if connection with database cannot be established.
     */
    public void addAllSongsToPlaylists(User user, List<Playlist> playlists) throws SQLException
    {
        String sqlStatement = "SELECT Playlists.id as playlistId, Songs.* FROM PlaylistSongs " +
                                        "INNER JOIN Playlists on PlaylistSongs.playlistId=Playlists.id " +
                                        "INNER JOIN Songs on PlaylistSongs.songId=Songs.id " +
                                        "WHERE Songs.userId=?";
        try(Connection con = connector.getConnection();
                PreparedStatement statement = con.prepareStatement(sqlStatement))
        {
            statement.setInt(1, user.getId());
            ResultSet rs = statement.executeQuery();
            if(rs.next())
            {
                for(Playlist p : playlists)
                {
                    while(!rs.isAfterLast() && rs.getInt("playlistId") == p.getId())
                    {
                        int id = rs.getInt("id");
                        String title = rs.getString("title");
                        String artist = rs.getString("artist");
                        String genre = rs.getString("genre");
                        String path = rs.getString("path");
                        int time = rs.getInt("time");
                        p.addSong(new Song(id, title, artist, genre, path, time));
                        rs.next();
                    }
                }
            }
        }
    }
    
    /**
     * Switches the places of songs in database.
     * 
     * @param playlist The playlist with songs to switch.
     * @param firstSong The first song to switch.
     * @param secondSong The second song to switch.
     * @return Updated playlist with switched positions of songs.
     * @throws SQLException if connection with database cannot be established. 
     */
    public Playlist switchSongPlacesOnPlaylist(Playlist playlist, Song firstSong, Song secondSong) throws SQLException
    {
        String sqlStatement = "UPDATE PlaylistSongs SET playlistId=?, songId=? WHERE playlistId=? and songId=?;" +
                    "UPDATE PlaylistSongs SET playlistId=?, songId=? WHERE playlistId=? and songId=?;";
        try(Connection con = connector.getConnection();
                PreparedStatement statement = con.prepareStatement(sqlStatement))
        {
            statement.setInt(1, playlist.getId());
            statement.setInt(2, secondSong.getId());
            statement.setInt(3, playlist.getId());
            statement.setInt(4, firstSong.getId());
            statement.setInt(5, playlist.getId());
            statement.setInt(6, firstSong.getId());
            statement.setInt(7, playlist.getId());
            statement.setInt(8, secondSong.getId());
            statement.execute();
        }
        return playlist;
    }
    
    /**
     * Deletes song from given playlist in database.
     * 
     * @param playlist The playlist with song to delete.
     * @param song The song to delete.
     * @throws SQLException if connection with database cannot be established.  
     */
    public void deleteSongFromPlaylist(Playlist playlist, Song song) throws SQLException
    {
        String sqlStatement = "DELETE FROM PlaylistSongs WHERE playlistId=? and songId=?";
        try(Connection con = connector.getConnection();
                PreparedStatement statement = con.prepareStatement(sqlStatement))
        {
            statement.setInt(1, playlist.getId());
            statement.setInt(2, song.getId());
            statement.execute();
        }
    }
    
    /**
     * Deletes all songs from given playlist in database.
     * 
     * @param playlist The playlist to clear.
     * @throws SQLException if connection with database cannot be established.
     */
    public void deleteAllSongsFromPlaylist(Playlist playlist) throws SQLException
    {
        String sqlStatement = "DELETE FROM PlaylistSongs WHERE playlistId=?";
        try(Connection con = connector.getConnection();
                PreparedStatement statement = con.prepareStatement(sqlStatement))
        {
            statement.setInt(1, playlist.getId());
            statement.execute();
        }
    }
    
    /**
     * Deletes given song from all playlists.
     * 
     * @param song The song to delete.
     * @throws SQLException if connection with database cannot be established. 
     */
    public void deleteSongFromAllPlaylists(Song song) throws SQLException
    {
        String sqlStatement = "DELETE FROM PlaylistSongs WHERE songId=?";
        try(Connection con = connector.getConnection();
                PreparedStatement statement = con.prepareStatement(sqlStatement))
        {
            statement.setInt(1, song.getId());
            statement.execute();
        }
    }
}
