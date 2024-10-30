package chess;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

    public static ChessBoard buildBoard() {
        ChessBoard board = new ChessBoard("White");

        board.board[0][0] = new Rook("White");
        board.board[0][1] = new Horse("White");
        board.board[0][2] = new Bishop("White");
        board.board[0][3] = new Queen("White");
        board.board[0][4] = new King("White");
        board.board[0][5] = new Bishop("White");
        board.board[0][6] = new Horse("White");
        board.board[0][7] = new Rook("White");
        board.board[1][0] = new Pawn("White");
        board.board[1][1] = new Pawn("White");
        board.board[1][2] = new Pawn("White");
        board.board[1][3] = new Pawn("White");
        board.board[1][4] = new Pawn("White");
        board.board[1][5] = new Pawn("White");
        board.board[1][6] = new Pawn("White");
        board.board[1][7] = new Pawn("White");

        board.board[7][0] = new Rook("Black");
        board.board[7][1] = new Horse("Black");
        board.board[7][2] = new Bishop("Black");
        board.board[7][3] = new Queen("Black");
        board.board[7][4] = new King("Black");
        board.board[7][5] = new Bishop("Black");
        board.board[7][6] = new Horse("Black");
        board.board[7][7] = new Rook("Black");
        board.board[6][0] = new Pawn("Black");
        board.board[6][1] = new Pawn("Black");
        board.board[6][2] = new Pawn("Black");
        board.board[6][3] = new Pawn("Black");
        board.board[6][4] = new Pawn("Black");
        board.board[6][5] = new Pawn("Black");
        board.board[6][6] = new Pawn("Black");
        board.board[6][7] = new Pawn("Black");
        return board;
    }

    public static void main(String[] args) throws IOException {
//        replaceChessNotationTest(); return;

        // Если первый параметр указывает на существующий файл с ходами, считываем его
        if (args.length == 1 && new File(args[0]).exists()) {
            args = Files.readString(Paths.get(args[0])).trim().split("\n");
        }
        ;

        ChessBoard board = buildBoard();
        Scanner scanner = new Scanner(System.in);
        System.out.println("""
                Чтобы проверить игру надо вводить команды:
                'exit' - для выхода
                'replay' - для перезапуска игры
                'castling0' или 'castling7' - для рокировки по соответствующей линии
                'move 1 1 2 3' - для передвижения фигуры с позиции 1 1 на 2 3(поле это двумерный массив от 0 до 7)
                Проверьте могут ли фигуры ходить друг сквозь друга, корректно ли съедают друг друга, можно ли поставить шах и сделать рокировку?""");
        System.out.println();
        board.printBoard();
        int argIndex = 0;
        while (true) {

            // Если ходы преданы в командной строке, считываем их оттуда, если нет, то просим ввести с клавиатуры
            String s;
            if (argIndex < args.length) {
                s = args[argIndex++];
                System.out.println(s);
            } else {
                s = scanner.nextLine();
            }
            if (s.equals("exit")) break;
            else if (s.equals("replay")) {
                System.out.println("Заново");
                board = buildBoard();
                board.printBoard();
            } else {

                // Сделаем обработку шахматной нотации, чтобы двигаться было можно e2e4 (или e2 e4)
                s = replaceChessNotation(s);

                try {
                    if (s.equals("castling0")) {
                        if (board.castling0(board.nowPlayerColor())) {
                            System.out.println("Рокировка удалась");
                            board.changePlayer();
                            board.printBoard();
                        } else {
                            System.out.println("Рокировка не удалась");
                        }
                    } else if (s.equals("castling7")) {
                        if (board.castling7(board.nowPlayerColor())) {
                            System.out.println("Рокировка удалась");
                            board.changePlayer();
                            board.printBoard();
                        } else {
                            System.out.println("Рокировка не удалась");
                        }
                    } else if (s.contains("move")) {
                        String[] a = s.split(" ");
                        int line = Integer.parseInt(a[1]);
                        int column = Integer.parseInt(a[2]);
                        int toLine = Integer.parseInt(a[3]);
                        int toColumn = Integer.parseInt(a[4]);
                        if (board.moveToPosition(line, column, toLine, toColumn)) {
                            System.out.println("Успешно передвинулись");
                            board.printBoard();
                        } else System.out.println("Передвижение не удалось");

                    }
                } catch (BadMoveException e) {
                    System.out.println(e.getMessage());
                } catch (Exception e) {
                    System.out.println("Вы что-то ввели не так, попробуйте ещё раз");
                }
            }
        }
    }

    private static @NotNull String replaceChessNotation(@NotNull String notation) {

        // Если это неподходящий формат записи - возвращаем строку как есть
        if (!notation.matches("[A-Ha-h]{1}\\d{1}\\s?[A-Ha-h]{1}\\d{1}")) {
            return notation;
        }
        notation = notation.replace(" ", "").toLowerCase();
        StringBuilder out = new StringBuilder("move");
        for (int n : new int[]{1, 0, 3, 2}) {
            char ch = notation.charAt(n);
            if (ch >= 'a' && ch <= 'h') {
                out.append(" ").append(ch - 97);
            } else {
                out.append(" ").append(ch - 49);
            }
        }
        return out.toString();
    }

    private static boolean replaceChessNotationTest() {
        boolean passed = true;
        char[] srcV = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        char[] srcH = new char[]{'1', '2', '3', '4', '5', '6', '7', '8'};
        char[] ref = new char[]{'0', '1', '2', '3', '4', '5', '6', '7'};
        for (int i = 0; i < srcV.length; i++) {
            for (int j = 0; j < srcH.length; j++) {
                for (int k = 0; k < srcV.length; k++) {
                    for (int m = 0; m < srcH.length; m++) {
                        String result = replaceChessNotation("" + srcV[i] + srcH[j] + srcV[k] + srcH[m]);
                        String expected = "move " + ref[j] + " " + ref[i] + " " + ref[m] + " " + ref[k];
                        if (!expected.equals(result)) {
                            System.out.println(expected + " " + result + " ");
                            passed = false;
                        }
                    }
                }
            }
        }
        return passed;
    }
}