package vaevictis.statistics;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class AchievementServiceTest {                
        
    @Autowired
    private AchievementService achievementService;

    @Test
    void shouldGetAllAchievements() {
        assertThat(this.achievementService.getAllAchievements()).isNotEmpty();
    }

    @Test
    void shouldSaveAndFindAchievementById() {
        Achievement a = new Achievement("logro1", "Logro de prueba", 1);        
        achievementService.save(a);
        assertThat(this.achievementService.findById(6).getName().equals(a.getName()));
    }

    

}