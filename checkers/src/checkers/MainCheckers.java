package checkers;


import java.util.Arrays;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class MainCheckers extends Application {
	
	 public static final int TILE_SIZE=100, WIDTH =8, HEIGHT =8;
	 private boolean TURN = true;
	 private TextArea text = new TextArea();
	 private short redCount = 12, whiteCount = 12;
	 private Tile[][] board = new Tile[WIDTH][HEIGHT];

	    private Group tileGroup = new Group();
	    private Group pieceGroup = new Group();
	    private Group textGroup = new Group();

	    private Parent createContent() {
	        Pane root = new Pane();
	        root.setPrefSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE +50);
	        root.getChildren().addAll(tileGroup, pieceGroup, textGroup);

	        for (int y = 0; y < HEIGHT; y++) {
	            for (int x = 0; x < WIDTH; x++) {
	                Tile tile = new Tile((x + y) % 2 == 0, x, y);
	                board[x][y] = tile;

	                tileGroup.getChildren().add(tile);

	                Piece piece = null;

	                if (y <= 2 && (x + y) % 2 != 0) {
	                    piece = makePiece(PieceType.RED, x, y);
	                }

	                if (y >= 5 && (x + y) % 2 != 0) {
	                    piece = makePiece(PieceType.WHITE, x, y);
	                }

	                if (piece != null) {
	                    tile.setPiece(piece);
	                    pieceGroup.getChildren().add(piece);
	                }
	            }
	        }
	       
	        text.setPrefHeight(50);
	        text.setPrefWidth(WIDTH * TILE_SIZE);
	        text.setLayoutX(0);
	        text.setLayoutY(TILE_SIZE * HEIGHT);
	        textGroup.getChildren().add(text);
	        text.setStyle("-fx-text-fill: deeppink; -fx-font-size: 30; -fx-font-style: italic; -fx-font-weight: bold;");
	        text.setText("White moves first");
	        text.setDisable(true);
	        return root;
	    }

	    
	    private boolean checkBounds(int newX, int newY) {
	        return newX >= 0 && newX <= 7 && newY >= 0 && newY <= 7;
	    }

	    private boolean checkForNext(int newX, int newY, PieceType type, boolean king) {
	        if (type != PieceType.WHITE || king) {
	            if (checkBounds(newX - 1, newY + 1) && checkBounds(newX - 2, newY + 2)) {
	                if (board[newX - 1][newY + 1].hasPiece() && type != board[newX - 1][newY + 1].getPiece().getType()
	                		&& !board[newX - 2][newY + 2].hasPiece())
	                    return true;
	            }
	            if (checkBounds(newX + 1, newY + 1) && checkBounds(newX + 2, newY + 2)) {
	                if (board[newX + 1][newY + 1].hasPiece() && type != board[newX + 1][newY + 1].getPiece().getType()
	                        && !board[newX + 2][newY + 2].hasPiece())
	                    return true;
	            }
	        }
	        if (type != PieceType.RED || king) {
	            if (checkBounds(newX - 1, newY - 1) && checkBounds(newX - 2, newY - 2)) {
	                if (board[newX - 1][newY - 1].hasPiece() && type != board[newX - 1][newY - 1].getPiece().getType()
	                        && !board[newX - 2][newY - 2].hasPiece())
	                    return true;
	            }
	            if (checkBounds(newX + 1, newY - 1) && checkBounds(newX + 2, newY - 2)) {
	                if (board[newX + 1][newY - 1].hasPiece() && type != board[newX + 1][newY - 1].getPiece().getType()
	                        && !board[newX + 2][newY - 2].hasPiece())
	                    return true;
	            }
	        }
	        return false;
	    }
	    
	    private boolean checkNear() {
	        PieceType type;
	        if (TURN)
	            type = PieceType.WHITE;
	        else type = PieceType.RED;
	        return Arrays.stream(board).anyMatch(i ->
	                	Arrays.stream(i).anyMatch(j -> 
	                	j.hasPiece() && j.getPiece().getType() == type &&
	                        checkForNext(toBoard(j.getPiece().getOldX()), toBoard(j.getPiece().getOldY()),j.getPiece().getType(), j.getPiece().king)
	                        				
	                        )
	                );
	    }
	    
	
	    
	    
	    private MoveResult tryMove(Piece piece, int newX, int newY) {
	        if (board[newX][newY].hasPiece() || (newX + newY) % 2 == 0)
	            return new MoveResult(MoveType.NONE);
	        if (piece.getType() == PieceType.RED && TURN)
	            return new MoveResult(MoveType.NONE);
	        if (piece.getType() == PieceType.WHITE && !TURN)
	            return new MoveResult(MoveType.NONE);

	        int x0 = toBoard(piece.getOldX());
	        int y0 = toBoard(piece.getOldY());

	        if (piece.king) {
	            if (Math.abs(newX - x0) == 1 && Math.abs(newY - y0) == 1) {
	                if (checkNear())
	                    return new MoveResult(MoveType.NONE);
	                setTurn(piece.getType());
	                return new MoveResult(MoveType.NORMAL);
	            }
	            if (Math.abs(newX - x0) == 2 && Math.abs(newY - y0) == 2) {
	                int x1 = x0 + (newX - x0) / 2;	               
	                int y1 = y0 + (newY - y0) / 2;

	                if (board[x1][y1].hasPiece() && board[x1][y1].getPiece().getType() != piece.getType()) {
	                    if (!checkForNext(newX, newY, piece.getType(), piece.king))
	                        setTurn(piece.getType());
	                    return new MoveResult(MoveType.KILL, board[x1][y1].getPiece());
	                }
	            }
	        }

	        if (Math.abs(newX - x0) == 1 && newY - y0 == piece.getType().moveDir) {
	            if (checkNear())
	                return new MoveResult(MoveType.NONE);
	            setTurn(piece.getType());
	            return new MoveResult(MoveType.NORMAL);
	        } else if (Math.abs(newX - x0) == 2 && newY - y0 == piece.getType().moveDir * 2) {

	            int x1 = x0 + (newX - x0) / 2;
	            int y1 = y0 + (newY - y0) / 2;
	            
	            if (board[x1][y1].hasPiece() && board[x1][y1].getPiece().getType() != piece.getType()) {
	                if (!checkForNext(newX, newY, piece.getType(), piece.king))
	                    setTurn(piece.getType());
	                return new MoveResult(MoveType.KILL, board[x1][y1].getPiece());
	            }
	        }
	        return new MoveResult(MoveType.NONE);
	    }

	    
	    
	    
	    private int toBoard(double pixel) {
	        return (int)(pixel + TILE_SIZE / 2) / TILE_SIZE;
	    }

	    @Override
	    public void start(Stage primaryStage) throws Exception {
	        Scene scene = new Scene(createContent());
	        primaryStage.setTitle("Checkers");
	        primaryStage.setScene(scene);
	        primaryStage.show();
	    }

	    private Piece makePiece(PieceType type, int x, int y) {
	        Piece piece = new Piece(type, x, y, false);

	        piece.setOnMouseReleased(e -> {
	        	int newX = toBoard(piece.getLayoutX());
	            int newY = toBoard(piece.getLayoutY());

	            MoveResult result;

	            if (newX < 0 || newY < 0 || newX >= WIDTH || newY >= HEIGHT) {
	                result = new MoveResult(MoveType.NONE);
	            } else {
	                result = tryMove(piece, newX, newY);
	            }

	            int x0 = toBoard(piece.getOldX());
	            int y0 = toBoard(piece.getOldY());

	            switch (result.getType()) {
	                case NONE:
	                    piece.abortMove();
	                    checkForWin();
	                    break;
	                case NORMAL:
	                    piece.move(newX, newY);
	                    board[x0][y0].setPiece(null);
	                    
	                    if (piece.getType() == PieceType.RED && newY == 7)
	                        piece.setToKing();
	                    else if (piece.getType() == PieceType.WHITE && newY == 0)
	                        piece.setToKing();
	                    board[newX][newY].setPiece(piece);
	                    break;
	                case KILL:
	                    piece.move(newX, newY);
	                    board[x0][y0].setPiece(null);
	                    
	                    if (piece.getType() == PieceType.RED && newY == 7)
	                        piece.setToKing();
	                    else if (piece.getType() == PieceType.WHITE && newY == 0)
	                        piece.setToKing();
	                    
	                    board[newX][newY].setPiece(piece);
	                    Piece otherPiece = result.getPiece();
	                    if (otherPiece.getType() == PieceType.RED)
	                        redCount--;
	                    if (otherPiece.getType() == PieceType.WHITE)
	                        whiteCount--;
	                  
	                    board[toBoard(otherPiece.getOldX())][toBoard(otherPiece.getOldY())].setPiece(null);
	                    pieceGroup.getChildren().remove(otherPiece);
	                    checkForWin();
	                    break;
	            }
	        });

	        return piece;
	    }
	    
	    private void setTurn(PieceType type) {
	        if (type == PieceType.RED) {
	            TURN = true;
	            text.setText("White moves");
	            return;
	        }
	        text.setText("Red moves");
	        TURN = false;
	    }
	    
	    private boolean checkForWin() {
	        if (whiteCount == 0) {
	            text.setText("Red wins");
	            
	            
	            return true;
	        }
	        if (redCount == 0) {
	            text.setText("White wins");
	            
	            
	            return true;
	        }
	        return false;
	    }

	    public static void main(String[] args) {
	        launch(args);
	    }
}
