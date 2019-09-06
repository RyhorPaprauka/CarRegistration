package by.jnetworks.roadcamera.mapper;

import by.jnetworks.roadcamera.dto.CarDto;
import by.jnetworks.roadcamera.entity.Car;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CarMapper {

    CarDto toDto(Car car);
}
