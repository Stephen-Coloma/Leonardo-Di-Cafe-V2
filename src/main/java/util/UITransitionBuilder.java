package util;

import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class UITransitionBuilder {

    public static FadeTransition createFadeTransition(Node node, double millisDuration, double initialOpacityValue, double finalOpacityValue) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(millisDuration), node);
        fadeTransition.setFromValue(initialOpacityValue);
        fadeTransition.setToValue(finalOpacityValue);
        return fadeTransition;
    } // end of createFadeTransition

    public static void createScaleTransition(Node node, double scaleInDuration, double scaleOutDuration, double initialScaleValue, double finalScaleValue) {
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(scaleInDuration), node);
        ScaleTransition scaleOut = new ScaleTransition(Duration.millis(scaleOutDuration), node);

        scaleIn.setFromX(initialScaleValue);
        scaleIn.setFromY(initialScaleValue);
        scaleIn.setToX(finalScaleValue);
        scaleOut.setToY(finalScaleValue);

        scaleOut.setFromX(finalScaleValue);
        scaleOut.setFromY(finalScaleValue);
        scaleOut.setToX(initialScaleValue);
        scaleOut.setToY(initialScaleValue);

        node.setOnMouseEntered(event -> {
            scaleOut.stop();
            scaleIn.play();
        });

        node.setOnMouseExited(event -> {
            scaleIn.stop();
            scaleOut.play();
        });
    } // end of createScaleTransition

    public static ScaleTransition createScaleTransition(Node node, double millisDuration, double initialScaleValue, double finalScaleValue) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(millisDuration), node);
        scaleTransition.setFromX(initialScaleValue);
        scaleTransition.setFromY(initialScaleValue);
        scaleTransition.setToX(finalScaleValue);
        scaleTransition.setToY(finalScaleValue);
        return scaleTransition;
    } // end of createScaleTransition

    public static FillTransition createFillTransition(Node node, double millisDuration, String initialHexValue, String finalHexValue) {
        FillTransition fillTransition = new FillTransition();

        if (node instanceof Rectangle rectangle) {
            fillTransition.setShape(rectangle);
        } else if (node instanceof  Circle circle) {
            fillTransition.setShape(circle);
        } else {
            return null;
        }

        fillTransition.setDuration(Duration.millis(millisDuration));
        fillTransition.setFromValue(Color.web(initialHexValue));
        fillTransition.setToValue(Color.web(finalHexValue));
        return fillTransition;
    } // end of createFillTransition
} // end of UITransitionBuilder class
