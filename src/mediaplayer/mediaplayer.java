/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mediaplayer;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.util.Duration;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.media.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import java.util.ArrayList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.WindowEvent;

/**
 *
 * @author rohan
 */
public class mediaplayer extends Application {

    String ttd;
    String title;
    int run_times = 0;
    public File f = new File("");
    public static Media media;
    public volatile static MediaPlayer Player;
    public static MediaView view;
    BorderPane root;
    StackPane cent;
    String style = getClass().getResource("New.css").toExternalForm();
    Duration duration;
    int playing_index;

    String url = "";
    public static ArrayList<String> plist = new ArrayList();
    public volatile static boolean pc = false;
    mediaController mc;
    menuList ml;

    class playlist_check implements Runnable {

        Stage pst;

        playlist_check(Stage ps) {
            pst = ps;
        }

        @Override
        public void run() {
            while (true) {
                if (pc) {
                    pc = false;
                    f = new File(plist.get(0));
                    playing_index = 0;
                    try {
                        url = f.toURI().toURL().toString();
                        try {
                            Player.pause();
                        } catch (Exception e) {

                        }
                        Platform.runLater(new Runnable() {

                            @Override
                            public void run() {
                                System.gc();
                                start(pst);
                            }
                        });

                    } catch (MalformedURLException ex) {

                    }
                }
            }
        }

    }

    public void start(final Stage primaryStage) {
        cent = new StackPane();
        mc = new mediaController();
        ml = new menuList();
        final FileChooser chooser = new FileChooser();
        root = new BorderPane();

        cent.setMinSize(0, 0);
        cent.setAlignment(Pos.CENTER);
        final Scene scene = new Scene(root, 500, 500, Color.BLACK);
        root.setCenter(cent);
        root.setTop(ml.menubar);
        root.setBottom(mc.box);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
        if (run_times == 0) {
            Thread t = new Thread(new playlist_check(primaryStage));
            t.start();
        }
        run_times++;
        //now add the external css file to the scene
        scene.getStylesheets().add(style);
        root.setStyle("-fx-background-color:transparent;");
        primaryStage.setScene(scene);
        if (f.isFile()) {
            chooser.setInitialDirectory(f.getParentFile());
        }
        primaryStage.show();

        try {
            try {
                url = f.toURI().toURL().toString();

            } catch (MalformedURLException ex) {

            }
            media = new Media(url);
            Player = new MediaPlayer(media);
            view = new MediaView(Player);
            cent.getChildren().add(view);
            DoubleProperty width = view.fitWidthProperty();

            DoubleProperty height = view.fitHeightProperty();
            width.bind(Bindings.selectDouble(view.sceneProperty(), "width"));
            height.bind(Bindings.selectDouble(view.sceneProperty(), "height"));
            Player.play();
            primaryStage.setTitle(f.getName() + "-XPlayer");
            
            Player.setOnReady(new Runnable() {
                @Override
                public void run() {
                    
                    mc.sl.setValue(0.0);
                    mc.sl.setMin(0.0);
                    mc.sl.setMax(Player.getTotalDuration().toSeconds());

                    mc.pb.setMinWidth(0.0);

                    mc.pb.setMaxWidth(mc.sl.getMaxWidth());
                    mc.pb.setMaxHeight(0.5);
                    mc.vol.setValue(0.70);
                    mc.vv.setProgress(0.70);
                    mc.vol.setMin(0.0);
                    mc.vol.setMax(1.0);
                    mc.vol.setMaxWidth(mc.vv.getWidth());

                    Player.setVolume(0.70);
                    
                    //sl.setTranslateX(30);
                    duration = Player.getMedia().getDuration();
                    updateValues();

                }
            });
            Player.setOnEndOfMedia(new Runnable() {

                @Override
                public void run() {
                    if (plist.isEmpty() == false) {
                        if (plist.size() > playing_index + 1) {
                            playing_index++;
                            f = new File(plist.get(playing_index));

                            try {
                                url = f.toURI().toURL().toString();
                                Player.pause();
                                start(primaryStage);
                            } catch (MalformedURLException ex) {

                            }
                        }
                    }
                }
            });
            mc.stop.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    Player.seek(Duration.ZERO);
                    Player.pause();
                    mc.vidlab.setText("--/--");
                    view.setVisible(false);
                    mc.b.setText("PLAY");
                    mc.b.setGraphic(new ImageView(mc.playim));
                }
            });
            mc.next.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    if (plist.isEmpty() == false) {
                        if (plist.size() > playing_index + 1) {
                            playing_index++;
                            f = new File(plist.get(playing_index));

                            try {
                                url = f.toURI().toURL().toString();
                                Player.stop();
                                System.gc();
                                start(primaryStage);
                            } catch (MalformedURLException ex) {

                            }
                        }
                    }
                }
            });
            mc.prev.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    if (plist.isEmpty() == false) {
                        if (playing_index != 0) {
                            playing_index--;
                            f = new File(plist.get(playing_index));

                            try {
                                url = f.toURI().toURL().toString();
                                Player.stop();
                                System.gc();
                                start(primaryStage);
                            } catch (MalformedURLException ex) {

                            }
                        }
                    }
                }
            });
            mc.vm.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    mc.ft.pause();
                    mc.box.setOpacity(1);
                    if (Player.getVolume() == 0.0) {
                        Player.setVolume(0.70);
                        mc.vol.setValue(0.70);
                        mc.vm.setGraphic(new ImageView(mc.vmim));
                    } else {
                        Player.setVolume(0.0);
                        mc.mute();
                    }
                    mc.vollab.setText("Vol:" + Integer.toString((int) (mc.vol.getValue() * 100)) + "%");
                    mc.ft.play();
                }
            });

            ml.i1m3.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    mc.ft.pause();
                    mc.box.setOpacity(1);
                    if (Player.getVolume() == 0.0) {
                        mc.vm.setGraphic(new ImageView(mc.vmim));
                    }
                    mc.vol.setValue(mc.vol.getValue() + 0.1);
                    Player.setVolume(mc.vol.getValue());
                    mc.vollab.setText("Vol:" + Integer.toString((int) (mc.vol.getValue() * 100)) + "%");
                    mc.ft.play();
                }
            });
            ml.i2m3.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    mc.ft.pause();
                    mc.box.setOpacity(1);
                    mc.vol.setValue(mc.vol.getValue() - 0.1);
                    Player.setVolume(mc.vol.getValue());
                    if (Player.getVolume() == 0.0) {
                        mc.mute();
                    }
                    mc.vollab.setText("Vol:" + Integer.toString((int) (mc.vol.getValue() * 100)) + "%");
                    mc.ft.play();
                }
            });
            final KeyCombination keyComb3 = new KeyCodeCombination(KeyCode.UP, KeyCombination.CONTROL_DOWN);
            scene.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if (keyComb3.match(event)) {
                        mc.ft.pause();
                        mc.box.setOpacity(1);
                        if (Player.getVolume() == 0.0) {
                            mc.mute();
                        }
                        mc.vol.setValue(mc.vol.getValue() + 0.05);
                        Player.setVolume(mc.vol.getValue());
                        mc.vollab.setText("Vol:" + Integer.toString((int) (mc.vol.getValue() * 100)) + "%");
                        mc.ft.play();
                    }
                }
            });
            final KeyCombination keyComb4 = new KeyCodeCombination(KeyCode.DOWN, KeyCombination.CONTROL_DOWN);
            scene.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if (keyComb4.match(event)) {
                        mc.ft.pause();
                        mc.box.setOpacity(1);
                        mc.vol.setValue(mc.vol.getValue() - 0.05);
                        if (Player.getVolume() == 0.0) {
                            mc.mute();
                        }
                        Player.setVolume(mc.vol.getValue());
                        mc.vollab.setText("Vol:" + Integer.toString((int) (mc.vol.getValue() * 100)) + "%");
                        mc.ft.play();
                    }
                }
            });
            view.setOnScroll(new EventHandler<ScrollEvent>() {
                @Override
                public void handle(ScrollEvent event) {
                    if (event.getDeltaY() > 0) {
                        if (Player.getVolume() == 0.0) {
                            mc.mute();
                        }
                        mc.vol.setValue(mc.vol.getValue() + 0.05);
                    } else {
                        mc.vol.setValue(mc.vol.getValue() - 0.05);
                        if (Player.getVolume() == 0.0) {
                            mc.mute();
                        }
                    }
                    Player.setVolume(mc.vol.getValue());
                    mc.vollab.setText("Vol:" + Integer.toString((int) (mc.vol.getValue() * 100)) + "%");
                }
            });
            ml.s1.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Player.setRate(1.25);
                }
            });
            ml.s2.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Player.setRate(1.50);
                }
            });
            ml.s3.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Player.setRate(2.00);
                }
            });
            ml.n.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Player.setRate(1.00);
                }
            });
            ml.d1.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Player.setRate(0.25);
                }
            });
            ml.d2.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Player.setRate(0.50);
                }
            });
            ml.i5m2.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    mc.ft.pause();
                    mc.box.setOpacity(1);
                    if (Duration.seconds(mc.sl.getValue()).lessThanOrEqualTo(duration)) {
                        mc.goToTime(mc.sl.getValue() + 3);

                        updateValues();
                    }

                    mc.ft.play();
                }
            });
            ml.i6m2.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    mc.ft.pause();
                    mc.box.setOpacity(1);
                    if (Duration.seconds(mc.sl.getValue()).greaterThanOrEqualTo(Duration.ZERO)) {
                        mc.goToTime(mc.sl.getValue() - 3);
                    }
                    updateValues();
                    mc.ft.play();
                }
            });
            final KeyCombination keyComb6 = new KeyCodeCombination(KeyCode.RIGHT, KeyCombination.SHIFT_DOWN);
            scene.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if (keyComb6.match(event)) {
                        mc.ft.pause();
                        mc.box.setOpacity(1);
                        if (Duration.seconds(mc.sl.getValue()).lessThanOrEqualTo(duration)) {
                            mc.goToTime(mc.sl.getValue() + 3);

                            updateValues();
                        }
                        mc.ft.play();

                    }
                }
            });
            final KeyCombination keyComb7 = new KeyCodeCombination(KeyCode.LEFT, KeyCombination.SHIFT_DOWN);
            scene.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if (keyComb7.match(event)) {
                        mc.ft.pause();
                        mc.box.setOpacity(1);

                        if (Duration.seconds(mc.sl.getValue()).greaterThanOrEqualTo(Duration.ZERO)) {
                            mc.goToTime(mc.sl.getValue() - 3);
                        }

                        updateValues();
                        mc.ft.play();
                    }
                }
            });

            ml.i1m4.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (view.isVisible()) {
                        view.setVisible(false);
                    } else {
                        view.setVisible(true);
                    }
                }
            });

            Player.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                    mc.sl.setValue(newValue.toSeconds());
                    mc.pb.setProgress(((newValue.toSeconds()) / duration.toSeconds()));
                    if (primaryStage.isFullScreen() == false) {
                        ml.menubar.setVisible(true);

                    }
                    if (view.isVisible() == false) {
                        view.setVisible(true);
                    }

                    updateValues();

                }
            });
            mc.sl.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    mc.ft.pause();
                    mc.box.setOpacity(1);
                    Player.seek(Duration.seconds(mc.sl.getValue()));
                    updateValues();
                }

            });
            mc.pb.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    mc.ft.pause();
                    mc.box.setOpacity(1);
                    Bounds b1 = mc.pb.getLayoutBounds();
                    double mouseX = event.getSceneX();
                    double percent = (((b1.getMinX() + mouseX)) / b1.getMaxX());

                    mc.pb.setProgress((percent));
                    Player.seek(Duration.seconds(mc.pb.getProgress() * duration.toSeconds() * 100));
                    updateValues();
                }

            });
            mc.vol.valueProperty().addListener(new ChangeListener<Number>() {

                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    mc.vol.setValue(newValue.doubleValue());
                    mc.vv.setProgress(newValue.doubleValue());

                }
            });
            mc.vol.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                   mc.changeVolume();
                }

            });
            mc.vol.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    mc.changeVolume();
                }

            });
            mc.sl.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    mc.ft.pause();
                    mc.box.setOpacity(1);
                    Player.seek(Duration.seconds(mc.sl.getValue()));
                }

            });
            mc.pb.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    mc.ft.pause();
                    mc.box.setOpacity(1);
                    Player.seek(Duration.seconds(mc.pb.getProgress()));
                }

            });
            mc.b.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    mc.ft.pause();
                    mc.box.setOpacity(1);
                    if (Player.getStatus().equals(MediaPlayer.Status.PAUSED)) {
                        mc.play();
                    } else {
                        mc.pause();
                    }
                }
            });
            final KeyCombination keyComb2 = new KeyCodeCombination(KeyCode.SPACE);
            scene.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if (keyComb2.match(event)) {
                        mc.ft.pause();
                        mc.box.setOpacity(1);
                        if (Player.getStatus().equals(MediaPlayer.Status.PAUSED)) {
                            mc.play();
                        } else {
                            mc.pause();
                        }
                    }
                }
            });
            ml.i1m2.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    mc.ft.pause();
                    mc.box.setOpacity(1);
                    if (Player.getStatus().equals(MediaPlayer.Status.PAUSED)) {
                        mc.play();
                    } else {
                        mc.pause();
                    }
                }
            });
            view.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    mc.ft.pause();
                    mc.box.setOpacity(1);
                    if (event.getClickCount() == 2) {
                        if (primaryStage.isFullScreen() == false) {
                            primaryStage.setFullScreen(true);
                            ml.menubar.setVisible(false);
                        } else {
                            primaryStage.setFullScreen(false);
                            ml.menubar.setVisible(true);
                        }
                    }
                    if (Player.getStatus().equals(MediaPlayer.Status.PAUSED)) {
                        mc.play();
                    } else {
                        mc.pause();

                    }

                }
            });
            mc.box.setOnMouseMoved(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    mc.ft.pause();
                    mc.box.setOpacity(1);
                }
            });
            mc.vol.setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    mc.ft.setDelay(Duration.seconds(5));
                    if (Player.getStatus().equals(MediaPlayer.Status.PLAYING)) {
                        mc.ft.setFromValue(1);
                        mc.ft.setToValue(0);
                        mc.ft.play();
                    }

                }
            });
            root.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    //ft.setDelay(Duration.seconds(5));
                    if (Player.getStatus().equals(MediaPlayer.Status.PLAYING)) {
                        mc.ft.setFromValue(1);
                        mc.ft.setToValue(0);
                        mc.ft.play();
                    }

                }
            });
            view.setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    mc.ft.setDelay(Duration.seconds(5));
                    if (Player.getStatus().equals(MediaPlayer.Status.PLAYING)) {
                        mc.ft.setFromValue(1);
                        mc.ft.setToValue(0);
                        mc.ft.play();
                    }

                }
            });
            view.setOnMouseMoved(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    mc.ft.pause();
                    mc.box.setOpacity(1);
                    mc.ft.setDelay(Duration.seconds(5));
                    if (Player.getStatus().equals(MediaPlayer.Status.PLAYING)) {

                        mc.ft.setFromValue(1);
                        mc.ft.setToValue(0);
                        mc.ft.play();
                    }
                }
            });
        } catch (MediaException ex) {
            primaryStage.setTitle("XPlayer");
            if (f.isFile()) {
                Alert a = new Alert(AlertType.ERROR);
                a.setContentText("Unsupported Format");
                a.showAndWait();
            }
        }
        ml.i1m5.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Alert a = new Alert(AlertType.INFORMATION);
                a.setHeaderText("About Xplayer");
                a.setContentText("Created and developed by Rohan,Raghav,Nishant,Harsh");
                a.showAndWait();
            }
        });
        ml.i2m1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mc.ft.pause();
                mc.box.setOpacity(1);
                Stage stage = new Stage();

                FXMLLoader f1 = new FXMLLoader(getClass().getResource("Playlist.fxml"));
                Parent root1;
                try {
                    root1 = (Parent) f1.load();
                    Scene scene1 = new Scene(root1, 600, 400);

                    stage.setTitle("Playlist Creator");
                    stage.setScene(scene1);
                    stage.show();
                } catch (IOException ex) {
                    Logger.getLogger(mediaplayer.class.getName()).log(Level.SEVERE, null, ex);
                }

                mc.ft.play();
            }
        });
        final KeyCombination keyComb05 = new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN);
        scene.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (keyComb05.match(event)) {
                    mc.ft.pause();
                    mc.box.setOpacity(1);
                    Stage stage = new Stage();

                    FXMLLoader f1 = new FXMLLoader(getClass().getResource("Playlist.fxml"));
                    Parent root1;
                    try {
                        root1 = (Parent) f1.load();
                        Scene scene1 = new Scene(root1, 600, 400);

                        stage.setTitle("Playlist Creator");
                        stage.setScene(scene1);
                        stage.show();
                    } catch (IOException ex) {
                        Logger.getLogger(mediaplayer.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    mc.ft.play();
                }
            }
        });
        final KeyCombination keyComb1 = new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN);
        scene.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (keyComb1.match(event)) {

                    try {
                        f = chooser.showOpenDialog(primaryStage);
                        chooser.setTitle("Open Media File");
                        try {

                            url = f.toURI().toURL().toString();
                            try {
                                Player.stop();
                            } catch (Exception e) {

                            }
                            start(primaryStage);
                        } catch (MalformedURLException ex) {

                        }
                    } catch (NullPointerException e) {

                    }
                }
            }
        });
        ml.i1m1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    f = chooser.showOpenDialog(primaryStage);
                    chooser.setTitle("Open Media File");
                    try {

                        url = f.toURI().toURL().toString();
                        try {
                            Player.stop();
                        } catch (Exception e) {

                        }
                        start(primaryStage);
                    } catch (MalformedURLException ex) {

                    }
                } catch (NullPointerException e) {

                }

            }
        });
        ml.i3m1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Platform.exit();
            }
        });
        final KeyCombination keyComb5 = new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN);
        scene.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (keyComb5.match(event)) {
                    Platform.exit();
                }
            }
        });

    }

    public void updateValues() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Duration currentTime = Player.getCurrentTime();
                mc.vidlab.setText(formatTime(currentTime, duration));
            }
        });
    }

    public String formatTime(Duration elapsed, Duration duration) {
        int intElapsed = (int) Math.floor(elapsed.toSeconds());
        int elapsedHours = intElapsed / (60 * 60);
        if (elapsedHours > 0) {
            intElapsed -= elapsedHours * 60 * 60;
        }
        int elapsedMinutes = intElapsed / 60;
        int elapsedSeconds = intElapsed - elapsedMinutes * 60;
        if (duration.greaterThan(Duration.ZERO)) {
            int intDuration = (int) Math.floor(duration.toSeconds());
            int durationHours = intDuration / (60 * 60);
            if (durationHours > 0) {
                intDuration -= durationHours * 60 * 60;
            }
            int durationMinutes = intDuration / 60;
            int durationSeconds = intDuration - durationMinutes * 60;

            if (durationHours > 0) {
                return String.format("%d:%02d:%02d/%d:%02d:%02d",
                        elapsedHours, elapsedMinutes, elapsedSeconds,
                        durationHours, durationMinutes, durationSeconds);
            } else {
                return String.format("%02d:%02d/%02d:%02d",
                        elapsedMinutes, elapsedSeconds, durationMinutes,
                        durationSeconds);
            }
        } else {
            if (elapsedHours > 0) {
                return String.format("%d:%02d:%02d", elapsedHours,
                        elapsedMinutes, elapsedSeconds);
            } else {
                return String.format("%02d:%02d", elapsedMinutes,
                        elapsedSeconds);
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
