package edu.htw.sefw.jukebox.web.pages.user;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.spring.injection.annot.SpringBean;

import edu.htw.sefw.jukebox.core.SpringWicketAuthenticatedWebSession;
import edu.htw.sefw.jukebox.domain.dao.ApplicationUserDao;
import edu.htw.sefw.jukebox.domain.entity.ApplicationUser;
import edu.htw.sefw.jukebox.web.base.JukeboxBasePage;
import edu.htw.sefw.jukebox.web.pages.administration.ApplicationUserEntityPanel;
import edu.htw.sefw.jukebox.web.pages.login.LoginPage;

@AuthorizeInstantiation("ROLE_USER")
public class UserProfileWebPage extends JukeboxBasePage {

	private static final long serialVersionUID = 1L;

	@SpringBean
	ApplicationUserDao userDao;

	public UserProfileWebPage() {

		SpringWicketAuthenticatedWebSession session = (SpringWicketAuthenticatedWebSession) getSession();

		if (session == null)
			setResponsePage(LoginPage.class);

		if (session.getUser() == null)
			setResponsePage(LoginPage.class);

		ApplicationUser user = userDao.findById(session.getUser().getId());
		
		add(new ApplicationUserEntityPanel("userPanel", user, this));

	}
}
