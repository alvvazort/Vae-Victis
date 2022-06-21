package vaevictis.game.turn.ActionPhase;

import org.springframework.stereotype.Repository;

import vaevictis.game.turn.Turn;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

@Repository
public interface ActionPhaseOptionsRepository extends CrudRepository<Turn, Integer> {
    @Transactional
    ActionPhaseOptions save(ActionPhaseOptions options);
}
