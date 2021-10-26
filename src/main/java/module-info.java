module com.example.fileextender {
  requires javafx.controls;
  requires javafx.fxml;
  opens com.example.fileextender to javafx.fxml;
  exports com.example.fileextender;
}