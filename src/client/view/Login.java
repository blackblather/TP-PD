package client.view;

import common.observable.ObservableController;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Login extends View{
    private TextField txtResp;

    public Login(ObservableController controller, Stage window) {
        super(controller);
        this.window = window;
        controller.AddObserver(this);
    }

    @Override
    public Scene GetScene() {

        VBox root = new VBox(10);
        Scene scene = new Scene(root, 200, 200);
        scene.setFill(Color.WHITE);

        //Label - Username
        Label lblUsername = new Label("Insert username:");

        //Textbox - Username
        TextField txtUsername = new TextField();

        //Label - Password
        Label lblPassword = new Label("Insert password:");

        //Textbox - Password
        TextField txtPassword = new TextField();

        //Button - Login
        Button btnLogin = new Button("Login");
        btnLogin.setOnAction(event -> {
            controller.Login(null, txtUsername.getText(), txtPassword.getText());
        });

        txtResp = new TextField();

        //Add children to root
        root.getChildren().addAll(lblUsername,
                                  txtUsername,
                                  lblPassword,
                                  txtPassword,
                                  btnLogin,
                                  txtResp);

        return scene;
    }

    @Override
    public void OnSuccessfulLogin(Object ref) {
        txtResp.setText("VERY GOOD LOGIN");
    }
}
