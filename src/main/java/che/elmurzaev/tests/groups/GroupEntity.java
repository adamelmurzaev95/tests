package che.elmurzaev.tests.groups;

import che.elmurzaev.tests.students.StudentEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "groups")
public class GroupEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "specialization")
    private String specialization;

    @Column(name = "education_start_year")
    private Integer educationStartYear;

    @OneToMany
    @JoinColumn(name = "group_id")
    private Set<StudentEntity> students = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public Set<StudentEntity> getStudents() {
        return students;
    }

    public void setStudents(Set<StudentEntity> students) {
        this.students = students;
    }

    public Integer getEducationStartYear() {
        return educationStartYear;
    }

    public void setEducationStartYear(Integer educationStartYear) {
        this.educationStartYear = educationStartYear;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupEntity that = (GroupEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
