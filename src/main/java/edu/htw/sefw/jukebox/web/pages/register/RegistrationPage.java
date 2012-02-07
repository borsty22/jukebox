package edu.htw.sefw.jukebox.web.pages.register;

import edu.htw.sefw.jukebox.domain.entity.ApplicationUser;
import edu.htw.sefw.jukebox.web.base.JukeboxBasePage;
import edu.htw.sefw.jukebox.web.pages.administration.ApplicationUserEntityPanel;
import edu.htw.sefw.jukebox.web.pages.login.LoginPage;

public class RegistrationPage extends JukeboxBasePage {

	private static final long serialVersionUID = 1L;

	public RegistrationPage() {

		add(new ApplicationUserEntityPanel("userPanel", new ApplicationUser(), this) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onSaveEntity(ApplicationUser user) {

				super.onSaveEntity(user);
				setResponsePage(new LoginPage("registration complete", null)) ;
			}
		});
	}
}
