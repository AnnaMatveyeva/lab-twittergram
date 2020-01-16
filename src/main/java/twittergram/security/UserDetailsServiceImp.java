package twittergram.security;

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
import twittergram.service.UserService;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImp implements UserDetailsService {

    private final UserService userService;

    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        User user = userService.findByNickname(nickname);

        UserDetails userDetails = null;
        List<GrantedAuthority> grantList = null;
        if (user == null || user.getStatus().equals("DELETED")) {
            throw new UsernameNotFoundException("user " + nickname + " not found");
        }
        String userRole = user.getRole().getName();
        if (userRole != null) {
            grantList = new ArrayList<GrantedAuthority>();
            GrantedAuthority authority = new SimpleGrantedAuthority(userRole);
            grantList.add(authority);
        }
        userDetails = new org.springframework.security.core.userdetails.User(user.getNickname(),
            user.getPassword(), grantList);

        return userDetails;
    }
}
