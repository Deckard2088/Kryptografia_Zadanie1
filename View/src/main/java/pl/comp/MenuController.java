package pl.comp;

import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javafx.scene.control.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ResourceBundle;

public class MenuController {

    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);
    private ResourceBundle bundle;
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

    // radio buttony
    @FXML
    private RadioButton radioButtonPlik;

    @FXML
    private RadioButton radioButtonOkno;

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

    @FXML
    public void saveFile(){
        logger.info("Rozpoczecie zapisu pliku.");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("tego typu");

        fileChooser.setInitialFileName("plik.txt");
        Stage stage = (Stage) saveButton.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                Files.writeString(file.toPath(), saveKeyToFileTextField.getText());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
