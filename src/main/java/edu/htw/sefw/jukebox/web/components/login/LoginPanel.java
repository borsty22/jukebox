package edu.htw.sefw.jukebox.web.components.login;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import edu.htw.sefw.jukebox.HomePage;
import edu.htw.sefw.jukebox.core.SpringWicketAuthenticatedWebSession;
import edu.htw.sefw.jukebox.domain.dao.ApplicationUserDao;
import edu.htw.sefw.jukebox.domain.entity.ApplicationUser;
import edu.htw.sefw.jukebox.web.base.JukeboxBasePanel;


public class LoginPanel extends JukeboxBasePanel {

	private static final long serialVersionUID = 1L;

	@SpringBean
	ApplicationUserDao userDao ;
		
	private TextField<String> userNameTextField = new TextField<String>("userNameTextField", new Model<String>()) ;
	private PasswordTextField passwordTextField = new PasswordTextField("passwordTextField", new Model<String>()) ;
	
	public LoginPanel(String id) {
		super(id);
		
		Form<?> loginForm = new Form<Void>("loginForm") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				if (AuthenticatedWebSession.get().signIn(userNameTextField.getValue(), passwordTextField.getValue()))
				{
					ApplicationUser user = userDao.findByUserName(userNameTextField.getValue()) ; 
					
					if (user != null)
					{
						((SpringWicketAuthenticatedWebSession) AuthenticatedWebSession.get()).setUser(user) ;
						setResponsePage(HomePage.class) ;
					}
					else
						error("can't create session") ;
				}
				else
					error("login failed") ;
			}
		} ;
		
		userNameTextField.setRequired(true) ;
		loginForm.add(userNameTextField) ;
		
		loginForm.add(passwordTextField) ;
		
		add(loginForm) ;
	}
	
	
}
