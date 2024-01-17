package org.sheer.speernotesharing.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.sheer.speernotesharing.entity.User;
import org.sheer.speernotesharing.repository.UserRepository;
import org.sheer.speernotesharing.speerDto.UserPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public User signup(UserPayload payload) {
        User user = new User();
        user.setUserName(payload.getUserName());
        user.setUserPhone(payload.getUserPhone());

        userRepository.save(user);

        return user;
    }

    public String login(UserPayload userPayload) {
        List<User> userList = userRepository.findAll();

        String token = "";

        for(User u : userList){
            if(u.getUserName().equals(userPayload.getUserName()) && u.getUserPhone().equals(userPayload.getUserPhone())){
                token = RandomStringUtils.randomAlphabetic(20) + u.getUserName();

                u.setToken(token);

                userRepository.save(u);
            }
        }

        return token;
    }
}
