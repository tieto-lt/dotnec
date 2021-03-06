package lt.tieto.msi2016.auth.services;


import lt.tieto.msi2016.auth.model.User;

import java.util.Collection;

public interface UserService {

    /**
     * Creates user entry in database from {@param user}
     *
     * @param user
     */
   User createUser(User user);

    /**
     * Returns user info
     *
     * @param id
     * @return
     */
    User getUserInfo(Long id);

    /**
     *
     *
     * @return all user objects
     */
    Collection<User> all();

    /**
     *
     *
     * @param userName
     * @return User object by user name
     */
    User getUserByUserName(String userName);

    boolean checkUsername (String userName);


    /**
     * Updates user role
     * @param user
     * @return updated user
     */
    User updateUser(User user,Long id);

    void updateUserInfo(User user);

    boolean checkPassword(String currentPassword, String userName);

    void updatePassword(String newPassword, String username);
}
