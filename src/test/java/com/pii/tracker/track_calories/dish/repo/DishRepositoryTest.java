package com.pii.tracker.track_calories.dish.repo;

import com.pii.tracker.track_calories.BaseRepositoryTest;
import com.pii.tracker.track_calories.dish.model.Dish;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@DataJpaTest
public class DishRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private DishRepository dishRepository;

    @Test
    public void testSave() {
        Dish dish = createDish();
        Dish savedDish = dishRepository.save(dish);
        assertNotNull(savedDish.getId());
        assertThat(savedDish.getName()).isEqualTo(dish.getName());
    }

    private Dish createDish() {
        return Dish.builder()
                .name("Салат Цезарь с курицей")
                .calories(250)
                .proteins(15)
                .fats(18)
                .carbohydrates(8)
                .build();
    }
}
