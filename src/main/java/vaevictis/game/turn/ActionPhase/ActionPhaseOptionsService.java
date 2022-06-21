package vaevictis.game.turn.ActionPhase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActionPhaseOptionsService {
    
    @Autowired
    ActionPhaseOptionsRepository actionPhaseOptionsRepository;

    @Autowired
    public ActionPhaseOptionsService(ActionPhaseOptionsRepository actionPhaseOptionsRepository) {
        this.actionPhaseOptionsRepository = actionPhaseOptionsRepository;
    }

    public void create(ActionPhaseOptions options) {
        this.actionPhaseOptionsRepository.save(options);
    }

}
