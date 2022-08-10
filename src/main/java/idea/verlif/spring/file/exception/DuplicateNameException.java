package idea.verlif.spring.file.exception;

import java.io.IOException;

/**
 * @author Verlif
 */
public class DuplicateNameException extends IOException {

    private final String filename;

    public DuplicateNameException(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
