package by.jnetworks.roadcamera.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CarNumber {

    @Pattern(message = "it must be in format 0000-XX-0",
            regexp = "^\\d{4} [A-Z]{2}-\\d$")
    private String value;
}
