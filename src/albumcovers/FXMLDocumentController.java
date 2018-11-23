package albumcovers;
//
//Client Secret
//3483b2516f0548a4a35664679a3fd569
/* imageURL = albums.get(i);
File outputFile = new File("./images/" + artistName + "/" +  SpotifyController.
getAlbumName(artistName,i) + ".png");
BufferedImage bImage = SwingFXUtils.fromFXImage(new Image(imageURL), null);
ImageIO.write(bImage, "png", outputFile);*/

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Yasaman Sabbagh Ziarani
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private Label label;
    @FXML
    private Label counter;
    @FXML
    private Button searchB;
    @FXML
    private Button nextB;
    @FXML
    private Button previousB;
    @FXML
    private TextField inputTF;
    @FXML
    private ImageView poster;
    @FXML
    ArrayList<Image> imageAlbum = new ArrayList();
    @FXML
    private Label message;
    @FXML
    ArrayList<String> albums = new ArrayList();
    int count = 1;
    static String artistName;
    String imageURL;

    @FXML
    // clears the imageAlbum and gets the artist id and retireve the image urls 
    // for the artist albums ,calls preload() and setStarterImage()
    private void handleButtonAction(ActionEvent event) {

        imageAlbum.clear();
        count = 1;
        artistName = inputTF.getText();

        String artistID = SpotifyController.getArtistId(artistName);

        albums = SpotifyController.getAlbumCoversFromArtist(artistID);
        counter.setText("Cover " + count + "/" + albums.size());

        preload();
        setStarterImage();

    }

    /**
     * pre loads the images to the imageAlbum to facilitate loading of the next
     * and previous images
     */
    @FXML
    public void preload() {
        for (int i = 0; i < albums.size(); i++) {
            imageURL = albums.get(i);
            imageAlbum.add(new Image(imageURL));

        }
    }

    @FXML
    // saves the artist's album iimages to a file of the same name 
    private void handleSaveAsAction(ActionEvent event) {

        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        chooser.setTitle("save file");
        chooser.setInitialDirectory(new File("./images"));
        File file = chooser.showSaveDialog(message.getScene().getWindow());
        if (file != null) {

            try {
                ImageIO.write(SwingFXUtils.fromFXImage(poster.getImage(), null),
                        "png", file);
                message.setText("image saved successfully");
            } catch (Exception e) {
                message.setText("unable to save the image");
            }
        }
    }

    @FXML
    // uses FileChooser to save the current album image
    private void handleSaveAlbumAction(ActionEvent event) {
        // save all artist album covers on (./images/[ARTIST NAME]/[ALBUM_NAME].png
        String imageURL;
        ArrayList<String> albumsNames = SpotifyController.getAlbumName(artistName);
        // create folder for artist
        File folder = new File("./images/" + artistName);
        if (folder.exists() == false) {
            folder.mkdir();
        }

        for (int i = 0; i < albumsNames.size(); i++) {

            try {
                imageURL = albums.get(i);
                String albumName = albumsNames.get(i);
                System.out.println(albumName);

                File outputFile = new File("./images/" + artistName + "/"
                        + albumName + ".png");
                if (!outputFile.exists()) {

                    outputFile = new File("./images/" + artistName + "/" + i + ".png");
                    FileUtils.copyURLToFile(new URL(imageURL), outputFile);
                }
                message.setText("file saved succesfully");

            } catch (Exception e) {
                System.out.println(e.getMessage());//
                message.setText("error while saving(invalid file name!!!)");

            }

        }

    }

    @FXML
    // quits from the application 
    private void handleQuitAction(ActionEvent event) {
        System.exit(0);
    }

    /**
     * set the poster to the first album image
     */
    @FXML
    public void setStarterImage() {

        try {

            poster.setImage(imageAlbum.get(0));//new Image(imageURL2));
            message.setText(albums.size() + " albums retrieved");
        } catch (Exception e) {
            setDefaultImage();
            message.setText("Invalid artist!!");
        }
    }

    @FXML
    // sets the poster to the next image 
    private void handleNextAction(ActionEvent event) {

        if (imageAlbum.isEmpty() == false && count < imageAlbum.size()) {
            count++;
            counter.setText("Cover " + count + "/" + albums.size());
            try {
                poster.setImage(imageAlbum.get(count - 1));
                message.setText("successful process");

            } catch (Exception e) {
                message.setText("Something went wrong..." + e);
            }

        } else {
            message.setText("nothing to show");
        }

    }

    @FXML
    //sets the poster to the previous image 
    private void handlePreviousAction(ActionEvent event) {

        if (imageAlbum.isEmpty() == false && count > 1) {
            count--;
            counter.setText("Cover " + count + "/" + albums.size());

            try {

                poster.setImage(imageAlbum.get(count - 1));//new Image(imageURL));
                message.setText("successful process");

            } catch (Exception e) {
                message.setText("Something went wrong..." + e);
            }

        } else {
            message.setText("nothing to show");
        }
    }

    /**
     * sets the text of the message label to msg
     *
     * @param msg gets the message from the guiContoller
     */
    public void setMessage(String msg) {
        // message.setText(msg);
    }

    /**
     * sets the poster to the default image
     */
    @FXML
    public void setDefaultImage() {
        File logoFile = new File("images/spotify.png");
        Image logoImage = new Image(logoFile.toURI().toString());
        try {
            poster.setImage(logoImage);

        } catch (Exception i) {
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        SpotifyController.registerGUIController(this);

        setDefaultImage();
    }

}
