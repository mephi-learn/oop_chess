package chess;

public abstract class ChessPiece {
    String color;
    boolean isMove = false;

    public ChessPiece(String color) {
        this.color = color.toLowerCase();
    }

    public String getColor() {
        return color;
    }

    // Если фигура сдвинулась, отмечаем это
    public void moved() {
        this.isMove = true;
    }

    public abstract String getSymbol();

    public abstract boolean canMoveToPosition(ChessBoard chessBoard, int line, int column, int toLine, int toColumn) throws BadMoveException;
}
