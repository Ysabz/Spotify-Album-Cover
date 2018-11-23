package albumcovers;
//
//Client Secret
//3483b2516f0548a4a35664679a3fd569
import java.util.ArrayList;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class FXMLDocumentController implements Initializable {

    @FXML
    private Label label;
    @FXML
    private Button searchB;
    @FXML
    private Button nextB;
    @FXML
    private Button previousB;
    @FXML
    private TextField inputTF;
    static  String artistName;
    @FXML
    private ImageView poster;
    @FXML
    ArrayList<String> albums;
    
    private void handleButtonAction(ActionEvent event) {
        artistName = inputTF.getText();
        String artistID = SpotifyController.getArtistId(artistName);
         albums = SpotifyController.getAlbumCoversFromArtist(artistID);
        
        
    
    }

   
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
