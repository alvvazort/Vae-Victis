package vaevictis.game.turn;

import org.springframework.stereotype.Repository;

import vaevictis.game.Game;
import vaevictis.player.Player;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

@Repository
public interface TurnRepository extends CrudRepository<Turn, Integer> {
    @Transactional
    Turn save(Turn turn);

    @Transactional
    @Modifying
    @Query("UPDATE Turn t set t.game = :game WHERE t = :turn")
    void setGameToTurn(@Param("game") Game game, @Param("turn") Turn turn);

    List<Turn> findByPlayerAndGame(Player player, Game game);

    List<Turn> findByGameAndPhase(Game game, String phase);
}
