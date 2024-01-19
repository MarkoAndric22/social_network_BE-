package com.example.network.services.authorization;

import com.example.network.entities.enums.Role;

public interface IUserResolver {

    Role getRole();

    String getUsername();
}
