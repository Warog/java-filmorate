package ru.yandex.practicum.filmorate.controller;

import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(webEnvironment = DEFINED_PORT)
class UserControllerTest {
/*
    private static final String HOST = "localhost";
    private static final String PORT = "8080";
    private static final String POINT = "/users";
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
    void addUser() {
        User user = User.builder()
                .id(1L)
                .email("ktoto@ya.ru")
                .login("Ktoto")
                .name("Kot")
                .birthday(LocalDate.of(2000, 1,1))
//                .friends(new HashSet<>())
                .build();

        postRequestBuilder(POINT, gson.toJson(user));

        List<User> users =  gson.fromJson(getRequestBuilder(POINT), new TypeToken<List<User>>() {}.getType());
        assertEquals(user, users.get(0), "Ошибка при добавлении нового пользователя");
    }

    @Test
    void addUserWithEmptyEmail() {
        User user = User.builder()
                .email(null)
                .login("Ktoto")
                .name("Kot")
                .birthday(LocalDate.of(2000, 1,1))
                .build();

        Exception exception = assertThrows(ValidationException.class, () -> {
            postRequestBuilder(POINT, gson.toJson(user));
        });

        assertTrue(exception.getMessage().contains("Ошибка! Статус код = 400"));
    }

    @Test
    void addUserWithEmailWithoutDog() {
        User user = User.builder()
                .email("ktotoya.ru")
                .login("ktoto")
                .name("Kot")
                .birthday(LocalDate.of(2000, 1,1))
//                .friends(new HashSet<>())
                .build();

        Exception exception = assertThrows(ValidationException.class, () -> {
            postRequestBuilder(POINT, gson.toJson(user));
        });

        assertTrue(exception.getMessage().contains("Ошибка! Статус код = 400"));
    }

    @Test
    void addUserWithEmptyLogin() {
        User user = User.builder()
                .email("ktoto@ya.ru")
                .login(null)
                .name("Kot")
                .birthday(LocalDate.of(2000, 1,1))
//                .friends(new HashSet<>())
                .build();

        Exception exception = assertThrows(ValidationException.class, () -> {
            postRequestBuilder(POINT, gson.toJson(user));
        });

        assertTrue(exception.getMessage().contains("Ошибка! Статус код = 400"));
    }

    @Test
    void addUserWithLoginWithSpacesAtTheBeginning() {
        User user = User.builder()
                .email("ktoto@ya.ru")
                .login(" Ktoto")
                .name("Kot")
                .birthday(LocalDate.of(2000, 1,1))
//                .friends(new HashSet<>())
                .build();

        Exception exception = assertThrows(ValidationException.class, () -> {
            postRequestBuilder(POINT, gson.toJson(user));
        });

        assertTrue(exception.getMessage().contains("Ошибка! Статус код = 400"));
    }

    @Test
    void addUserWithLoginWithSpacesAtTheEnd() {
        User user = User.builder()
                .email("ktoto@ya.ru")
                .login("Ktoto ")
                .name("Kot")
                .birthday(LocalDate.of(2000, 1,1))
//                .friends(new HashSet<>())
                .build();

        Exception exception = assertThrows(ValidationException.class, () -> {
            postRequestBuilder(POINT, gson.toJson(user));
        });

        assertTrue(exception.getMessage().contains("Ошибка! Статус код = 400"));
    }

    @Test
    void addUserWithLoginWithSpacesAtTheMiddle() {
        User user = User.builder()
                .email("ktoto@ya.ru")
                .login("Kto to")
                .name("Kot")
                .birthday(LocalDate.of(2000, 1,1))
//                .friends(new HashSet<>())
                .build();

        Exception exception = assertThrows(ValidationException.class, () -> {
            postRequestBuilder(POINT, gson.toJson(user));
        });

        assertTrue(exception.getMessage().contains("Ошибка! Статус код = 400"));
    }

    @Test
    void addUserWithEmptyName() {
        User user = User.builder()
                .email("ktoto@ya.ru")
                .login("Ktoto")

                .birthday(LocalDate.of(2000, 1,1))
                .build();

        postRequestBuilder(POINT, gson.toJson(user));

        List<User> users =  gson.fromJson(getRequestBuilder(POINT), new TypeToken<List<User>>() {}.getType());

        assertEquals(user.getLogin(), users.get(0).getName());
    }

    @Test
    void addUserWithDateOfBirthdayInFuture() {
        User user = User.builder()
                .email("ktoto@ya.ru")
                .login("Ktoto")
                .name("")
                .birthday(LocalDate.now().plusDays(1))
//                .friends(new HashSet<>())
                .build();

        Exception exception = assertThrows(ValidationException.class, () -> {
            postRequestBuilder(POINT, gson.toJson(user));
        });

        assertTrue(exception.getMessage().contains("Ошибка! Статус код = 400"));
    }

    @Test
    void updateUser() {
        User user = User.builder()
                .email("ktoto@ya.ru")
                .login("Ktoto")
                .name("Kto")
                .birthday(LocalDate.now().minusDays(1))
//                .friends(new HashSet<>())
                .build();

        postRequestBuilder(POINT, gson.toJson(user));

        User forUpdate = User.builder()
                .id(1L)
                .email("chtoto@ya.ru")
                .login("Chtoto")
                .name("Chto")
                .birthday(LocalDate.now().minusDays(1))
//                .friends(new HashSet<>())
                .build();

        putRequestBuilder(POINT, gson.toJson(forUpdate));

        List<User> users =  gson.fromJson(getRequestBuilder(POINT), new TypeToken<List<User>>() {}.getType());
        assertEquals(forUpdate, users.get(0), "Ошибка при обновлении данных пользователя");

    }

    @Test
    void allUsers() {
        User user1 = User.builder()
                .id(1L)
                .email("ktoto@ya.ru")
                .login("Ktoto")
                .name("Kto")
                .birthday(LocalDate.now().minusDays(1))
//                .friends(new HashSet<>())
                .build();

        User user2 = User.builder()
                .id(2L)
                .email("chtoto@ya.ru")
                .login("Chtoto")
                .name("Chto")
                .birthday(LocalDate.now().minusDays(1))
//                .friends(new HashSet<>())
                .build();

        postRequestBuilder(POINT, gson.toJson(user1));
        postRequestBuilder(POINT, gson.toJson(user2));

        Set<User> users =  gson.fromJson(getRequestBuilder(POINT), new TypeToken<Set<User>>() {}.getType());

        Set<User> addedUsers = new HashSet<>();
        addedUsers.add(user1);
        addedUsers.add(user2);

        assertEquals(addedUsers, users, "Ошибка при выводе списка пользователей");

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
    }*/
}