package ru.alterland.java.launcher;

import javafx.animation.FadeTransition;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.util.Duration;

public class EffectUtilities {
    public static void makeDraggable(final Stage stage, final Node byNode) {
        final Delta dragDelta = new Delta();
        byNode.setOnMousePressed(mouseEvent -> {
            dragDelta.x = stage.getX() - mouseEvent.getScreenX();
            dragDelta.y = stage.getY() - mouseEvent.getScreenY();
            byNode.setCursor(Cursor.MOVE);
        });
        byNode.setOnMouseReleased(mouseEvent -> byNode.setCursor(Cursor.DEFAULT));
        byNode.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() + dragDelta.x);
            stage.setY(mouseEvent.getScreenY() + dragDelta.y);
        });
        byNode.setOnMouseEntered(event -> {
            FadeTransition fadeTransition = new FadeTransition(Duration.millis(120), byNode);
            fadeTransition.setToValue(0.6);
            fadeTransition.play();
        });
        byNode.setOnMouseExited(mouseEvent -> {
            FadeTransition fadeTransition = new FadeTransition(Duration.millis(120), byNode);
            fadeTransition.setToValue(0.3);
            fadeTransition.play();
            if (!mouseEvent.isPrimaryButtonDown()) {
                byNode.setCursor(Cursor.DEFAULT);
            }
        });
    }

    private static class Delta {
        double x, y;
    }
}
