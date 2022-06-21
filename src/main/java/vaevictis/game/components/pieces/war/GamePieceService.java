package vaevictis.game.components.pieces.war;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GamePieceService {
    
    @Autowired
    GamePieceRepository pieceRepo;

    public GamePiece findById(Integer id){
        GamePiece gamepiece = pieceRepo.findById(id);

        //gamepiece.generatePositions(gamepiece.getType()); No se guarda en memoria por lo que aqui es inutil al crear los tableros y guardarlos //Genera las posiciones de eje x e Y
        return gamepiece;
    }
    public GamePiece createGamePiece(String type){
        GamePiece gamePiece = new GamePiece(type);

        return this.pieceRepo.save(gamePiece); // Las listas de posiciones aqui no se guardan
    }
    public GamePiece savePiece(GamePiece gamePiece){
        return this.pieceRepo.save(gamePiece);
    }
}
