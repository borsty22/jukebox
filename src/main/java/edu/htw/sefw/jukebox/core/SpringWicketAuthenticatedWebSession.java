package edu.htw.sefw.jukebox.core;

import org.apache.log4j.Logger;
import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.request.Request;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import edu.htw.sefw.jukebox.domain.dao.ApplicationUserDao;
import edu.htw.sefw.jukebox.domain.entity.ApplicationUser;

@SuppressWarnings("serial")
public class SpringWicketAuthenticatedWebSession extends
		AuthenticatedWebSession {

	@Autowired
	ApplicationUserDao userDao ;
	
	private ApplicationUser loginedUser ;
	
	private static final Logger logger = Logger
			.getLogger(SpringWicketAuthenticatedWebSession.class);

	@SpringBean(name = "authenticationManager")
	private AuthenticationManager authenticationManager;

	public SpringWicketAuthenticatedWebSession(Request request) {
		super(request);
		injectDependencies();
		ensureDependenciesNotNull();
	}

	public static SpringWicketAuthenticatedWebSession getSpringWicketAuthenticatedWebSession() {
		return (SpringWicketAuthenticatedWebSession) Session.get();
	}

	private void ensureDependenciesNotNull() {
		if (authenticationManager == null) {
			throw new IllegalStateException("Requires an authentication");
		}
	}

	private void injectDependencies() {
		Injector.get().inject(this);
	}

	@Override
	public boolean authenticate(String username, String password) {
		boolean authenticated = false;

		try {
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(
							username, password));
			SecurityContextHolder.getContext()
					.setAuthentication(authentication);
			authenticated = authentication.isAuthenticated();
		} catch (AuthenticationException e) {
			authenticated = false;
		}

		return authenticated;
	}

	@Override
	public Roles getRoles() {
		Roles roles = new Roles();
		getRolesIfSignedIn(roles);
		return roles;
	}

	private void getRolesIfSignedIn(Roles roles) {
		if (isSignedIn()) {
			Authentication authentication = SecurityContextHolder.getContext()
					.getAuthentication();
			addRolesFromAuthentication(roles, authentication);
		}
	}

	private void addRolesFromAuthentication(Roles roles,
			Authentication authentication) {
		if (authentication != null) {
			if (authentication.getAuthorities() != null) {
				for (GrantedAuthority authority : authentication
						.getAuthorities()) {
					roles.add(authority.getAuthority());
				}
			}
		}
		else
			signIn(false) ;
	}

	@Override
	public void signOut() {
		logger.info("logout") ;
		
		setUser(null) ;
		SecurityContextHolder.getContext().setAuthentication(null);
		super.signOut();
	}

	public ApplicationUser getUser() {
		return this.loginedUser ;
	}

	public void setUser(ApplicationUser user) {
		this.loginedUser = user;
	}
}
