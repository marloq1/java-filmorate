package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.mappers.FilmDaoRowMapper;
import ru.yandex.practicum.filmorate.storage.mappers.UserDaoRowMapper;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class, FilmDbStorage.class,UserDaoRowMapper.class, FilmDaoRowMapper.class})
class FilmoRateApplicationTests {

	private User user1 = new User();
	private User user2 = new User();
	private List<User> users = List.of(user1, user2);

	private Film film1 = new Film();
	private Film film2 = new Film();
	private List<Film> films = List.of(film1, film2);

	private final UserDbStorage userStorage;
	private final FilmDbStorage filmStorage;
	private final JdbcTemplate jdbcTemplate;


	@AfterEach
	public void resetAutoIncrement() {
		jdbcTemplate.execute("ALTER TABLE films ALTER COLUMN id RESTART WITH 1");
		jdbcTemplate.execute("ALTER TABLE users ALTER COLUMN id RESTART WITH 1");
	}


	@BeforeEach
	public void fillDb() {
		user1.setEmail("1@mail.ru");
		user1.setName("name1");
		user1.setLogin("login1");
		user1.setBirthday(LocalDate.of(1998, Month.APRIL, 12));
		userStorage.postUser(user1);

		user2.setEmail("2@mail.ru");
		user2.setName("name2");
		user2.setLogin("login2");
		user2.setBirthday(LocalDate.of(1998, Month.APRIL, 13));
		userStorage.postUser(user2);

		film1.setName("film1");
		film1.setDescription("desc1");
		film1.setReleaseDate(LocalDate.of(1998, Month.APRIL, 14));
		film1.setDuration(100);
		GenreDto genreDto1 = new GenreDto();
		genreDto1.setId(5L);
		film1.getGenres().add(genreDto1);
		filmStorage.postFilm(film1);

		film2.setName("film2");
		film2.setDescription("desc2");
		film2.setReleaseDate(LocalDate.of(1998, Month.APRIL, 15));
		film2.setDuration(120);
		GenreDto genreDto2 = new GenreDto();
		genreDto2.setId(5L);
		film2.getGenres().add(genreDto2);
		filmStorage.postFilm(film2);
	}


	@Test
	public void testFindUserById() {
		Optional<User> userOptional = userStorage.findById(1L);


		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
				);

	}


	@Test
	public void testGetAllUsers() {
		List<User> usersFromDb = userStorage.getUsers();
		assertThat(usersFromDb).containsExactlyInAnyOrderElementsOf(users);


	}

	@Test
	public void testAddAndDeleteFriends() {
		userStorage.addFriend(1L, 2L, false);
		User userFromDb = userStorage.findById(1L).orElseThrow();
		Long id = 3L;
		for (Long friendId : userFromDb.getFriends()) {
			id = friendId;
		}
		assertEquals(id, 2L);
		userStorage.addFriend(2L,1L,true);
		userFromDb = userStorage.findById(2L).orElseThrow();
		id = 3L;
		for (Long friendId : userFromDb.getFriends()) {
			id = friendId;
		}
		assertEquals(id, 1L);


		userStorage.deleteFriend(1L,2L);
		userFromDb = userStorage.findById(2L).orElseThrow();
		User userFromDb2 = userStorage.findById(1L).orElseThrow();
		id = 3L;
		for (Long friendId : userFromDb.getFriends()) {
			id = friendId;
		}
		assertEquals(id, 1L);
		id = 3L;
		for (Long friendId : userFromDb2.getFriends()) {
			id = friendId;
		}
		assertEquals(id, 3L);

		userStorage.addFriend(1L, 2L, true);
		userStorage.deleteFriend(1L,2L);
		userFromDb = userStorage.findById(2L).orElseThrow();
		userFromDb2 = userStorage.findById(1L).orElseThrow();
		id = 3L;
		for (Long friendId : userFromDb.getFriends()) {
			id = friendId;
		}
		assertEquals(id, 1L);
		id = 3L;
		for (Long friendId : userFromDb2.getFriends()) {
			id = friendId;
		}
		assertEquals(id, 3L);

		userStorage.deleteFriend(2L,1L);
		userFromDb = userStorage.findById(2L).orElseThrow();
		userFromDb2 = userStorage.findById(1L).orElseThrow();
		id = 3L;
		for (Long friendId : userFromDb.getFriends()) {
			id = friendId;
		}
		assertEquals(id, 3L);
		id = 3L;
		for (Long friendId : userFromDb2.getFriends()) {
			id = friendId;
		}
		assertEquals(id, 3L);
	}

	@Test
	public void TestPutUser() {
		user2.setId(2L);
		user2.setEmail("3@mail.ru");
		user2.setName("name3");
		user2.setLogin("login3");
		user2.setBirthday(LocalDate.of(1998, Month.APRIL, 14));
		userStorage.putUser(user2);
		User userFromDb = userStorage.findById(2L).orElseThrow();
		assertEquals(userFromDb,user2);

		user2.setId(null);
	}

	@Test
	public void testFindFilmById() {
		Optional<Film> filmOptional = filmStorage.findById(1L);


		assertThat(filmOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
				);
	}

	@Test
	public void testGetAllFilms() {
		List<Film> filmsFromDb = filmStorage.getFilms();
		assertThat(filmsFromDb).containsExactlyInAnyOrderElementsOf(films);

	}

	@Test
	public void TestPutFilm() {
		film2.setName("film3");
		film2.setDescription("desc3");
		film2.setReleaseDate(LocalDate.of(1998, Month.APRIL, 16));
		film2.setDuration(130);
		filmStorage.putFilm(film2);
		Film filmFromDb = filmStorage.findById(2L).orElseThrow();
		assertEquals(filmFromDb,film2);

		film2.setId(null);
	}

	@Test
	public void TestAddAndDeleteLike() {
		filmStorage.addLike(1L,2L);
		Film filmFromDb = filmStorage.findById(1L).orElseThrow();
		Long id = 3L;
		for (Long userId : filmFromDb.getLikes()) {
			id = userId;
		}
		assertEquals(id, 2L);
		filmStorage.deleteLike(1L,2L);
		filmFromDb = filmStorage.findById(1L).orElseThrow();
		id = 3L;
		for (Long userId : filmFromDb.getLikes()) {
			id = userId;
		}
		assertEquals(id, 3L);
	}




}
