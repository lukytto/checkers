package checkers;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;


import static checkers.MainCheckers.TILE_SIZE;

public class Piece extends StackPane {
	
	Ellipse ellipse = new Ellipse(TILE_SIZE * 0.3125, TILE_SIZE * 0.26);
    private PieceType type;
    boolean king =false;
    private double mouseX, mouseY;
    private double oldX, oldY;

    public PieceType getType() {
        return type;
    }

    public double getOldX() {
        return oldX;
    }

    public double getOldY() {
        return oldY;
    }
    

 

	

	public Piece(PieceType type, int x, int y, boolean king) {
        this.type = type;
        this.king = king;
        move(x, y);
        
        if(!king) {
        
        ellipse.setFill(type == PieceType.RED
                ? Color.valueOf("#c40003") : Color.valueOf("#fff9f4"));

        ellipse.setStroke(Color.BLACK);
        ellipse.setStrokeWidth(TILE_SIZE * 0.03);

        ellipse.setTranslateX((TILE_SIZE - TILE_SIZE * 0.3125 * 2) / 2);
        ellipse.setTranslateY((TILE_SIZE - TILE_SIZE * 0.26 * 2) / 2);
        }else {
        	 ellipse.setFill(type == PieceType.RED
                     ? Color.valueOf("#91195d") : Color.valueOf("#f7dd34"));
        }

        getChildren().add(ellipse);

        setOnMousePressed(e -> {
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
        });

        setOnMouseDragged(e -> {
            relocate(e.getSceneX() - mouseX + oldX, e.getSceneY() - mouseY + oldY);
        });
    }

    public void move(int x, int y) {
        oldX = x * TILE_SIZE;
        oldY = y * TILE_SIZE;
        relocate(oldX, oldY);
    }

    public void abortMove() {
        relocate(oldX, oldY);
    }

	public void setToKing() {
	   	ellipse.setFill(type == PieceType.RED
                ? Color.valueOf("#91195d") : Color.valueOf("#f7dd34"));
		king=true;
		
	}


    

}