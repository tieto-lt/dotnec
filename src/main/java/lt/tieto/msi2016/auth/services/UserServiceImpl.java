package lt.tieto.msi2016.auth.services;


import lt.tieto.msi2016.auth.model.User;
import lt.tieto.msi2016.auth.repository.UserRepository;
import lt.tieto.msi2016.auth.repository.model.UserDb;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.stream.Collectors;

import static lt.tieto.msi2016.utils.constants.Roles.CUSTOMER;


@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserRepository userRepository;
    @Resource
    private BCryptPasswordEncoder encoder;

    /**
     *{@inheritDoc}
     */
    @Transactional
    @Override
    public User createUser(final User user) {
        UserDb userDb = UserDb.valueOf(user);
        userDb.setPassword(encoder.encode(userDb.getPassword()));
        userDb.setEnabled(1);
        User newUser = User.valueOf(userRepository.save(userDb));
        userRepository.insertUserAuthority(user.getUserName(),CUSTOMER);
        return newUser;
    }

    @Transactional(readOnly = true)
    @Override
    public User getUserInfo(Long id) {
        return User.valueOf(userRepository.findOne(id));
    }

    public boolean checkUsername (String userName)
    {
        User user = getUserByUserName(userName);
        if (user==null)
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    @Transactional(readOnly = true)
    public Collection<User> all() {
        return userRepository.findAll().stream().map(User::valueOf).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public User getUserByUserName(String userName) {
        return User.valueOf(userRepository.findByUserName(userName));
    }
    @Transactional
    @Override
    public User updateUserInfo(User user) {
        UserDb userDb = userRepository.findOne(user.getId());
        userDb.setUserName(user.getUserName().isEmpty() ? userDb.getUserName() : user.getUserName());
        userDb.setEmail(user.getEmail().isEmpty() ? userDb.getEmail() : user.getEmail());
        userDb.setPhone(user.getPhone().isEmpty() ? userDb.getPhone() : user.getPhone());
        userDb.setName(user.getName().isEmpty() ? userDb.getName() : user.getName());
        User updatedUser = User.valueOf(userRepository.save(userDb));
        if(user.getUserRole() != null || !user.getUserRole().isEmpty()) {
            userRepository.insertUserAuthority(user.getUserName(), user.getUserRole());
        }
        return updatedUser;
    }

}
