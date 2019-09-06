package by.jnetworks.roadcamera.service;

import by.jnetworks.roadcamera.dto.CarFilter;
import by.jnetworks.roadcamera.dto.CarNumber;
import by.jnetworks.roadcamera.entity.Car;
import by.jnetworks.roadcamera.filter.ExpressionBuilder;
import by.jnetworks.roadcamera.repository.CarRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static by.jnetworks.roadcamera.entity.QCar.car;

@Service
@Transactional
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CarService {

    private final CarRepository carRepository;

    public Car registerCar(CarNumber number) {
        Car car = Car.builder()
                .number(number.getValue())
                .date(OffsetDateTime.now(ZoneId.of("UTC")))
                .build();

        return carRepository.save(car);
    }

    public List<Car> getFilteredCars(CarFilter filter) {
        BooleanExpression expression = getFilterExpression(filter);
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getLimit());

        return carRepository.findAll(expression, pageable)
                .getContent();
    }

    public Long getCarsCount() {
        return carRepository.count();
    }

    public Optional<Car> getById(Long id) {
        return carRepository.findById(id);
    }

    private BooleanExpression getFilterExpression(CarFilter filter) {
        ExpressionBuilder exp = new ExpressionBuilder();
        exp.add(filter.getCarNumber(), car.number::eq);
        if (filter.getDate() != null) {
            exp.add(filter.getDate().toString(), car.date.stringValue()::startsWith);
        }

        return exp.getExpression();
    }
}
