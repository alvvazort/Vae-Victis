package vaevictis.statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AchievementService {
    @Autowired
    private AchievementRepository achievementRepository;

    public Iterable<Achievement> getAllAchievements(){
        return this.achievementRepository.findAll();
    }

    public Achievement save(Achievement achievement){
        return this.achievementRepository.save(achievement);
    }

    public Achievement findById(int i) {
        return this.achievementRepository.findById(i).get();
    }

    public Achievement findByName(String name) {
        return this.achievementRepository.findByName(name).get();
    }

}
