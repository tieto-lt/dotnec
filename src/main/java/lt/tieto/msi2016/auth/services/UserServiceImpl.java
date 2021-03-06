package lt.tieto.msi2016.auth.services;


import lt.tieto.msi2016.auth.model.User;
import lt.tieto.msi2016.auth.repository.UserRepository;
import lt.tieto.msi2016.auth.repository.model.UserDb;
import lt.tieto.msi2016.operator.repository.OperatorRepository;
import lt.tieto.msi2016.utils.exception.FieldValidationException;
import org.springframework.jdbc.UncategorizedSQLException;
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
    @Resource
    private OperatorRepository operatorRepository;


    /**
     *{@inheritDoc}
     */
    @Transactional
    @Override
    public User createUser(final User user) {
        UserDb userDb = UserDb.valueOf(user);
        userDb.setPassword(encoder.encode(userDb.getPassword()));
        userDb.setEnabled(1);
        User newUser = User.valueOf(userRepository.create(userDb));
        userRepository.insertUserAuthority(user.getUserName(),CUSTOMER);
        newUser.setUserRole(CUSTOMER);
        return newUser;
    }


    @Transactional
    @Override
    public void updatePassword(String newPassword, String username){
        String password = encoder.encode(newPassword);
        userRepository.updatePassword(password, username);
    }

    @Transactional(readOnly = true)
    @Override
    public User getUserInfo(Long id) {
        return fillUser(userRepository.findOne(id));
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


    public User updateUser(User user,Long id) {

        if(user.getUserRole()!="ROLE_CUSTOMER" && operatorRepository.findById(user.getId())!=null)
        {
            operatorRepository.deleteOperatorByUserId(user.getId());
        }
        UserDb userDb = userRepository.findOne(id);
        try {
            userRepository.insertUserAuthority(userDb.getUserName(), user.getUserRole());
        } catch (UncategorizedSQLException e){
            throw new FieldValidationException("userRole",e.getSQLException().getMessage());
        }

        return user;
    }

    public void updateUserInfo(User user){
        userRepository.updateUser(user);
    }


    @Transactional(readOnly = true)
    public Collection<User> all() {
        return userRepository.findAll().stream().map(this::fillUser).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public User getUserByUserName(String userName) {
        UserDb userDb = userRepository.findByUserName(userName);
        return userDb == null ? null : User.valueOf(userDb);

    }

    @Transactional
    private User fillUser(UserDb userDb){
        User user = User.valueOf(userDb);
        user.setUserRole(userRepository.getUserRole(userDb.getUserName()).stream().findFirst().get());
        return user;
    }

    public boolean checkPassword(String currentPassword, String userName){
        String password = userRepository.getPassword(userName);
        return encoder.matches(currentPassword,password);
    }
}
