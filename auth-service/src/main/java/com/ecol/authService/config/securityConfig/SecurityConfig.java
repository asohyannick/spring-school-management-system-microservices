package com.ecol.authService.config.securityConfig;
import com.ecol.authService.config.JwtConfig.JWTAuthenticationFilter;
import com.ecol.authService.config.corsConfig.CorsConfig;
import com.ecol.authService.exception.GlobalExceptionResponseHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
   private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final CorsConfig corsConfig;
    private final ObjectMapper objectMapper;

    @Value("${api.version}")
    private String apiVersion;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
      return  new BCryptPasswordEncoder();
  }
  
  private String[] publicEndpoints() {
		String base = "/api/" + apiVersion + "/auth";
    return new String[] {
			base + "register",
		    base + "login",
		    base + "logout",
		    "/v3/api-docs/**" ,
		    "/swagger-ui/**" ,
		    "/swagger-ui.html",
		    "/error"
    };
  }

   @Bean
   public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws  Exception {
      return  authenticationConfiguration.getAuthenticationManager();
  }

	@Bean
	public AccessDeniedHandler accessDeniedHandler ( ) {
		return ( request , response , accessDeniedException ) -> {
			log.warn ( "Access denied for [{}] {}: {}" ,
					request.getMethod ( ) ,
					request.getRequestURI ( ) ,
					accessDeniedException.getMessage ( )
			);
			
			response.setStatus ( HttpStatus.FORBIDDEN.value ( ) );
			response.setContentType ( MediaType.APPLICATION_JSON_VALUE );
			response.setCharacterEncoding ( StandardCharsets.UTF_8.name ( ) );
			
			GlobalExceptionResponseHandler error = new GlobalExceptionResponseHandler (
					Instant.now ( ) ,
					"Access denied" ,
					"You do not have permission to access this resource" ,
					request.getRequestURI (),
					HttpStatus.FORBIDDEN.value ( ) ,
					"FORBIDDEN" ,
					request.getRequestURI ( ) ,
					request.getMethod ( )
			);
			response.getWriter( ).write ( objectMapper.writeValueAsString ( error ) );
		};
	}
	
	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint ( ) {
		return ( request , response , authException ) -> {
			log.warn ( "Unauthorized access for [{}] {}: {}" ,
					request.getMethod ( ) ,
					request.getRequestURI ( ) ,
					authException.getMessage ( )
			);
			
			response.setStatus (HttpStatus.UNAUTHORIZED.value ( ) );
			response.setContentType ( MediaType.APPLICATION_JSON_VALUE );
			response.setCharacterEncoding ( StandardCharsets.UTF_8.name ( ) );
			
			GlobalExceptionResponseHandler error = new GlobalExceptionResponseHandler (
					Instant.now ( ) ,
					"Unauthorized" ,
					"Authentication is required to access this resource" ,
					request.getRequestURI () ,
					HttpStatus.UNAUTHORIZED.value () ,
					"UNAUTHORIZED" ,
					request.getRequestURI(),
					request.getMethod()
			);
			response.getWriter ( ).write ( objectMapper.writeValueAsString ( error ) );
		};
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain ( HttpSecurity http ) throws Exception {
		http
				.cors ( cors -> cors.configurationSource (
						corsConfig.corsConfigurationSource () ) )
				.csrf ( AbstractHttpConfigurer::disable )
				.sessionManagement ( sessionManagement -> sessionManagement
						                                          .sessionCreationPolicy ( SessionCreationPolicy.STATELESS ) )
				.authorizeHttpRequests ( auth -> auth
						.requestMatchers ( publicEndpoints() ).permitAll ()
						.anyRequest ( ).authenticated ( )
				)
				.exceptionHandling ( exception -> exception
						.accessDeniedHandler ( accessDeniedHandler ( ) )
						.authenticationEntryPoint ( authenticationEntryPoint () )
				)
				.addFilterBefore ( jwtAuthenticationFilter , UsernamePasswordAuthenticationFilter.class );
		return http.build ( );
	}
}
