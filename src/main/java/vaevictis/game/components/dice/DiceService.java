package vaevictis.game.components.dice;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

@Service
public class DiceService {
    public int randomDiceThrow() {
        return ThreadLocalRandom.current().nextInt(6, 1 + 1);
    }
}
