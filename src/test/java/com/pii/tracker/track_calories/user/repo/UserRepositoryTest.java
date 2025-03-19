package com.pii.tracker.track_calories.user.repo;

import com.pii.tracker.track_calories.BaseRepositoryTest;
import com.pii.tracker.track_calories.user.model.User;
import com.pii.tracker.track_calories.user.model.WeightGoal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@DataJpaTest
public class UserRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private UserRepository userRepository;

/*    @Test
    public void testDatabaseIsEmptyBeforeEachTest() {
        assertEquals(0, userRepository.count());
    }*/

    @Test
    public void testSave() {
        User user = createTestUser();
        User savedUser = userRepository.save(user);
        assertNotNull(savedUser.getId());
        assertEquals(user.getName(), savedUser.getName());
        assertEquals(user.getEmail(), savedUser.getEmail());
        assertEquals(user.getAge(), savedUser.getAge());
        assertEquals(user.getWeight(), savedUser.getWeight());
        assertEquals(user.getHeight(), savedUser.getHeight());
        assertEquals(user.getWeightGoal(), savedUser.getWeightGoal());
        assertEquals(user.getDailyCalorieIntake(), savedUser.getDailyCalorieIntake());
    }

    @Test
    public void testFindByEmail() {
        User user = createTestUser();
        userRepository.save(user);
        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
        assertTrue(optionalUser.isPresent());
        assertEquals(user, optionalUser.get());
    }

    private User createTestUser() {
        return User.builder()
                .name("vasYa")
                .email("vasYa@ya.ru")
                .weight(80)
                .height(175)
                .age(35)
                .weightGoal(WeightGoal.LOSE)
                .build();
    }

}
