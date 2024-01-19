package com.example.network.services.authorization;

import com.example.network.entities.enums.Role;
import com.example.network.exceptions.AuthorizationCustomException;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRoleValidator {

    Logger logger = LoggerFactory.getLogger(UserRoleValidator.class);

    @Autowired
    IUserResolver userResolver;

    public void validateAdmin() throws AuthorizationCustomException {
        boolean roleIsAdmin = (userResolver.getRole() == Role.ROLE_ADMIN);
        if (!roleIsAdmin) {
            throw new AuthorizationCustomException("User with role " + userResolver.getRole() + " is not authorized to perform action!");
        }
        logger.debug("validateManagerOrAdmin validation completed.");
    }

    public void validateUser() throws AuthorizationCustomException {
        boolean roleIsUser = (userResolver.getRole() == Role.ROLE_USER);
        if (!roleIsUser) {
            throw new AuthorizationCustomException("User with role " + userResolver.getRole() + " is not authorized to perform action!");
        }
        logger.debug("validateManagerOrAdmin validation completed.");
    }


    public void validateUserWithUsernameAndAdmin(String username) throws AuthorizationCustomException {
        val userRole = userResolver.getRole();
        boolean roleIsUserOrRequestedEmailDoesMatchUserEmail =
                 userRole == Role.ROLE_ADMIN
                        || (userRole == Role.ROLE_USER && userResolver.getUsername().equals(username));
        if (!roleIsUserOrRequestedEmailDoesMatchUserEmail) {
            throw new AuthorizationCustomException("User with role "
                    + userResolver.getRole()
                    + " and username: "
                    + username
                    + " is not authorized to perform action!");
        }
        logger.debug("validateReadAll validation completed.");
    }

    public void validateUserAndAdmin() throws AuthorizationCustomException {
        val userRole = userResolver.getRole();
        boolean roleExists =
                userRole == Role.ROLE_USER || userRole == Role.ROLE_ADMIN;
        if (!roleExists) {
            throw new AuthorizationCustomException("User with role "
                    + userResolver.getRole()
                    + " is not authorized to perform action!");
        }
        logger.debug("validateReadAll validation completed.");
    }

    public void validateUserWithUsername(String username) throws AuthorizationCustomException {
        val userRole = userResolver.getRole();
        boolean roleIsUserOrRequestedEmailDoesMatchUserEmail =
                userRole == Role.ROLE_USER && userResolver.getUsername().equals(username);
        if (!roleIsUserOrRequestedEmailDoesMatchUserEmail) {
            throw new AuthorizationCustomException("User with role "
                    + userResolver.getRole()
                    + " and username: "
                    + username
                    + " is not authorized to perform action!");
        }
        logger.debug("validateReadAll validation completed.");
    }
}
