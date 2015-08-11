package com.plter.lrceditor.controllers;

import com.plter.lrceditor.views.ViewTool;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by plter on 8/11/15.
 */
public class AboutViewController {


    public Hyperlink lbPlter;
    public Hyperlink lbChaoyang805;

    static public void showView(Window owner){
        Stage s = new Stage();
        s.setScene(new Scene(ViewTool.loadView("About.fxml"), 400, 300));
        s.setTitle("关于该软件");
        s.initOwner(owner);
        s.initModality(Modality.APPLICATION_MODAL);
        s.show();
    }

    public void linkButtonClickedHandler(ActionEvent actionEvent) {
        try {
            Object source = actionEvent.getSource();
            if (source==lbPlter){
                Desktop.getDesktop().browse(new URI("http://github.com/plter"));
            }else if (source==lbChaoyang805){
                Desktop.getDesktop().browse(new URI("http://github.com/chaoyang805"));
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
