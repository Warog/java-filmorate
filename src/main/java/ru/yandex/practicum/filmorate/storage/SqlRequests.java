package ru.yandex.practicum.filmorate.storage;

public class SqlRequests {
    public static final String SQL_GET_USER_BY_ID = "SELECT * FROM " + Tables.CLIENT + " WHERE id = ?";
    public static final String SQL_GET_USER_FRIEND_LIST_BY_ID = "SELECT * FROM " + Tables.FRIEND + " WHERE client_id = ?";
    public static final String SQL_INSERT_USER = "INSERT INTO " + Tables.CLIENT + "(email, login, name, birthday) VALUES (?, ?, ?, ?, )";
    public static final String SQL_UPDATE_USER = "UPDATE " + Tables.CLIENT + " SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? WHERE ID = ?";
    public static final String SQL_GET_ALL_USERS = "SELECT * FROM " + Tables.CLIENT;
    public static final String SQL_DELETE_ALL_USERS = "DELETE FROM " + Tables.CLIENT;
    public static final String SQL_GET_FILM_BY_ID = "SELECT ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, LIKES, GENRE, RATING FROM " + Tables.FILM + " WHERE ID = ?";
    public static final String SQL_INSERT_FILM = "INSERT INTO " + Tables.FILM + "(name, description, release_date, duration, likes, genre, rating) VALUES (?, ?, ?, ?, ?, ?, ?)";
    public static final String SQL_UPDATE_FILM = "UPDATE " + Tables.FILM + " SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, LIKES = ?, GENRE = ?, RATING = ? WHERE ID = ?";
    public static final String SQL_GET_ALL_FILMS = "SELECT * FROM " + Tables.FILM;
    public static final String SQL_DELETE_ALL_FILMS = "DELETE FROM " + Tables.FILM;
    public static final String SQL_GET_LIKES_OF_FILM_BY_ID = "SELECT CLIENT_ID FROM " + Tables.LIKES + " WHERE film_id = ?";

    public enum Tables {
        CLIENT("CLIENT"), //на USER ругается БД
        FRIEND("FRIEND"),
        FILM("FILM"),
        GENRE("GENRE"),
        RATING("RATING"),
        LIKES("LIKES");

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
