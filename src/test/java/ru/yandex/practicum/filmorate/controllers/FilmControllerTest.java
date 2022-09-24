package ru.yandex.practicum.filmorate.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.gson.adapter.LocalDateAdapter;
import ru.yandex.practicum.filmorate.gson.adapter.LocalDateDeserializer;
import ru.yandex.practicum.filmorate.model.Film;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(webEnvironment = DEFINED_PORT)
class FilmControllerTest {
    private static final String HOST = "localhost";
    private static final String PORT = "8080";
    private static final String POINT = "/films";
    private static Gson gson;

    @BeforeAll
    static void creatUtils() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());

        gson = gsonBuilder.create();

    }

    @BeforeEach
    void deleteFilmList() {
        deleteRequestBuilder(POINT);
    }

    @Test
    void addFilm() {
        Film film = Film.builder()
                .id(0)
                .name("Interstellar")
                .description("Научно-фантастический фильм, снятый Кристофером Ноланом по сценарию, написанному в соавторстве с Джонатаном Ноланом")
                .releaseDate(LocalDate.of(2014, 10, 26))
                .duration(169)
                .build();

        List<Film> films = gson.fromJson(getRequestBuilder(POINT), new TypeToken<List<Film>>() {
        }.getType());
        assertEquals(0, films.size(), "В списке фильмов есть записи");

        postRequestBuilder(POINT, gson.toJson(film));

        films = gson.fromJson(getRequestBuilder(POINT), new TypeToken<List<Film>>() {
        }.getType());
        assertEquals(1, films.size(), "Добавление фильма прошло НЕуспешно!");

        Film testFilm = films.get(0);
        assertEquals(film, testFilm, "Данные добавленного фильма неверны");
    }

    @Test
    void addFilmWithEmptyName() {
        Film film = Film.builder()
                .id(1)
                .name(null)
                .description("Научно-фантастический фильм, снятый Кристофером Ноланом по сценарию, написанному в соавторстве с Джонатаном Ноланом")
                .releaseDate(LocalDate.of(2014, 10, 26))
                .duration(169)
                .build();

        Exception exception = assertThrows(ValidationException.class, () -> {
            postRequestBuilder(POINT, gson.toJson(film));
        });

        assertTrue(exception.getMessage().contains("Ошибка! Статус код = 400"));

    }

    @Test
    void addFilmWithDescriptionLongestThan200() {
        Film film = Film.builder()
                .id(2)
                .name("Interstellar")
                .description("Научно-фантастический фильм, снятый Кристофером Ноланом по сценарию, написанному в соавторстве с Джонатаном Ноланом. Фильм повествует о путешествиях группы исследователей, которые используют недавно обнаруженный пространственно-временной тоннель, чтобы обойти ограничения полёта человека в космосе и покорить огромные расстояния на межзвёздном корабле")
                .releaseDate(LocalDate.of(2014, 10, 26))
                .duration(169)
                .build();

        Exception exception = assertThrows(ValidationException.class, () -> {
            postRequestBuilder(POINT, gson.toJson(film));
        });

        assertTrue(exception.getMessage().contains("Ошибка! Статус код = 500"));

    }

    @Test
    void addFilmWithReleaseDateBefore1895_12_28() {
        Film film = Film.builder()
                .id(3)
                .name("Interstellar")
                .description("Научно-фантастический фильм, снятый Кристофером Ноланом по сценарию, написанному в соавторстве с Джонатаном Ноланом.")
                .releaseDate(LocalDate.of(1895, 12, 27))
                .duration(169)
                .build();

        Exception exception = assertThrows(ValidationException.class, () -> {
            postRequestBuilder(POINT, gson.toJson(film));
        });

        assertTrue(exception.getMessage().contains("Ошибка! Статус код = 500"));

    }

    @Test
    void addFilmWithReleaseDateAfter1895_12_28() {
        Film film = Film.builder()
                .id(4)
                .name("Interstellar")
                .description("Научно-фантастический фильм, снятый Кристофером Ноланом по сценарию, написанному в соавторстве с Джонатаном Ноланом.")
                .releaseDate(LocalDate.of(1895, 12, 29))
                .duration(169)
                .build();

        List<Film> films = gson.fromJson(getRequestBuilder(POINT), new TypeToken<List<Film>>() {
        }.getType());

        int filmsSize = films.size();
        postRequestBuilder(POINT, gson.toJson(film));

        List<Film> films2 = gson.fromJson(getRequestBuilder(POINT), new TypeToken<List<Film>>() {
        }.getType());

        assertEquals(filmsSize + 1, films2.size(), "Валидация фильма с датой полсе 1895-12-28 прошла с ошибкой!");

    }

    @Test
    void addFilmWithReleaseDateEquals1895_12_28() {
        Film film = Film.builder()
                .id(5)
                .name("Interstellar")
                .description("Научно-фантастический фильм, снятый Кристофером Ноланом по сценарию, написанному в соавторстве с Джонатаном Ноланом.")
                .releaseDate(LocalDate.of(1895, 12, 28))
                .duration(169)
                .build();

        Exception exception = assertThrows(ValidationException.class, () -> {
            postRequestBuilder(POINT, gson.toJson(film));
        });

        assertTrue(exception.getMessage().contains("Ошибка! Статус код = 500"));

    }

    @Test
    void addFilmWithDurationLessThenZero() {
        Film film = Film.builder()
                .id(6)
                .name("Interstellar")
                .description("Научно-фантастический фильм, снятый Кристофером Ноланом по сценарию, написанному в соавторстве с Джонатаном Ноланом.")
                .releaseDate(LocalDate.of(1895, 12, 28))
                .duration(-1)
                .build();

        Exception exception = assertThrows(ValidationException.class, () -> {
            postRequestBuilder(POINT, gson.toJson(film));
        });

        assertTrue(exception.getMessage().contains("Ошибка! Статус код = 400"));

    }

    @Test
    void updateFilm() {
        Film film = Film.builder()
                .id(7)
                .name("Interstellar")
                .description("Научно-фантастический фильм, снятый Кристофером Ноланом по сценарию, написанному в соавторстве с Джонатаном Ноланом")
                .releaseDate(LocalDate.of(2014, 10, 26))
                .duration(169)
                .build();

        postRequestBuilder(POINT, gson.toJson(film));

        List<Film> films = gson.fromJson(getRequestBuilder(POINT), new TypeToken<List<Film>>() {
        }.getType());

        int size = films.size();

        Film updatedFilm = Film.builder()
                .id(7)
                .name("Inter")
                .description("Научно-фантастический фильм.")
                .releaseDate(LocalDate.of(2013, 11, 22))
                .duration(123)
                .build();

        putRequestBuilder(POINT, gson.toJson(updatedFilm));

        List<Film> films2 = gson.fromJson(getRequestBuilder(POINT), new TypeToken<List<Film>>() {
        }.getType());

        assertEquals(updatedFilm, films2.get(size - 1));
    }

    @Test
    void allFilms() {
        Film film1 = Film.builder()
                .id(1)
                .name("Interstellar")
                .description("Научно-фантастический фильм, снятый Кристофером Ноланом по сценарию, написанному в соавторстве с Джонатаном Ноланом")
                .releaseDate(LocalDate.of(2014, 10, 26))
                .duration(169)
                .build();

        Film film2 = Film.builder()
                .id(2)
                .name("Hancock")
                .description("Американский фантастический боевик о супергероях 2008 года, снятый режиссёром Питером Бергом.")
                .releaseDate(LocalDate.of(2008, 6, 10))
                .duration(92)
                .build();

        List<Film> forAdd = new ArrayList<>();
        forAdd.add(film1);
        forAdd.add(film2);

        postRequestBuilder(POINT, gson.toJson(film1));
        postRequestBuilder(POINT, gson.toJson(film2));

        List<Film> films = gson.fromJson(getRequestBuilder(POINT), new TypeToken<List<Film>>() {
        }.getType());

        assertEquals(2,films.size());
        assertEquals(forAdd, films, "Список всех фильмом заполнен неправильно");
    }


    public static String getRequestBuilder(String point) {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://" + HOST + ":" + PORT + point))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200)
                throw new ValidationException("Ошибка! Статус код = " + response.statusCode());

            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new ValidationException("Ошибка запроса!", e);
        }
    }

    public static void postRequestBuilder(String point, String body) {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://" + HOST + ":" + PORT + point))
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .header("Content-Type", "application/json")
                    .build();
            HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
            if (response.statusCode() != 200)
                throw new ValidationException("Ошибка! Статус код = " + response.statusCode());

            response.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String putRequestBuilder(String point, String body) {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://" + HOST + ":" + PORT + point))
                    .PUT(HttpRequest.BodyPublishers.ofString(body))
                    .header("Content-Type", "application/json")
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200)
                throw new ValidationException("Ошибка! Статус код = " + response.statusCode());

            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new ValidationException("Ошибка запроса!", e);
        }
    }

    public String deleteRequestBuilder(String point) {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://" + HOST + ":" + PORT + point))
                    .DELETE()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200)
                throw new ValidationException("Ошибка! Статус код= " + response.statusCode());

            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new ValidationException("Ошибка запроса!", e);
        }
    }
}