package vaevictis.game.boards.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActionBoardService {
    
    @Autowired
    ActionBoardRepository boardRepo;

    public ActionBoard createActionBoard(int maxTreasury){
		
        ActionBoard actionBoard = new ActionBoard(maxTreasury);
        return this.boardRepo.save(actionBoard); // Las listas de posiciones aqui no se guardan
    }
    public ActionBoard savePiece(ActionBoard actionBoard){

        return this.boardRepo.save(actionBoard);
    }
}
