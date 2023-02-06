package ru.job4j.grabber;

import org.junit.jupiter.api.Test;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;
import static org.assertj.core.api.Assertions.*;

class HabrCareerParseTest {

    @Test
    void parseRightDate() {
        HabrCareerDateTimeParser parcer = new HabrCareerDateTimeParser();
        String date = "2023-02-06T15:27:50+03:00";
        LocalDateTime expected = LocalDateTime.parse("2023-02-06T15:27:50+03:00", ISO_OFFSET_DATE_TIME);
        LocalDateTime result = parcer.parse(date);
        assertThat(result).isEqualTo(expected);
    }

}
