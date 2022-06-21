package vaevictis.game.components.card;

import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.validator.constraints.Range;

import lombok.Getter;
import lombok.Setter;
import vaevictis.model.BaseEntity;

@Entity
@Getter
@Setter
@Table(name = "Cards")
public class Card extends BaseEntity {
    String name;
    String text;
    @Range(min=0,max=13)
    Integer index;
    String url;

    public Card(Integer index){
        this.index=index;
        generateTextandUrl();
    }
    public Card(boolean tipo){
        if(tipo){
        this.index = (int)Math.random()*12+1;
        this.index = this.index%2 == 0 ? 5 : 6;
        }
        else this.index=0;
        generateTextandUrl();
    }
    public Card(){
        this.index=0;
        this.text="Use this card to delete one dice from yours or from the rivals.";
        this.url="/resources/images/cartas/0.PNG";
    }
    private void generateTextandUrl(){
        switch(this.index){
            case 0:
            this.name = "Discard";
            this.text="Use this card to delete one card from the rivals.";
            this.url="/resources/images/cartas/0.PNG";
            break;
            case 1:
            this.name = "Asinus";
            this.text="Use this card on your Support or Inuence phase. This phase, you ignore the penalties of performing the same action twice";
            this.url="/resources/images/cartas/1.PNG";
            break;
            case 2:
            this.name = "Centurio";
            this.text="Place your LIMITES card face-up in front of you. The first time you take WAR action on your turn, you may advance any 1 war token 1 additional space";
            this.url="/resources/images/cartas/2.PNG";
            break;
            case 3:
            this.name = "Eleptes";
            this.text="Choose 1 game area and place this card there. The next time a player takes action there, that player gives you 1 coin and you discard this card";
            this.url="/resources/images/cartas/3.PNG";
            break;
            case 4:
            this.name = "Consul";
            this.text="Place your CURIA card face-up in front of you. If at the end of any turn (not just yours!) the condition of your card is met, you get a coin from the reserve";
            this.url="/resources/images/cartas/4.PNG";
            break;
            case 5:
            this.name = "Contionator";
            this.text="Move down or up 1 space, in any combination, the wealth token and/or the public health token and/or the rebellion token";
            this.url="/resources/images/cartas/5.PNG";
            break;
            case 6:
            this.name = "Danista";
            this.text="You get 2 coins from the reserve";
            this.url="/resources/images/cartas/6.PNG";
            break;
            case 7:
            this.name = "Publicanus";
            this.text="A player of your choice loses 1 coin";
            this.url="/resources/images/cartas/7.PNG";
            break;
            case 8:
            this.name = "Tempter";
            this.text="Choose 1 opponent and 1 option: Look at their hand and discard 1 forum card; or look at their LIMITES card; or look at their CURIA goal card";
            this.url="/resources/images/cartas/8.PNG";
            break;
            case 9:
            this.name = "Vestales";
            this.text="Use this card on any player’s (including you!) Support or Inuence phase. That player can’t take action in this phase";
            this.url="/resources/images/cartas/9.PNG";
            break;
            case 10:
            this.name = "Vidua Nigra";
            this.text="Use this card when another player uses a forum card, or on a revealed forum card. The eect of that card is cancelled and the card discarded";
            this.url="/resources/images/cartas/19.PNG";
            break;
            case 11:
            this.name = "Vastator";
            this.text="Use this card after another player rolls any 3 die (not just Fate die!). That player must reroll up to 3 die of your choice";
            this.url="/resources/images/cartas/11.PNG";
            break;
            case 12:
            this.name = "Haruspex";
            this.text="Use this card after you roll any 3 die (not just Fate die!). Choose 1 option: Change 1 dice to the result ofyour choice; or Reroll all 3 die";
            this.url="/resources/images/cartas/12.PNG";
            break;
        }
    }
}