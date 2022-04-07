package che.elmurzaev.tests.groups;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class GroupService {
    private final GroupRepo groupRepo;

    public GroupService(GroupRepo groupRepo) {
        this.groupRepo = groupRepo;
    }

    @Transactional
    public boolean existsById(Integer groupId) {
        return groupRepo.existsById(groupId);
    }
}
