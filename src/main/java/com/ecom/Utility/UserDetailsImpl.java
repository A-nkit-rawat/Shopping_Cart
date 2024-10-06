package com.ecom.Utility;

import com.ecom.model.UserDtl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {

    private final String email;
    private final String password;
    private final Collection<?extends GrantedAuthority> authorities;
    private final Boolean isEnabled;
    private final Boolean isAccountNonLock;

    UserDetailsImpl(UserDtl user) {
        this.email=user.getEmail();
        this.password=user.getPassword();
        this.authorities= List.of(new SimpleGrantedAuthority(user.getRole()));
        this.isEnabled=user.getIsEnabled();
        this.isAccountNonLock=user.getAccountNonLocked();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLock;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
