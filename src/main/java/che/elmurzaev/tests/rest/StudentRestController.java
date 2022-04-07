package che.elmurzaev.tests.rest;

import che.elmurzaev.tests.students.CreateStudentCommand;
import che.elmurzaev.tests.students.Student;
import che.elmurzaev.tests.students.StudentService;
import che.elmurzaev.tests.students.Students;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/students")
public class StudentRestController {
    private final StudentService studentService;

    public StudentRestController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{studentId}")
    public Student getStudentById(@PathVariable Integer studentId) {
        return studentService.getStudentById(studentId);
    }

    @GetMapping
    public Students getStudentsByGroupId(@RequestParam Integer groupId) {
        return studentService.getStudentsByGroupId(groupId);

    }

    @PostMapping
    public void createStudent(@RequestBody CreateStudentCommand createStudentCommand) {
        studentService.createStudent(createStudentCommand);
    }
}
