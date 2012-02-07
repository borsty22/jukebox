package edu.htw.sefw.jukebox.web.pages.register;

import org.apache.wicket.spring.injection.annot.SpringBean;

import edu.htw.sefw.jukebox.domain.dao.ApplicationUserDao;
import edu.htw.sefw.jukebox.domain.entity.ApplicationUser;
import edu.htw.sefw.jukebox.web.base.JukeboxBasePage;
import edu.htw.sefw.jukebox.web.pages.administration.ApplicationUserEntityPanel;
import edu.htw.sefw.jukebox.web.pages.login.LoginPage;

public class RegistrationWebPage extends JukeboxBasePage {

	@SpringBean
	ApplicationUserDao userDao ;
	
	private static final long serialVersionUID = 1L;

	public RegistrationWebPage() {
		add(new ApplicationUserEntityPanel("userPanel", new ApplicationUser(), this) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onSaveEntity(ApplicationUser user) {
				
				super.onSaveEntity(user);
				
				setResponsePage(new LoginPage(null, "Registration successful")) ;
			}
			
		}) ;
	}
	
}
