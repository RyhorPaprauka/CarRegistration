package by.jnetworks.roadcamera.service;

import by.jnetworks.roadcamera.dto.CarNumber;
import by.jnetworks.roadcamera.entity.Car;
import by.jnetworks.roadcamera.repository.CarRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CarService.class)
public class CarServiceTest {

    @Autowired
    private CarService service;

    @MockBean
    private CarRepository repository;

    @Before
    public void setUp() {
        Car first = Car.builder()
                .id(1L)
                .number("1234 ZZ-9")
                .date(OffsetDateTime.parse("2019-09-04T12:34:56+00:00"))
                .build();
        Car second = Car.builder()
                .id(2L)
                .number("1234 ZZ-8")
                .date(OffsetDateTime.parse("2019-09-04T12:34:56+00:00"))
                .build();
        Car third = Car.builder()
                .id(3L)
                .number("1234 ZZ-7")
                .date(OffsetDateTime.parse("2019-09-04T12:34:56+00:00"))
                .build();
        Car fourth = Car.builder()
                .id(4L)
                .number("1234 ZZ-6")
                .date(OffsetDateTime.parse("2019-09-04T12:34:56+00:00"))
                .build();


        List<Car> cars = Arrays.asList(first, second, third, fourth);

        Mockito.when(repository.findById(first.getId())).thenReturn(Optional.of(first));
        Mockito.when(repository.findById(99L)).thenReturn(Optional.empty());
        Mockito.when(repository.count()).thenReturn(11L);
        Mockito.when(repository.save(any(Car.class))).thenReturn(first);
        Mockito.when(repository.findAll()).thenReturn(cars);
    }

    @Test
    public void registerTest() {
        CarNumber number = new CarNumber("1234 ZZ-9");
        Car car = service.registerCar(number);
        assertThat(car.getNumber()).isEqualTo(number.getValue());
    }

    @Test
    public void countTest() {
        Long count = service.getCarsCount();
        assertThat(count).isEqualTo(11);
    }

    @Test
    public void validIdTest() {
        Car fromDb = service.getById(1L).orElse(null);
        assertThat(fromDb.getNumber()).isEqualTo("1234 ZZ-9");
    }

    @Test
    public void invalidIdTest() {
        Car fromDb = service.getById(99L).orElse(null);
        assertThat(fromDb).isNull();
    }
}