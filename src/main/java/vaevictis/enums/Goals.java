package vaevictis.enums;

import java.util.Random;

public class Goals {
    public enum Faction {
        BritanniavsGermania,
        GalliavsBritania,
        GalliavsGermania, 
        HispaniavsBritannia, 
        HispaniavsGallia,
        HispaniavsGermania,
    }

    public enum CuriaGoal {
        LEFT, 
        MIDDLE, 
        RIGHT, 
        LEFTRIGHT, 
        LEFTCENTER, 
        RIGHTCENTER
    }

    public static String getWarGoals() {
        
        return Faction.values()[new Random().nextInt(Faction.values().length)].toString();

    }

    public static String getCuriaGoal() {
        return CuriaGoal.values()[new Random().nextInt(CuriaGoal.values().length)].toString();
    }
}
