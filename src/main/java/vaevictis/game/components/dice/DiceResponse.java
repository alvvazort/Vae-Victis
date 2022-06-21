package vaevictis.game.components.dice;

public class DiceResponse {
    
    private String response;

    public DiceResponse(Integer DiceThrow) {
        this.response = DiceThrow.toString();
    }
}
