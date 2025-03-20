package com.pii.tracker.track_calories.dish.repo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.pii.tracker.track_calories.util.TestDataFactory.createTestDish;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DishRepositoryTest {

    @Autowired
    private DishRepository dishRepository;

    @Test
    public void testSave() {
        var dish = createTestDish();
        var savedDish = dishRepository.save(dish);
        assertThat(savedDish.getId()).isNotNull();
        assertThat(savedDish.getName()).isEqualTo(dish.getName());
    }
}
