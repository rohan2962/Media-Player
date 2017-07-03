/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mediaplayer;

import javafx.animation.FadeTransition;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import static mediaplayer.mediaplayer.Player;

/**
 *
 * @author RAGHAV SABOO
 */
public class mediaController {

    VBox vbox;
    HBox hbox;
    HBox hbox1;
    Button b, vm, next, prev, stop;
    Slider sl;
    Slider vol;
    Label vollab;
    Label vidlab;
    ProgressBar pb, vv;
    BorderPane box;
    Image playim, pauseim, vmim, mute, stopim, previm, nextim;
    FadeTransition ft;
    StackPane sp, sp1;

    mediaController() {
        playim = new Image(getClass().getResourceAsStream("play.png"));
        pauseim = new Image(getClass().getResourceAsStream("pause.png"));
        stopim = new Image(getClass().getResourceAsStream("stop.png"));
        previm = new Image(getClass().getResourceAsStream("prev.png"));
        nextim = new Image(getClass().getResourceAsStream("next.png"));
        vbox = new VBox();
        hbox = new HBox(10);
        hbox1 = new HBox(10);
        box = new BorderPane();
        b = new Button();
        vv = new ProgressBar();
        next = new Button();
        prev = new Button();
        sl = new Slider();
        vol = new Slider();
        vm = new Button();
        stop = new Button();
        vm.setId("btn1");
        next.setId("btn1");
        prev.setId("btn1");
        stop.setId("btn1");
        vmim = new Image(getClass().getResourceAsStream("vol.png"));
        mute = new Image(getClass().getResourceAsStream("mute.png"));
        vm.setGraphic(new ImageView(vmim));
        stop.setGraphic(new ImageView(stopim));
        prev.setGraphic(new ImageView(previm));
        next.setGraphic(new ImageView(nextim));
        b.setText("PAUSE");
        b.setGraphic(new ImageView(pauseim));
        b.getStyleClass().add("btn");
        sl.setId("slider");
        vollab = new Label();
        vidlab = new Label();
        pb = new ProgressBar(0);
        pb.setId("progress-bar");
        sp = new StackPane();
        sp1 = new StackPane();
        sp.getChildren().addAll(pb, sl);
        vbox.getChildren().addAll(sp);
        vbox.getChildren().add(vidlab);
        hbox.getChildren().addAll(b, prev, stop, next);
        hbox1.getChildren().addAll(vollab);
        vv.setId("progress-bar");
        sp1.getChildren().addAll(vv, vol);

        hbox1.getChildren().addAll(sp1, vm);
        box.setTop(vbox);
        box.setLeft(hbox);
        box.setRight(hbox1);
        vidlab.setText("--/--");
        vidlab.setTextFill(Color.WHITE);
        vidlab.setFont(Font.font("Comic Sans", FontWeight.BOLD, 15));
        vidlab.setLabelFor(vol);
        ft = new FadeTransition(Duration.millis(400), box);
        vol.setId("slider");
        pb.prefWidthProperty().bind(vbox.widthProperty());
        
        vol.setMin(0.0);
        vol.setMax(1.0);
        vol.setValue(0.70);
        vv.setProgress(0.70);
        vol.setMaxWidth(vv.getWidth());
        vollab.setText("Vol:" + Integer.toString((int) (vol.getValue() * 100)) + "%");
        vollab.setTextFill(Color.WHITE);
        vollab.setFont(Font.font("Comic Sans", FontWeight.BOLD, 15));
        vollab.setLabelFor(vol);
    }

    public void play() {
        mediaplayer.Player.play();
        b.setText("PAUSE");
        b.setGraphic(new ImageView(pauseim));
    }

    public void pause() {
        mediaplayer.Player.pause();
        b.setText("PLAY");
        b.setGraphic(new ImageView(playim));
    }

    public void mute() {
        vol.setValue(0.0);
        vm.setGraphic(new ImageView(mute));
    }

    public void goToTime(Double d) {
        sl.setValue(d);
        mediaplayer.Player.seek(Duration.seconds(d));
    }

    public void changeVolume() {
        ft.pause();
        box.setOpacity(1);
        Player.setVolume(vol.getValue());
        if (Player.getVolume() == 0.0) {
            vm.setGraphic(new ImageView(mute));
        } else {
            vm.setGraphic(new ImageView(vmim));
        }
        vollab.setText("Vol:" + Integer.toString((int) (vol.getValue() * 100)) + "%");
    }
}
