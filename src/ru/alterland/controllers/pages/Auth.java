package ru.alterland.controllers.pages;

import animatefx.animation.FadeIn;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import ru.alterland.Main;
import ru.alterland.controllers.MainWrapper;
import ru.alterland.java.ServerData;
import ru.alterland.java.UserData;
import ru.alterland.java.api.exceptions.ApiExceptions;
import ru.alterland.java.api.exceptions.AuthException;
import ru.alterland.java.api.Servers;
import ru.alterland.java.values.Pages;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Auth implements Initializable {

    private static Logger log = Logger.getLogger(Auth.class.getName());

    @FXML
    private ImageView image_shape;

    @FXML
    private JFXTextField login_textField;

    @FXML
    private JFXPasswordField pass_passField;

    /*@FXML
    private GridPane login_wrapper;*/
    @FXML
    private Label error_label;

    private MainWrapper mainWrapper;

    public Auth(MainWrapper mainWrapper){
        this.mainWrapper = mainWrapper;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("Init");
        TranslateTransition transitionX = new TranslateTransition(Duration.millis(4569), image_shape);
        transitionX.setToX(5);
        transitionX.setAutoReverse(true);
        transitionX.setCycleCount(TranslateTransition.INDEFINITE);
        transitionX.play();

        TranslateTransition transitionY = new TranslateTransition(Duration.millis(5731), image_shape);
        transitionY.setToY(5);
        transitionY.setAutoReverse(true);
        transitionY.setCycleCount(TranslateTransition.INDEFINITE);
        transitionY.play();

        RequiredFieldValidator loginValidator = new RequiredFieldValidator();
        loginValidator.setMessage("Введите логин");
        RequiredFieldValidator passValidator = new RequiredFieldValidator();
        passValidator.setMessage("Введите пароль");
        login_textField.getValidators().add(loginValidator);
        pass_passField.getValidators().add(passValidator);
    }

    public void onMouseClicked(MouseEvent mouseEvent) {
        login_textField.setText("testUser");
        pass_passField.setText("testUser");
        log.info("Login button clicked");
        if (!login_textField.validate() || !pass_passField.validate()) return;
        mainWrapper.showMessage("Авторизация...");
        log.info("Auth manager started");
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try {
                log.info("Submit data");
                UserData userData = ru.alterland.java.api.Auth.loginUser(login_textField.getText(), pass_passField.getText());
                Main.setUserData(userData);
                Platform.runLater(() -> mainWrapper.showMessage("Загрузка..."));
                List<ServerData> serverData = Servers.getServerList(userData);
                Platform.runLater(() -> {
                    mainWrapper.showToolbar(userData.getNickname());
                    try {
                        mainWrapper.nextScene(new Pages(mainWrapper).loadMainServers(serverData), MainWrapper.Direction.Right);
                    } catch (IOException e) {
                        Main.fatalError(mainWrapper, e);
                    }
                });
            } catch (AuthException e) {
                switch (e.getAuthType()) {
                    case UserNotFound:
                        log.log(Level.WARNING, "User not found");
                        Platform.runLater(() -> {
                            error_label.setText("Неверные имя пользователя или пароль");
                            new FadeIn(error_label).setSpeed(2).play();
                        });
                        break;
                    case AccessError:
                        log.log(Level.WARNING, "Access error");
                        Platform.runLater(() -> {
                            error_label.setText("Ошибка доступа");
                            new FadeIn(error_label).setSpeed(2).play();
                        });
                        break;
                }
            } catch (ApiExceptions apiExceptions) {
                switch (apiExceptions.getType()) {
                    case ConnectionError:
                        Main.fatalError("Не удается подключиться, проверьте соединение и перезапустите лаунчер", mainWrapper, apiExceptions);
                        break;
                    default:
                        Main.fatalError(mainWrapper, apiExceptions);
                }
            }
            mainWrapper.hideMessage();
        });
    }
}
