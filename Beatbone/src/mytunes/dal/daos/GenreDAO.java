package mytunes.dal.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import mytunes.dal.DbConnectionProvider;

/**
 * The {@code GenreDAO} class is responsible for
 * operations on Genres table in our database.
 * 
 * @author schemabuoi
 * @author kiddo
 */
public class GenreDAO {
    
    private DbConnectionProvider connector;
    
    /**
     * Creates connector with database.
     */
    public GenreDAO()
    {
        connector = new DbConnectionProvider();
    }
    
    /**
     * Gets a list with names of all genres from the database.
     * 
     * @return List with genres.
     * @throws SQLException if connection with database cannot be established.
     */
    public List<String> getAllGenres() throws SQLException
    {
        List<String> allGenres = new ArrayList();
        String sqlStatement = "SELECT * FROM Genres";
        try(Connection con = connector.getConnection();
                PreparedStatement statement = con.prepareStatement(sqlStatement))
        {
            ResultSet rs = statement.executeQuery();
            while(rs.next())
            {
                allGenres.add(rs.getString("name"));
            }
            return allGenres;
        }
    }
    
}
