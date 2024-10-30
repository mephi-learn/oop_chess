package chess;

public class Pawn extends ChessPiece {
    public Pawn(String color) {
        super(color);
    }

    @Override
    public boolean canMoveToPosition(ChessBoard chessBoard, int line, int column, int toLine, int toColumn) throws BadMoveException {

        int diff = toLine - line;
        int diffH = Math.abs(toColumn - column);

        ChessPiece figure;
        // Белые могут рубить на один ход по диагонали вперёд
        if (color.equals("white") && (diff == 1 && diffH == 1) && (figure = chessBoard.board[toLine][toColumn]) != null && figure.getColor().equalsIgnoreCase("black")) {
            return chessBoard.checkPath(line, column, toLine, toColumn);
        }

        // Чёрные могут рубить на один ход по диагонали вперёд
        if (color.equals("black") && (diff == -1 && diffH == 1) && (figure = chessBoard.board[toLine][toColumn]) != null && figure.getColor().equalsIgnoreCase("white")) {
            return chessBoard.checkPath(line, column, toLine, toColumn);
        }


        // Только вперёд!
        if (column != toColumn) {
            throw new BadMoveException("пешка может двигаться только вперёд");
        }

        // Белые могут ходить вверх на одну клетку, но если стоят на стартовой позиции, то и на две. При этом ходить можно только в пустую клетку
        if (color.equals("white") && (diff == 1 || (line == 1 && diff == 2)) && chessBoard.board[toLine][toColumn] == null) {
            return chessBoard.checkPath(line, column, toLine, toColumn);
        }

        // Чёрные могут ходить вниз на одну клетку, но если стоят на стартовой позиции, то и на две. При этом ходить можно только в пустую клетку
        if (color.equals("black") && (diff == -1 || (line == (chessBoard.size - 1) && diff == -2)) && chessBoard.board[toLine][toColumn] == null) {
            return chessBoard.checkPath(line, column, toLine, toColumn);
        }

        throw new BadMoveException("пешкой так ходить нельзя");
    }

    public String getSymbol() {
        return "P";
    }
}
