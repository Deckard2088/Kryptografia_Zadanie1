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
import java.util.*;

public class MenuController {

    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);
    private ResourceBundle bundle;
    // --- KEY SECTION ---
    @FXML
    private Button generateKeyButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button loadButton;

    @FXML
    private TextField keyValueTextField;

    @FXML
    private TextField loadKeyFromFileTextField;

    @FXML
    private TextField saveKeyToFileTextField;

    @FXML
    private Button openFileWithPlaintextButton;

    @FXML
    private TextArea plaintextTextField;

    @FXML
    private TextField nameOfFileWithPlaintextTextField;

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

    public void saveFileAsText(TextInputControl textInput, TextInputControl userTextInput, String defaultName) {
        File file = showSaveDialog(userTextInput, defaultName);
        if (file != null) {
            try {
                Files.writeString(file.toPath(), textInput.getText(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                logger.error("Błąd zapisu pliku", e);
            }
        }
    }

    public void saveFileAsBytes(byte[] bytes, TextInputControl userTextInput, String defaultName) {
        File file = showSaveDialog(userTextInput, defaultName);
        if (file != null) {
            try {
                Files.write(file.toPath(), bytes);
            } catch (IOException e) {
                logger.error("Błąd zapisu pliku", e);
            }
        }
    }

    private File showSaveDialog(TextInputControl userTextInput, String defaultName) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Zapisz plik");
        fileChooser.setInitialFileName(
                userTextInput.getText().isEmpty() ? defaultName : userTextInput.getText()
        );
        Stage stage = (Stage) saveButton.getScene().getWindow();
        return fileChooser.showSaveDialog(stage);
    }

    @FXML
    public void saveFileKey(){
        String nameOfSavedFile = saveKeyToFileTextField.getText();
        if (nameOfSavedFile.isEmpty()){
            nameOfSavedFile = "klucz.txt";
        }
        saveFileAsText(keyValueTextField, saveKeyToFileTextField, nameOfSavedFile);
    }

    @FXML
    public void saveFileCiphertext(){
        String nameOfSavedFile = nameOfFileWithCiphertextTextFieldEnd.getText();
        if (nameOfSavedFile.isEmpty()){
            nameOfSavedFile = "szyfrogram.txt";
        }
        byte[] encryptedBytes = Base64.getDecoder().decode(ciphertextTextField.getText());
        saveFileAsBytes(encryptedBytes, nameOfFileWithCiphertextTextFieldEnd, nameOfSavedFile);
    }

    @FXML
    public void saveFilePlaintext(){
        String nameOfFile = nameOfFileWithPlaintextTextField.getText();
        String nameOfSavedFile = nameOfFileWithPlaintextTextFieldEnd.getText();
        if (radioButtonOkno.isSelected() || nameOfFile.endsWith(".txt")) {
            if (nameOfSavedFile.isEmpty()){
                nameOfSavedFile = "tekst_jawny.txt";
            }
            saveFileAsText(plaintextTextField, nameOfFileWithPlaintextTextField, nameOfSavedFile);
        } else {
            if (nameOfSavedFile.isEmpty()){
                nameOfSavedFile = "tekst_jawny";
            }
            byte[] plaintextBytes = Base64.getDecoder().decode(plaintextTextField.getText());
            saveFileAsBytes(plaintextBytes, nameOfFileWithPlaintextTextField, nameOfSavedFile);
        }
    }

    @FXML
    public void loadFileKey(){
        File file = showOpenDialog();
        if (file != null) {
            try {
                // wczytujemy string hex bezpośrednio
                String hexKey = Files.readString(file.toPath(), StandardCharsets.UTF_8).trim();
                keyValueTextField.setText(hexKey);
                loadKeyFromFileTextField.setText(file.getName());
                logger.info("Wczytano klucz: {}", file.getAbsolutePath());
            } catch (IOException e) {
                logger.error("Błąd odczytu klucza", e);
            }
        }
    }

    @FXML
    public void loadFileCiphertext(){
        File file = showOpenDialog();
        if (file != null) {
            try {
                byte[] fileBytes = Files.readAllBytes(file.toPath());
                nameOfFileWithCiphertextTextField.setText(file.getName());
                logger.info("Wczytano szyfrogram: {}", file.getAbsolutePath());
                // szyfrogram zawsze jako Base64 w TextArea
                ciphertextTextField.setText(Base64.getEncoder().encodeToString(fileBytes));
            } catch (IOException e) {
                logger.error("Błąd odczytu pliku", e);
            }
        }
    }

    @FXML
    public void loadFilePlaintext(){
        File file = showOpenDialog();
        if (file != null) {
            try {
                byte[] fileBytes = Files.readAllBytes(file.toPath());
                String fileName = file.getName();
                nameOfFileWithPlaintextTextField.setText(fileName);
                logger.info("Wczytano plik: {}", file.getAbsolutePath());

                if (fileName.endsWith(".txt") || fileName.endsWith(".xml")) {
                    plaintextTextField.setText(new String(fileBytes, StandardCharsets.UTF_8));
                } else {
                    plaintextTextField.setText(Base64.getEncoder().encodeToString(fileBytes));
                }
            } catch (IOException e) {
                logger.error("Błąd odczytu pliku", e);
            }
        }
    }

    private File showOpenDialog() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Otwórz plik");
        Stage stage = (Stage) loadButton.getScene().getWindow();
        return fileChooser.showOpenDialog(stage);
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
        //zabezpieczenie przed przypadkiem, gdy klucz nie jest w HEX
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

        //tworzymy podklucze w oparciu o bajty klucza
        DES des = new DES();
        des.createSubKeysArray(keyBytes);

        //pobieramy tekst jawny jako Sting z text area
        String plaintext = plaintextTextField.getText();
        //przekształcamy String na bajty: jeśli wcześniej pobrano plik .txt lub wybrano opcję 'Okno' kodowanie jest uznane za UTF-8
        //w przeciwnym razie - base64
        byte[] textBytes;
        String nameOfOpenedFile = nameOfFileWithPlaintextTextField.getText();
        if (radioButtonOkno.isSelected() || nameOfOpenedFile.endsWith(".txt")) {
            textBytes = plaintext.getBytes(StandardCharsets.UTF_8);
        } else {
            textBytes = Base64.getDecoder().decode(plaintext);
        }
        //szyfrujemy, a następnie wypisujemy w drugim textArea jako base64 (ZAWSZE)
        byte[] encrypted = des.encryptBlocks(textBytes);
        String encryptedBase64 = Base64.getEncoder().encodeToString(encrypted);
        ciphertextTextField.setText(encryptedBase64);
    }

    @FXML
    public void decrypt(){
        String key = keyValueTextField.getText();
        //zabezpieczenie przed przypadkiem, gdy klucz nie jest w HEX
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

        //tworzymy podklucze w oparciu o bajty klucza
        DES des = new DES();
        des.createSubKeysArray(keyBytes);
        //pobieramy zawartość textArea z kodowaniem base64
        byte[] textBytes = Base64.getDecoder().decode(ciphertextTextField.getText());
        byte[] decrypted = des.decryptBlocks(textBytes);
        //zapisujemy odszyfrowany tekst w pierwszym textArea z kodowaniem UTF-8 (jeśli wcześniej był otwierany plik .txt
        //lub jeśli wybrano opcje 'Okno', w przeciwnym razie base64
        String plaintext;
        String nameOfOpenedFile = nameOfFileWithPlaintextTextField.getText();
        if (radioButtonOkno.isSelected() || nameOfOpenedFile.endsWith(".txt")) {
            plaintext = new String(decrypted, StandardCharsets.UTF_8);
        } else {
            //textBytes = Base64.getDecoder().decode(plaintext);
            plaintext = Base64.getEncoder().encodeToString(decrypted);
        }
        plaintextTextField.setText(plaintext);
    }

    @FXML
    public void generateKey(){
        //generujemy klucz w sposób losowy
        byte[] generatedKey = Algorithms.generateRandomKey();
        String key = HexFormat.of().formatHex(generatedKey);
        keyValueTextField.setText(key);
    }
}
