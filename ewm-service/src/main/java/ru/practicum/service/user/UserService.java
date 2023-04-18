package ru.practicum.service.user;

import ru.practicum.error.NotFoundException;
import ru.practicum.model.User;

import javax.transaction.Transactional;
import java.util.Collection;

public interface UserService {
    User create(User user);

    Collection<User> getUsers(Collection<Long> ids, int from, int size);

    User getUserByIdOrElseThrow(long userId) throws NotFoundException;

    @Transactional
    Boolean deleteUser(long userId) throws NotFoundException;
}
