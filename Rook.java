package chess;

public class Rook extends ChessPiece {
    public Rook(String color) {
        super(color);
    }

    @Override
    public String getSymbol() {
        return "R";
    }

    @Override
    public boolean canMoveToPosition(ChessBoard chessBoard, int line, int column, int toLine, int toColumn) throws BadMoveException {

        // Нельзя двигаться по диагоналям
        if ((line != toLine && column != toColumn)) {
            throw new BadMoveException("ладья может двигаться только по прямым");
        }

        // Возвращаем результат перемещения, не встретилось ли на пути фигур
        return chessBoard.checkPath(line, column, toLine, toColumn);
    }
}
