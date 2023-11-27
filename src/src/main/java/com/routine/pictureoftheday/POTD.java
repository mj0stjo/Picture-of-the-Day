package com.routine.pictureoftheday;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Main class of the "Picture Of The Day" application.
 *
 * @author Johannes Möst
 */
public class POTD extends Application {

    /**
     * The formatter to format the dates: dd.MM.yyyy
     */
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    /**
     * Today's date.
     */
    private LocalDate today;

    /**
     * The index of the picture shown in the last session.
     */
    private int lastShown;

    private NodeInitializer nodeInitializer;

    @Override
    public void start(Stage stage) throws IOException {
        //today = LocalDate.of(2023, 1, 4);
        today = LocalDate.now();
        nodeInitializer = new NodeInitializer();

        loadData();

        Scene scene = new Scene(initializeGUI(today.getDayOfYear() - 1 > lastShown));
        stage.setTitle("Picture Of The Day: " + formatter.format(today));
        Image icon = new Image("file:rsc/pictures/calendar_icon.png");
        //Image icon = new Image("file:src/main/resources/com/routine/pictureoftheday/pictures/calendar_icon.png");
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        double x = bounds.getMinX() + (bounds.getWidth() - scene.getWidth()) * 0.5;
        double y = bounds.getMinY() + (bounds.getHeight() - scene.getHeight()) * 0.5;
        stage.setX(x);
        stage.setY(y);

        if (today.getYear() == 2022) writeIntoFile(getDataFile(), String.valueOf(today.getDayOfYear()));
    }

    /**
     * Loads the data of the last session.
     */
    private void loadData() {
        try {
            File dataFile = getDataFile();
            if (dataFile.createNewFile()) {
                System.out.println("File created: " + dataFile.getName());
                writeIntoFile(dataFile, "0");
            } else {
                System.out.println("File already exists.");
            }
            lastShown = Integer.parseInt(readFromFile(dataFile));
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Loads the file where the data for this application is stored.
     *
     * @return
     */
    private File getDataFile() {
        //return new File("src/main/resources/com/routine/pictureoftheday/data/POTD_DATA.txt");
        return new File("rsc/data/POTD_DATA.txt");
    }

    /**
     * Writes the given text into the given file.
     *
     * @param dataFile The file this method should write into.
     * @param text     The text that should be written into the file.
     */
    private void writeIntoFile(File dataFile, String text) {
        try {
            FileWriter myWriter = new FileWriter(dataFile);
            myWriter.write(text);
            myWriter.close();
            System.out.println("Successfully wrote to the Data-File.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Reads text from a given file.
     *
     * @param dataFile The file which should be read from.
     * @return The text contained in the given file (first line).
     */
    private String readFromFile(File dataFile) {
        Scanner myReader = null;
        try {
            myReader = new Scanner(dataFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String data = new String();

        if (myReader.hasNextLine()) {
            data = myReader.nextLine();
            System.out.println(data);
        }
        return data;
    }

    /**
     * Creates a Pane for the window which should show up on startup.
     *
     * @param missedPictures Set this true if there were any missed pictures.
     * @return The created Pane containing all the needed Nodes.
     */
    private Pane initializeGUI(boolean missedPictures) {

        Button showAllButton = new Button("Show Previous Pictures");
        showAllButton.setFont(new Font("MV Boli", 20));
        showAllButton.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-background-color: #ff9ebd;");
        showAllButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                new SlideImageWindow(nodeInitializer, "POTD: All Pictures", 1, today.getDayOfYear());
            }
        });

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(5);
        root.setStyle("-fx-background-color: #9effe0");
        root.setPadding(new Insets(5));

        if (today.getYear() != 2022) {
            root.getChildren().addAll(nodeInitializer.createDateLabel(today), nodeInitializer.createImageView("default"));
            missedPictures = false;
            Label otherYearLabel = new Label();
            otherYearLabel.setFont(new Font("MV Boli", 15));
            if (today.getYear() < 2022) {
                otherYearLabel.setText("This calendar starts on the first of january 2022. Stay tuned!");
                root.getChildren().add(otherYearLabel);
            } else {
                otherYearLabel.setText("2022's already over, we hope you had fun with this calendar!");
                root.getChildren().addAll(otherYearLabel, showAllButton);
            }
        } else
            root.getChildren().addAll(nodeInitializer.createDateLabel(today), nodeInitializer.createImageView(String.valueOf(today.getDayOfYear())), showAllButton);

        if (missedPictures) {
            Button missedButton = new Button("Show missed Pictures");
            missedButton.setFont(new Font("Arial", 20));
            missedButton.setFont(new Font("MV Boli", 20));
            missedButton.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-background-color: #ffc1c7;");
            missedButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent arg0) {
                    new SlideImageWindow(nodeInitializer, "POTD: Missed Pictures", lastShown + 1, today.getDayOfYear() - 1);
                }
            });
            root.getChildren().add(missedButton);
        }

        return root;
    }

    public static void main(String[] args) {
        launch(args);
    }
}