INSERT INTO GENRE (id, NAME)
VALUES (1, 'Comedy'),
       (2, 'Drama'),
       (3, 'Animation'),
       (4, 'Thriller'),
       (5, 'Documentary'),
       (6, 'Action');

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
