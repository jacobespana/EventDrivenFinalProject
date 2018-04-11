package EnhancedMediaPlayer;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 *
 * @author Jacob Espana <joespana@neiu.edu>
 */
public class EnhancedMediaPlayer extends Application
{
    Scene scene;
    BorderPane bPane = new BorderPane();
    
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
        
        HBox hb_bottomParent = new HBox(10);
        HBox hb_btnHolder = new HBox(8);
        HBox hb_sliderHolder = new HBox();
        hb_sliderHolder.prefWidthProperty().bind(bPane.widthProperty().divide(1.5));
        hb_bottomParent.getChildren().addAll(hb_btnHolder, hb_sliderHolder);
        bPane.setBottom(hb_bottomParent);
        hb_btnHolder.prefWidthProperty().bind(bPane.widthProperty().divide(2.3));
        
        Button btn_prevSong = new Button("<<<");
        Button btn_scrubBack = new Button("<<");
        Button btn_play_pause = new Button(">");
        Button btn_scrubForward = new Button(">>");
        Button btn_nextSong = new Button(">>>");
        hb_btnHolder.getChildren().addAll(btn_prevSong, btn_scrubBack, btn_play_pause, btn_scrubForward, btn_nextSong);
        hb_btnHolder.setPadding(new Insets(0,40,50,40));
        
        Slider sld_scrubber = new Slider();
        sld_scrubber.setValue(0);
        sld_scrubber.prefWidthProperty().bind(hb_sliderHolder.widthProperty().divide(2));
        Slider sld_volume = new Slider();
        sld_volume.setValue(50);
        
        
        hb_sliderHolder.getChildren().addAll(sld_scrubber, sld_volume);
       
        
        
        
    }
}
