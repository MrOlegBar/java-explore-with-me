package ru.practicum.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.error.NotFoundException;
import ru.practicum.model.User;
import ru.practicum.repository.UserRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Collection<User> getUsers(Collection<Long> userIds, int from, int size) {
        return userRepository.findAllByIdIn(userIds, PageRequest.of(from, size));
    }

    @Override
    public Collection<User> getUsers(int from, int size) {
        return userRepository.findAllBy(PageRequest.of(from, size));
    }

    @Override
    public User getUserByIdOrElseThrow(long userId) throws NotFoundException {
        return userRepository.findById(userId).orElseThrow(() -> {
            log.debug("Пользователь с userId = {} не найден.", userId);
            throw new NotFoundException(String.format("Пользователь с userId = %s не найден.",
                    userId));
        });
    }

    @Override
    public Boolean deleteUser(long userId) {
        userRepository.deleteById(userId);
        return !userRepository.existsById(userId);
    }
}