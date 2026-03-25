package pl.comp;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;

public class MenuController extends Application {
    // --- KEY SECTION ---
    @FXML
    private Label keyLabel;

    @FXML
    private Button generateKeyButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button loadButton;

    @FXML
    private Label keyValueLabel;

    @FXML
    private Label generateKeyValueLabel;

    @FXML
    private TextField keyValueTextField;

    @FXML
    private Label loadKeyFromFileLabel;

    @FXML
    private TextField loadKeyFromFileTextField;

    @FXML
    private Label saveKeyToFileLabel;

    @FXML
    private TextField saveKeyToFileTextField;

    // --- ENCRYPTION SECTION ---
    @FXML
    private Label encryptionDecryptionLabel;

    @FXML
    private Button openFileWithPlaintextButton;

    @FXML
    private TextArea plaintextTextField;

    @FXML
    private Label fileWithPlaintextlabel;

    @FXML
    private TextField nameOfFileWithPlaintextTextField;

    @FXML
    private Label fileWithCiphertextlabel;

    @FXML
    private TextField nameOfFileWithCiphertextTextField;

    @FXML
    private Button openFileWithCiphertextButton;

    @FXML
    private TextArea ciphertextTextField;

    @FXML
    private Button encryptionButton;

    @FXML
    private Button decryptionButton;

    // radio buttony (nie mają fx:id w XML, więc możesz dodać ręcznie jeśli chcesz)

    @FXML
    private Label saveFileWithPlaintextLabel;

    @FXML
    private Label saveFileWithCiphertextLabel;

    @FXML
    private TextField nameOfFileWithPlaintextTextFieldEnd;

    @FXML
    private TextField nameOfFileWithCiphertextTextFieldEnd;

    @FXML
    private Button saveFileWithPlaintextButton;

    @FXML
    private Button saveFileWithCiphertextButton;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/des_menu.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
