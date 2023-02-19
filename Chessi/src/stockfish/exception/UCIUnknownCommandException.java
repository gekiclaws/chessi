package stockfish.exception;

public class UCIUnknownCommandException extends UCIRuntimeException {
    public UCIUnknownCommandException(String msg) {
        super(msg);
    }
}
