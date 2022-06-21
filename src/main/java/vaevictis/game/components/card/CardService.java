package vaevictis.game.components.card;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardService {
    
    @Autowired
    CardRepository cardRepo;

    public Card createCard(boolean tipo){
        Card card = new Card(tipo);
        this.cardRepo.save(card);
        return card;
    }

    public Card saveCard(Card card){
        return this.cardRepo.save(card);
    }

    public Card s2aveCard(Card card){
        return this.cardRepo.save(card);
    }


    public void deleteCard(Card card){
        this.cardRepo.delete(card);
    }

    public Optional<Card> getCardById(Integer cardId) {
        return this.cardRepo.getCardById(cardId);
    }
}
