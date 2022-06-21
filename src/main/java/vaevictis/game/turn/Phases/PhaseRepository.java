package vaevictis.game.turn.Phases;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.repository.CrudRepository;

@Repository
public interface PhaseRepository extends CrudRepository<Phase, Integer> {

    @Transactional
    public Phase save(Phase phase);
    
    
}
