package ru.yandex.practicum.filmorate;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
//@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {

	private final UserDbStorage userStorage;

	public FilmoRateApplicationTests(@Qualifier("userDbStorage") UserDbStorage userStorage) {
		this.userStorage = userStorage;
	}

	@Test
	public void testFindUserById() {
		User userTest = User.builder().login("test").email("test@test.ru").build();
		userStorage.create(userTest);

		Optional<User> userOptional = userStorage.getById(1L);

		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("id", 1)
				);
	}
}