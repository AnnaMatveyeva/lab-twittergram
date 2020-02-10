package twittergram.security;

import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import twittergram.entity.User;
import twittergram.exception.UserNotActiveException;
import twittergram.exception.UserNotFoundException;
import twittergram.service.UserService;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImp implements UserDetailsService {

    private final UserService userService;

    public UserDetails loadUserByUsername(String nickname) {
        User user = null;
        try {
            user = userService.findByNickname(nickname);
        } catch (UserNotFoundException ex) {
            throw new UsernameNotFoundException("User " + nickname + " not found");
        }

        UserDetails userDetails = null;
        List<GrantedAuthority> grantList = null;
        if (!user.isActive()) {
            throw new UserNotActiveException("User " + nickname + " not active");
        }
        String userRole = user.getRole().getName();

        grantList = Collections.singletonList(new SimpleGrantedAuthority(userRole));
        userDetails = new org.springframework.security.core.userdetails.User(user.getNickname(),
            user.getPassword(), grantList);

        return userDetails;
    }
}
