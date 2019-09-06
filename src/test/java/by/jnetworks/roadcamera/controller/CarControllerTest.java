package by.jnetworks.roadcamera.controller;

import by.jnetworks.roadcamera.dto.CarFilter;
import by.jnetworks.roadcamera.dto.CarNumber;
import by.jnetworks.roadcamera.entity.Car;
import by.jnetworks.roadcamera.mapper.CarMapperImpl;
import by.jnetworks.roadcamera.service.CarService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest({CarController.class, CarMapperImpl.class})
public class CarControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CarService service;

    @Test
    public void registerCarTest() throws Exception {
        Car test = Car.builder()
                .id(111L)
                .number("1234 XX-9")
                .date(OffsetDateTime.now())
                .build();
        CarNumber number = new CarNumber("1234 XX-9");

        given(service.registerCar(any(CarNumber.class))).willReturn(test);

        mvc.perform(post("/registeredCars")
                .contentType(APPLICATION_JSON)
                .content(JsonUtil.toJson(number)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number", is("1234 XX-9")));

        reset(service);
    }

    @Test
    public void getCarsTest() throws Exception {
        Car test = Car.builder()
                .id(113L)
                .number("1234 XX-9")
                .date(OffsetDateTime.now())
                .build();
        Car test2 = Car.builder()
                .id(112L)
                .number("1234 XX-8")
                .date(OffsetDateTime.now())
                .build();
        Car test3 = Car.builder()
                .id(111L)
                .number("1234 XX-7")
                .date(OffsetDateTime.now())
                .build();

        List<Car> all = Arrays.asList(test, test2, test3);

        given(service.getFilteredCars(any(CarFilter.class))).willReturn(all);

        mvc.perform(get("/registeredCars?page=2&limit=6").contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].number", is(test.getNumber())))
                .andExpect(jsonPath("$[1].number", is(test2.getNumber())))
                .andExpect(jsonPath("$[2].number", is(test3.getNumber())));
    }

    @Test
    public void countTest() throws Exception {
        given(service.getCarsCount()).willReturn(11L);

        mvc.perform(get("/registeredCars/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(11)));

        verify(service, VerificationModeFactory.times(1)).getCarsCount();
        reset(service);
    }
}