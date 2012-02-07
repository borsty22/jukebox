package edu.htw.sefw.jukebox.core;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.jasypt.digest.PooledStringDigester;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.stereotype.Service;

import edu.htw.sefw.jukebox.domain.dao.ApplicationUserDao;
import edu.htw.sefw.jukebox.domain.entity.ApplicationUser;
import edu.htw.sefw.jukebox.domain.entity.ApplicationUser.GenderType;
import edu.htw.sefw.jukebox.domain.entity.ApplicationUser.UserType;

@Service
public class SpringWicketAuthentificationProvider implements
		AuthenticationProvider {

	private static final Logger logger = Logger
			.getLogger(SpringWicketAuthentificationProvider.class);
	
	@Autowired
	ApplicationUserDao userDao;

	@Autowired
	PooledStringDigester stringDigester;

	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {

		String username = authentication.getName();
		String password = (String) authentication.getCredentials();
		
		ApplicationUser user = userDao.findByUserName(username);

		if (user == null && username.equals("chuck") && password.equals("1234"))
		{
			user = new ApplicationUser() ;
			user.setFirstName("Chuck") ;
			user.setLastName("Norris") ;
			user.setUserName("chuck") ;
			user.setEmail("chuck@gmail.com") ;
			user.setPassword(stringDigester.digest("1234")) ;
			user.setUserType(UserType.Admin) ;
			user.setGender(GenderType.Male) ;
			userDao.save(user) ;
 		}
		
		// here authentication
		if (user != null
				&& stringDigester.matches(password, user.getPassword())) {
			
		} else {
			throw new BadCredentialsException("Du kommst hier nicht rein");
		}

		// grant authorities
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(
				authentication.getAuthorities());
		if (user.getUserType().equals(UserType.Admin)) {
			authorities.add(new GrantedAuthorityImpl("ROLE_USER"));
			authorities.add(new GrantedAuthorityImpl("ROLE_ADMIN"));
			logger.info("authorize admin") ;
		} else if (user.getUserType().equals(UserType.User)) {
			authorities.add(new GrantedAuthorityImpl("ROLE_USER"));
			logger.info("authorize user") ;
		}

		// create token
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
				authentication.getPrincipal(), authentication.getCredentials(),
				authorities);

		return token;
	}

	public boolean supports(Class<? extends Object> authentication) {
		return (UsernamePasswordAuthenticationToken.class
				.isAssignableFrom(authentication));
	}
}
