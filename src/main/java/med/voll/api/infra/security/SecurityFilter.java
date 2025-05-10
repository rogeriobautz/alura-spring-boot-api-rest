package med.voll.api.infra.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import med.voll.api.domain.usuario.UsuarioRepository;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
    
        String authHeader = request.getHeader("Authorization");

        System.out.println("getRequestURI: " + request.getRequestURI());
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Authorization header is missing or invalid: " + authHeader);
        }
        
        String tokenJWT = authHeader.replace("Bearer ", "");

        String subject = tokenService.getSubject(tokenJWT);

        UserDetails usuario = usuarioRepository.findByLogin(subject);

        var authToken = new UsernamePasswordAuthenticationToken(subject, null, usuario.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

    // @Override
    // protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
    //     return request.getRequestURI().endsWith("/token")
    //     || request.getRequestURI().contains("/swagger-ui")
    //     || request.getRequestURI().contains("/v3/api-docs")
    //     || request.getRequestURI().endsWith("/favicon.ico");
    // }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        List<String> excludedPaths = Arrays.asList("/token$", "/swagger-ui/.*", "/v3/api-docs/.*", "/favicon.ico$");
        String requestURI = request.getRequestURI();
        for (String path : excludedPaths) {
            if (requestURI.matches(path)) {
                return true;
            }
        }
        return false;
    }

}
