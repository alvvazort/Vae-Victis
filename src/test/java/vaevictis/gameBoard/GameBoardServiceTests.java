package vaevictis.gameBoard;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import vaevictis.game.boards.war.GameBoard;
import vaevictis.game.boards.war.GameBoardRepository;
import vaevictis.game.boards.war.GameBoardService;
import vaevictis.game.boards.war.TypeGameBoard;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class GameBoardServiceTests {

    @Autowired
    protected GameBoardService gameBoardService;

    @Autowired
    protected GameBoardRepository gameBoardRepository;

    @Test
    @DisplayName("Should create war board")
    void shouldCreateWarBoard(){

        GameBoard warBoard= gameBoardService.createGameBoard(TypeGameBoard.WARBOARD);
        assertThat(warBoard.getType().equals(TypeGameBoard.WARBOARD)).isTrue();
        assertThat(gameBoardService.getGameBoardById(warBoard.getId()));

        gameBoardRepository.delete(warBoard);

    }

    @Test
    @DisplayName("Should create city state board")
    void shouldCreateCityStateBoard(){

        GameBoard cityStateBoard= gameBoardService.createGameBoard(TypeGameBoard.CITYSTATEBOARD);
        assertThat(cityStateBoard.getType().equals(TypeGameBoard.CITYSTATEBOARD)).isTrue();
        assertThat(gameBoardService.getGameBoardById(cityStateBoard.getId()));

        gameBoardRepository.delete(cityStateBoard);
    }

    @Test
    @DisplayName("Should load pieces of a city state board when is created")
    void shouldLoadPiecesOfCityStateBoard(){

        GameBoard cityStateBoard= gameBoardService.createGameBoard(TypeGameBoard.CITYSTATEBOARD);

        assertThat(cityStateBoard.getType().equals(TypeGameBoard.CITYSTATEBOARD)).isTrue();
        assertThat(gameBoardService.getGameBoardById(cityStateBoard.getId()));

        assertThat(cityStateBoard.getPieces().size()==3).isTrue();

        gameBoardRepository.delete(cityStateBoard);
    }

    @Test
    @DisplayName("Should load pieces of a war board when is created")
    void shouldLoadPiecesOfWarBoard(){

        GameBoard warBoard= gameBoardService.createGameBoard(TypeGameBoard.WARBOARD);

        assertThat(warBoard.getType().equals(TypeGameBoard.WARBOARD)).isTrue();
        assertThat(gameBoardService.getGameBoardById(warBoard.getId()));

        assertThat(warBoard.getPieces().size()==4).isTrue();

        gameBoardRepository.delete(warBoard);
    }



}
