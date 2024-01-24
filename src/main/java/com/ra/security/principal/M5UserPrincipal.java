package com.ra.security.principal;

import com.ra.model.entity.M5User;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class M5UserPrincipal implements UserDetails {
    private M5User user;
    private Collection<? extends GrantedAuthority> authorities;

    public static UserDetails build(M5User user) {
        return M5UserPrincipal.builder()
                .user(user)
                .authorities(user.getRoles().stream()
                        .map(item ->
                                new SimpleGrantedAuthority(item.getRoleName())).toList())
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
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
