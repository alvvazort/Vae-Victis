package vaevictis.game.components.pieces.war;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import vaevictis.model.BaseEntity;

@Entity
@Getter
@Setter
@ToString
@Table(name = "Gamepieces")
public class GamePiece extends BaseEntity {

    @NotEmpty
    @NotNull
    private String type;

    @Range(min=0,max=12)
    private Integer index;

    @NotEmpty
    @NotNull
    private String url;

    public GamePiece(String tipo){
        this.type=tipo;
        generateIndex();
        generateUrl();
    }
    
    public GamePiece(){
        this.type= "GALLIA";
        this.index= 4;
        this.url = "/resources/images/fichas/Ficha_Gallo.png";
    }

    private void generateIndex(){
        if(this.type=="DIVITIAE" || this.type=="SANITAS"|| this.type=="RAPINA"){
            this.index=9;
        }else{
            this.index=4;
        }
    }

    private void generateUrl(){
        switch(this.type){
            case "HISPANIA":
            this.url="/resources/images/fichas/Ficha_Zorro.png";
            break;
            case "GALLIA":
            this.url="/resources/images/fichas/Ficha_Gallo.png";
            break;
            case "BRITANNIA":
            this.url="/resources/images/fichas/Ficha_Ciervo.png";
            break;
            case "GERMANIA":
            this.url="/resources/images/fichas/Ficha_Lobo.png";
            break;
            case "DIVITIAE":
            this.url="/resources/images/fichas/Ficha_Oro.jpg";
            break;
            case "SANITAS":
            this.url="/resources/images/fichas/Ficha_Rata.jpg";
            break;
            case "RAPINA":
            this.url="/resources/images/fichas/Ficha_Fuego.jpg";
        }
    }

}