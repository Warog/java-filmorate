/*
DELETE FROM CLIENT;
DELETE FROM FILM;

ALTER TABLE CLIENT ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE FILM ALTER COLUMN ID RESTART WITH 1;

INSERT INTO GENRE (id, NAME)
VALUES (1, 'Комедия'),
       (2, 'Драма'),
       (3, 'Мультфильм'),
       (4, 'Триллер'),
       (5, 'Документальный'),
       (6, 'Боевик');

INSERT INTO RATING (id, NAME)
VALUES (1, 'G'),
       (2, 'PG'),
       (3, 'PG-13'),
       (4, 'R'),
       (5, 'NC-17');

INSERT INTO CLIENT (EMAIL, LOGIN, NAME, BIRTHDAY)
VALUES ('Bugaga','Vaho','VahoName','2022-12-24'),
       ('Hahaha','Vaso','VasoName','2011-10-02'),
       ('Ugaga','Vano','VanoName','2012-12-12'),
       ('Tratata','Gio','GioName','2009-02-14'),
       ('Nanana','Eliko','ElikoName','2008-01-14');

INSERT INTO FRIEND (CLIENT_ID, FRIEND_ID, STATUS)
VALUES (1,3,false),
       (2,3,false),
       (3,4,false),
       (4,1,false),
       (5,1,false),
       (5,2,false),
       (5,3,false);

INSERT INTO PUBLIC.FILM (NAME,DESCRIPTION,RELEASE_DATE,DURATION,RATING)
VALUES ('DUNE','Дюна','2009-06-04',123,1),
       ('TOP GUN','Топ ган','1998-08-11',212,2);

INSERT INTO PUBLIC.LIKES (FILM_ID,CLIENT_ID)
VALUES (1,2),
       (1,3),
       (2,1);*/
