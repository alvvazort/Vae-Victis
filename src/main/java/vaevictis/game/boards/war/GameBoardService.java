package vaevictis.game.boards.war;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vaevictis.game.components.pieces.war.GamePiece;
import vaevictis.game.components.pieces.war.GamePieceService;

@Service
public class GameBoardService {
    
    @Autowired
    GameBoardRepository boardRepo;

    public Optional<GameBoard> getGameBoardById(int id){
        return boardRepo.findById(id);
    }

    @Autowired
	GamePieceService gamePieceService;
    
    public List<GamePiece> LoadPieces(TypeGameBoard type){
		List<GamePiece> list= new ArrayList<>();
		switch(type){
			case WARBOARD:
			List<String> WarPieces= Arrays.asList("HISPANIA", "GALLIA", "BRITANNIA","GERMANIA");
			for(int i =0; i<WarPieces.size() ; i++){
				GamePiece gp = gamePieceService.createGamePiece(WarPieces.get(i));
				list.add(gp);
			}
			break;
			case CITYSTATEBOARD:
			List<String> CityPieces= Arrays.asList("DIVITIAE", "SANITAS", "RAPINA");
			for(int i =0; i<CityPieces.size(); i++){
				GamePiece gp = gamePieceService.createGamePiece(CityPieces.get(i));
				list.add(gp);
			}
		}
		return list;
	}

    public GameBoard createGameBoard(TypeGameBoard type){
		
        GameBoard gameBoard = new GameBoard(type);
        gameBoard.setPieces(LoadPieces(type));
        return this.boardRepo.save(gameBoard);
    }
}
