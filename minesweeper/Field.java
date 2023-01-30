package com.example.minesweeper;

import javafx.scene.control.Alert;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Field extends StackPane {

    private int x;
    private int y;
    public boolean hasBomb = false;         // If this specific Field has a bomb
    public Text bombCount;                  // The surrounding bombcount
    public Rectangle fieldNode = null;      // The Field
    public boolean isOpen;                  // If the Field has been already clicken on
    public boolean flagset = false;         // If the Field has been marked with a flag

    public int flags;

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public boolean isHasBomb() {
        return hasBomb;
    }

    public Field(int x, int y, boolean hasBomb) {
        this.x = x;
        this.y = y;
        this.hasBomb = hasBomb;

        //Create Node //Rectange oder Button für Feld
        fieldNode = new Rectangle(MinesweeperApplication.FIELD_SIZE - 2, MinesweeperApplication.FIELD_SIZE - 2);
        fieldNode.setFill(Color.LIGHTGREEN);
        fieldNode.setStroke(Color.BLACK);
        fieldNode.setVisible(true);

        bombCount = new Text();
        bombCount.setText(this.hasBomb ? "X" : "");
        bombCount.setStroke(Color.BLACK);
        bombCount.setVisible(false);

        setOnMouseClicked(this::onFieldClicked);

        getChildren().addAll(fieldNode, bombCount);
        setTranslateX(x * MinesweeperApplication.FIELD_SIZE);
        setTranslateY(y * MinesweeperApplication.FIELD_SIZE);

    }

    private void onFieldClicked(MouseEvent e) {
        if(e.getButton().equals(MouseButton.PRIMARY) && !flagset) open();
        else if (e.getButton().equals(MouseButton.SECONDARY)) flagSet();
    }

    // Opens Field
    public void open(){
        if (this.isOpen || flagset)
            return;
        this.isOpen = true;
        bombCount.setVisible(true);
        fieldNode.setFill(Color.SANDYBROWN);
        if(bombCount.getText().isEmpty()) {     //Recursion when no bombs are near
            MinesweeperApplication.getNeighbours(this).forEach(Field::open);
        }
        MinesweeperApplication.openedFields++;
        stop();
    }

    public void flagSet(){

        if(flagset && !isOpen){
            fieldNode.setFill(Color.LIGHTGREEN);
            flagset = false;
            MinesweeperApplication.flagsChoosen--;
        } else if(!flagset && !isOpen){
            fieldNode.setFill(Color.RED);
            flagset = true;
            MinesweeperApplication.flagsChoosen++;
        }
    }

    public void stop(){             // Stops the game after won or hit a bomb
        if(this.hasBomb){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);           // Makes an alert after hit a bomb
            alert.setTitle("The End");
            alert.setContentText("You have clicked on a bomb!");
            alert.showAndWait();

            System.exit(0);
        } else if((MinesweeperApplication.fields - MinesweeperApplication.maxBombs == MinesweeperApplication.openedFields)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);           // Makes an alert after won a game
            alert.setTitle("The End");
            alert.setContentText("You have won the game!!!");
            alert.showAndWait();

            System.exit(0);
        }
    }

}
