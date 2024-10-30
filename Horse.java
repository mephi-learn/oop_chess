package chess;

public class Horse extends ChessPiece {

    public Horse(String color) {
        super(color);
    }

    @Override
    public boolean canMoveToPosition(ChessBoard chessBoard, int line, int column, int toLine, int toColumn) throws BadMoveException {

        // Разница в ходе коня должна быть в единицу по одной оси и в двойку по другой
        if ((Math.abs(line - toLine) == 1 && Math.abs(column - toColumn) == 2) || (Math.abs(line - toLine) == 2 && Math.abs(column - toColumn) == 1)) {
            return true;
        }
        throw new BadMoveException("конь ходит буквой Г");
    }

    public String getSymbol() {
        return "H";
    }
}
