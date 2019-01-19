package mytunes.gui.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.EventObject;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import mytunes.be.Playlist;
import mytunes.be.Song;
import mytunes.bll.util.TimeConverter;
import mytunes.gui.PlayingMode;
import mytunes.gui.model.MainModel;
import mytunes.gui.util.WarningDisplayer;
import mytunes.gui.util.WindowDecorator;

/**
 * The {@code MainViewController} class is a controller for
 * {@code MainView}. It performs all actions connected with playing song.
 * It is als responsible for displaying views for operating on songs and playlists.
 * 
 * @author schemabuoi
 * @author kiddo
 */
public class MainViewController implements Initializable {
    
    private MainModel model;
    private MediaPlayer mediaPlayer;
    private boolean songTimeChanged = false;
    private double previousVolume;
    private Timeline stopPlayer;
    private long timeOfPlayingLastSong = System.currentTimeMillis();
    private WarningDisplayer warningDisplayer;
    private double xOffset;
    private double yOffset;
    private boolean buttonPlaySelected;

    @FXML
    private ToggleButton btnPlaySong;
    @FXML
    private Button btnNextSong;
    @FXML
    private Button btnPreviousSong;
    @FXML
    private TableView<Song> tblSongs;
    @FXML
    private TableColumn<Song, String> colSongTitle;
    @FXML
    private TableColumn<Song, String> colSongArtist;
    @FXML
    private TableColumn<Song, String> colSongGenre;
    @FXML
    private TableColumn<Song, Integer> colSongTime;
    @FXML
    private TableView<Playlist> tblPlaylists;
    @FXML
    private TableColumn<Playlist, String> colPlaylistName;
    @FXML
    private TableColumn<Playlist, Integer> colPlaylistSongs;
    @FXML
    private TableColumn<Playlist, Integer> colPlaylistTime;
    @FXML
    private ListView<Song> lstPlaylistSongs;
    @FXML
    private Button btnEditPlaylist;
    @FXML
    private Button btnDeletePlaylist;
    @FXML
    private Button btnMoveUpOnPlaylist;
    @FXML
    private Button btnMoveDownOnPlaylist;
    @FXML
    private Button btnDeleteSongFromPlaylist;
    @FXML
    private Button btnEditSong;
    @FXML
    private Button btnDeleteSongFromSongs;
    @FXML
    private Button btnAddSongToPlaylist;
    @FXML
    private Label labelCurrentSong;
    @FXML
    private Slider sldVolume;
    @FXML
    private TextField txtSearchSongs;
    @FXML
    private TextField txtSearchPlaylists;
    @FXML
    private Slider sldTime;
    @FXML
    private Label lblSongEndTime;
    @FXML
    private Label lblSongCurrentTime;
    @FXML
    private ToggleButton btnMute;
    @FXML
    private ToggleButton btnRepeat;
    @FXML
    private Button btnNewPlaylist;
    @FXML
    private ToggleButton btnShuffle;
   
    /**
     * Creates a connection with {@code MainModel instance}.
     */
    public MainViewController()
    {
        model = MainModel.createInstance();
        warningDisplayer = new WarningDisplayer();
    }
    
    /**
     * Loads all the data to tables, creates a listeners for sliders and sets the elements 
     * to their initial state on the {@code MainView}
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setPlayButton();
        disableElements();
        createSliderListeners();
        loadData();
    }
    
    /**
     * Disables all elements that should be disabled at a starting point of the {@code MainView}.
     */
    private void disableElements()
    {
        btnEditSong.setDisable(true);
        btnAddSongToPlaylist.setDisable(true);
        btnDeleteSongFromSongs.setDisable(true);
        btnEditPlaylist.setDisable(true);
        btnDeletePlaylist.setDisable(true);
        btnMoveUpOnPlaylist.setDisable(true);
        btnMoveDownOnPlaylist.setDisable(true);
        btnDeleteSongFromPlaylist.setDisable(true);
        btnPreviousSong.setDisable(true);
        btnNextSong.setDisable(true);
        sldTime.setDisable(true);
        btnMute.setDisable(true);
    }
    
    /**
     * Invokes methods for creating listeners for Volume and Time sliders.
     */
    public void createSliderListeners()
    {
        createVolumeSliderListener();
        createTimeSliderListener();
    }
    
    /**
     * Creates a listener vor volume slider. When value of volume slider is changing it also
     * changes the actual volume of the {@code mediaPlayer} object.
     */
    private void createVolumeSliderListener()
    {
        sldVolume.valueProperty().addListener(new ChangeListener()
            {
                @Override
                public void changed(ObservableValue arg0, Object arg1, Object arg2)
                {
                    if(btnMute.isSelected() && getVolume() != 0)
                    {
                        btnMute.setSelected(false);
                    }
                    else if(!btnMute.isSelected() && getVolume() == 0)
                    {
                        previousVolume=0;
                        btnMute.setSelected(true);
                    }
                    if(mediaPlayer != null)
                    {
                        mediaPlayer.setVolume(getVolume());
                    }
                }
            }     
        );
    }
    
    /**
     * Creates a listener for time slider. When value of time slider is changing it sets
     * the label with current song time to the value of the slider converted to proper format
     * using {@code TimeConverter} class.
     */
    private void createTimeSliderListener()
    {
        sldTime.valueProperty().addListener(new ChangeListener()
            {
                @Override
                public void changed(ObservableValue arg0, Object arg1, Object arg2)
                {
                    lblSongCurrentTime.setText(TimeConverter.convertToString((int)sldTime.getValue()));
                }
            }     
        );
    }
    
    /**
     * Loads the data to the tables.
     */
    private void loadData()
    {
        //load playlists
        colPlaylistName.setCellValueFactory(new PropertyValueFactory("name"));
        colPlaylistSongs.setCellValueFactory(new PropertyValueFactory("numberOfSongs"));
        colPlaylistTime.setCellValueFactory(new PropertyValueFactory("timeInString"));
        tblPlaylists.setItems(model.getPlaylists());
        
        //load songs
        colSongTitle.setCellValueFactory(new PropertyValueFactory("title"));
        colSongArtist.setCellValueFactory(new PropertyValueFactory("artist"));
        colSongGenre.setCellValueFactory(new PropertyValueFactory("genre"));
        colSongTime.setCellValueFactory(new PropertyValueFactory("timeInString"));
        tblSongs.setItems(model.getSongs());
    }
    
    /**
     * Method which is invoked everytime some key is typed on a text field for filtering songs.
     * It uses {@code MainModel} for getting filtered list of songs
     * and sets the Table View with songs to results received from {@code MainModel}.
     */
    @FXML
    private void inputSearchSongs(KeyEvent event) {
        String filter = txtSearchSongs.getText().trim();
        if(filter.isEmpty())
        {
            tblSongs.setItems(model.getSongs());
        }
        else
        {
            tblSongs.setItems(model.getFilteredSongs(filter));
        }
    }
    
    /**
     * Method which is invoked everytime some key is typed on a text field for filtering playlists.
     * It uses {@code MainModel} for getting filtered list of playlists
     * and sets the Table View with playlists to results received from {@code MainModel}.
     */
    @FXML
    private void inputSearchPlaylists(KeyEvent event) {
        String filter = txtSearchPlaylists.getText().trim();
        if(filter.isEmpty())
        {
            tblPlaylists.setItems(model.getPlaylists());
        }
        else
        {
            tblPlaylists.setItems(model.getFilteredPlaylists(filter));
        }
    }
    
    /**
     * Method which is invoked after clicking Play/Stop button on the {@code MainView}.
     * It is performing proper action depending on {@code mediaPlayer} state.
     */
    @FXML
    private void clickPlay(ActionEvent event) {
        if(mediaPlayer == null)
        {
            Song songToPlay = model.getSong();
            if(songToPlay != null)
            {
                playSong(songToPlay, PlayingMode.SONG_LIST);
            }
            else
            {
                Stage currentStage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
                warningDisplayer.displayError(currentStage, "Cannot play a song", "Your list of songs is empty");
            }
            
        }
        else if(mediaPlayer.getStatus().equals(Status.PLAYING))
        {
            stopSong();
        }
        else
        {
            resumeSong();
        }
    }
    
    /**
     * Stops the {@code mediaPlayer}. Uses the Timeline on volume property
     * of mediaPlayer to achieve the fading effect on stopping song.
     */
    private void stopSong()
    {
        stopPlayer.play();
        switchPlayButton();
    }
    
    /**
     * Resumes the {@code mediaPlayer}. Uses the Timeline on volume property
     * of mediaPlayer to achieve the fading effect on resuming song. It firstly resumes
     * mediaPlayer then invokes play method on Timeline object for fading the volume.
     */
    private void resumeSong()
    {
        Timeline resumePlayer = new Timeline(
            new KeyFrame(Duration.seconds(0.30),
                new KeyValue(mediaPlayer.volumeProperty(), getVolume())));
        mediaPlayer.play();
        resumePlayer.play();
        switchPlayButton();
    }
    
    /**
     * Method which is invoked after clicking Next Song button on the
     * {@code MainView}. Invokes the methods for getting the next song from model
     * and for playing the song.
     */
    @FXML
    private void clickNextSong(ActionEvent event) {    
        if(isPlayable())
        {
            Song songToPlay = model.getNextSong();
            playSong(songToPlay, model.getCurrentPlayingMode());
        }
    }

    /**
     * Method which is invoked after clicking Previous Song button on the
     * {@code MainView}. Invokes the methods for getting the previous song from {@code MainModel}.
     * and for playing the song.
     */
    @FXML
    private void clickPreviousSong(ActionEvent event) {
        if(isPlayable())
        {
            Song songToPlay = model.getPreviousSong();
            playSong(songToPlay, model.getCurrentPlayingMode());
        }
    }
    
    /**
     * Method which is invoked after clicking shuffle button on the
     * {@code MainView}. Invokes method for switching shuffle mode in {@code MainModel}.
     */
    @FXML
    private void clickShuffle(ActionEvent event) {
        model.switchShuffling();
    }
    
    /**
     * Method which is invoked after clicking mute button. It is 
     * invoking method for setting the volume of {@code mediaPlayer} with value which depends
     * on the mute button state.
     */
    @FXML
    private void clickMute(ActionEvent event) {
        if(btnMute.isSelected())
        {
            previousVolume = getVolume();
            setVolume(0);
        }
        else
        {
            if(getVolume() == 0)
            {
                btnMute.setSelected(true);
            }
            setVolume(previousVolume);
        }
    }

    /**
     * Method which is invoked after dropping time slider. It is setting
     * the time of the mediaPlayer to the value of slider with time.
     */
    @FXML
    private void dropTimeSlider(MouseEvent event) {
        mediaPlayer.seek(Duration.seconds(sldTime.getValue()));
        songTimeChanged=true;
    }

    /**
     * Method which is invoked after clicking on Table View with songs.
     * It checks if item on the list was double-clicked - if yes it invokes method
     * for playing clicked song. If item was single-clicked it enables the proper buttons
     * on the screen.
     */
    @FXML
    private void clickOnSongs(MouseEvent event) {
        if(tblSongs.getSelectionModel().getSelectedItem() != null)
        {
            if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2)
            {
                Song selectedSong = tblSongs.getSelectionModel().getSelectedItem();
                playSong(selectedSong, PlayingMode.SONG_LIST);
            }
            else
            {
                enableButtonsForSongs();
                if(tblPlaylists.getSelectionModel().getSelectedItem() != null)
                {
                    btnAddSongToPlaylist.setDisable(false);
                }
            }
        }
    }
    
    /**
     * Method which is invoked after clicking on Table View with playlists.
     * It checks if item on the list was double-clicked - if yes it invokes method
     * for playing the proper song from clicked playlist. If item was single-clicked it enables the proper buttons
     * on the screen and sets the List View with songs on selected playlist.
     */
    @FXML
    private void clickOnPlaylists(MouseEvent event) {
        disableButtonsForPlaylistSongs();
        if(tblPlaylists.getSelectionModel().getSelectedItem() != null)
        {
            Playlist selectedPlaylist = tblPlaylists.getSelectionModel().getSelectedItem();
            if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2)
            {
                Song songToPlay = model.getSongFromPlaylist(selectedPlaylist);
                if(songToPlay != null)
                {
                    playSong(songToPlay, PlayingMode.PLAYLIST);
                    model.setCurrentlyPlayingPlaylist(selectedPlaylist);
                }
            }
            else
            {
                enableButtonsForPlaylists();
                model.setPlaylistSongs(selectedPlaylist);
                lstPlaylistSongs.setItems(model.getPlaylistSongs());
                if(tblSongs.getSelectionModel().getSelectedItem() != null)
                {
                    btnAddSongToPlaylist.setDisable(false);
                }
            }
        }
    }
    
    /**
     * Method which invoked after clicking on a List View with songs on selected playlist.
     * It invokes method for playing the proper song from clicked playlist.
     */
    @FXML
    private void clickOnPlaylistSongs(MouseEvent event) {
        if(lstPlaylistSongs.getSelectionModel().getSelectedItem() != null)
        {
            Playlist selectedPlaylist = tblPlaylists.getSelectionModel().getSelectedItem();
            Song selectedSong = lstPlaylistSongs.getSelectionModel().getSelectedItem();
            if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2)
            {              
                playSong(selectedSong, PlayingMode.PLAYLIST);
                model.setCurrentlyPlayingPlaylist(selectedPlaylist);
            }
            else
            {                
                btnDeleteSongFromPlaylist.setDisable(false);
                if(selectedPlaylist.getPositionOfSong(selectedSong) == 0)
                {
                    btnMoveUpOnPlaylist.setDisable(true);
                }
                else
                {
                    btnMoveUpOnPlaylist.setDisable(false);
                }
                if(selectedPlaylist.getPositionOfSong(selectedSong) == selectedPlaylist.getNumberOfSongs() - 1)
                {
                    btnMoveDownOnPlaylist.setDisable(true);  
                }
                else
                {
                    btnMoveDownOnPlaylist.setDisable(false);  
                }
            }
        }
    }
    
    /**
     * Method which is invoked after clicking New Playlist button on the {@code MainView}.
     * It creates and shows a new stage for creating new playlist.
     */
    @FXML
    private void clickNewPlaylist(ActionEvent event) throws IOException {
        Stage currentStage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
        WindowDecorator.fadeOutStage(currentStage);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mytunes/gui/view/PlaylistView.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("New Playlist");
        stage.setScene(new Scene(root));  
        stage.initStyle(StageStyle.UNDECORATED);
        stage.showAndWait();
        WindowDecorator.fadeInStage(currentStage);
    }
    
    /**
     * Method which is invoked after clicking Edit Playlist button on the {@code MainView}.
     * It creates and shows a new stage for editing playlist which is selected on Table View.
     */
    @FXML
    private void clickEditPlaylist(ActionEvent event) throws IOException {
        Stage currentStage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
        WindowDecorator.fadeOutStage(currentStage);
        Playlist selectedPlaylist = tblPlaylists.getSelectionModel().getSelectedItem();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mytunes/gui/view/PlaylistView.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        PlaylistViewController controller = (PlaylistViewController) fxmlLoader.getController();
        controller.setEditingMode(selectedPlaylist);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Edit Playlist");
        stage.setScene(new Scene(root));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.showAndWait();
        WindowDecorator.fadeInStage(currentStage);
    }

    /**
     * Method which is invoked after clicking Delete Playlist button on the {@code MainView}.
     * It sends the request to the {@code MainModel} for the playlist selected on a Table View with playlists.
     */
    @FXML
    private void clickDeletePlaylist(ActionEvent event) {
        Playlist selectedPlaylist = tblPlaylists.getSelectionModel().getSelectedItem();
        Stage currentStage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
        Optional<ButtonType> action = warningDisplayer.displayConfirmation(currentStage, "Confirmation", 
                "Are you sure you want to delete \"" + selectedPlaylist.getName() + "\" from your playlists?");
        if(action.get() == ButtonType.OK)
        {
            model.deletePlaylist(selectedPlaylist);
            tblPlaylists.getSelectionModel().clearSelection();
            model.clearPlaylistSongs();
            btnAddSongToPlaylist.setDisable(true);
            disableButtonsForPlaylists();
            disableButtonsForPlaylistSongs();
        }
    }
    
    /**
     * Method which is invoked after clicking Move Song Up button on the {@code MainVIew}.
     * It sends the request to the {@code MainModel} for moving selected song up on playlist.
     * It also enables and siables the proper buttons.
     */
    @FXML
    private void clickMoveUpOnPlaylist(ActionEvent event) {
        Playlist selectedPlaylist = tblPlaylists.getSelectionModel().getSelectedItem();
        Song selectedSong = lstPlaylistSongs.getSelectionModel().getSelectedItem();
        model.moveSongUpOnPlaylist(selectedPlaylist, selectedSong);
        lstPlaylistSongs.getSelectionModel().select(selectedPlaylist.getPositionOfSong(selectedSong));
        if(selectedPlaylist.getPositionOfSong(selectedSong) == 0)
        {
            btnMoveUpOnPlaylist.setDisable(true);
            btnMoveDownOnPlaylist.setDisable(false);
        }
        else
        {
            btnMoveUpOnPlaylist.setDisable(false);
            btnMoveDownOnPlaylist.setDisable(false);
        }
    }
    
    /**
     * Method which is invoked after clicking Move Song Down button on the {@code MainVIew}.
     * It sends the request to the {@code MainModel} for moving selected song down on playlist.
     * It also enables and siables the proper buttons.
     */
    @FXML
    private void clickMoveDownOnPlaylist(ActionEvent event) {
        Playlist selectedPlaylist = tblPlaylists.getSelectionModel().getSelectedItem();
        Song selectedSong = lstPlaylistSongs.getSelectionModel().getSelectedItem();
        model.moveSongDownOnPlaylist(selectedPlaylist, selectedSong);
        lstPlaylistSongs.getSelectionModel().select(selectedPlaylist.getPositionOfSong(selectedSong));
        if(selectedPlaylist.getPositionOfSong(selectedSong) == selectedPlaylist.getNumberOfSongs()-1)
        {
            btnMoveDownOnPlaylist.setDisable(true);
            btnMoveUpOnPlaylist.setDisable(false);
        }
        else
        {
            btnMoveDownOnPlaylist.setDisable(false);
            btnMoveUpOnPlaylist.setDisable(false);            
        }
    }
    
    /**
     * Method which is invoked after clicking Delete song on playlist button on
     * {@code MainView}. It sends a request to the {@code MainModel} for deleting song selected
     * on a list view with songs on playlist from selected playlist.
     */
    @FXML
    private void clickDeleteSongInPlaylist(ActionEvent event) {
        Song selectedSong = lstPlaylistSongs.getSelectionModel().getSelectedItem();
        Playlist selectedPlaylist = tblPlaylists.getSelectionModel().getSelectedItem();
        Stage currentStage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
        Optional<ButtonType> action = warningDisplayer.displayConfirmation(currentStage, "Confirmation", 
                "Are you sure you want to delete \"" + selectedSong.getTitle() + "\" from \"" + selectedPlaylist.getName() + "\"?");;
        if(action.get() == ButtonType.OK)
        {
            model.deleteSongFromPlaylist(selectedPlaylist, selectedSong);
            tblPlaylists.getSelectionModel().select(selectedPlaylist);
        }
    }

    /**
     * Method which is invoked after clicking New Song button on the {@code MainView}.
     * It creates and shows a new stage for creating new song.
     */
    @FXML
    private void clickNewSong(ActionEvent event) throws IOException {
        Stage currentStage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
        WindowDecorator.fadeOutStage(currentStage);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mytunes/gui/view/SongView.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("New Song");
        stage.setScene(new Scene(root));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.showAndWait();
        WindowDecorator.fadeInStage(currentStage);
    }

    /**
     * Method which is invoked after clicking Edit Song button on the {@code MainView}.
     * It creates and shows a new stage for editing song which is selected on Table View.
     */
    @FXML
    private void clickEditSong(ActionEvent event) throws IOException {
        Stage currentStage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
        WindowDecorator.fadeOutStage(currentStage);
        Song selectedSong = tblSongs.getSelectionModel().getSelectedItem();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mytunes/gui/view/SongView.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        SongViewController controller = (SongViewController) fxmlLoader.getController();
        controller.setEditingMode(selectedSong);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Edit Song");
        stage.setScene(new Scene(root));  
        stage.initStyle(StageStyle.UNDECORATED);
        stage.showAndWait();
        WindowDecorator.fadeInStage(currentStage); 
    }

    /**
     * Method which is invoked after clicking Delete Song button on the {@code MainView}.
     * It sends the request to the {@code MainModel} for the song selected on a Table View with playlists.
     */
    @FXML
    private void clickDeleteSong(ActionEvent event) {
        Song selectedSong = tblSongs.getSelectionModel().getSelectedItem();
        Stage currentStage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
        Optional<ButtonType> action = warningDisplayer.displayConfirmation(currentStage, "Confirmation", 
                "Are you sure you want to delete \"" + selectedSong.getTitle() + "\" from your songs?");;
        if(action.get() == ButtonType.OK)
        {
            model.deleteSong(selectedSong);
            tblSongs.getSelectionModel().clearSelection();
            btnAddSongToPlaylist.setDisable(true);
            btnEditSong.setDisable(true);
            btnDeleteSongFromSongs.setDisable(true);
        }
    }

    /**
     * Method which is invoked after clicking add song to palylist button on the {@code MainView}
     * It sends a request to {@code MainModel} for adding a selected song to the selected playlist.
     */
    @FXML
    private void clickAddSongToPlaylist(ActionEvent event) {
        Song selectedSong = tblSongs.getSelectionModel().getSelectedItem();
        Playlist selectedPlaylist = tblPlaylists.getSelectionModel().getSelectedItem();
        if(!selectedPlaylist.isSongOnTracklist(selectedSong))
        {
            model.addSongToPlaylist(selectedPlaylist, selectedSong);
            tblPlaylists.getSelectionModel().select(selectedPlaylist);
        }
        else
        {
            Stage currentStage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
            warningDisplayer.displayError(currentStage, "Duplicate song", "This song is already in your playlist");
        }
    }
    

    /**
     * Plays the song using {@code mediaPlayer}. If some song is currently playing it stops the current song
     * to prevent imposing two songs on each other. If path for the song is wrong it displays an error.
     * 
     * @param songToPlay The song to play.
     * @param mode The proper playing mode in which song should be played.
     */
    private void playSong(Song songToPlay, PlayingMode mode)
    {
        if(mediaPlayer != null && mediaPlayer.getStatus().equals(Status.PLAYING))
        {
            mediaPlayer.stop();
        }
        try
        {
            File fileSong = new File(songToPlay.getPath());
            Media song = new Media(fileSong.toURI().toString());
            if(mediaPlayer == null)
            {
                enablePlayingButtons();
            }
            mediaPlayer = new MediaPlayer(song);
            setMediaPlayer(songToPlay, mode);
            mediaPlayer.play();
        }
        catch(MediaException e)
        {
            Stage currentStage = (Stage) btnPlaySong.getScene().getWindow();
            warningDisplayer.displayError(currentStage, "Cannot play a song", "Could not find path to song \"" + songToPlay.getTitle() + "\"");
        }
    }    
    
    /**
     * Passes to the {@code MainModel} informations about currently playing song
     * and playing mode and invokes methods for setting the {@code mediaPlayer}.
     * Also adjusts elements on the {@code MainView} for given song.
     * 
     * @param songToPlay The song to be played.
     * @param mode The selected playing mode.
     */
    public void setMediaPlayer(Song songToPlay, PlayingMode mode)
    {
        setMediaPlayerSettings(songToPlay);  
        model.setCurrentlyPlayingSong(songToPlay, mode);
        mediaPlayer.setVolume(getVolume());
        lblSongCurrentTime.setText("00:00");
        lblSongEndTime.setText(songToPlay.getTimeInString());
        sldTime.setMax(songToPlay.getTime());
        labelCurrentSong.setText("Now playing: " + songToPlay.getTitle());
        btnPlaySong.setGraphic(new ImageView("/mytunes/gui/images/StopButton.png"));
        buttonPlaySelected = false;
        selectPlayedSong(songToPlay, mode);
    }
    
    /**
     * Invokes methods for setting the {@code mediaPlayer} settings.
     * 
     * @param songToPlay Th song to which mediaPlayer should be adjusted.
     */
    private void setMediaPlayerSettings(Song songToPlay)
    {
        setAutoplay();
        setTimeListener();
        setFadingForStopping();
    }
    
    /**
     * Sets the autoplay of {@code mediaPlayer}. If button repeat is selected it goes to the
     * start of the current song, if not it fires the Next Song button.
     * 
     */
    private void setAutoplay()
    {
        mediaPlayer.setOnEndOfMedia(new Runnable()
            {
                @Override
                public void run()
                {
                    if(btnRepeat.isSelected())
                    {
                        mediaPlayer.seek(Duration.ZERO);
                    }
                    else
                    {
                        btnNextSong.fire();
                    }
                }
            }      
        );
    }
    
    /**
     * Sets the listener for time property of the {@code mediaPlayer}.
     * Everytime time property is changing it sets the value of time slider to the value
     * compatible with time of the song.
     */
    private void setTimeListener()
    {
        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>()
            {
                @Override
                public void changed(ObservableValue arg0, Duration arg1, Duration arg2)
                {
                    if(!sldTime.isPressed() && !songTimeChanged)
                    {
                        sldTime.setValue(arg2.toSeconds());
                    }
                    else if(songTimeChanged)
                        songTimeChanged=false;
                }
            }
        );
    }
    
    /**
     * Sets the Timeline object for fading the volume while stopping the song.
     * After fading out the volume, {@code mediaPlayer} would be paused.
     */
    private void setFadingForStopping()
    {
        stopPlayer = new Timeline(
            new KeyFrame(Duration.seconds(0.30),
                new KeyValue(mediaPlayer.volumeProperty(), 0)));
        stopPlayer.setOnFinished(new EventHandler()
            {
                @Override
                public void handle(Event event)
                {
                    mediaPlayer.pause();
                }
            }       
        );
    }
    
    /**
     * It selects the playing song on the proper table depending on the
     * current Playing Mode.
     * 
     * @param playedSong Currently playing song.
     * @param mode Current playing mode.
     */
    private void selectPlayedSong(Song playedSong, PlayingMode mode)
    {
        if(mode == PlayingMode.PLAYLIST)
        {

        }
        else
        {
            tblSongs.getSelectionModel().select(playedSong);
        }
    }
    
    /**
     * Returns the information if song can be played. It checks if enough time has passed since
     * previous song was played. It prevents a bug while
     * spamming on next/previous song button and playing multiple songs at the same time.
     * 
     * @return true if song can be played.
     */
    private boolean isPlayable()
    {
        if(System.currentTimeMillis() - timeOfPlayingLastSong < 200)
        {
            return false;
        }
        timeOfPlayingLastSong =  System.currentTimeMillis();
        return true;
    }
    
    /**
     * Switches the image of Play/Stop button.
     */
    private void switchPlayButton()
    {
        if(buttonPlaySelected)
        {
            btnPlaySong.setGraphic(new ImageView("/mytunes/gui/images/StopButtonWhite.png"));
            buttonPlaySelected = false;
        }
        else
        {
            btnPlaySong.setGraphic(new ImageView("/mytunes/gui/images/PlayButtonWhite.png"));
            buttonPlaySelected = true;
        }
    }
    
    /**
     * Sets the image and invokes methods for setting animations of play/stop button.
     */
    private void setPlayButton()
    {
        btnPlaySong.setGraphic(new ImageView("/mytunes/gui/images/PlayButton.png"));
        buttonPlaySelected = true;
        setPlayButtonHoverIn();
        setPlayButtonHoverOut();
    }
    
    /**
     * Sets the Hover in animation of play/stop button.
     */
    private void setPlayButtonHoverIn()
    {
        btnPlaySong.setOnMouseEntered(new EventHandler() 
            {
                @Override
                public void handle(Event event) 
                {
                    if(buttonPlaySelected)
                    {
                        btnPlaySong.setGraphic(new ImageView("/mytunes/gui/images/PlayButtonWhite.png"));
                    }
                    else
                    {
                        btnPlaySong.setGraphic(new ImageView("/mytunes/gui/images/StopButtonWhite.png"));
                    }
                }
            
            }
        );
    }
    
    /**
     * Sets the Hover out animation of play/stop button
     */
    private void setPlayButtonHoverOut()
    {
        btnPlaySong.setOnMouseExited(new EventHandler() 
            {
                @Override
                public void handle(Event event) 
                {
                    if(buttonPlaySelected)
                    {
                        btnPlaySong.setGraphic(new ImageView("/mytunes/gui/images/PlayButton.png"));
                    }
                    else
                    {
                        btnPlaySong.setGraphic(new ImageView("/mytunes/gui/images/StopButton.png"));
                    }
                }
            
            }
        );
    }
    
    /**
     * Enables songs buttons.
     */
    private void enableButtonsForSongs() {
        btnEditSong.setDisable(false);
        btnDeleteSongFromSongs.setDisable(false);
    }
    
    /**
     * Enables playlists buttons.
     */
    private void enableButtonsForPlaylists() 
    {
        btnEditPlaylist.setDisable(false);
        btnDeletePlaylist.setDisable(false);
    }
    
    /**
     * Enables buttons for manipulating song.
     */
    private void enablePlayingButtons() 
    {
        btnPreviousSong.setDisable(false);
        btnNextSong.setDisable(false);
        sldTime.setDisable(false);
        btnMute.setDisable(false);
    }
    
    /**
     * Disables playlists buttons.
     */
    private void disableButtonsForPlaylists() 
    {
        btnEditPlaylist.setDisable(true);
        btnDeletePlaylist.setDisable(true);
    }
    
    /**
     * Disable buttons for songs on playlist.
     */
    private void disableButtonsForPlaylistSongs()
    {
        btnDeleteSongFromPlaylist.setDisable(true);
        btnMoveUpOnPlaylist.setDisable(true);
        btnMoveDownOnPlaylist.setDisable(true);
    }
    
    /**
     * Sets the value of volume slider to the scaled value in range 0.0 to 10.0.
     * 
     * @param volume real volume value in range 0.0 to 1.0
     */
    public void setVolume(double volume)
    {
        sldVolume.setValue(volume*10);
    }
    
    /**
     * Returns the real volume value in range 0.0 to 1.0 by scaling it
     * from value getted from the volume slider in range 0.0 to 10.0.
     * 
     * @return real volume value in range 0.0 to 1.0
     */
    public double getVolume()
    {
        return sldVolume.getValue()/10;
    }

    /**
     * Method which is invoked after clicking on Close
     * button on {@code CreateUserView}. It is closing the
     * current stage.
     */
    @FXML
    private void clickClose(ActionEvent event) {
        Platform.exit();
    }
    
    /**
     * Method which is invoked after clicking on Minimalize
     * button on {@code CreateUserView}. It is minimalizing the
     * current stage.
     */
    @FXML
    private void clickMinimalize(ActionEvent event) {
        Stage stage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
        stage.setIconified(true);
    }
    
    /**
     * Method which is invoked after clicking on a top stage bar. It performs a moving stage
     * while dragging top stage bar.
     */
    @FXML
    private void clickMouseDragged(MouseEvent event) {
        Stage stage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() + xOffset);
        stage.setY(event.getScreenY() + yOffset);
    }

    /**
     * Method which is invoked after clicking on a top stage bar. It sets the data necessary for moving
     * a stage.
     */
    @FXML
    private void clickMousePressed(MouseEvent event) {
        Stage stage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
        xOffset = stage.getX() - event.getScreenX();
        yOffset = stage.getY() - event.getScreenY();
    }
}
