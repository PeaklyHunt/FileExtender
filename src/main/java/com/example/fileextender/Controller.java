package com.example.fileextender;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Controller {
  public static final Background BACKGROUND_GREEN = new Background(new BackgroundFill(Color.web("#e1eedd"), CornerRadii.EMPTY, Insets.EMPTY));
  public static final char FILETYPE_INITALIZER = '.';

  @FXML private Label resultMessage;

  @FXML private TextField extension;

  @FXML private CheckBox prefix;

  @FXML private VBox fullScene;

  @FXML private Button fileChooserButton;

  @FXML
  protected void onFileChooser(){
    String extensionInput = extension.getText();
    if(validateInput(extensionInput)) {
      boolean isPrefix = prefix.isSelected();
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Open Files");
      Stage stage = (Stage) fullScene.getScene().getWindow();
      List<File> files = fileChooser.showOpenMultipleDialog(stage);
      if (files != null) {
        files.stream().filter(f -> f.exists()).forEach(file -> file.renameTo(extendFileName(file.getAbsolutePath(),extensionInput, isPrefix)));
        resultMessage.setText(files.size() + " - Filenames changed!");
      } else {
        resultMessage.setText("No Filenames changed!");
      }
    } else {
      resultMessage.setText("Please dont use special characters for filenames!");
    }
  }

  private boolean validateInput(String extensionInput) {
    return !extensionInput.isEmpty() && !extensionInput.isBlank() && extensionInput.matches("^[^<>:!%;,?\"*|/]+$");
  }

  private File extendFileName(String absolutePath, String extension, boolean isPrefix) {
    Path path =  Paths.get(absolutePath);
    String filePath = path.getParent().toString() + File.separator;
    String fileName = path.getFileName().toString().substring(0,path.getFileName().toString().lastIndexOf(FILETYPE_INITALIZER));
    String fileType = absolutePath.substring(absolutePath.lastIndexOf(FILETYPE_INITALIZER));
    return isPrefix ? getPrefix(filePath,fileName, fileType, extension): getPostfix(filePath,fileName, fileType, extension);
  }

  private File getPrefix(String filePath, String fileName, String fileType, String extension) {
    return new File(filePath + extension + fileName +fileType);
  }

  private File getPostfix(String filePath, String fileName, String fileType, String extension){
    return new File(filePath+ fileName + extension + fileType);
  }
  @FXML
  public void initialize(){
    fullScene.setBackground(BACKGROUND_GREEN);
    extension.textProperty().addListener((observable, oldValue, newValue) -> {
      fileChooserButton.setDisable(!validateInput(newValue));
      if(!resultMessage.getText().isEmpty()){
        resultMessage.setText("");
      }
    });

  }


}