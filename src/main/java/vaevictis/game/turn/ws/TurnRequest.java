package vaevictis.game.turn.ws;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TurnRequest {

    public Integer gameId;

    public String username;
    public Integer cardId;

    public List<Integer> removeDices;

    public List<Integer> piecesGoForward; //Lista de enteros ya que se accede mediante los indices a la lista de fichas de un tablero
    public List<Integer> piecesGoBack;

    public List<Integer> actions; // Lista de enteros con las acciones

    public List<Integer> limitesOption;

    public Integer selectedCuria; // Sección del senado seleccionado
    public List<Integer> selectedCityState; // Token de city state seleccionado

    public String warning;

    public Boolean discard;

    public List<Integer> getDicesToRemove() {
        return this.removeDices;
    }

}