package edu.htw.sefw.jukebox;

import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import edu.htw.sefw.jukebox.core.SpringWicketAuthenticatedWebSession;
import edu.htw.sefw.jukebox.web.pages.login.LoginPage;


/**
 * Application object for your web application. If you want to run this
 * application without deploying, run the Start class.
 * 
 * @see edu.htw.sefw.jukebox.Start#main(String[])
 */

@Component
public class WicketApplication extends AuthenticatedWebApplication implements
		ApplicationContextAware {

	@Autowired
	private ApplicationContext ctx;

	boolean isInitialized = false;

	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<HomePage> getHomePage() {
		return HomePage.class;
	}

	/**
	 * @see org.apache.wicket.Application#init()
	 */
	@Override
	public void init() {
		//super.init();

		getComponentInstantiationListeners().add(
				new SpringComponentInjector(this, ctx, true));

		isInitialized = true;
		/*UTF 8 Problem*/
		getRequestCycleSettings().setResponseRequestEncoding("UTF-8"); 
        getMarkupSettings().setDefaultMarkupEncoding("UTF-8");
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.ctx = applicationContext;

	}

	@Override
	protected Class<? extends WebPage> getSignInPageClass() {
		return LoginPage.class;
	}

	@Override
	protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
		return SpringWicketAuthenticatedWebSession.class;
	}
}
