package com.example.network.dtos;

import com.example.network.entities.enums.Role;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Builder()
public record  UserDetailsDTO(
        String name,
        String username,
        String password,
        String email,
        Role role
) implements UserDetails {

    public Map<String, Object> getExtraClaims() {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", role.toString());
        extraClaims.put("username",username );
        return extraClaims;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority(role.name()));
        return authorityList;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}