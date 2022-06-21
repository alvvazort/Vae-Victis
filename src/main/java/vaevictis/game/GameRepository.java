package vaevictis.game;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vaevictis.player.Player;
import vaevictis.user.User;

@Repository
public interface GameRepository extends CrudRepository<Game, Integer> {
    
    @Transactional
    Game save(Game game);

    @Transactional
    void delete(Game game);

    @Transactional
    @Query("SELECT g FROM Game g JOIN FETCH g.playersIn p WHERE p.user = :user AND g.removedAt IS NULL")
    Optional<Game> getUserPlayingGame(@Param("user") User user);

    @Query("SELECT g FROM Game g ORDER BY g.removedAt DESC")
    public List<Game> findGamesOrdered();

    @Transactional
    Iterable<Game> findAll();

    
    @Query("SELECT g.playersIn FROM Game g WHERE g.id =?1 AND g.removedAt IS NULL")
    List<Player> findPlayersByGameId(Integer id);
}
