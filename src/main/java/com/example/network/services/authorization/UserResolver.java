package com.example.network.services.authorization;

import com.example.network.dtos.UserDetailsDTO;
import com.example.network.entities.enums.Role;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Primary
public class UserResolver implements IUserResolver{

    @Override
    public Role getRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsDTO userDetailsDTO = (UserDetailsDTO) authentication.getPrincipal();
        return userDetailsDTO.role();
    }

    @Override
    public String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsDTO userDetailsDTO = (UserDetailsDTO) authentication.getPrincipal();
        return userDetailsDTO.username();

    }
}
