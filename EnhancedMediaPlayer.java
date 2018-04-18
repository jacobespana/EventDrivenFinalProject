package EnhancedMediaPlayer;

import java.io.File;
import javafx.util.Duration;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;


/**
 *
 * @author Jacob Espana <joespana@neiu.edu>
 */
public class EnhancedMediaPlayer extends Application
{
    Scene scene;
    BorderPane bPane = new BorderPane();
    HBox hb_btnHolder, hb_sldHolder, hb_bottomParent;
    VBox vb_playlistContain;
    ObservableList<File> files;
    File src;
    File defaultImage;
    ListView<String> lv_playlist;
    Button btn_prevSong, btn_play_pause, btn_nextSong, btn_scrubBack, btn_scrubForward, btn_addSong;
    Slider sld_volume, sld_scrubber;
    MediaPlayer mp;
    Media m;
    ImageView iv_AlbumArt;
    Image img_AlbumArt;
    
    
    //indices of current track
    private int startIndex = 0;
    private int nextIndex = startIndex;
    private int prevIndex = nextIndex;
    //Labels for title 
    private Label artist = new Label("");
    private Label title = new Label("");
    //HBoxes
    private HBox songBox;
    private HBox artistBox;
    private HBox titleBox;
    
    
    
    
    
    
    public static void main(String[] args)
    {
        Application.launch(args);
    }
    
    @Override
    public void start(Stage primaryStage)
    {
        scene = new Scene(bPane, 800,600);
        setUpMediaPane();
        mediaHandler();
        allActions();
        primaryStage.setTitle("Enhanced Media Player");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public void setUpMediaPane()
    {
        bPane.prefWidthProperty().bind(scene.widthProperty());
        bPane.prefHeightProperty().bind(scene.heightProperty());
        
        hb_bottomParent = new HBox(5);
        
        hb_btnHolder = new HBox(8);
        hb_btnHolder.prefWidthProperty().bind(hb_bottomParent.widthProperty().multiply(.4));
        hb_btnHolder.setPadding(new Insets(0, 0, 40, 60));
        
        hb_sldHolder = new HBox();
        hb_sldHolder.prefWidthProperty().bind(hb_bottomParent.widthProperty().multiply(.7));
        hb_sldHolder.setAlignment(Pos.CENTER);
        hb_sldHolder.setPadding(new Insets(0, 50, 40, 0));
        hb_bottomParent.getChildren().addAll(hb_btnHolder, hb_sldHolder);
        
        bPane.setBottom(hb_bottomParent);
        
        btn_prevSong = new Button("<<<");
        btn_scrubBack = new Button("<<");
        btn_play_pause = new Button(">");
        
        
        btn_scrubForward = new Button(">>");
        btn_nextSong = new Button(">>>");
        hb_btnHolder.getChildren().addAll(btn_prevSong, btn_scrubBack, btn_play_pause, btn_scrubForward, btn_nextSong);
        
        
        sld_scrubber = new Slider();
        sld_scrubber.setValue(0);
        sld_scrubber.setShowTickMarks(true);
        sld_scrubber.setMin(0.00);
        sld_scrubber.prefWidthProperty().bind(hb_sldHolder.widthProperty().multiply(.7));
        
        sld_volume = new Slider();
        sld_volume.setValue(50);
        sld_volume.setShowTickMarks(true);
        
        hb_sldHolder.getChildren().addAll(sld_scrubber, sld_volume);
        
        vb_playlistContain = new VBox(10);
        
        files = FXCollections.observableArrayList();
        lv_playlist = new ListView<>();
        vb_playlistContain.setPadding(new Insets(40, 50, 0, 20));
        btn_addSong = new Button("+");
        
        
        
        vb_playlistContain.getChildren().addAll(lv_playlist, btn_addSong);
        bPane.setRight(vb_playlistContain);
        
        
        HBox hb_imgHolder = new HBox();
        hb_imgHolder.prefWidthProperty().bind(bPane.widthProperty().multiply(.45));
        hb_imgHolder.prefHeightProperty().bind(bPane.heightProperty().multiply(.45));
        hb_imgHolder.setAlignment(Pos.CENTER);
        
        
        //TEST OF VBOX - GET RID OF IF NOT WORKING <<<<<<<<<<<<<<<<< ADDED 
        //ALIGNMENT OR SPACING GOTTA BE FIXED        
        VBox vbCenter = new VBox();
        vbCenter.prefWidthProperty().bind(bPane.widthProperty().multiply(.45));
        vbCenter.prefHeightProperty().bind(bPane.heightProperty().multiply(.45));
        vbCenter.setAlignment(Pos.CENTER);
        //END OF VBOX
        
                
        /*temporary holder to show where album art will go. This
        will be replaced down the line with a method to pull metadata
        from mp3 file.
        */
        
        final Reflection reflection = new Reflection(); //Adds reflection effect to artwork
        reflection.setFraction(0.2);
        //img_AlbumArt = new Image("EnhancedMediaPlayer\\defaultImg.jpg"); //will be a default artwork, cant get it to work yet
        iv_AlbumArt = new ImageView(img_AlbumArt);
        iv_AlbumArt.setPreserveRatio(true);
        
        iv_AlbumArt.setFitWidth(240);
        iv_AlbumArt.setPreserveRatio(true);
        iv_AlbumArt.setSmooth(true);
        iv_AlbumArt.setEffect(reflection);
        
        //END OF ARTWORK
        
        
        //ADDED SONG LABEL
        //HBox inception for displaying song name
        songBox = new HBox();
        artistBox = new HBox();
        titleBox = new HBox();
        artistBox.getChildren().add(artist);
        titleBox.getChildren().add(title);        
        songBox.setAlignment(Pos.CENTER);
        songBox.getChildren().addAll(artistBox, titleBox);
        //END OF SONG LABEL
       
        hb_imgHolder.getChildren().add(iv_AlbumArt);
        vbCenter.getChildren().addAll(songBox, hb_imgHolder); //put everything in a VBox
        
        
        bPane.setCenter(vbCenter);
        
         
        
        
    }
    
    public void mediaHandler(){
        
      //THIS GETS METADATA FORM A FILE
        
      try {      
        m.getMetadata().addListener(new MapChangeListener<String, Object>() {
          @Override
          public void onChanged(MapChangeListener.Change<? extends String, ? extends Object> ch) {
            if (ch.wasAdded()) {
              metadataHandler(ch.getKey(), ch.getValueAdded());
            }
          }
        });

        mp = new MediaPlayer(m);
        mp.volumeProperty().bind(sld_volume.valueProperty().divide(100)); //binds volume to slider, ADDED HERE cause it wasn't working in the allAction()
        mp.setOnError(new Runnable() {
          @Override
          public void run() {
            final String errorMessage = m.getError().getMessage();
            // Handle errors during playback
            System.out.println("MediaPlayer Error: " + errorMessage);
          }
      });

  

    } catch (RuntimeException re) {
      // Handle construction errors
      System.out.println("Caught Exception: " + re.getMessage());
    }
        
        
        
        
    }
    public void metadataHandler(String key, Object value){
        //THIS SETS ARTWORK, ARTIST, TITLE LABELS etc.
        if (key.equals("image")) {
            iv_AlbumArt.setImage((Image)value);
        }else if (key.equals("artist")) {
          artist.setText(value.toString() + " - ");
        } if (key.equals("title")) {
          title.setText(value.toString());
        }            

        
    }
    
    
    
    public void chooseFile()
    {
        FileChooser f = new FileChooser();
        f.getExtensionFilters().add(new ExtensionFilter("Mp3 Files", "M4A Files", "*.mp3", "*.m4a")); //ADDED M4A files for testing purposes
        src = f.showOpenDialog(null);
                if (src != null)
                {
                    
                    lv_playlist.getItems().add(src.getName());
                    if(files.isEmpty())
                    {
                        files.add(src);
                        createMedia(startIndex); //<- starting index added instead of something else that was here, dont remember what
                        
                    }
                    else
                        files.add(src);
                    
                    
                }
                else
                    System.out.println("No File Selected.");
    }
    
    public void allActions()
    {
        //lambda to add mp3 to listview
        btn_addSong.setOnMouseClicked(e -> {
           chooseFile(); 
        });
        
        /*lambda to control pause and play
        needs functionality once media player is created.
        */
        
        btn_play_pause.setOnAction(e->{
                if (btn_play_pause.getText().equals(">"))
                {
                   mp.play();
                   btn_play_pause.setText("||");
                }
                else 
                {
                   mp.pause();                 
                   btn_play_pause.setText(">");
                }
                      
                
        });
        
        btn_scrubBack.setOnAction(e->{
           if(mp.getCurrentTime().lessThanOrEqualTo(new Duration(5000))) 
            {
               mp.pause(); //replace this with createMedia(prevIndex);
            } else {
               mp.seek(mp.getCurrentTime().subtract(new Duration(5000)));
            }
        });
        
        
        //PREVIOUS SONG BUTTON
        //When gets back to the first song, and prevButton is clicked
        //stops the playlist
        btn_prevSong.setOnAction(e->{
            
            prevIndex = prevIndex - 1;             
            nextIndex = prevIndex;
            mp.stop();

            if(prevIndex == -1){
                
                mp.stop();
                btn_play_pause.setText(">");
                startIndex = 0;
                nextIndex = startIndex;
                createMedia(startIndex);
                
            }else{            
                createMedia(prevIndex);
                mp.play();
            }
          
            
        });
        
        //NEXT SONG BUTTON
        //Currently i set this up as a loop
        //so by clicking nextButton you can play all of the songs
        //over and over again, but we gotta change it
        
        btn_nextSong.setOnAction(e->{
            
            nextIndex = nextIndex + 1;
            prevIndex = nextIndex;
            mp.stop();
            
            if(files.size() == nextIndex){
                
                mp.stop();
                startIndex = 0;
                nextIndex = startIndex;
                createMedia(startIndex);
                
            }else{            
                createMedia(nextIndex);
            }
            mp.play();

            
            
        });
        
    }
    
    public void createMedia(int index) // <- i added a parameter 'index'
    {
        m = new Media(files.get(index).toURI().toString());
        mp = new MediaPlayer(m);
        lv_playlist.getSelectionModel().select(index);

        mediaHandler();
        //<- starts the song/playlist on start once a song is loaded
        //mp.play(); 
    }
}
