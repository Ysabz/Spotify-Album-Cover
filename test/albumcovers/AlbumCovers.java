/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package albumcovers;

import java.util.ArrayList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author bergeron
 */
public class AlbumCovers extends Application {   
    
    @Override
  
    public void start(Stage stage) throws Exception {

        // Temporary code to debug the spotify controller...
        // Retrieve the beatles id from the spotify web api (it should return "3WrFJ7ztbogyGnTHbHJFl2")
      
       // System.out.println("The beatles id is: " + beatlesID);
        
        // Retrieve albums covers URL from the beatles
       // ArrayList<String> albums = SpotifyController.getAlbumCoversFromArtist(artistID);
        //System.out.println("Beatles albums URL are: " + albums);
        
        // This will launch the user interface!!
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
