package com.plter.lrceditor;

import com.plter.lrceditor.views.ViewTool;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = ViewTool.loadView("LRCEditor.fxml");
        primaryStage.setTitle("LRC歌词编辑器");
        primaryStage.setScene(new Scene(root, 600, 700));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
