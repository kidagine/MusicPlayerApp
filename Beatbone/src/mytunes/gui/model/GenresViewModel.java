package mytunes.gui.model;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mytunes.bll.BllManager;

/**
 * The {@code GenresViewModel} class is responsible for 
 * getting genres informations from Business Logic Layer. 
 * It is using singleton design pattern. It is storing
 * temporary state of data with all genres and list of main genres.
 * It is also storing information about currently selected
 * genre.
 * 
 * @author schemabuoi
 * @author kiddo
 */
public class GenresViewModel {
    
    private static GenresViewModel instance;
    private BllManager bllManager;
    private ObservableList<String> mainGenres;
    private ObservableList<String> allGenres;
    private String selectedGenre;
    
    /**
     * Creates connection with BLL, fetches all genres from it and fills
     * list with main genres.
     */
    private GenresViewModel()
    {
        bllManager = new BllManager();
        mainGenres = FXCollections.observableArrayList(new ArrayList());
        addMainGenres();
        allGenres = FXCollections.observableArrayList(bllManager.getAllGenres());
    }
    
    /**
     * Returns single instance of GenresViewModel class.
     * 
     * @return The instance of GenresViewModel class.
     */
    public static GenresViewModel createInstance()
    {
        if(instance == null)
        {
            instance = new GenresViewModel();
        }
        return instance;
    }
    
    /**
     * Returns list with main genres.
     * 
     * @return The list with genres.
     */
    public ObservableList<String> getMainGenres()
    {
        return mainGenres;
    }
    
    /**
     * Returns list with all genres.
     * 
     * @return The list with genres.
     */
    public ObservableList<String> getAllGenres()
    {
        return allGenres;
    }
    
    /**
     * Returns the currently selected genre.
     * 
     * @return The selected genre.
     */
    public String getSelectedGenre()
    {
        return selectedGenre;
    }
    
    /**
     * Sets selected genre to null. 
     */
    public void clearSelectedGenre()
    {
        selectedGenre = null;
    }
    
    /**
     * Sets selected genre to given string with name.
     * 
     * @param name The string with name of genre.
     */
    public void setSelectedGenre(String name)
    {
        selectedGenre = name;
    }
    
    /**
     * Adds genres to the list with main genres.
     */
    private void addMainGenres() {
        mainGenres.add("Rap");
        mainGenres.add("Jazz");
        mainGenres.add("Blues");
        mainGenres.add("Rock");
        mainGenres.add("Pop");
    }
}
