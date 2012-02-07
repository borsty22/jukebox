package edu.htw.sefw.jukebox.web.pages.register;

import edu.htw.sefw.jukebox.domain.entity.ApplicationUser;
import edu.htw.sefw.jukebox.web.base.JukeboxBasePanel;
import edu.htw.sefw.jukebox.web.pages.administration.ApplicationUserEntityPanel;

public class RegistrationPanel extends JukeboxBasePanel {

	private static final long serialVersionUID = 1L;

	public RegistrationPanel(String id) {
		super(id);

		add(new ApplicationUserEntityPanel("userPanel", new ApplicationUser(), this.getPage()) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onSaveEntity(ApplicationUser user) {

				super.onSaveEntity(user);
				onRegisterEntity();
			}

		});
	}

	protected void onRegisterEntity() {
		this.setVisible(false);
	}
}
