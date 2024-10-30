package chess;

import org.jetbrains.annotations.NotNull;

public class ChessBoard {
    final int size = 7;
    int[] kings = new int[]{0, 4, size, 4};
    boolean chessNotation = true;
    public ChessPiece[][] board = new ChessPiece[this.size + 1][this.size + 1]; // creating a field for game
    String nowPlayer;

    public ChessBoard(String nowPlayer) {
        this.nowPlayer = nowPlayer;
    }

    public String nowPlayerColor() {
        return this.nowPlayer;
    }

    public boolean moveToPosition(int startLine, int startColumn, int endLine, int endColumn) throws BadMoveException {

        // Внешние вторжения запрещены
        if (!onBoard(startLine, startColumn)) throw new BadMoveException("Ход из-за пределов доски");

        // Ходить за пределы доски запрещено
        if (!onBoard(endLine, endColumn)) throw new BadMoveException("Необходимо оставаться в пределах доски");

        // Стоять на месте запрещено
        if (startLine == endLine && startColumn == endColumn) throw new BadMoveException("Фигура не может оставаться на месте");

        // Чужие фигуры двигать нельзя
        if (!nowPlayer.equalsIgnoreCase(board[startLine][startColumn].getColor())) throw new BadMoveException("ходить можно только фигурами своего цвета");

        // Свои фигуры есть нельзя
        if (board[endLine][endColumn] != null && nowPlayer.equalsIgnoreCase(board[endLine][endColumn].getColor())) {
            throw new BadMoveException("свои фигуры есть нельзя");
        }

        // Проверяем, может ли фигура ходить в указанную клетку
        if (board[startLine][startColumn].canMoveToPosition(this, startLine, startColumn, endLine, endColumn)) {

            // Запоминаем фигуру в месте перемещения, на случай отката хода
            ChessPiece piece = board[endLine][endColumn];

            // Двигаем фигуру
            board[endLine][endColumn] = board[startLine][startColumn]; // if piece can move, we moved a piece
            board[startLine][startColumn] = null; // set null to previous cell

            // Вычисляем координаты короля текущего цвета
            King king;
            int kingLine, kingColumn, kingIndex = this.nowPlayerColor().equals("White") ? 0 : 2;
            if (board[endLine][endColumn].getSymbol().equalsIgnoreCase("K")) {
                king = (King) board[endLine][endColumn];
                kingLine = endLine;
                kingColumn = endColumn;
            } else {
                kingLine = kings[kingIndex];
                kingColumn = kings[kingIndex + 1];
                king = (King) this.board[kingLine][kingColumn];
            }

            // Если после хода свой король находится под шахом
            if (king.isUnderAttack(this, kingLine, kingColumn)) {

                // Возвращаем фигуру на место
                board[startLine][startColumn] = board[endLine][endColumn];
                board[endLine][endColumn] = piece;
                throw new BadMoveException("Королю шах");
            }

            // Если пешка дошла до противоположного края, то она становится ферзём
            if ((endLine == 0 || endLine == size) && board[endLine][endColumn].getSymbol().equalsIgnoreCase("P")) {
                board[endLine][endColumn] = new Queen(nowPlayerColor());
            }

            // Отмечаем, что фигура ходила
            board[endLine][endColumn].moved();

            // Обновляем координаты короля
            kings[kingIndex] = kingLine;
            kings[kingIndex + 1] = kingColumn;

            // Если ходил король, запоминаем его новое расположение, чтобы отслеживать шахи
            if (board[endLine][endColumn].getSymbol().equals("K")) {
                kings[kingIndex] = endLine;
                kings[kingIndex + 1] = endColumn;
            }

            // Меняем игрока
            changePlayer();

            return true;
        } else {
            throw new BadMoveException("Неизвестная проблема");
        }
    }

    protected void changePlayer() {
        this.nowPlayer = this.nowPlayerColor().equals("White") ? "Black" : "White";
    }

    public void printBoard() {  //print board in console
        System.out.println("Turn " + nowPlayer);
        System.out.println();
        System.out.println("Player 2(Black)");
        System.out.println();
        System.out.println(chessNotation ? "\t  A    B    C    D    E    F    G    H" : "\t  0    1    2    3    4    5    6    7");

        for (int i = this.size; i > -1; i--) {
            System.out.println("\t+----+----+----+----+----+----+----+----+");
            System.out.println("\t|    |    |    |    |    |    |    |    |");
            System.out.print((chessNotation ? i + 1 : i) + "\t| ");
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == null) {
                    System.out.print("   | ");
                } else {
                    System.out.print(board[i][j].getSymbol() + board[i][j].getColor().substring(0, 1).toLowerCase() + " | ");
                }
            }
            System.out.println("\t" + (chessNotation ? i + 1 : i));
            System.out.println("\t|    |    |    |    |    |    |    |    |");
        }
        System.out.println("\t+----+----+----+----+----+----+----+----+");
        System.out.println();
        System.out.println(chessNotation ? "\t  A    B    C    D    E    F    G    H" : "\t  0    1    2    3    4    5    6    7");
        System.out.println();
        System.out.println("Player 1(White)");
    }

    public boolean checkPos(int pos) {
        return pos >= 0 && pos <= 7;
    }

    // Рокировка по левой колонке
    public boolean castling0(String color) throws BadMoveException {
        int line = 0, king = 4;
        if (color.equalsIgnoreCase("black")) {
            line = this.size;
        }

        if (board[line][this.size] == null || board[line][king] == null || !board[line][this.size].getSymbol().equalsIgnoreCase("R") || !board[line][king].getSymbol().equalsIgnoreCase("K")) {
            throw new BadMoveException("королём или ладья находятся не на своих местах");
        }

        // Если расстояние между королём и ладьёй не пусто - рокировку делать нельзя
        for (int i = 1; i < king; i++) {
            if (board[line][i] != null) {
                throw new BadMoveException("между королём и ладьёй не должно быть фигур");
            }
        }

        // Если король или ладья ходили - рокировку делать нельзя
        if (board[line][0] == null || (board[line][0].isMove || board[line][king] == null || board[line][king].isMove)) {
            throw new BadMoveException("король или ладья уже ходили");
        }

        // Если король под шахом или попадёт под шах в случае рокировки - ход делать нельзя
        if (board[line][king].getSymbol().equals("K") && ((King) board[line][king]).isUnderAttack(this, line, king) &&
                ((King) board[line][king]).isUnderAttack(this, line, king - 2)) {
            throw new BadMoveException("королю шах или будет шах после рокировки");
        }

        board[line][king - 2] = board[line][king];
        board[line][king] = null;
        board[line][king - 1] = board[line][0];
        board[line][0] = null;

        // Запоминаем новую позицию короля
        this.kings[this.nowPlayerColor().equals("White") ? 1 : 3] = king - 2;

        return true;
    }

    // Рокировка по правой колонке
    public boolean castling7(@NotNull String color) throws BadMoveException {
        int line = 0, king = 4;
        if (color.equalsIgnoreCase("black")) {
            line = this.size;
        }

        if (board[line][0] == null || board[line][king] == null || !board[line][0].getSymbol().equalsIgnoreCase("R") || !board[line][king].getSymbol().equalsIgnoreCase("K")) {
            throw new BadMoveException("королём или ладья находятся не на своих местах");
        }

        // Если расстояние между королём и ладьёй не пусто - рокировку делать нельзя
        for (int i = king + 1; i < this.size; i++) {
            if (board[line][i] != null) {
                throw new BadMoveException("между королём и ладьёй не должно быть фигур");
            }
        }

        // Если король или ладья ходили - рокировку делать нельзя
        if (board[line][this.size] == null || (board[line][this.size].isMove || board[line][king] == null || board[line][king].isMove)) {
            throw new BadMoveException("король или ладья уже ходили");
        }

        // Если король под шахом или попадёт под шах в случае рокировки - ход делать нельзя
        if (board[line][king].getSymbol().equals("K") && ((King) board[line][king]).isUnderAttack(this, line, king) &&
                ((King) board[line][king]).isUnderAttack(this, line, king + 2)) {
            throw new BadMoveException("королю шах или будет шах после рокировки");
        }

        board[line][king + 2] = board[line][king];
        board[line][king] = null;
        board[line][king + 1] = board[line][this.size];
        board[line][this.size] = null;

        // Запоминаем новую позицию короля
        this.kings[this.nowPlayerColor().equals("White") ? 1 : 3] = king + 2;

        return true;
    }

    // Проверяем, не пресекли ли мы границу доски
    public boolean crossBorder(int toLine, int toColumn) {
        return toLine < 0 || toLine > this.size || toColumn < 0 || toColumn > this.size;
    }

    // Проверяем, остаёмся ли мы в пределах доски
    public boolean onBoard(int toLine, int toColumn) {
        return !crossBorder(toLine, toColumn);
    }

    // НЕ ДЛЯ КОНЯ. Только для фигур, движущихся по прямым и диагоналям
    // Проверяет, не встретилось ли посреди пути какой-нибудь фигуры, исключая конечную точку. В конечной точке может быть только фигура другого цвета
    public boolean checkPath(int startLine, int startColumn, int toLine, int toColumn) {
        int line = startLine, column = startColumn;

        // В цикле двигаемся к следующей клетке и проверяем наличие фигуры.
        while (line != toLine && column != toColumn) {
            if (board[line += Integer.compare(toLine, line)][column += Integer.compare(toColumn, column)] != null && (line != toLine || column != toColumn)) {
                return false;
            }
        }

        // В конечной точке может быть фигура, но она не должна быть одного цвета с ходившей фигурой
        if (board[toLine][toColumn] == null || !board[startLine][startColumn].getColor().equals(board[toLine][toColumn].getColor())) {
            return true;
        }

        return false;
    }
}
