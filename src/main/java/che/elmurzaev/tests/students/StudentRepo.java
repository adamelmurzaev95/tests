package che.elmurzaev.tests.students;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepo extends JpaRepository<StudentEntity, Integer> {
    @Query(value = "SELECT * FROM students WHERE group_id = ?1", nativeQuery = true)
    List<StudentEntity> getAllByGroupId(Integer groupId);
}
