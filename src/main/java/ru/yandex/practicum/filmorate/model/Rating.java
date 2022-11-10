package ru.yandex.practicum.filmorate.model;

public enum Rating {
    G(1),
    PG(2),
    PG_13(3),
    R(4),
   NC_17(5);

    public final int rating;
    Rating(int rating) {
        this.rating = rating;
    }
    public static Rating valueOfName(int name) {
        for (Rating r : values()) {
            if (r.rating == name) {
                return r;
            }
        }

        return null;
    }
}
