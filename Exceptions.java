package chess;

class BadMoveException extends Exception {
    public BadMoveException(String message) {
        super("Ошибка хода: " + message);
    }
}
