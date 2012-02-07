package edu.htw.sefw.jukebox.web.pages.login;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import edu.htw.sefw.jukebox.web.base.JukeboxBasePage;
import edu.htw.sefw.jukebox.web.components.login.LoginPanel;
import edu.htw.sefw.jukebox.web.pages.register.RegistrationPage;

public class LoginPage extends JukeboxBasePage {

	private static final long serialVersionUID = 1L;

	private LoginPanel loginPanel = new LoginPanel("loginPanel");

	private Form<Void> registerForm = new Form<Void>("registerForm");

	private Button registerButton = new Button("registerButton") {
		private static final long serialVersionUID = 1L;

		@Override
		public void onSubmit() {
			setResponsePage(RegistrationPage.class);
		}
	};

	public LoginPage(String infoMessage, String errorMessage) {
		this(null) ;
		
		logger.info(infoMessage + " - " + errorMessage) ;
		
		if (infoMessage != null)
		{
			this.info("info") ;
			this.info(infoMessage.toString());
		}
		if (errorMessage != null)
			error(errorMessage.toString());

	}

	public LoginPage() {
		this(null);
	}

	public LoginPage(final PageParameters parameters) {

		add(loginPanel) ;

		registerForm.add(registerButton);
		add(registerForm);
	}
}
