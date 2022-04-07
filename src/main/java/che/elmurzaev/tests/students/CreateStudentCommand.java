package che.elmurzaev.tests.students;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateStudentCommand(
        @JsonProperty(value = "firstName", required = true) String firstName,
        @JsonProperty(value = "lastName", required = true) String lastName,
        @JsonProperty(value = "age", required = true) Integer age,
        @JsonProperty(value = "groupId") Integer groupId
) {
}
