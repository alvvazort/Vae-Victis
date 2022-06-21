package vaevictis.game.turn.Phases;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.repository.CrudRepository;

@Repository
public interface PhaseStateRepository extends CrudRepository<PhaseState, Integer> {

    @Transactional
    public PhaseState save(Phase phase);
    
    
}
