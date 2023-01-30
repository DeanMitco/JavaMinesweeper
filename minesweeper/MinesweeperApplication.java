package com.example.minesweeper;

import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

// The main class for the Minesweeper game, extends the javafx Application class.
public class MinesweeperApplication extends Application implements Initializable {

    /**
     * The starting point of the application, sets the scene and shows the stage.
     * @param stage the primary stage for the application.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MinesweeperApplication.class.getResource("minesweeper-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Minesweeper");
        scene.setFill(Color.rgb(241, 250, 238));
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    // The Pane for the scene
    Pane root = new Pane();

    // AnchorField, where the Minesweeper game will be shown
    @FXML
    AnchorPane anchorField;
    @FXML
    private Button easyBtn;
    @FXML
    private Button mediumBtn;
    @FXML
    private Button hardBtn;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private AnchorPane anchorBackground;
    @FXML
    private HBox difficultyHbox;
    @FXML
    public Label flagsSeted;
    @FXML
    private Label fieldBombs;
    @FXML
    private Image flagsSet;

    // public static Image img = new File("img/flag.png");

    // all the flags, which have been already set
    public static int flagsChoosen = 0;

    // Displays the desired mode. Mode 0 is easy, mode 1 is medium and mode 2 is hard
    public static int mode = 0;
    // The size of a field in pixels.
    public static int FIELD_SIZE = 30;

    // The number of vertical fields in the grid for easy.
    private static int Y_FIELDS = 8;

    // The number of horizontal fields in the grid.
    private static int X_FIELDS = 8;

    // The 2D array representing the game grid.
    private static Field[][] grid = null;
    // The number of bombs in the game.
    static int maxBombs;
    // The number of opened fields in the game.
    static int openedFields = 0;

    static int fields;

    @FXML
    public void handleButtonAction(Event event){
        if(event.getSource() == hardBtn){
            mode = 2;
            X_FIELDS = 30;
            Y_FIELDS = 16;
            FIELD_SIZE = 20;
            fields = 480;
            createContent();
        } else if (event.getSource() == mediumBtn) {
            mode = 1;
            X_FIELDS = 16;
            Y_FIELDS = 16;
            FIELD_SIZE = 25;
            fields = 256;
            createContent();
        } else {
            mode = 0;
            X_FIELDS = 8;
            Y_FIELDS = 8;
            FIELD_SIZE = 30;
            fields = 64;
            createContent();
        }

        difficultyHbox.setVisible(false);
        root = (Pane) createContent();
    }

    /**
     * Creates and returns the root pane for the scene.
     * @return the root pane for the scene.
     */
    public Parent createContent(){
        root = new Pane();
        root.setPrefSize(500,400);
        anchorField.getChildren().add(root);
        root.setPrefSize(320,320);
        Field field = null;
        grid = new Field[X_FIELDS][Y_FIELDS];

            for(int y = 0; y < Y_FIELDS; y++) {
                for (int x = 0; x < X_FIELDS; x++) {
                    if(mode == 2){
                        field = new Field(x, y, Math.random() <= 0.2);   // Generate for Hard the chance Bombs
                    }else if (mode == 1){
                        field = new Field(x, y, Math.random() <= 0.15);   // Generate for Middle the chance Bombs
                    }else {
                        field = new Field(x, y, Math.random() <= 0.1);   // Generate for Easy the chance Bombs
                    }
                    grid[x][y] = field;
                    root.getChildren().add(field);
                }
            }

        setBombCount();
        fieldBombs.setText(Integer.toString(maxBombs));
        return root;
    }

    // Sets the bomb count for each field in the grid.

    private void setBombCount(){
        if(mode == 2){
            maxBombs = 480;
        } else if (mode == 1){
            maxBombs = 256;
        } else {
            maxBombs = 64;
        }
        for(int x = 0; x < X_FIELDS; x++){
            for(int y = 0; y < Y_FIELDS; y++){
                Field field = grid[x][y];
                if(!field.isHasBomb()){
                    int bombs = 0;
                    maxBombs--;
                    List<Field> neighbours = getNeighbours(field);

                    for (Field f: neighbours) if(f.isHasBomb()) bombs++;        // Add bombs to the field

                    if(bombs > 0) grid[x][y].bombCount.setText(String.valueOf(bombs));
                }
            }
        }
    }

    // Get Surrounding Fields
    static List<Field> getNeighbours(Field field){
        ArrayList<Field> neighbours = new ArrayList<>();
            int[] points = new int[]{-1, -1, -1, 0, -1, 1, 0, -1, 0, 1, 1, -1, 1, 0, 1, 1};         // Surrounding Field
            for (int i = 0; i< points.length; i+=2) {
                int dx = points[i];     //delta X
                int dy = points[i+1];   // delta Y

                int newX = field.getX() + dx;   // get the field on the specific Position
                int newY = field.getY() + dy;

                //add neighbour only if in bounds
                if(newX >= 0 && newX < X_FIELDS && newY >= 0 && newY < Y_FIELDS) {
                    neighbours.add(grid[newX][newY]);
                }
            }
        return neighbours;
    }

    public void restart() {
        maxBombs = 0;
        fieldBombs.setText(Integer.toString(maxBombs));
        difficultyHbox.setVisible(true);
        anchorField.getChildren().clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        root = (Pane) null;
    }

    public static void main(String[] args) {
        launch();
    }
}