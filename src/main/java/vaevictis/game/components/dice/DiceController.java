package vaevictis.game.components.dice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class DiceController {
    
    private DiceService diceService;

    @Autowired
    public DiceController(DiceService diceService) {
        this.diceService = diceService;
    }

}
