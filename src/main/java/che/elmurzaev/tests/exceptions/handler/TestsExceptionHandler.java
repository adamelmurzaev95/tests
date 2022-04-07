package che.elmurzaev.tests.exceptions.handler;

import che.elmurzaev.tests.exceptions.groups.GroupNotFoundException;
import che.elmurzaev.tests.exceptions.students.StudentNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class TestsExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
            = { StudentNotFoundException.class, GroupNotFoundException.class })
    protected ResponseEntity<Object> handleConflict(
            RuntimeException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
