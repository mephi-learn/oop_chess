package chess;

import org.jetbrains.annotations.NotNull;

public class King extends ChessPiece {
    public King(String color) {
        super(color);
    }

    @Override
    public String getSymbol() {
        return "K";
    }

    @Override
    public boolean canMoveToPosition(ChessBoard chessBoard, int line, int column, int toLine, int toColumn) throws BadMoveException {

        // Нельзя ходить более чем на клетку.
        if (Math.abs(line - toLine) > 1 || Math.abs(column - toColumn) > 1) {
            throw new BadMoveException("король не может ходить больше чем на одну клетку");
        }

        // Возвращаем результат перемещения, не встретилось ли на пути фигур
        return chessBoard.checkPath(line, column, toLine, toColumn);
    }

    public boolean isUnderAttack(@NotNull ChessBoard board, int posLine, int posColumn) {

        try {
            // Сначала будем проверять клетки вверх по доске, потом вниз
            for (String direction : new String[]{"up", "down"}) {
                boolean vertical = true, left = true, right = true;

                // Сначала будем проверять доску построчно вверх (включая текущую), затем вниз
                for (int line = direction.equals("up") ? posLine + 1 : posLine - 1; board.onBoard(line, posColumn); line += direction.equals("up") ? 1 : -1) {
                    int diff = Math.abs(line - posLine);
                    int column;
                    ChessPiece figure;

                    // Если строго по вертикали найдена фигура
                    if (vertical && (figure = board.board[line][posColumn]) != null) {

                        // Если она представляет угрозу - сообщаем об этом
                        if (figure.canMoveToPosition(board, line, posColumn, posLine, posColumn)) {
                            return true;
                        }

                        // А дальше проверять смысла нет
                        vertical = false;
                    }

                    // Если по диагонали вправо найдена фигура
                    if (right && board.onBoard(line, (column = posColumn + diff)) && (figure = board.board[line][column]) != null) {

                        // Если она представляет угрозу - сообщаем об этом
                        if (figure.canMoveToPosition(board, line, column, posLine, posColumn)) {
                            return true;
                        }

                        // А дальше проверять смысла нет
                        right = false;
                    }

                    // Если по диагонали влево найдена фигура
                    if (left && board.onBoard(line, (column = posColumn - diff)) && (figure = board.board[line][column]) != null) {

                        // Если она представляет угрозу - сообщаем об этом
                        if (figure.canMoveToPosition(board, line, column, posLine, posColumn)) {
                            return true;
                        }

                        // А дальше проверять смысла нет
                        left = false;
                    }

                }
            }

            // Также просмотрим строго слева и справа
            for (String direction : new String[]{"left", "right"}) {
                for (int column = direction.equals("left") ? posColumn - 1 : posColumn + 1; board.onBoard(posLine, column); column += direction.equals("left") ? -1 : 1) {
                    ChessPiece figure;

                    // Если слева найдена фигура
                    if ((figure = board.board[posLine][column]) != null) {

                        // Если она представляет угрозу - сообщаем об этом
                        if (figure.canMoveToPosition(board, posLine, column, posLine, posColumn)) {
                            return true;
                        }
                        break;
                    }
                }
            }
        } catch (BadMoveException ignored) {
        }

        return false;
    }
}
