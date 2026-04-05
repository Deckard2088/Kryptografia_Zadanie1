package pl.comp;

import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javafx.scene.control.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;
import java.util.HexFormat;
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

    /*
    Funkcja do zapisywania do pliku; w zależności od tego który przycisk 'zapisz' został kliknięty
    plik ma nadaną inną nazwę domyślną oraz pobiera dane z innego pola tekstowego
    TextInputControl to klasa bazowa dla TextArea i TextField
     */
    public void saveFile(TextInputControl textInput,TextInputControl userTextInput , String defaultName){
        logger.info("Rozpoczecie zapisu pliku.");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("tego typu");

        //jeśli wcześniej nic nie otwierano to domyślna nazwa pliku, w przeciwnym razie domyślna nazwa to nazwa pliku otwieranego
        if (userTextInput.getText().isEmpty()) {
            fileChooser.setInitialFileName(defaultName);
        } else {
            fileChooser.setInitialFileName(userTextInput.getText());
        }
        Stage stage = (Stage) saveButton.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                //Files.writeString(file.toPath(), saveKeyToFileTextField.getText());
                Files.writeString(file.toPath(), textInput.getText());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void saveFileKey(){
        saveFile(keyValueTextField, saveKeyToFileTextField,"klucz.txt");
    }

    @FXML
    public void saveFileCiphertext(){
        saveFile(ciphertextTextField, nameOfFileWithCiphertextTextFieldEnd, "szyfrogram.txt");
    }

    @FXML
    public void saveFilePlaintext(){
        saveFile(plaintextTextField, nameOfFileWithPlaintextTextFieldEnd, "tekst jawny.txt");
    }

    public void loadFile(TextInputControl textInput, TextInputControl textFieldWithFileName) {
        logger.info("Rozpoczecie procesu otwierania pliku.");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("testtyy");

        Stage stage = (Stage) loadButton.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            try {
                String content = Files.readString(file.toPath());
                textInput.setText(content);
                logger.info("Wczytano plik: {}", file.getAbsolutePath());

                String fileName = file.getName();
                textFieldWithFileName.setText(fileName);
            } catch (IOException e) {
                logger.error("Błąd odczytu pliku", e);
            }
        }
    }

    @FXML
    public void loadFileKey(){
        loadFile(keyValueTextField, loadKeyFromFileTextField);
    }

    @FXML
    public void loadFileCiphertext(){
        loadFile(ciphertextTextField, nameOfFileWithCiphertextTextField);
    }

    @FXML
    public void loadFilePlaintext(){
        loadFile(plaintextTextField, nameOfFileWithPlaintextTextField);
    }

    @FXML
    public void radioButtonFileChosen(){
        logger.info("Wybrano tryb 'Plik' przez radio button.");
        keyValueTextField.setDisable(true);
        plaintextTextField.setDisable(true);
        ciphertextTextField.setDisable(true);

        loadButton.setDisable(false);
        openFileWithCiphertextButton.setDisable(false);
        openFileWithPlaintextButton.setDisable(false);
        generateKeyButton.setDisable(true);
        loadKeyFromFileTextField.setDisable(false);
        nameOfFileWithCiphertextTextField.setDisable(false);
        nameOfFileWithPlaintextTextField.setDisable(false);
    }

    @FXML
    public void radioButtonWindowChosen(){
        logger.info("Wybrano tryb 'Okno' przez radio button.");
        keyValueTextField.setDisable(false);
        plaintextTextField.setDisable(false);
        ciphertextTextField.setDisable(false);
        generateKeyButton.setDisable(false);
        loadButton.setDisable(true);
        openFileWithCiphertextButton.setDisable(true);
        openFileWithPlaintextButton.setDisable(true);
        loadKeyFromFileTextField.setDisable(true);
        nameOfFileWithCiphertextTextField.setDisable(true);
        nameOfFileWithPlaintextTextField.setDisable(true);
    }

    @FXML
    public void initialize(){
        radioButtonPlik.setSelected(true);
        radioButtonFileChosen();
    }

    @FXML
    public void encrypt(){
        String key = keyValueTextField.getText();
        //sprawdzenie czy klucz na pewno jest w hex
        if (key == null || !key.matches("[0-9A-Fa-f]{16}")) {
            System.out.println("Niepoprawny klucz HEX!");
            return;
        }

        byte[] keyBytes;
        try {
            keyBytes = HexFormat.of().parseHex(key);
        } catch (IllegalArgumentException e) {
            System.out.println("Błąd parsowania klucza w HEX!");
            return;
        }
        DES des = new DES();
        des.createSubKeysArray(keyBytes);

        String testowyBlok = plaintextTextField.getText();
        byte[] textBytes = testowyBlok.getBytes(StandardCharsets.UTF_8);
        byte[] encrypted = des.processBlock(textBytes, false);
        String encryptedBase64 = Base64.getEncoder().encodeToString(encrypted);
        ciphertextTextField.setText(encryptedBase64);
    }

    @FXML
    public void decrypt(){
        String key = keyValueTextField.getText();
        //sprawdzenie czy klucz na pewno jest w hex
        if (key == null || !key.matches("[0-9A-Fa-f]{16}")) {
            System.out.println("Niepoprawny klucz HEX!");
            return;
        }

        byte[] keyBytes;
        try {
            keyBytes = HexFormat.of().parseHex(key);
        } catch (IllegalArgumentException e) {
            System.out.println("Błąd parsowania klucza w HEX!");
            return;
        }
        DES des = new DES();
        des.createSubKeysArray(keyBytes);

        String testowyBlok = ciphertextTextField.getText();
        byte[] textBytes = testowyBlok.getBytes(StandardCharsets.UTF_8);
        byte[] decrypted = des.processBlock(textBytes, true);
        String str = new String(decrypted, StandardCharsets.UTF_8);
        plaintextTextField.setText(str);
    }

    @FXML
    public void generateKey(){
        byte[] generatedKey = Algorithms.generateRandomKey();
        String key = HexFormat.of().formatHex(generatedKey);
        keyValueTextField.setText(key);
    }
}
