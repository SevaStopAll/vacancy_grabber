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
    private static final int PAGE = 5;
    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer?page=", SOURCE_LINK);

    private final DateTimeParser dateTimeParser;

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    private static String retrieveDescription(String link) {
        try {
            StringBuilder builder = new StringBuilder();
            Connection connection = Jsoup.connect(link);
            Document document = connection.get();
            builder.append(document.select(".vacancy-description__text").text());
            return builder.toString();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private Post getPost(Element element) {
        Post post = null;
        Element titleElement = element.select(".vacancy-card__title").first();
        Element dateElement = element.select(".vacancy-card__date").first().child(0);
        Element linkElement = titleElement.child(0);
        String vacancyName = titleElement.text();
        String data = dateElement.attr("datetime");
        String vacancyLink = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
        post = new Post(vacancyName, vacancyLink, retrieveDescription(vacancyLink), dateTimeParser.parse(data));
        return post;
    }

    @Override
    public List<Post> list(String link) {
        List<Post> posts = new ArrayList<>();
        for (int index = 1; index <= PAGE; index++) {
            Connection connection = Jsoup.connect(PAGE_LINK + index);
            Document document = null;
            try {
                document = connection.get();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Elements rows = document.select(".vacancy-card__inner");
            rows.forEach(row -> posts.add(getPost(row)));
        }
        return posts;
    }

    public static void main(String[] args) {
        HabrCareerParse parser = new HabrCareerParse(new HabrCareerDateTimeParser());
        System.out.println(parser.list(PAGE_LINK).get(0).getName());
    }
}