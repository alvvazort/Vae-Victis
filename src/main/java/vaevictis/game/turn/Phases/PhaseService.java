package vaevictis.game.turn.Phases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhaseService {
    
    @Autowired
    PhaseRepository phaseRepository;

    @Autowired
    PhaseStateService phaseStateService;

    @Autowired
    public PhaseService(PhaseRepository phaseRepository, PhaseStateService phaseStateService)
    {
        this.phaseRepository = phaseRepository;
        this.phaseStateService = phaseStateService;
    }

    public Phase create(Phase phase) {
        phase.state = this.phaseStateService.create(phase.state);
        return this.phaseRepository.save(phase);
    }

    public Phase save(Phase phase) {
        return this.phaseRepository.save(phase);
    }
}
