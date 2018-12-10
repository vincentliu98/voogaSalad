package social;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class RegisterScreen {
    public static final String MOTTO = "Enter a username and password below.";

    private GridPane myPane;
    private Scene myScene;
    private Stage myStage;

    public RegisterScreen() { }

    public Stage launchRegistration(){
        myStage = new Stage();

        initPane();
        initScene();
        initMotto();
        initFields();

        myStage.setScene(myScene);
        myStage.setTitle("Registration");
        return myStage;
    }

    private void initPane(){
        myPane = new GridPane();
        myPane.setAlignment(Pos.TOP_CENTER);
        myPane.setVgap(15.0D);
        myPane.setPadding(new Insets(40.0D, 70.0D, 40.0D, 70.0D));

        for(int i = 0; i < 4; ++i) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(25.0D);
            myPane.getColumnConstraints().add(col);
        }

        myPane.setGridLinesVisible(false);
    }

    private void initScene(){
        myScene = new Scene(myPane, 400.0D, 500.0D);
    }

    private void initMotto(){
        Text mottoText = new Text(MOTTO);
        HBox mottoBox = new HBox();
        mottoBox.getChildren().add(mottoText);
        mottoBox.setAlignment(Pos.CENTER);

        myPane.add(mottoBox, 0, 1, 4, 1);
    }

    private void initFields(){
        TextField usernameField = new TextField();
        usernameField.setPromptText("username");
        TextField passwordField = new TextField();
        passwordField.setPromptText("password");
        Button btn = new Button("CREATE ACCOUNT");
        btn.setPrefWidth(260.0D);

        btn.setOnMouseClicked(e -> {
            // TODO: Check for username not taken, add user
            myStage.close();
        });

        myPane.add(usernameField, 0, 2, 4, 1);
        myPane.add(passwordField, 0, 3, 4, 1);
        myPane.add(btn, 0, 5, 4, 1);
    }
}