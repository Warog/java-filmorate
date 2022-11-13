package ru.yandex.practicum.filmorate.storage;

public class SqlRequests {
    public static final String SQL_GET_USER_BY_ID = "SELECT * FROM " + Tables.CLIENT + " WHERE id = ?";
    public static final String SQL_GET_USER_FRIEND_LIST_BY_ID = "SELECT * FROM " + Tables.FRIEND + " WHERE client_id = ?";
    public static final String SQL_INSERT_USER = "INSERT INTO " + Tables.CLIENT + "(email, login, name, birthday) VALUES (?, ?, ?, ?)";
    public static final String SQL_UPDATE_USER = "UPDATE " + Tables.CLIENT + " SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? WHERE ID = ?";
    public static final String SQL_GET_ALL_USERS = "SELECT * FROM " + Tables.CLIENT;
    public static final String SQL_DELETE_ALL_USERS = "DELETE FROM " + Tables.CLIENT;
    public static final String SQL_GET_FILM_BY_ID = "SELECT ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, RATING FROM " + Tables.FILM + " WHERE ID = ?";
    public static final String SQL_INSERT_FILM = "INSERT INTO " + Tables.FILM + "(name, description, release_date, duration, rate, rating) VALUES (?, ?, ?, ?, ?, ?)";
    public static final String SQL_UPDATE_FILM = "UPDATE " + Tables.FILM + " SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, RATE = ?, RATING = ? WHERE ID = ?";
    public static final String SQL_GET_ALL_FILMS = "SELECT * FROM " + Tables.FILM;
    public static final String SQL_DELETE_ALL_FILMS = "DELETE FROM " + Tables.FILM;
    public static final String SQL_GET_LIKES_OF_FILM_BY_ID = "SELECT CLIENT_ID FROM " + Tables.LIKES + " WHERE film_id = ?";
    public static final String SQL_FIND_USER_ID_BOOLEAN = "SELECT EXISTS (SELECT * FROM " + Tables.CLIENT + " WHERE id = ?)";
    public static final String SQL_FIND_FILM_ID_BOOLEAN = "SELECT EXISTS (SELECT * FROM " + Tables.FILM + " WHERE id = ?)";
    public static final String SQL_GET_MAX_USER_ID = "SELECT MAX(ID) FROM " + Tables.CLIENT;
    public static final String SQL_GET_MAX_FILM_ID = "SELECT MAX(ID) FROM " + Tables.FILM;
    public static final String SQL_SET_USER_LIKE_TO_FILM = "INSERT INTO " + Tables.LIKES + "(FILM_ID, CLIENT_ID) VALUES(?, ?)";
    public static final String SQL_DELETE_USER_LIKE_OF_FILM = "DELETE FROM " + Tables.LIKES + " WHERE FILM_ID = ? AND CLIENT_ID = ?";
    public static final String SQL_GET_FILM_GENRES_BY_ID = "SELECT g.ID, g.NAME FROM " + Tables.FILM_GENRE + " AS fg LEFT JOIN " + Tables.GENRE + " AS g ON fg.GENRE_ID = g.ID WHERE fg.FILM_ID = ?";
    public static final String SQL_SET_GENRE_TO_FILM = "INSERT INTO " + Tables.FILM_GENRE + "(FILM_ID, GENRE_ID) VALUES(?, ?)";
    public static final String SQL_GET_POPULAR_FILMS_WITH_LIMIT = "SELECT FILM_ID, COUNT(FILM_ID) AS film_count FROM " + Tables.LIKES +
            " GROUP BY FILM_ID " +
            "ORDER BY film_count DESC " +
            "LIMIT ?;";

    public static final String SQL_GET_COMMON_FRIENDS_IDs = "SELECT f2.FRIEND_ID FROM " + Tables.FRIEND + " f2 " +
            "WHERE f2.CLIENT_ID = ? " +
            "AND " +
            "FRIEND_ID IN " +
            "(" +
            "SELECT f.FRIEND_ID FROM " + Tables.FRIEND + " f " +
            "WHERE f.CLIENT_ID = ?" +
            ")";

    public static final String SQL_INSERT_FRIEND = "INSERT INTO " + Tables.FRIEND + " VALUES (?,?,?)";
    public static final String SQL_UPDATE_FRIENDSHIP_STATUS = "UPDATE " + Tables.FRIEND +  " SET STATUS = ? WHERE CLIENT_ID = ? AND FRIEND_ID = ?";
    public static final String SQL_DELETE_FRIEND = "DELETE FROM " + Tables.FRIEND + " WHERE CLIENT_ID = ? AND FRIEND_ID = ?";
    public static final String SQL_GET_MPA = "SELECT * FROM " + Tables.RATING + " WHERE ID = ?";
    public static final String SQL_GET_ALL_MPA = "SELECT * FROM " + Tables.RATING;
    public static final String SQL_GET_GENRE = "SELECT * FROM " + Tables.GENRE + " WHERE ID = ?";
    public static final String SQL_GET_ALL_GENRES = "SELECT * FROM " + Tables.GENRE;
    public static final String SQL_DELETE_GENRE_OF_FILM_WITH_ID = "DELETE FROM " + Tables.FILM_GENRE + " WHERE FILM_ID = ? AND GENRE_ID = ?";
    public static final String SQL_DELETE_ALL_GENRE_OF_FILM_WITH_ID = "DELETE FROM " + Tables.FILM_GENRE + " WHERE FILM_ID = ?";
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
