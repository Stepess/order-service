package ua.stepess.dnipro.orderservice.config.properties;

import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.hibernate.validator.constraints.time.DurationMin;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Data
@Validated
public abstract class AbstractClientProperties {
    @URL
    @NotBlank
    private String rootUrl;

    @DurationMin(millis = 0)
    @DurationUnit(ChronoUnit.MILLIS)
    private Duration connectionTimeout;

    @DurationMin(millis = 0)
    @DurationUnit(ChronoUnit.MILLIS)
    private Duration socketTimeout;
}
