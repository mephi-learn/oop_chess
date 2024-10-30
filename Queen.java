package chess;

public class Queen extends ChessPiece {
    public Queen(String color) {
        super(color);
    }

    @Override
    public String getSymbol() {
        return "Q";
    }

    @Override
    public boolean canMoveToPosition(ChessBoard chessBoard, int line, int column, int toLine, int toColumn) throws BadMoveException {

        int diffLine = Math.abs(toLine - line);
        int diffColumn = Math.abs(toColumn - column);

        // Если переместились не по прямой, но и не по диагонали
        if (line != toLine && column != toColumn && diffLine != diffColumn) {
            throw new BadMoveException("ферзь двигается либо по прямой, либо по диагонали");
        }

        // Возвращаем результат перемещения, не встретилось ли на пути фигур
        return chessBoard.checkPath(line, column, toLine, toColumn);
    }
}
