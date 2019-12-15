package checkers;

public class MoveResult {
	
	private MoveType type;
	private Piece piece;
	
	public MoveType getType() {
		return type;
	}
	
	public Piece getPiece() {
		return piece;
	}


	public MoveResult(MoveType type) { //jei ner ka judint
		this(type, null);
	}

	public MoveResult(MoveType type, Piece piece) {
		this.type = type;
		this.piece = piece;
	}

}
