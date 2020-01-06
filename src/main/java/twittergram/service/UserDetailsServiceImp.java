package twittergram.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import twittergram.entity.User;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImp implements UserDetailsService {

    private final UserService userService;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByName(username);

        UserDetails userDetails = null;
        List<GrantedAuthority> grantList = null;
        if (user == null) {
            throw new UsernameNotFoundException("user " + username + " not found");
        }
        String userRole = user.getRole().getName();
        if (userRole != null) {
            grantList = new ArrayList<GrantedAuthority>();
            GrantedAuthority authority = new SimpleGrantedAuthority(userRole);
            grantList.add(authority);
        }
        userDetails = new org.springframework.security.core.userdetails.User(user.getName(),
            user.getPassword(), grantList);

        return userDetails;
    }
}
