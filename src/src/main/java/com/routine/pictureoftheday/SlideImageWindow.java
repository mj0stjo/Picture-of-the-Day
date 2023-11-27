package com.routine.pictureoftheday;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.time.LocalDate;

/**
 * Represents a window containing a ImageSlide.
 *
 * @author Johannes Möst
 */
public class SlideImageWindow {

    /**
     * The index of the currently displayed image.
     */
    private int currentIndex;
    private int maxIndex;
    private int minIndex;

    private Pane root;
    private Stage stage;
    private Scene scene;
    private NodeInitializer nodeInitializer;

    /**
     * Constructor to create and open a new SlideImageWindow.
     *
     * @param nI         A NodeInitializer so the needed Nodes can be created.
     * @param title      The title of the window.
     * @param firstIndex The index of the "first" image that should be displayed.
     * @param lastIndex  The index of the "last" image that should be displayed.
     */
    public SlideImageWindow(NodeInitializer nI, String title, int firstIndex, int lastIndex) {
        currentIndex = lastIndex;
        minIndex = firstIndex;
        maxIndex = lastIndex;
        nodeInitializer = nI;

        root = nodeInitializer.initializeImageSlideGUI(LocalDate.ofYearDay(2022, currentIndex), minIndex != maxIndex, createLeftEventHandler(), false, createRightEventHandler());

        scene = new Scene(root);
        stage = new Stage();
        stage.setTitle(title);
        //Image icon = new Image("file:src/main/resources/com/routine/pictureoftheday/pictures/calendar_icon.png");
        Image icon = new Image("file:rsc/pictures/calendar_icon.png");
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.setX(0);
        stage.setY(0);
        stage.show();

        fixWindowSize();
    }

    /**
     * Creates an EventHandler for the left Button;
     *
     * @return The created EventHandler.
     */
    private EventHandler<ActionEvent> createLeftEventHandler() {
        EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                root = nodeInitializer.initializeImageSlideGUI(LocalDate.ofYearDay(2022, --currentIndex), currentIndex != minIndex, createLeftEventHandler(), true, createRightEventHandler());
                scene.setRoot(root);
                fixWindowSize();
            }
        };
        return eventHandler;
    }

    /**
     * Creates an EventHandler for the right Button;
     *
     * @return The created EventHandler.
     */
    private EventHandler<ActionEvent> createRightEventHandler() {
        EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                root = nodeInitializer.initializeImageSlideGUI(LocalDate.ofYearDay(2022, ++currentIndex), true, createLeftEventHandler(), currentIndex != maxIndex, createRightEventHandler());
                scene.setRoot(root);
                fixWindowSize();
            }
        };
        return eventHandler;
    }

    /**
     * Adjust the Window size according to the showed image.
     */
    private void fixWindowSize() {
        if(stage.isMaximized()) return;
        stage.setWidth(((ImageView) ((BorderPane) scene.getRoot()).getCenter()).getFitWidth() + 150);
        stage.setHeight(((ImageView) ((BorderPane) scene.getRoot()).getCenter()).getFitHeight() + 125);
        stage.show();
    }

}
