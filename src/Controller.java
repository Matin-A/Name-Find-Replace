import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.GREEN;
import static javafx.scene.paint.Color.RED;

/**
 * Created by Matin Afkhami on 10/14/17.
 * Java 10 Recommended.
 */
public class Controller {

    private double x = 0;
    private double y = 0;
    private static NameFindAndReplace dirRename;
    private static boolean inTask=false;
    private static boolean rollbackIsDisable=true;
    private static boolean startIsDisable=false;
    private static boolean isRegex=false;
    private static String keyword;
    private static String repKeyword;
    private static String directory;
    private static String message;
    private static Color messageColor;

    @FXML RadioButton regexKeyword;
    @FXML RadioButton exactKeyword;
    @FXML TextField pathTextField;
    @FXML TextField keywordTextField;
    @FXML TextField repKeywordTextField;
    @FXML Label textMessage;
    @FXML Button start;
    @FXML Button rollback;
    @FXML Button info;
    @FXML Button exit;
    @FXML Button cancel;
    @FXML Button minimize;

    
    public void initialize(){
        if (keywordTextField!=null){
            keywordTextField.setText(keyword);
        }
        if (repKeywordTextField!=null){
            repKeywordTextField.setText(repKeyword);
        }
        if (pathTextField!=null){
            pathTextField.setText(directory);
        }
        if (rollback!=null){
            rollback.setDisable(rollbackIsDisable);
        }
        if (start!=null){
            start.setDisable(startIsDisable);
        }
        if (textMessage!=null){
            textMessage.setText(message);
            textMessage.setTextFill(messageColor);
        }
        if (regexKeyword!=null){
            regexKeyword.setSelected(isRegex);
            exactKeyword.setSelected(!isRegex);
        }
    }
    
    public void startClicked() {
        if (Objects.equals(pathTextField.getText(), "") || Objects.equals(keywordTextField.getText(), "")){
            textMessage.setTextFill(RED);
            messageColor = RED;
            message = "Keyword or Path couldn't be empty.";
            textMessage.setText(message);
        }else if (!new File(pathTextField.getText()).exists()) {
            textMessage.setTextFill(RED);
            messageColor = RED;
            message = "Path not exists.";
            textMessage.setText(message);

        }else {
            Executors.newSingleThreadScheduledExecutor().schedule(this::rename,0,TimeUnit.MILLISECONDS);
        }
    }

    
    public void rollbackClicked() {
        if (!new File(dirRename.getCurrentPath()).exists()){
            textMessage.setTextFill(RED);
            messageColor = RED;
            message = "Path not exists.";
            textMessage.setText(message);
        }else{
            Executors.newSingleThreadScheduledExecutor().schedule(this::rollback,0,TimeUnit.MILLISECONDS);
        }
    }

    
    public void telegramClicked() {
        try {
            Desktop.getDesktop().browse(URI.create("https://t.me/MatinAfkhami"));
        } catch (IOException ignored) {}
    }

    
    public void googlePlusClicked() {
        try {
            Desktop.getDesktop().browse(URI.create("http://google.com/+MatinAfkhami"));
        } catch (IOException ignored) {}
    }

    
    public void githubClicked() {
        try {
            Desktop.getDesktop().browse(URI.create("https://github.com/Matin-A"));
        } catch (IOException ignored) {}
    }

    
    public void infoOpen() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/infoForm.fxml"));
        root.setOnMousePressed(e -> {
            x = e.getSceneX();
            y = e.getSceneY();
        });
        root.setOnMouseDragged(e -> {
            Main.primaryStage.setX(e.getScreenX() - x);
            Main.primaryStage.setY(e.getScreenY() - y);
        });
        Main.primaryStage.setScene(new Scene(root, 520, 350));
    }

    
    public void infoClose() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("view/mainForm.fxml"));
        root.setOnMousePressed(e -> {
            x = e.getSceneX();
            y = e.getSceneY();
        });
        root.setOnMouseDragged(e -> {
            Main.primaryStage.setX(e.getScreenX() - x);
            Main.primaryStage.setY(e.getScreenY() - y);
        });
        Main.primaryStage.setScene(new Scene(root,520 ,350));
    }

    
    public void minimClicked() {
        Stage stage = (Stage) minimize.getScene().getWindow();
        stage.setIconified(true);
    }

    
    public void invertClicked() {
        String key = keywordTextField.getText();
        keywordTextField.setText(repKeywordTextField.getText());
        repKeywordTextField.setText(key);
    }

    
    public void fileChooserClicked() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose Directory");
        File selectedDirectory = chooser.showDialog(Main.primaryStage);
        if (selectedDirectory!=null){
            pathTextField.setText(selectedDirectory.getPath());
        }
    }

    
    public void openClicked() {
        if (!new File(pathTextField.getText()).exists()) {
            textMessage.setTextFill(RED);
            messageColor = RED;
            message = "Path not exists.";
            textMessage.setText(message);
        }else {
            try {
                Desktop.getDesktop().open(new File(pathTextField.getText()));
            } catch (IOException ignored) {}
        }
    }

    
    public void exitClicked() throws IOException {
        if (inTask || !rollbackIsDisable){
            Parent root = FXMLLoader.load(getClass().getResource("view/exitForm.fxml"));
            Stage stage = new Stage();
            root.setOnMousePressed(event -> {
                x = event.getSceneX();
                y = event.getSceneY();
            });
            root.setOnMouseDragged(event -> {
                stage.setX(event.getScreenX() - x);
                stage.setY(event.getScreenY() - y);
            });
            stage.setScene(new Scene(root,300 ,150));
            stage.setResizable(false);
            stage.setTitle("Confirm Exit");
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        } else{
            Platform.exit();
        }
    }

    
    public void confirmExitClicked() {
        Platform.exit();
    }

    
    public void cancelClicked() {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    
    public void regexKeywordClicked() {
        isRegex = true;
        exactKeyword.setSelected(false);
    }

    
    public void exactKeywordClicked() {
        isRegex = false;
        regexKeyword.setSelected(false);
    }

    
    public void infoHovered() {
        info.setStyle("-fx-background-color:  linear-gradient(#77aaff, #478cff);" +
                "-fx-background-insets: 4; " +
                "-fx-background-radius: 30; " +
                "-fx-text-fill:  White");
    }

    
    public void infoUnHovered() {
        info.setStyle("-fx-background-color:  linear-gradient(#d1e2ff, #adcbff);" +
                "-fx-background-insets: 4; " +
                "-fx-background-radius: 30; " +
                "-fx-text-fill:  White");
    }

    
    public void infoPressed() {
        info.setStyle("-fx-background-color:  linear-gradient(#478cff, #116aff);" +
                "-fx-background-insets: 4; " +
                "-fx-background-radius: 30; " +
                "-fx-text-fill:  White");
    }

    
    public void infoReleased() {
        info.setStyle("-fx-background-color:  linear-gradient(#d1e2ff, #adcbff);" +
                "-fx-background-insets: 4; " +
                "-fx-background-radius: 30; " +
                "-fx-text-fill:  White");
    }

    
    public void exitPressed() {
        exit.setStyle("-fx-background-color:  linear-gradient(#dd3838,#e01f1f);" +
                "-fx-background-insets: 4; " +
                "-fx-background-radius: 30; " +
                "-fx-text-fill:  White");
    }

    
    public void exitReleased() {
        exit.setStyle("-fx-background-color:  linear-gradient(#e06464,#e05555);" +
                "-fx-background-insets: 4; " +
                "-fx-background-radius: 30; " +
                "-fx-text-fill:  White");
    }

    
    public void exitHovered() {
        exit.setStyle("-fx-background-color:  linear-gradient(#e06464,#e05555);" +
                "-fx-background-insets: 4; " +
                "-fx-background-radius: 30; " +
                "-fx-text-fill:  White");
    }

    
    public void exitUnHovered() {
        exit.setStyle("-fx-background-color:  linear-gradient(#e8c7c7,#e5a7a7);" +
                "-fx-background-insets: 4; " +
                "-fx-background-radius: 30; " +
                "-fx-text-fill:  White");
    }

    
    public void minimPressed() {
        minimize.setStyle("-fx-background-color:  linear-gradient(#0fd830,#00d122);" +
                "-fx-background-insets: 4; " +
                "-fx-background-radius: 30; " +
                "-fx-text-fill:  White");
    }

    
    public void minimReleased() {
        minimize.setStyle("-fx-background-color:  linear-gradient(#c9edcf, #a7e5b2);" +
                "-fx-background-insets: 4; " +
                "-fx-background-radius: 30; " +
                "-fx-text-fill:  White");
    }

    
    public void minimHovered() {
        minimize.setStyle("-fx-background-color:  linear-gradient(#55e087, #2fe070);" +
                "-fx-background-insets: 4; " +
                "-fx-background-radius: 30; " +
                "-fx-text-fill:  White");
    }

    
    public void minimUnHovered() {
        minimize.setStyle("-fx-background-color:  linear-gradient(#c9edcf, #a7e5b2);" +
                "-fx-background-insets: 4; " +
                "-fx-background-radius: 30; " +
                "-fx-text-fill:  White");
    }

    private void rename() {
        inTask = true;
        start.setDisable(true);
        startIsDisable = true;
        keyword = keywordTextField.getText();
        repKeyword = repKeywordTextField.getText();
        directory = pathTextField.getText();
        try {
            textMessage.setTextFill(BLACK);
            messageColor = BLACK;
            Platform.runLater(() -> textMessage.setText("Analysing Directory..."));
            message = "Analysing Directory...";
            dirRename = new NameFindAndReplace(new File(directory), keyword, repKeyword, regexKeyword.isSelected());
        } catch (Exception ignored){}
        try {
            textMessage.setTextFill(BLACK);
            messageColor = BLACK;
            Platform.runLater(() -> textMessage.setText("Renaming..."));
            message = "Renaming...";
            dirRename.replaceKeyword();
            textMessage.setTextFill(GREEN);
            messageColor = GREEN;
            Platform.runLater(() -> textMessage.setText(dirRename.getFilesToRename() +"/" +dirRename.getTotalFiles() +" File(s) or Folder(s) Rename Done!"));
            message = dirRename.getFilesToRename() +"/" +dirRename.getTotalFiles() +" File(s) or Folder(s) Rename Done!";
            if (dirRename.getFilesToRename()!=0) {
                rollback.setDisable(false);
                rollbackIsDisable = false;
            }
        } catch(Exception e){
            textMessage.setTextFill(RED);
            messageColor = RED;
            Platform.runLater(() -> textMessage.setText("Something went wrong.\nSome/All file(s) may not be renamed."));
            message = "Something went wrong.\nSome/All file(s) may not be renamed.";
        }
        start.setDisable(false);
        startIsDisable = false;
        inTask = false;
    }

    private void rollback() {
        inTask = true;
        rollback.setDisable(true);
        rollbackIsDisable = true;
        try {
            textMessage.setTextFill(BLACK);
            messageColor = BLACK;
            Platform.runLater(() -> textMessage.setText("Renaming..."));
            message = "Renaming...";
            dirRename.rollbackKeyword();
            textMessage.setTextFill(GREEN);
            messageColor = GREEN;
            Platform.runLater(() -> textMessage.setText(dirRename.getFilesToRename() +"/" +dirRename.getTotalFiles() +" File(s) or Folder(s) Rollback Done!"));
            message = dirRename.getFilesToRename() +"/" +dirRename.getTotalFiles() +" File(s) or Folder(s) Rollback Done!";
        } catch (Exception e){
            textMessage.setTextFill(RED);
            messageColor = RED;
            Platform.runLater(() -> textMessage.setText("Something went wrong.\n Rollback unsuccessful."));
            message = "Something went wrong.\n Rollback unsuccessful.";
        }
        inTask = false;
    }
}