package che.elmurzaev.tests.rest;

import che.elmurzaev.tests.exceptions.groups.GroupNotFoundException;
import che.elmurzaev.tests.exceptions.students.StudentNotFoundException;
import che.elmurzaev.tests.groups.GroupService;

import che.elmurzaev.tests.students.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTests {
    @Mock
    private StudentRepo studentRepo;

    @Mock
    private GroupService groupService;

    @Captor
    private ArgumentCaptor<StudentEntity> captor;

    private final CreateStudentCommand createStudentCommand = new CreateStudentCommand(
            "Adam",
            "Elmurzaev",
            20,
            1
    );

    private final CreateStudentCommand createStudentCommand2 = new CreateStudentCommand(
            "Adam",
            "Elmurzaev",
            20,
            null
    );

    @Test
    public void getStudentsByGroupIdShouldThrowGroupNotFoundExceptionWhenGroupDoesntExists() {
        Mockito.when(groupService.existsById(1)).thenReturn(false);

        assertThrows(
                GroupNotFoundException.class,
                () -> new StudentService(studentRepo, groupService).getStudentsByGroupId(1)
        );
    }

    @Test
    public void getStudentsByGroupIdShouldReturnCorrectResult() {
        var entities = List.of(
                new StudentEntity(1, "Adam", "Elmurzaev", 20, 1),
                new StudentEntity(2, "Mohmad", "Tutaev", 24, null),
                new StudentEntity(3, "Ahmad", "Tutaev", 24, null)
        );

        Mockito.when(groupService.existsById(1))
                .thenReturn(true);
        Mockito.when(studentRepo.getAllByGroupId(1))
                .thenReturn(entities);

        var expected = new Students(entities.stream().map(
                entity -> new Student(
                        entity.getId(),
                        entity.getFirstName(),
                        entity.getLastName(),
                        entity.getAge(),
                        entity.getGroupId()
                )
        ).collect(Collectors.toList()));

        assertEquals(
                expected,
                new StudentService(studentRepo, groupService).getStudentsByGroupId(1)
        );
    }

    @Test
    public void getStudentByIdShouldThrowStudentNotFoundExceptionWhenStudentDoesntExist() {
        Mockito.when(studentRepo.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                StudentNotFoundException.class,
                () -> new StudentService(studentRepo, groupService).getStudentById(1)
        );
    }

    @Test
    public void getStudentByIdShouldReturnCorrectResult() {
        Mockito.when(studentRepo.findById(1))
                .thenReturn(
                        Optional.of(new StudentEntity(
                                1, "Adam", "Elmurzaev", 20, 1
                        ))
                );

        var expected = new Student(1, "Adam", "Elmurzaev", 20, 1);
        var actual = new StudentService(studentRepo, groupService).getStudentById(1);

        assertEquals(
            expected,
            actual
        );
    }

    @Test
    public void createStudentShouldThrowExceptionWhenGroupDoesntExist() {
        Mockito.when(groupService.existsById(1)).thenReturn(false);

        assertThrows(
                GroupNotFoundException.class,
                () -> new StudentService(studentRepo, groupService).createStudent(createStudentCommand));

        Mockito.verify(studentRepo, Mockito.times(0)).save(any());
    }

    @Test
    public void createStudentShouldDoesntThrowExceptionWhenGroupIsNull() {
        new StudentService(studentRepo, groupService).createStudent(createStudentCommand2);

        Mockito.verify(studentRepo, Mockito.times(1)).save(captor.capture());

        assertEquals(
                createStudentCommand2.firstName(),
                captor.getValue().getFirstName()
        );

        assertEquals(
                createStudentCommand2.lastName(),
                captor.getValue().getLastName()
        );

        assertEquals(
                createStudentCommand2.age(),
                captor.getValue().getAge()
        );
    }

    @Test
    public void createStudentShouldWorksCorrect() {
        Mockito.when(groupService.existsById(1)).thenReturn(true);

        new StudentService(studentRepo, groupService).createStudent(createStudentCommand);

        Mockito.verify(studentRepo, Mockito.times(1)).save(captor.capture());

        assertEquals(
                createStudentCommand.firstName(),
                captor.getValue().getFirstName()
        );

        assertEquals(
                createStudentCommand.lastName(),
                captor.getValue().getLastName()
        );

        assertEquals(
                createStudentCommand.age(),
                captor.getValue().getAge()
        );
    }
}
