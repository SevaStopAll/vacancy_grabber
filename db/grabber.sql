    create table post(
        id serial primary key,
        name varchar(250),
        text text,
        link varchar(250),
        created date,
        UNIQUE(link)
    );