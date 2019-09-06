package by.jnetworks.roadcamera.repository;

import by.jnetworks.roadcamera.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface CarRepository extends JpaRepository<Car, Long>, QuerydslPredicateExecutor<Car> {
}
