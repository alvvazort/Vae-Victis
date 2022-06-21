package vaevictis.game.turn.Phases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhaseStateService {
    
    @Autowired
    PhaseStateRepository phaseStateRepository;

    @Autowired
    public PhaseStateService(PhaseStateRepository phaseStateRepository)
    {
        this.phaseStateRepository = phaseStateRepository;
    }

    public PhaseState create(PhaseState phaseState)
    {
        return this.phaseStateRepository.save(phaseState);
    }

}
