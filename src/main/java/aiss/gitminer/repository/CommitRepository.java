package aiss.gitminer.repository;

import aiss.gitminer.model.Commit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface CommitRepository extends JpaRepository<Commit, String> {
}
