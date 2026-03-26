package pl.comp;

import javafx.fxml.FXML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javafx.scene.control.*;

public class MenuController {

    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

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
        logger.info("Rozpoczęcie zapisu pliku.");
    }
}
