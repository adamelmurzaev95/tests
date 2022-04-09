package che.elmurzaev.tests.rest;

import che.elmurzaev.tests.exceptions.groups.GroupNotFoundException;
import che.elmurzaev.tests.exceptions.students.StudentNotFoundException;
import che.elmurzaev.tests.rest.StudentRestController;
import che.elmurzaev.tests.students.CreateStudentCommand;
import che.elmurzaev.tests.students.Student;
import che.elmurzaev.tests.students.StudentService;
import che.elmurzaev.tests.students.Students;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {StudentRestController.class})
public class StudentsRestControllerTests {
    @MockBean
    private StudentService studentService;

    @Autowired
    private MockMvc mockMvc;

    private final String studentJSON = """
            {
                "id": 1,
                "firstName": "Adam",
                "lastName": "Elmurzaev",
                "age": 20,
                "groupId": 1
            }
            """;

    private final String createStudentJSON = """
            {
                "firstName": "Adam",
                "lastName": "Elmurzaev",
                "age": 20,
                "groupId": 1
            }
            """;

    private final String studentsJSON = """
            {
                "students": [
                    {
                        "id": 1,
                        "firstName": "Adam",
                        "lastName": "Elmurzaev",
                        "age": 20,
                        "groupId": 1
                    },
                    {
                        "id": 2,
                        "firstName": "Mohmad",
                        "lastName": "Tutaev",
                        "age": 24,
                        "groupId": null
                    },
                    {
                        "id": 3,
                        "firstName": "Ahmad",
                        "lastName": "Tutaev",
                        "age": 24,
                        "groupId": null
                    }
                ]
            }
            """;

    @Test
    public void getStudentByIdShouldReturnNotFoundWhenStudentDoesntExist() throws Exception {
        Mockito.when(studentService.getStudentById(1))
                .thenThrow(new StudentNotFoundException("Student with id 1 not found"));

        mockMvc
                .perform(get("/api/v1/students/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getStudentByIdShouldReturnOK() throws Exception {
        Mockito.when(studentService.getStudentById(1))
                .thenReturn(new Student(1, "Adam", "Elmurzaev", 20, 1));

        mockMvc.perform(get("/api/v1/students/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(studentJSON));
    }

    @Test
    public void createStudentShouldReturnBadRequestForInvalidBody() throws Exception {
        mockMvc.perform(
                post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createStudentShouldReturnNotFoundWhenGroupNotFound() throws Exception {
        doThrow(new GroupNotFoundException("No group with such id"))
                .when(studentService).createStudent(
                new CreateStudentCommand(
                        "Adam",
                        "Elmurzaev",
                        20,
                        1
                )
        );

        mockMvc.perform(
                post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createStudentJSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createStudentShouldReturnOK() throws Exception {
        mockMvc.perform(
                post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createStudentJSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getStudentsByGroupIdShouldReturnBadRequestWhenNoParamsDefined() throws Exception {
        mockMvc.perform(get("/api/v1/students"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getStudentsByGroupIdShouldReturnNotFoundWhenGroupDoesntExist() throws Exception {
        Mockito.when(studentService.getStudentsByGroupId(1))
                .thenThrow(new GroupNotFoundException("No group with such id"));

        mockMvc.perform(get("/api/v1/students?groupId=1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getStudentsByGroupIdShouldReturnOK() throws Exception {
        Mockito.when(studentService.getStudentsByGroupId(1))
                .thenReturn(new Students(
                        List.of(
                                new Student(1, "Adam", "Elmurzaev", 20, 1),
                                new Student(2, "Mohmad", "Tutaev", 24, null),
                                new Student(3, "Ahmad", "Tutaev", 24, null)
                        )
                ));

        mockMvc.perform(get("/api/v1/students?groupId=1"))
                .andExpect(status().isOk())
                .andExpect(content().json(studentsJSON));
    }
}