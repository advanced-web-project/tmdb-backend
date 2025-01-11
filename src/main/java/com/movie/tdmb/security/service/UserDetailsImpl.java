package com.movie.tdmb.security.service;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.movie.tdmb.model.User;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Data
@Builder
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;
    private String id;
    private String username;
    private String email;
    private Boolean isActive;
    @JsonIgnore
    private String password;
    private Date passwordChangedAt;
    private Collection<? extends GrantedAuthority> authorities; // Collection of user's authorities (roles)

    /**
     * Builds a UserDetailsImpl instance from a User object.
     *
     * @param user The User object.
     * @return A UserDetailsImpl instance.
     */
    public static UserDetailsImpl build(User user) {
        // Map the roles of the user to GrantedAuthority
//        List<GrantedAuthority> authorities = user.getRoles().stream()
//                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
//                .collect(Collectors.toList());
        List<GrantedAuthority> authorities = new ArrayList<>();
        return UserDetailsImpl.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .passwordChangedAt(user.getPasswordChangedAt())
                .authorities(authorities)
                .isActive(user.getIsActive())
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities; // Return user's authorities
    }

    @Override
    public String getPassword() {
        return password; // Return password
    }
    @Override
    public String getUsername() {
        return username; // Return username
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
        return true; // Credentials are not expired
    }
    @Override
    public boolean isEnabled() {
        return isActive; // Check if the user is active
    }
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass()) // Check if the object is null or not of the same class
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o; // Cast to UserDetailsImpl
        return Objects.equals(id, user.id); // Check if IDs are equal
    }
}
