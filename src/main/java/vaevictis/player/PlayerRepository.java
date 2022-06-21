package vaevictis.player;

import org.springframework.stereotype.Repository;

import vaevictis.game.components.card.Card;
import vaevictis.user.User;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Integer> {
    @Transactional
    Player save(Player player);

    public Optional<Player> findByUser(User user);

    @Query("SELECT c.cards FROM Player c WHERE c = :player ")
    List<Card> findCardsByPlayer(@Param("player") Player player);

    
}
