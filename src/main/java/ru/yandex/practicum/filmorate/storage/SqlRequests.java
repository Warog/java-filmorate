package ru.yandex.practicum.filmorate.storage;

public class SqlRequests {
    public static final String SQL_GET_USER_BY_ID = "SELECT * FROM " + Tables.CLIENT + " WHERE id = ?";
    public static final String SQL_GET_USER_FRIEND_LIST_BY_ID = "SELECT * FROM " + Tables.FRIEND + " WHERE client_id = ?";
    public static final String SQL_INSERT_USER = "INSERT INTO " + Tables.CLIENT + "(email, login, name, birthday) VALUES (?, ?, ?, ?)";
    public static final String SQL_UPDATE_USER = "UPDATE " + Tables.CLIENT + " SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? WHERE ID = ?";
    public static final String SQL_GET_ALL_USERS = "SELECT * FROM " + Tables.CLIENT;
    public static final String SQL_DELETE_ALL_USERS = "DELETE FROM " + Tables.CLIENT;
    public static final String SQL_GET_FILM_BY_ID = "SELECT ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, LIKES, GENRE, RATING FROM " + Tables.FILM + " WHERE ID = ?";
    public static final String SQL_INSERT_FILM = "INSERT INTO " + Tables.FILM + "(name, description, release_date, duration, likes, genre, rating) VALUES (?, ?, ?, ?, ?, ?, ?)";
    public static final String SQL_UPDATE_FILM = "UPDATE " + Tables.FILM + " SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, LIKES = ?, GENRE = ?, RATING = ? WHERE ID = ?";
    public static final String SQL_GET_ALL_FILMS = "SELECT * FROM " + Tables.FILM;
    public static final String SQL_DELETE_ALL_FILMS = "DELETE FROM " + Tables.FILM;
    public static final String SQL_GET_LIKES_OF_FILM_BY_ID = "SELECT CLIENT_ID FROM " + Tables.LIKES + " WHERE film_id = ?";
    public static final String SQL_FIND_USER_ID_BOOLEAN = "SELECT EXISTS (SELECT * FROM " + Tables.CLIENT + " WHERE id = ?)";
    public static final String SQL_FIND_FILM_ID_BOOLEAN = "SELECT EXISTS (SELECT * FROM " + Tables.FILM + " WHERE id = ?)";
    public static final String SQL_GET_MAX_USER_ID = "SELECT MAX(ID) FROM " + Tables.CLIENT;
    public static final String SQL_GET_MAX_FILM_ID = "SELECT MAX(ID) FROM " + Tables.FILM;
    public static final String SQL_SET_USER_LIKE_TO_FILM = "INSERT INTO " + Tables.LIKES + "(FILM_ID, CLIENT_ID) VALUES(?, ?)";
    public static final String SQL_DELETE_USER_LIKE_OF_FILM = "DELETE FROM " + Tables.LIKES + " WHERE FILM_ID = ? AND CLIENT_ID = ?";
    public static final String SQL_GET_FILM_GENRES_BY_ID = "SELECT GENRE_ID FROM " + Tables.FILM_GENRE + " WHERE film_id = ?";
    public static final String SQL_SET_GENRE_TO_FILM = "INSERT INTO " + Tables.FILM_GENRE + "(FILM_ID, GENRE_ID) VALUES(?, ?)";
    public static final String SQL_GET_POPULAR_FILMS_WITH_LIMIT = "SELECT FILM_ID, COUNT(FILM_ID) AS film_count FROM " + Tables.LIKES +
            " GROUP BY FILM_ID " +
            "ORDER BY film_count DESC " +
            "LIMIT ?;";

    public enum Tables {
        CLIENT("CLIENT"), //на USER ругается БД
        FRIEND("FRIEND"),
        FILM("FILM"),
        GENRE("GENRE"),
        RATING("RATING"),
        LIKES("LIKES"),
        FILM_GENRE("FILM_GENRE");

        private final String table;

        Tables(final String table) {
            this.table = table;
        }

        @Override
        public String toString() {
            return table;
        }
    }
}
