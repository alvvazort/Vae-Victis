package vaevictis.gamePiece;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import vaevictis.game.components.pieces.war.GamePiece;
import vaevictis.game.components.pieces.war.GamePieceRepository;
import vaevictis.game.components.pieces.war.GamePieceService;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class GamePieceServiceTests {

    @Autowired
    protected GamePieceService gamePieceService;

    @Autowired
    protected GamePieceRepository gamePieceRepository;

    @Test
    @DisplayName("Should create a GamePiece")
    void shouldCreateGamePiece(){
        GamePiece gamePiece= gamePieceService.createGamePiece("HISPANIA");
        assertThat(gamePiece.getType().equals("HISPANIA")).isTrue();

        gamePieceRepository.delete(gamePiece);
    }
    
}
