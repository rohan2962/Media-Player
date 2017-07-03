/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mediaplayer;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author RAGHAV SABOO
 */
public class PlaylistController implements Initializable {

    @FXML
    private Button add;
    @FXML
    private Button remove;
    @FXML
    private Button cancel;
    @FXML
    private Button play;
    @FXML
    private ListView<Text> list;

    private ObservableList<Text> labels
            = FXCollections.observableArrayList();

    FileChooser ch = new FileChooser();

    int remid;
    File f;
    ArrayList<String> plist1 = new ArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        plist1.addAll(mediaplayer.plist);
        mediaplayer.plist.clear();
        play.setDisable(true);
        for (String s : plist1) {
            f = new File(s);
            addfile();
            play.setDisable(false);
        }
        list.setItems(labels);
        remove.setDisable(true);
        try {
            ch.setInitialDirectory(f.getParentFile());
        } catch (Exception e) {

        }
        add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage primaryStage = (Stage) add.getScene().getWindow();
                try {
                    List<File> fil = ch.showOpenMultipleDialog(primaryStage);

                    for (File f2 : fil) {
                        f = f2;
                        addfile();
                    }
                } catch (Exception e) {

                }

            }
        });
        remove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                removefile();
            }
        });
        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage primaryStage = (Stage) add.getScene().getWindow();
                labels.clear();
                primaryStage.close();
            }
        });
        play.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage primaryStage = (Stage) add.getScene().getWindow();
                primaryStage.close();
                mediaplayer.pc = true;
            }
        });
    }

    void addfile() {
        Text l = new Text(f.getAbsolutePath());
        labels.add(l);
        mediaplayer.plist.add(f.getAbsolutePath());
        play.setDisable(false);
        l.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                remove.setDisable(false);
                remid = labels.indexOf(l);

            }
        });
    }

    void removefile() {
        mediaplayer.plist.remove(remid);
        labels.remove(remid);
        remove.setDisable(true);
        if (labels.isEmpty()) {
            play.setDisable(true);
        }
    }
}
