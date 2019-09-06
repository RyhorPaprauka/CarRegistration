package by.jnetworks.roadcamera.repository;

import by.jnetworks.roadcamera.entity.Car;
import by.jnetworks.roadcamera.entity.QCar;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CarRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CarRepository repository;

    @Test
    public void saveAndGetTest() {
        Car test = Car.builder()
                .number("1234 XX-9")
                .date(OffsetDateTime.parse("2019-09-04T12:34:56+00:00"))
                .build();
        entityManager.persistAndFlush(test);

        Car fromDb = repository.findById(test.getId()).orElse(null);
        assertThat(fromDb.getNumber()).isEqualTo(test.getNumber());
    }

    @Test
    public void invalidIdTest() {
        Car fromDb = repository.findById(-111l).orElse(null);
        assertThat(fromDb).isNull();
    }

    @Test
    public void getAllTest() {
        List<Car> allCars = repository.findAll();
        assertThat(allCars).hasSize(9);
    }

    @Test
    public void getAllWithExpressionTest() {
        BooleanExpression exp = QCar.car.number.eq("1234 ZZ-9");
        Iterable<Car> filtered = repository.findAll(exp);
        assertThat(filtered).hasSize(2);
    }

    @Test
    public void getAllWithPaginationTest() {
        Pageable pageable = PageRequest.of(1, 7);
        Iterable<Car> filtered = repository.findAll(pageable);
        assertThat(filtered).hasSize(2);
    }
}