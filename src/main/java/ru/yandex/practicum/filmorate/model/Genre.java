package ru.yandex.practicum.filmorate.model;

public enum Genre {
    Comedy(1),
    Drama(2),
    Animation(3),
    Thriller(4),
    Documentary(5),
    Action(6);

    public final int name;

    Genre(int s) {
        this.name = s;
    }

    public static Genre valueOfName(int name) {
        for (Genre g : values()) {
            if (g.name == name) {
                return g;
            }
        }
        return null;
    }
}
