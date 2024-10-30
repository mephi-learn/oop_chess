package chess;

public class Bishop extends ChessPiece {
    public Bishop(String color) {
        super(color);
    }

    @Override
    public String getSymbol() {
        return "B";
    }

    @Override
    public boolean canMoveToPosition(ChessBoard chessBoard, int line, int column, int toLine, int toColumn) throws BadMoveException {

        // Нельзя двигаться по прямым или стоять на месте
        if (line == toLine || column == toColumn) {
            throw new BadMoveException("слон ходит только по диагоналям");
        }

        // Возвращаем результат перемещения, не встретилось ли на пути фигур
        return chessBoard.checkPath(line, column, toLine, toColumn);
    }
}
