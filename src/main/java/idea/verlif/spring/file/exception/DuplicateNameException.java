package idea.verlif.spring.file.exception;

/**
 * @author Verlif
 */
public class DuplicateNameException extends RuntimeException {


    public DuplicateNameException(String filename) {
        super(filename);
    }

}
