package ru.job4j.grabber;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store {

    private Connection cnn;

    public PsqlStore(Properties cfg) throws SQLException {
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
            cnn = DriverManager.getConnection(
                    cfg.getProperty("url"),
                    cfg.getProperty("username"),
                    cfg.getProperty("password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void save(Post post) {
        PreparedStatement ps =
                connect.prepareStatement("insert into post(name, text, link, created_date) " +
                        "values (?)");
    }


    @Override
    public List<Post> getAll() {
        PreparedStatement ps =
                connect.prepareStatement("select * from post");
        return null;
    }

    @Override
    public Post findById(int id) {
        PreparedStatement ps =
                connect.prepareStatement("select * from post " +
                        "where id = (?)");
        return null;
    }

    @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }
}
