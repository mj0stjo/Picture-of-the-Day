package com.routine.pictureoftheday;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.time.LocalDate;

/**
 * This class creates different GUI elements (Nodes).
 *
 * @author Johannes Möst
 */
public class NodeInitializer {

    /**
     * This Method creates a Pane filled with all the elements of a ImageSlideGUI.
     *
     * @param date            The date of which the image should be shown.
     * @param showLeftButton  Set this true if there should be a button to navigate left.
     * @param leftEvent       This event is called when the left button is pressed.
     * @param showRightButton Set this true if there should be a button to navigate right.
     * @param rightEvent      This event is called when the right button is pressed.
     * @return The pane containing all the created nodes.
     */
    public Pane initializeImageSlideGUI(LocalDate date, boolean showLeftButton, EventHandler<ActionEvent> leftEvent, boolean showRightButton, EventHandler<ActionEvent> rightEvent) {
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(5));
        pane.setStyle("-fx-background-color: #ff9ebd");

        //SET TOP
        Label top = createDateLabel(date);
        pane.setTop(top);
        pane.setAlignment(top, Pos.CENTER);

        //SET CENTER
        ImageView iView = createImageView(String.valueOf(date.getDayOfYear()));
        pane.setCenter(iView);
        pane.setMargin(iView, new Insets(5));

        //SET LEFT
        VBox left = new VBox();
        left.setMinWidth(40);
        left.setAlignment(Pos.CENTER);
        if (showLeftButton) {
            Button leftButton = new Button();
            leftButton.setPrefHeight(50);
            leftButton.setPrefWidth(35);
            ImageView leftGraphic = null;
            try {
                //leftGraphic = new ImageView(new Image(new FileInputStream("src\\main\\resources\\com\\routine\\pictureoftheday\\pictures\\arrow.png")));
                leftGraphic = new ImageView(new Image(new FileInputStream("rsc\\pictures\\arrow.png")));
                leftGraphic.setFitWidth(35);
                leftGraphic.setFitHeight(50);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            leftButton.setGraphic(leftGraphic);
            leftButton.setBackground(null);
            leftButton.setOnAction(leftEvent);
            left.getChildren().add(leftButton);
        } else {
            Region spacer = new Region();
            spacer.setPrefWidth(40);
            pane.setLeft(spacer);
        }
        pane.setLeft(left);

        //SET RIGHT
        VBox right = new VBox();
        right.setMinWidth(40);
        right.setAlignment(Pos.CENTER);
        if (showRightButton) {
            Button rightButton = new Button();
            rightButton.setPrefHeight(50);
            rightButton.setPrefWidth(35);
            ImageView rightGraphic = null;
            try {
                //rightGraphic = new ImageView(new Image(new FileInputStream("src/main/resources/com/routine/pictureoftheday/pictures/arrow.png")));
                rightGraphic = new ImageView(new Image(new FileInputStream("rsc/pictures/arrow.png")));
                rightGraphic.setFitWidth(35);
                rightGraphic.setFitHeight(50);
                rightGraphic.setRotate(rightGraphic.getRotate() + 180);
                ;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            rightButton.setGraphic(rightGraphic);
            rightButton.setBackground(null);
            rightButton.setOnAction(rightEvent);
            right.getChildren().add(rightButton);
        } else {
            Region spacer = new Region();
            spacer.setPrefWidth(40);
            pane.setRight(spacer);
        }
        pane.setRight(right);

        return pane;
    }

    /**
     * Creates and returns an ImageView.
     *
     * @param imageName The filename (without file-extension) of the image that should be in the ImageView.
     * @return ImageView containing the given image.
     */
    public ImageView createImageView(String imageName) {

        Image image = null;
        try {
            image = new Image(new FileInputStream("rsc/pictures/" + imageName + ".jpg"));
            //image = new Image(new FileInputStream("src/main/resources/com/routine/pictureoftheday/pictures/" + imageName + ".jpg"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ImageView iView = new ImageView(image);
        iView.setPreserveRatio(true);
        int fitWidth = (image.getWidth() >= image.getHeight()) ? 800 : 250;
        int fitHeight = (int) Math.round(((double) image.getHeight() / (double) image.getWidth()) * (double)fitWidth);
        iView.setFitWidth(fitWidth);
        iView.setFitHeight(fitHeight);
        return iView;
    }

    /**
     * Creates and returns a Label containing a formatted date.
     *
     * @param date The date that should be displayed in the Label.
     * @return The Label containing the date.
     */
    public Label createDateLabel(LocalDate date) {
        Label dateLabel = new Label(date.format(POTD.formatter));
        dateLabel.setFont(new Font("MV Boli", 32));
        return dateLabel;
    }

}
