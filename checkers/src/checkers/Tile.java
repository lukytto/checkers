package checkers;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends Rectangle {
	
	private Piece piece;
	
	public boolean hasPiece() {
		return piece !=null;
	}
	
	public Piece getPiece() {
		return piece;
	}

	public void setPiece(Piece piece) {
		this.piece = piece;
	}

	public Tile(boolean light, int x, int y) {
	setWidth(MainCheckers.TILE_SIZE);
	setHeight(MainCheckers.TILE_SIZE);
	
	relocate(x*MainCheckers.TILE_SIZE, y*MainCheckers.TILE_SIZE);
	setFill(light ? Color.valueOf("#FFD2A6") : Color.valueOf("#A85D5D"));
	}
}
