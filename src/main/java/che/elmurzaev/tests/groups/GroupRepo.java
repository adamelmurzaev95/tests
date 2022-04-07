package che.elmurzaev.tests.groups;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepo extends JpaRepository<GroupEntity, Integer> {

}
