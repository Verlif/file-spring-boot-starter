package idea.verlif.spring.file.exception;

public class BlockedFileException extends RuntimeException {

    public BlockedFileException(String filename) {
        super(filename);
    }
}
