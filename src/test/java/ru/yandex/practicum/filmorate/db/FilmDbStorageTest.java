package ru.yandex.practicum.filmorate.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {

    private final FilmDbStorage filmStorage;

    @Test
    public void testGetFilm() {

        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.getFilm(4));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 4)
                );
    }

    @Test
    public void testAddFilm() {
        Film testFilm = new Film(
                3,
                "Titanic",
                "It's a trap",
                LocalDate.of(1997, 12, 19),
                194,
                List.of(1L, 2L, 3L, 4L),
                5,
                new Mpa(1, null),
                List.of(new Genre(1, null))
        );

        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.addFilm(testFilm));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film -> {
                            assertThat(film).hasFieldOrPropertyWithValue("id", 3);
                            assertThat(film).hasFieldOrPropertyWithValue("name", "Titanic");
                            assertThat(film).hasFieldOrPropertyWithValue("description", "It's a trap");
                            assertThat(film).hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(1997, 12, 19));
                            assertThat(film).hasFieldOrPropertyWithValue("duration", 194);
                            assertThat(film).hasFieldOrPropertyWithValue("rate", 5);
                            assertThat(film).hasFieldOrPropertyWithValue("mpa", new Mpa(1, null));
                        }
                );
    }

    @Test
    public void testUpdateFilm() {
        Film testFilm = new Film(
                4,
                "Titanic",
                "It's a trap",
                LocalDate.of(1997, 12, 19),
                194,
                List.of(1L, 2L, 3L, 4L),
                5,
                new Mpa(1, null),
                List.of(new Genre(1, null))
        );

        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.addFilm(testFilm));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film -> {
                            assertThat(film).hasFieldOrPropertyWithValue("id", 4);
                            assertThat(film).hasFieldOrPropertyWithValue("name", "Titanic");
                            assertThat(film).hasFieldOrPropertyWithValue("description", "It's a trap");
                            assertThat(film).hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(1997, 12, 19));
                            assertThat(film).hasFieldOrPropertyWithValue("duration", 194);
                            assertThat(film).hasFieldOrPropertyWithValue("rate", 5);
                            assertThat(film).hasFieldOrPropertyWithValue("mpa", new Mpa(1, null));
                        }
                );


        Film testUpdatedFilm = new Film(
                4,
                "TitanicUpdate",
                "It's a trap Update",
                LocalDate.of(1997, 12, 19),
                194,
                List.of(1L, 2L, 3L, 4L),
                5,
                new Mpa(1, null),
                List.of(new Genre(1, null))
        );

        Optional<Film> filmOptional2 = Optional.ofNullable(filmStorage.updateFilm(testUpdatedFilm));

        assertThat(filmOptional2)
                .isPresent()
                .hasValueSatisfying(film -> {
                            assertThat(film).hasFieldOrPropertyWithValue("id", 4);
                            assertThat(film).hasFieldOrPropertyWithValue("name", "TitanicUpdate");
                            assertThat(film).hasFieldOrPropertyWithValue("description", "It's a trap Update");
                            assertThat(film).hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(1997, 12, 19));
                            assertThat(film).hasFieldOrPropertyWithValue("duration", 194);
                            assertThat(film).hasFieldOrPropertyWithValue("rate", 5);
                            assertThat(film).hasFieldOrPropertyWithValue("mpa", new Mpa(1, "G"));
                        }
                );
    }


    @Test
    public void testAllFilms() {
        Optional<List<Film>> allFilms = Optional.ofNullable(filmStorage.allFilms());

        assertThat(allFilms)
                .isPresent()
                .hasValueSatisfying(films -> {
                            assertThat(films.get(0)).hasFieldOrPropertyWithValue("id", 1);
                            assertThat(films.get(0)).hasFieldOrPropertyWithValue("name", "DUNE");
                            assertThat(films.get(0)).hasFieldOrPropertyWithValue("description", "Дюна");
                            assertThat(films.get(0)).hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2009, 6, 4));
                            assertThat(films.get(0)).hasFieldOrPropertyWithValue("duration", 123);
                            assertThat(films.get(0)).hasFieldOrPropertyWithValue("likes", List.of(3L));
                            assertThat(films.get(0)).hasFieldOrPropertyWithValue("rate", 0);
                            assertThat(films.get(0)).hasFieldOrPropertyWithValue("mpa", new Mpa(1, "G"));

                            assertThat(films.get(1)).hasFieldOrPropertyWithValue("id", 2);
                            assertThat(films.get(1)).hasFieldOrPropertyWithValue("name", "TOP GUN");
                            assertThat(films.get(1)).hasFieldOrPropertyWithValue("description", "Топ ган");
                            assertThat(films.get(1)).hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(1998, 8, 11));
                            assertThat(films.get(1)).hasFieldOrPropertyWithValue("duration", 212);
//                                assertThat(films.get(1)).hasFieldOrPropertyWithValue("likes", List.of(2L, 3L));
                            assertThat(films.get(1)).hasFieldOrPropertyWithValue("rate", 0);
                            assertThat(films.get(1)).hasFieldOrPropertyWithValue("mpa", new Mpa(2, "PG"));
                        }
                );
    }


    @Test
    public void testSetLikes() {
        Film testFilm = new Film(
                4,
                "Titanic",
                "It's a trap",
                LocalDate.of(1997, 12, 19),
                194,
                List.of(1L, 2L, 3L, 4L),
                5,
                new Mpa(1, null),
                List.of(new Genre(1, null))
        );

        filmStorage.addFilm(testFilm);

        filmStorage.setLike(5, 1);

        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.getFilm(5));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film -> {
                            assertThat(film).hasFieldOrPropertyWithValue("id", 5);
                            assertThat(film).hasFieldOrPropertyWithValue("likes", List.of(1L));
                        }
                );
    }

    @Test
    @Sql({"classpath:/sql/test_data.sql"})
    public void testUnsetLikes() {

        filmStorage.unsetLike(1, 2);

        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.getFilm(1));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film -> {
                            assertThat(film).hasFieldOrPropertyWithValue("id", 1);
                            assertThat(film).hasFieldOrPropertyWithValue("likes", List.of(3L));
                        }
                );
    }

    @Test
    public void testDeleteAllFilms() {

        Optional<List<Film>> allFilms = Optional.ofNullable(filmStorage.allFilms());

        assertThat(allFilms)
                .isPresent()
                .hasValueSatisfying(films -> assertThat(films.size()).isEqualTo(3));

        filmStorage.deleteAllFilms();

        Optional<List<Film>> allFilmsNull = Optional.ofNullable(filmStorage.allFilms());

        assertThat(allFilmsNull)
                .isPresent()
                .hasValueSatisfying(films -> assertThat(films.size()).isEqualTo(0));
    }

    @Test
    public void testGetMostPopularFilms() {

        filmStorage.setLike(4, 1);

        Optional<List<Film>> allFilms = Optional.ofNullable(filmStorage.getMostPopularFilms(1));

        assertThat(allFilms)
                .isPresent()
                .hasValueSatisfying(films -> {
                    assertThat(films.size()).isEqualTo(1);
                    assertThat(films.get(0)).hasFieldOrPropertyWithValue("id", 4);
                });
    }

    @Test
    public void testGetMpaById() {

        Optional<Mpa> mpa = Optional.ofNullable(filmStorage.getMpaById(2));

        assertThat(mpa)
                .isPresent()
                .hasValueSatisfying(mpaRating -> {
                    assertThat(mpaRating).hasFieldOrPropertyWithValue("id", 2);
                    assertThat(mpaRating).hasFieldOrPropertyWithValue("name", "PG");
                });
    }

    @Test
    public void testGetAllMpa() {

        Optional<List<Mpa>> mpaList = Optional.ofNullable(filmStorage.getAllMpa());

        assertThat(mpaList)
                .isPresent()
                .hasValueSatisfying(mpas -> {
                    assertThat(mpas.size()).isEqualTo(5);
                    assertThat(mpas.get(0)).hasFieldOrPropertyWithValue("id", 1);
                    assertThat(mpas.get(1)).hasFieldOrPropertyWithValue("id", 2);
                    assertThat(mpas.get(2)).hasFieldOrPropertyWithValue("id", 3);
                    assertThat(mpas.get(3)).hasFieldOrPropertyWithValue("id", 4);
                    assertThat(mpas.get(4)).hasFieldOrPropertyWithValue("id", 5);
                });
    }

    @Test
    public void testGetGenreById() {

        Optional<Genre> genre = Optional.ofNullable(filmStorage.getGenreById(2));

        assertThat(genre)
                .isPresent()
                .hasValueSatisfying(filmGenre -> {
                    assertThat(filmGenre).hasFieldOrPropertyWithValue("id", 2);
                    assertThat(filmGenre).hasFieldOrPropertyWithValue("name", "Драма");
                });
    }

    @Test
    public void testGetAllGenres() {

        Optional<List<Genre>> genreList = Optional.ofNullable(filmStorage.getAllGenres());

        assertThat(genreList)
                .isPresent()
                .hasValueSatisfying(genres -> {
                    assertThat(genres.size()).isEqualTo(6);
                    assertThat(genres.get(0)).hasFieldOrPropertyWithValue("id", 1);
                    assertThat(genres.get(1)).hasFieldOrPropertyWithValue("id", 2);
                    assertThat(genres.get(2)).hasFieldOrPropertyWithValue("id", 3);
                    assertThat(genres.get(3)).hasFieldOrPropertyWithValue("id", 4);
                    assertThat(genres.get(4)).hasFieldOrPropertyWithValue("id", 5);
                    assertThat(genres.get(5)).hasFieldOrPropertyWithValue("id", 6);
                });
    }
}
