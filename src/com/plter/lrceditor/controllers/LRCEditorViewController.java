package com.plter.lrceditor.controllers;

import com.plter.lrceditor.lrc.LRCReader;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by plter on 7/27/15.
 */
public class LRCEditorViewController implements Initializable, ChangeListener<Duration> {
    public Button btnInsertTimeTag;
    public TextArea taTextContent;
    public VBox root;
    public Button btnPlayMp3;
    public Button btnPauseMp3;
    public Button btnStopMp3;
    public Label labelTestSongText;
    public TextField tfSongTitle;
    public TextField tfSongArtist;

    private String timeTag;
    private File fileMp3;
    private LRCReader lrc = null;
    private MediaPlayer mp3Player;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setTimeTag("[00:00.00]");
    }

    public void setTimeTag(String timeTag) {
        this.timeTag = timeTag;
        btnInsertTimeTag.setText(String.format("插入时间%s", timeTag));
    }

    public String getTimeTag() {
        return timeTag;
    }

    public void btnInsertTimeTagActionHandler(ActionEvent actionEvent) {
        int selectStartIndex = taTextContent.getSelection().getStart();
        int currentLineStartIndex = taTextContent.getText().lastIndexOf("\n", selectStartIndex) + 1;

        taTextContent.insertText(currentLineStartIndex, getTimeTag());

        int nextLineStartIndex = taTextContent.getText().indexOf("\n", currentLineStartIndex);
        if (nextLineStartIndex > -1) {
            nextLineStartIndex = nextLineStartIndex + 1;

            int nextLineEndIndex = taTextContent.getText().indexOf("\n", nextLineStartIndex);
            taTextContent.selectRange(nextLineStartIndex, nextLineEndIndex > -1 ? nextLineEndIndex : taTextContent.getLength());
        }
    }

    public void btnOpenSongActionHandler(ActionEvent actionEvent) throws MalformedURLException {
        FileChooser fc = new FileChooser();
        fc.setTitle("打开一个MP3文件");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3文件(*.mp3)", "*.mp3"));
        fileMp3 = fc.showOpenDialog(root.getScene().getWindow());

        //sync controll buttons state
        btnPlayMp3.setDisable(fileMp3 == null);
        btnPauseMp3.setDisable(fileMp3 == null);
        btnStopMp3.setDisable(fileMp3 == null);

        if (fileMp3 != null) {
            if (mp3Player != null) {
                mp3Player.stop();
                mp3Player.currentTimeProperty().removeListener(this);
            }

            mp3Player = new MediaPlayer(new Media(fileMp3.toURI().toURL().toString()));
            mp3Player.currentTimeProperty().addListener(this);
        }
    }

    public void btnPlayMp3ActionHandler(ActionEvent actionEvent) {
        if (mp3Player != null) {
            mp3Player.play();
            lrc = new LRCReader(taTextContent.getText());
        }
    }

    public void btnPauseActionHandler(ActionEvent actionEvent) {
        if (mp3Player != null) {
            mp3Player.pause();
        }
    }

    public void btnStopActionHandler(ActionEvent actionEvent) {
        if (mp3Player != null) {
            mp3Player.stop();
        }
    }

    @Override
    public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
        setTimeTag(String.format("[%02d:%02d.%02d]", (int) newValue.toMinutes(), ((int) newValue.toSeconds()) % 60,
                (int) (newValue.toMillis() % 1000 / 10)));
        if (lrc != null) {
            String content = lrc.getContent((int) newValue.toSeconds());
            if (content != null) {
                labelTestSongText.setText(content);
            }
        }
    }

    public void btnSaveLrcActionHandler(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        String title = tfSongTitle.getText();
        String artist = tfSongArtist.getText();
        taTextContent.insertText(0, artist.isEmpty() ? "" : String.format("[ar:%s]\n", artist));
        taTextContent.insertText(0, artist.isEmpty() ? "" : String.format("[ti:%s]\n", title));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("LRC文件(*.lrc)", "*.lrc"));
        fc.setTitle("保存Lrc文件");
        fc.setInitialFileName(tfSongTitle.getText() == null ? ".lrc" : tfSongTitle.getText() + ".lrc");
        File f = fc.showSaveDialog(root.getScene().getWindow());
        if (f != null) {
            try {
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(taTextContent.getText().getBytes("UTF-8"));
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void btnAboutActionHandler(ActionEvent actionEvent) {
        AboutViewController.showView(root.getScene().getWindow());
    }
}
