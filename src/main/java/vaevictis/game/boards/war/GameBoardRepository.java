package vaevictis.game.boards.war;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface GameBoardRepository extends CrudRepository<GameBoard, Integer>{
    @Transactional
    GameBoard save(GameBoard gameBoard);
    
}
