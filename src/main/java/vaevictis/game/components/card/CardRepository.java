package vaevictis.game.components.card;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CardRepository extends CrudRepository<Card, String>{
    @Transactional
    Card save(Card card);

    Optional<Card> getCardById(Integer cardId);
    
    @Transactional
    @Modifying
    @Query("delete from Card u where u.id = ?1")
    public void deleteById(Integer id);
}
