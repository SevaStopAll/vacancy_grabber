package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class HabrCareerParse {

    private static final String SOURCE_LINK = "https://career.habr.com";
    private static int page = 1;
    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer?page=%d", SOURCE_LINK, page);

    private static String retrieveDescription(String link) throws IOException {
        StringBuilder builder = new StringBuilder();
        Connection connection = Jsoup.connect(link);
        Document document = connection.get();
        Elements rows = document.select(".vacancy-description__text");
        rows.forEach(row -> {
                    Element description  = row.select(".style-ugc").first();
                    builder.append(description.text());
                }
        );
        return builder.toString();
    }

    public static void main(String[] args) throws IOException {
        for (int index = 1; index <= 1; index++) {
            page = index;
            Connection connection = Jsoup.connect(PAGE_LINK);
            Document document = connection.get();
            Elements rows = document.select(".vacancy-card__inner");
            rows.forEach(row -> {
                Element titleElement = row.select(".vacancy-card__title").first();
                Element dateElement = row.select(".vacancy-card__date").first().child(0);
                Element linkElement = titleElement.child(0);
                String vacancyName = titleElement.text();
                String data = dateElement.attr("datetime");
                String link = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
                System.out.printf("%s %s %s%n", vacancyName, data, link);
                try {
                    System.out.println(HabrCareerParse.retrieveDescription(link));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}