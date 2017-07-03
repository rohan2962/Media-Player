/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mediaplayer;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

/**
 *
 * @author RAGHAV SABOO
 */
public class menuList {

    Menu m1, m2, m3, m4, m5, i2m2;
    MenuItem i1m1, i2m1, i3m1, i1m2, i3m2, i4m2, i5m2, i6m2, i7m2, i1m3, i2m3, i1m4, i1m5, s1, s2, s3, n, d1, d2;
    MenuBar menubar;

    menuList() {
        menubar = new MenuBar();
        m1 = new Menu("Media");
        i1m1 = new MenuItem("Open File           Ctrl+O");
        i2m1 = new MenuItem("Add to playlist   Ctrl+L");
        i3m1 = new MenuItem("Close                 Ctrl+X");
        m1.getItems().addAll(i1m1, i2m1, i3m1);
        m2 = new Menu("Playback");
        i1m2 = new MenuItem("Play/Pause");
        i2m2 = new Menu("Speed");
        s1 = new MenuItem("1.25x");
        s2 = new MenuItem("1.50x");
        s3 = new MenuItem("2.00x");
        n = new MenuItem("1.00x");
        d1 = new MenuItem("0.25x");
        d2 = new MenuItem("0.50x");
        i2m2.getItems().addAll(s3, s2, s1, n, d1, d2);
        i3m2 = new MenuItem("Next");
        i4m2 = new MenuItem("Previous");
        i5m2 = new MenuItem("Jump forward");
        i6m2 = new MenuItem("Jump backward");
        m2.getItems().addAll(i1m2, i2m2, i3m2, i4m2, i5m2, i6m2);
        m3 = new Menu("Audio");
        i1m3 = new MenuItem("Increase Volume");
        i2m3 = new MenuItem("Decrease Volume");
        m3.getItems().addAll(i1m3, i2m3);
        m4 = new Menu("Video");
        i1m4 = new MenuItem("Video track Enable/Disable");
        m4.getItems().add(i1m4);
        m5 = new Menu("Help");
        i1m5 = new MenuItem("About");
        m5.getItems().add(i1m5);
        menubar.getMenus().addAll(m1, m2, m3, m4, m5);
    }
}
