package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HabrCareerParse implements Parse {
    private static final String SOURCE_LINK = "https://career.habr.com";
    private static int page = 1;
    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer?page=%d", SOURCE_LINK, page);

    private final DateTimeParser dateTimeParser;

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    private static String retrieveDescription(String link) throws IOException {
        StringBuilder builder = new StringBuilder();
        Connection connection = Jsoup.connect(link);
        Document document = connection.get();
        Elements rows = document.select(".vacancy-description__text");
        rows.forEach(row -> builder.append(row.select(".style-ugc").text())
        );
        return builder.toString();
    }

    @Override
    public List<Post> list(String link) {
        List<Post> posts = new ArrayList<>();
        for (int index = 1; index <= 1; index++) {
            page = index;
            Connection connection = Jsoup.connect(PAGE_LINK);
            Document document = null;
            try {
                document = connection.get();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Elements rows = document.select(".vacancy-card__inner");
            rows.forEach(row -> {
                Element titleElement = row.select(".vacancy-card__title").first();
                Element dateElement = row.select(".vacancy-card__date").first().child(0);
                Element linkElement = titleElement.child(0);
                String vacancyName = titleElement.text();
                String data = dateElement.attr("datetime");
                String vacancyLink = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
                try {
                    posts.add(new Post(vacancyName, vacancyLink, retrieveDescription(vacancyLink), dateTimeParser.parse(data)));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return posts;
    }
}