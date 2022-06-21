package vaevictis.game.turn.DiceResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiceResultService {
    
    @Autowired
    DiceResultRepository diceResultRepository;

    @Autowired
    public DiceResultService(DiceResultRepository diceResultRepository) {
        this.diceResultRepository = diceResultRepository;
    }

    public DiceResult create(DiceResult diceResult) {
        return this.diceResultRepository.save(diceResult);
    }

}
