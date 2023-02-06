package ru.job4j.grabber;

import org.junit.jupiter.api.Test;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static org.assertj.core.api.Assertions.*;

class HabrCareerParseTest {

    @Test
    void parseRightDate() {
        HabrCareerDateTimeParser parcer = new HabrCareerDateTimeParser();
        String date = "2023-02-05T13:35:09";
        LocalDateTime expected = LocalDateTime.parse("2023-02-05T13:35:09", ISO_LOCAL_DATE_TIME);
        LocalDateTime result = parcer.parse(date);
        assertThat(result).isEqualTo(expected);
    }

}