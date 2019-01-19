package mytunes.gui.controller;

import java.net.URL;
import java.util.EventObject;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import mytunes.be.Playlist;
import mytunes.gui.model.MainModel;

/**
 * The {@code PlaylistViewController} class is a controller for
 * {@code PlaylistView}. Sends the requests to model for updating
 * and creating the playlist. By default while initializing the {@code PlaylistView} 
 * its opened in "Create Playlist" mode. To switch the functionality to "Edit Playlist"
 * mode the {@code setEditingMode} method have to be invoked.
 * 
 * @author schemabuoi
 * @author kiddo
 */

public class PlaylistViewController implements Initializable {
    
    private MainModel model;
    private boolean editing;
    private Playlist editingPlaylist;

    @FXML
    private TextField txtName;
    @FXML
    private Button btnSave;
    
    /**
     * Creates a connection with {@code MainModel} instance. Sets editing variable
     * to false - by default if we initialize the {@code PlaylistView} its opened in
     * "Create Playlist" mode.
     */
    public PlaylistViewController()
    {
        editing = false;
        model = MainModel.createInstance();      
    }

    /**
     * Initializes the controller class. Disables the save button to
     * prevent creating playlist with empty name.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnSave.setDisable(true);
    }    
    
    /**
     * Method which is invoked everytime some key is typed in a field
     * with playlist name on {@code PlaylistView}. Enables the Save button
     * if field with playlists name is not empty, otherwise disables the Save
     * button.
     */
    @FXML
    private void keyNameTyped(KeyEvent event) {
        String name = txtName.getText().trim();
        if(!name.isEmpty())
        {
            btnSave.setDisable(false);
        }
        else
        {
            btnSave.setDisable(true);
        }
    }
    
    /**
     * Method which is invoked after clicking Save button on {@code PlaylistView}.
     * Checks which mode is selected (Editing playlist or creating new playlists) and
     * sends the corresponded request to {@code MainModel}. Closes the current stage.
     */
    @FXML
    private void clickSave(ActionEvent event) {
        String name = txtName.getText().trim();
        if(!editing)
        {
            model.createPlaylist(name);
        }
        else
        {
            model.updatePlaylist(editingPlaylist, name);
        }
        Stage stage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
        stage.close();
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
     * Sets the mode to editing mode and sets the playlist to edit.
     * 
     * @param playlist The playlist to edit.
     */
    public void setEditingMode(Playlist playlist)
    {
        editing = true;
        editingPlaylist = playlist;
        txtName.setText(editingPlaylist.getName());
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
