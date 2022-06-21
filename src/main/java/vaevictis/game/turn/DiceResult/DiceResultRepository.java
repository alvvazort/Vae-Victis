package vaevictis.game.turn.DiceResult;

import org.springframework.stereotype.Repository;

import vaevictis.game.turn.Turn;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

@Repository
public interface DiceResultRepository extends CrudRepository<Turn, Integer> {
    @Transactional
    DiceResult save(DiceResult diceResult);
}
