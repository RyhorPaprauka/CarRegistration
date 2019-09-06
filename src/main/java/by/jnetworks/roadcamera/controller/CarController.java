package by.jnetworks.roadcamera.controller;

import by.jnetworks.roadcamera.dto.CarDto;
import by.jnetworks.roadcamera.dto.CarFilter;
import by.jnetworks.roadcamera.dto.CarNumber;
import by.jnetworks.roadcamera.mapper.CarMapper;
import by.jnetworks.roadcamera.service.CarService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/registeredCars")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CarController {

    private static final String DATE_FORMAT = "yyyyMMdd";
    private final CarService carService;
    private final CarMapper mapper;

    @PostMapping
    public ResponseEntity<CarDto> registerCar(@RequestBody @Valid CarNumber number) {
        return ResponseEntity.ok(
                mapper.toDto(carService.registerCar(number)));
    }

    @GetMapping
    public ResponseEntity<List<CarDto>> getCars(@RequestParam(required = false) String number,
                                                @RequestParam(required = false) String date,
                                                @RequestParam(required = false) Integer page,
                                                @RequestParam(required = false) Integer limit) {
        CarFilter filter = CarFilter.builder()
                .carNumber(number)
                .date(date == null ? null : LocalDate.parse(date, DateTimeFormatter.ofPattern(DATE_FORMAT)))
                .page(page)
                .limit(limit)
                .build();

        return ResponseEntity.ok(carService.getFilteredCars(filter).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getCarsCount() {
        return ResponseEntity.ok(carService.getCarsCount());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarDto> find(@PathVariable Long id) {
        return carService.getById(id)
                .map(mapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }
}
