package mytunes.gui.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.EventObject;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mytunes.be.Song;
import mytunes.gui.model.GenresViewModel;
import mytunes.gui.model.MainModel;
import mytunes.gui.util.WindowDecorator;

/**
 * The {@code SongViewController} class is a controller for
 * {@code SongView}. Sends the requests to model for updating
 * and creating the song. By default while initializing the {@code SongView} 
 * its opened in "Create Song" mode. To switch the functionality to "Edit Song"
 * mode the {@code setEditingMode} method have to be invoked.
 * 
 * @author schemabuoi
 * @author kiddo
 */
public class SongViewController implements Initializable {
    
    private boolean editing;
    private Song editingSong;
    private MainModel model;
    private GenresViewModel genresModel;

    @FXML
    private TextField txtTitle;
    @FXML
    private TextField txtArtist;
    @FXML
    private TextField txtTime;
    @FXML
    private TextField txtFile;
    @FXML
    private ComboBox<String> cmbGenre;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnChoosePath;
    
    /**
     * Creates a connection with {@code MainModel} and {@code GenresModel} instances.
     */
    public SongViewController()
    {
        editing = false;
        model = MainModel.createInstance();
        genresModel = GenresViewModel.createInstance();
    }

    /**
     * Initializes the controller class. Sets the ComboBox with genres to the list of main genres
     * fetched from {@code GenresModel}. Disables elements to prevent creating a song without entering data
     * or editing song without changing data.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        disableElements();
        cmbGenre.getItems().setAll(genresModel.getMainGenres());
    }    
    
    /**
     * Method which is invoked everytime some key on title text field is typed.
     * It invokes method for checking inputs on all elements with songs data.
     */
    @FXML
    private void keyTitleTyped(KeyEvent event) {
        checkInputs();
    }
    
    /**
     * Method which is invoked everytime some key on artist text field is typed.
     * It invokes method for checking inputs on all elements with songs data.
     */
    @FXML
    private void keyArtistTyped(KeyEvent event) {
        checkInputs();
    }
    
    /**
     * Method which is invoked everytime some item on genre combo box is selected.
     * It invokes method for checking inputs on all elements with songs data.
     */
    @FXML
    private void clickGenrePicked(ActionEvent event) {
        checkInputs();
    }
    
    /**
     * Method which is invoked after clicking on More button on {@code SongView}.
     * It opens {@code GenresView} and waits until its closed = then it gets the genre
     * which was selected from {@code GenresModel}. If some genre was selected its selecting
     * this genre in combo box.
     */
    @FXML
    private void clickMoreGenres(ActionEvent event) throws IOException {
        Stage currentStage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
        WindowDecorator.fadeOutStage(currentStage);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mytunes/gui/view/GenresView.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("More Genres");
        stage.setScene(new Scene(root));  
        stage.initStyle(StageStyle.UNDECORATED);
        stage.showAndWait();
        String selectedGenre = genresModel.getSelectedGenre();
        if(selectedGenre != null)
        {
            setGenre(selectedGenre);
        }
        WindowDecorator.fadeInStage(currentStage);
    }
    
    /**
     * Method which is invoked after clicking on Save button on {@code SongView}.
     * Checks which mode is selected (Editing playlist or creating new playlists) and
     * sends the corresponded request to {@code MainModel}. Closes the current stage.
     */
    @FXML
    private void clickSave(ActionEvent event) {
        if(!editing)
        {
            model.createSong(txtTitle.getText(), txtArtist.getText(), cmbGenre.getValue(), 
                    txtFile.getText(), model.getTimeInInt(txtTime.getText()));
        }
        else
        {
            model.updateSong(editingSong, txtTitle.getText(), txtArtist.getText(), cmbGenre.getValue());
        }
        Stage stage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
        stage.close();
    }
    
    /**
     * Method which is invoked after clicking on Choose button on
     * {@code SongView}. It opens a file chooser dialog and sets
     * path and time fields if some file was selected. It also 
     * invokes method for checking inputs on all elements with songs data.
     */
    @FXML
    private void clickChooseFilePath(ActionEvent event)
    {
        FileChooser fileChooser = createSongChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if(selectedFile != null)
        {
            txtFile.setText(selectedFile.getPath());
            setTimeField(selectedFile);
            checkInputs();
        }
    }
    
    /**
     * Creates a file chooser for file with song.
     */
    private FileChooser createSongChooser()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select song");
        FileChooser.ExtensionFilter generalFilter = new FileChooser.ExtensionFilter("All Music Files", "*.mp3", "*.wav", "*.mp4", "*.m4a", "*.m4v");
        FileChooser.ExtensionFilter mp3Filter = new FileChooser.ExtensionFilter("MP3 (*.mp3)", "*.mp3");
        FileChooser.ExtensionFilter mp4Filter = new FileChooser.ExtensionFilter("MP4 (*.mp4, *.m4a, *.m4v)","*.mp4", "*.m4a", "*.m4v");
        FileChooser.ExtensionFilter wavFilter = new FileChooser.ExtensionFilter("WAV (*.wav)","*.wav");
        fileChooser.getExtensionFilters().add(generalFilter);
        fileChooser.getExtensionFilters().add(mp3Filter);
        fileChooser.getExtensionFilters().add(mp4Filter);
        fileChooser.getExtensionFilters().add(wavFilter);       
        return fileChooser;
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
     * Disables elements on the {@code SongView} which should be disabled at the start.
     */
    private void disableElements()
    {
        btnSave.setDisable(true);
        txtTime.setDisable(true);
        txtFile.setDisable(true);
        txtArtist.setFocusTraversable(false);
        txtTitle.setFocusTraversable(false);
    }
    
    /**
     * Sets the mode to editing mode and sets the song to edit.
     * Also sets the elements on a screen to for editing song data.
     * 
     * @param song The song to edit.
     */
    public void setEditingMode(Song song)
    {
        editing = true;
        editingSong = song;
        btnChoosePath.setDisable(true);
        txtTitle.setText(editingSong.getTitle());
        txtArtist.setText(editingSong.getArtist());
        cmbGenre.setValue(editingSong.getGenre());
        txtFile.setText(editingSong.getPath());       
        txtTime.setText(editingSong.getTimeInString());
    }
    
    /**
     * Checks if every elements with songs data is filled - if yes, enables the save button.
     * Otherwise disables save button.
     */
    private void checkInputs()
    {
        if(!(txtArtist.getText().isEmpty()) && !(txtTitle.getText().isEmpty()) && cmbGenre.getValue() != null && !(txtFile.getText().isEmpty()))
        {
            btnSave.setDisable(false);
        }
        else
        {
            btnSave.setDisable(true);
        }
    }
    
    /**
     * Sets the text field with time to the time of the given song file.
     * 
     * @param selectedFile The file which time should be specified.
     */
    private void setTimeField(File selectedFile) 
    {
        Media mediaFile = new Media(selectedFile.toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(mediaFile);
        mediaPlayer.setOnReady(new Runnable()
            {
                @Override
                public void run()
                {
                    int timeOfSong = (int) mediaFile.getDuration().toSeconds();
                    txtTime.setText(model.getTimeInString(timeOfSong));
                }
            }      
        );
    }
    
    /**
     *  Sets the genre in combo box with genres to the given string.
     * If genre wasnt in combo box before - its adding it.
     * 
     * @param genre The genre to set.
     */
    private void setGenre(String genre)
    {
        if(!cmbGenre.getItems().contains(genre))
        {
            cmbGenre.getItems().add(genre);
        }
        cmbGenre.getSelectionModel().select(genre);
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
