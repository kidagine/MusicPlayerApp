package mytunes.gui.controller;

import java.net.URL;
import java.util.EventObject;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import mytunes.gui.model.GenresViewModel;

/**
 * The {@code GenresViewController} class is a controller for
 * {@code GenresView}. It is responsible for passing informations about
 * selected genre to {@code GenresViewModel}.
 * 
 * @author schemabuoi
 * @author kiddo
 */

public class GenresViewController implements Initializable {
    
    private GenresViewModel genresModel;

    @FXML
    private ListView<String> lstGenres;
    @FXML
    private Button btnSave;
    
    /**
     * Creates connection with {@code GenresViewModel} instance.
     */
    public GenresViewController()
    {
        genresModel = GenresViewModel.createInstance();
    }

    /**
     * Initializes the controller class. Disables the save button because at the start
     * no genre on a ListView is selected and sets the ListView with genres to the list of all genres
     * fetched from {@code GenresModel}. It also clears the selected genre in {@code GenresModel}
     * in case if some genre was selected in previous opening of window with {@code GenresView}.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        genresModel.clearSelectedGenre();
        lstGenres.setItems(genresModel.getAllGenres());
        btnSave.setDisable(true);
    }   
    
    /**
     * Method which is invoked after clicking Save button
     * on {@code GenresView}. Its responsibility is to pass genre
     * selected on the view to {@code GenresModel} and to close the
     * current stage.
     */
    @FXML
    private void clickSaveGenre(ActionEvent event) {
        genresModel.setSelectedGenre(lstGenres.getSelectionModel().getSelectedItem());
        Stage stage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
        stage.close();
    }
    
    /**
     * Method which is invoked after clicking on a ListView with 
     * genres on {@code GenresView}. It is checking if some item
     * was selected to check if Save button can be enabled.
     */
    @FXML
    private void clickOnGenres(MouseEvent event) {
        if(lstGenres.getSelectionModel().getSelectedItem() != null)
        {
            btnSave.setDisable(false);
        }
    }
    
    /**
     * Method which is invoked after clicking on Cancel
     * button on {@code CreateUserView}. It is closing the
     * current stage.
     */
    @FXML
    private void clickCancel(ActionEvent event) {
        Stage stage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
        stage.close();
    }
    
    /**
     * Method which is invoked after clicking on Close
     * button on {@code CreateUserView}. It is closing the
     * current stage.
     */
    @FXML
    private void clickClose(ActionEvent event) {
        Stage stage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
        stage.close();
    }

    
}
