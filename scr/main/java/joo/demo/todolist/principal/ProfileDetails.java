package joo.demo.todolist.principal;

import joo.demo.todolist.domain.Profile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
@AllArgsConstructor
public class ProfileDetails implements UserDetails {

    private String username;
    private String email;
    private String password;
    private Boolean enabled;
    private Boolean confirmed;
    private List<GrantedAuthority> authorities;

    public ProfileDetails(Profile profile) {
        this.username = profile.getUsername();
        this.email = profile.getEmail();
        this.password = profile.getPassword();
        this.enabled = profile.getEnabled();
        this.confirmed = profile.getConfirmed();
        this.authorities = Arrays.stream(profile.getRoles().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
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
        return confirmed;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
