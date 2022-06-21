package vaevictis.game.components.pieces.war;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface GamePieceRepository extends CrudRepository<GamePiece, String>{
    @Transactional
    GamePiece save(GamePiece gamePiece);
    
    GamePiece findById(int id);
}
