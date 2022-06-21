package vaevictis.player;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vaevictis.game.components.card.Card;
import vaevictis.game.components.card.CardService;
import vaevictis.user.User;

@Service
public class PlayerService {
    
    @Autowired
    PlayerRepository playerRepository;


    @Autowired
	CardService cardService;
    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Player save(Player p){
        return this.playerRepository.save(p);
    }

    public Player createPlayer(User user) {
        Player player = new Player(user);
        player.setCoins(8);
        return this.playerRepository.save(player);
    }
    public Player savePlayer(Player player) {
        return this.playerRepository.save(player);
    }

    public Optional<Player> getPlayerByUser(User user) {
        return this.playerRepository.findByUser(user);
    }

    public Player setCoins(Player player, Integer coins){
        player.setCoins(player.getCoins()-coins);
        return this.playerRepository.save(player);
    }

    public List<Card> getPlayerCards(Player player){
        return this.playerRepository.findCardsByPlayer(player);
    }

    public Player drawCard(boolean tipo, Player player){
        Set<Card> cards = player.getCards();
        Card carta = this.cardService.createCard(tipo);
        cards.add(carta);
        player.setCards(cards);

        
        return this.playerRepository.save(player);
    }

}
