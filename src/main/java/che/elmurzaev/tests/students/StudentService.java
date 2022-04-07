package che.elmurzaev.tests.students;

import che.elmurzaev.tests.exceptions.groups.GroupNotFoundException;
import che.elmurzaev.tests.exceptions.students.StudentNotFoundException;
import che.elmurzaev.tests.groups.GroupService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepo studentRepo;
    private final GroupService groupService;

    public StudentService(StudentRepo studentRepo, GroupService groupService) {
        this.studentRepo = studentRepo;
        this.groupService = groupService;
    }

    @Transactional(readOnly = true)
    public Student getStudentById(Integer studentId) {
        return studentRepo.findById(studentId)
                .map(this::fromEntity)
                .orElseThrow(() -> new StudentNotFoundException("Student with id " + studentId + " not found"));
    }

    @Transactional(readOnly = true)
    public Students getStudentsByGroupId(Integer groupId) {
        if (!groupService.existsById(groupId))
            throw new GroupNotFoundException("Group with id " + groupId + " not found");

        return new Students(studentRepo.getAllByGroupId(groupId)
                .stream()
                .map(this::fromEntity)
                .collect(Collectors.toList()));
    }

    @Transactional
    public void createStudent(CreateStudentCommand createStudentCommand) {
        var groupId = createStudentCommand.groupId();
        if (groupId != null && !groupService.existsById(groupId))
            throw new GroupNotFoundException("Group with id " + groupId + " not found");

        studentRepo.save(toEntity(createStudentCommand));
    }

    private StudentEntity toEntity(CreateStudentCommand createStudentCommand) {
        var entity = new StudentEntity();
        entity.setFirstName(createStudentCommand.firstName());
        entity.setLastName(createStudentCommand.lastName());
        entity.setAge(createStudentCommand.age());
        entity.setGroupId(createStudentCommand.groupId());
        return entity;
    }

    private Student fromEntity(StudentEntity entity) {
        return new Student(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getAge(),
                entity.getGroupId()
        );
    }
}
