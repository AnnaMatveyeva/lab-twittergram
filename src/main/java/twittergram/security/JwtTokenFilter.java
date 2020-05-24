package twittergram.security;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request,
									HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String token = jwtTokenProvider.resolveToken(request);
			if (token != null && jwtTokenProvider.validateToken(token, response)) {
				Authentication auth = jwtTokenProvider.getAuthentication(token);

				if (auth != null) {
					SecurityContextHolder.getContext().setAuthentication(auth);
				}
			}
			filterChain.doFilter(request, response);
		} catch (JwtException | AuthenticationException ex) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed");
			log.debug("Authentication failed");
		}
	}
}
