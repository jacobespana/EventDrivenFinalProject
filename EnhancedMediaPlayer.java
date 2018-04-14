package EnhancedMediaPlayer;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    ListView lv_playlist;
    Button btn_prevSong, btn_play_pause, btn_nextSong, btn_scrubBack, btn_scrubForward, btn_addSong;
    Slider sld_volume, sld_scrubber;
    
    public static void main(String[] args)
    {
        Application.launch(args);
    }
    
    @Override
    public void start(Stage primaryStage)
    {
        scene = new Scene(bPane, 800,600);
        setUpMediaPane();
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
        btn_play_pause.setOnMouseClicked(e -> {
            if(btn_play_pause.getText().equals(">"))
                btn_play_pause.setText("||");
            else 
                btn_play_pause.setText(">");
        });
        
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
        lv_playlist = new ListView();
        vb_playlistContain.setPadding(new Insets(40, 50, 0, 20));
        btn_addSong = new Button("+");
        vb_playlistContain.getChildren().addAll(lv_playlist, btn_addSong);
        bPane.setRight(vb_playlistContain);
        
        HBox hb_imgHolder = new HBox();
        hb_imgHolder.prefWidthProperty().bind(bPane.widthProperty().multiply(.45));
        hb_imgHolder.prefHeightProperty().bind(bPane.heightProperty().multiply(.45));
        hb_imgHolder.setAlignment(Pos.CENTER);
        
        /*temporary holder to show where album art will go. This
        will be replaced down the line with a method to pull metadata
        from mp3 file.
        */
        Image img_AlbumArt = new Image("EnhancedMediaPlayer\\download.jpg");
        ImageView iv_AlbumArt = new ImageView(img_AlbumArt);
        iv_AlbumArt.preserveRatioProperty();
        
        hb_imgHolder.getChildren().add(iv_AlbumArt);
        
        bPane.setCenter(hb_imgHolder);
        
        
        
       
        
        
        
    }
}
