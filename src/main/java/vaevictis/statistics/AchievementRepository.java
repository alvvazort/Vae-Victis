package vaevictis.statistics;

import org.springframework.stereotype.Repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

@Repository
public interface AchievementRepository extends CrudRepository<Achievement, Integer> {
    
    @Transactional
    Achievement save(Achievement achievement);

    public Iterable<Achievement> findAll();

    public Optional<Achievement> findById(Integer id);

    public Optional<Achievement> findByName(String name);

}
