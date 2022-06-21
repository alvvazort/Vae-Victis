package vaevictis.game.boards.action;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ActionBoardRepository extends CrudRepository<ActionBoard, Integer>{
    @Transactional
    ActionBoard save(ActionBoard gameBoard);
    
}
