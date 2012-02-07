package edu.htw.sefw.jukebox.web.base;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.htw.sefw.jukebox.HomePage;
import edu.htw.sefw.jukebox.core.SpringWicketAuthenticatedWebSession;
import edu.htw.sefw.jukebox.domain.dao.MessageDao;
import edu.htw.sefw.jukebox.domain.dao.SwappingDao;
import edu.htw.sefw.jukebox.domain.entity.ApplicationUser;
import edu.htw.sefw.jukebox.domain.entity.Message;
import edu.htw.sefw.jukebox.domain.entity.Swapping;
import edu.htw.sefw.jukebox.web.components.link.LinkLabel;
import edu.htw.sefw.jukebox.web.components.menu.JukeboxMenu;
import edu.htw.sefw.jukebox.web.components.menu.JukeboxMenuTab;
import edu.htw.sefw.jukebox.web.pages.administration.UserAdministrationWebPage;
import edu.htw.sefw.jukebox.web.pages.library.JukeboxAllCdPage;
import edu.htw.sefw.jukebox.web.pages.library.JukeboxCdWebPage;
import edu.htw.sefw.jukebox.web.pages.login.LoginPage;
import edu.htw.sefw.jukebox.web.pages.transaction.Transaction;
import edu.htw.sefw.jukebox.web.pages.transaction.TransactionView;
import edu.htw.sefw.jukebox.web.pages.user.UserProfileWebPage;
import edu.htw.sefw.jukebox.web.resources.JukeboxResources;

public abstract class JukeboxBasePage extends WebPage {

	private static final long serialVersionUID = 1L;

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@SpringBean
	SwappingDao swappingDao;

	@SpringBean
	MessageDao messageDao;

	ApplicationUser logginUser;

	private Link<String> loginLink = new Link<String>("loginLink") {

		private static final long serialVersionUID = 1L;

		@Override
		public void onClick() {
			setResponsePage(LoginPage.class);
		}
	};

	private Link<String> logoutLink = new Link<String>("logoutLink") {

		private static final long serialVersionUID = 1L;

		@Override
		public void onClick() {
			AuthenticatedWebSession.get().signOut();
			setResponsePage(HomePage.class);
		}
	};

	private Label viewTransactionLinkLabel = new Label(
			"viewTransactionLinkLabel", "Offen");

	public JukeboxBasePage() {

		Roles roles = ((SpringWicketAuthenticatedWebSession) getSession())
				.getRoles();

		List<JukeboxMenuTab> tabList = new ArrayList<JukeboxMenuTab>();

		/***** Home *****/
		tabList.add(new JukeboxMenuTab("1", new LinkLabel("home",
				HomePage.class, "home"), null));

		/***** Libary *****/
		List<LinkLabel> libaryLinkList = new ArrayList<LinkLabel>();

		if (AuthenticatedWebSession.get().isSignedIn()) {
			libaryLinkList.add(new LinkLabel("eigene cds", JukeboxCdWebPage.class,
					"eigene cds"));
		}

		libaryLinkList.add(new LinkLabel("alle cds", JukeboxAllCdPage.class, "alle cds"));
		tabList.add(new JukeboxMenuTab("2", new LinkLabel("bibliothek", null,
				"bibliothek"), libaryLinkList));

		/***** Transactions *****/
		if (AuthenticatedWebSession.get().isSignedIn()) {
			List<LinkLabel> transactionLinkList = new ArrayList<LinkLabel>();
			transactionLinkList.add(new LinkLabel("neu", Transaction.class,
					"neu"));
			
			SpringWicketAuthenticatedWebSession session = (SpringWicketAuthenticatedWebSession) getSession();

			logginUser = session.getUser();

			List<Swapping> swappingList = swappingDao
					.findByRequestedUserAndUnreadStatus(logginUser, true);

			List<Message> messageList = messageDao.findByReceiverAndUnreadStatus(
					logginUser, true);

			if ((messageList.size() + swappingList.size()) > 0) {
				int size = swappingList.size();

				for (Message m : messageList)
					if (!swappingList.contains(m.getSwapping()))
						size++;

				
				transactionLinkList.add(new LinkLabel("anfragen ansehen",
						TransactionView.class, "anfragen ansehen (" + size + ")"));
			} else
				transactionLinkList.add(new LinkLabel("anfragen ansehen",
						TransactionView.class, "anfragen ansehen"));
			
			tabList.add(new JukeboxMenuTab("3", new LinkLabel("anfrage erstellen",
					null, "anfrage erstellen"), transactionLinkList));
		}

		/***** User *****/
		if (AuthenticatedWebSession.get().isSignedIn()) {
			List<LinkLabel> userLinkList = new ArrayList<LinkLabel>();
			userLinkList.add(new LinkLabel("eigenes profil", UserProfileWebPage.class,
					"eigenes profil"));

			if (roles.hasRole("ROLE_ADMIN")) {
				userLinkList.add(new LinkLabel("alle nutzer anzeigen",
						UserAdministrationWebPage.class, "alle nutzer anzeigen"));
			}
			tabList.add(new JukeboxMenuTab("4", new LinkLabel("benutzer", null,
					"benutzer"), userLinkList));
		}

		JukeboxMenu menu = new JukeboxMenu("jukeboxMenu", tabList);

		add(menu);

		if (AuthenticatedWebSession.get().isSignedIn()) {
			loginLink.setVisible(false);
		} else {
			logoutLink.setVisible(false);
		}

		add(loginLink);
		add(logoutLink);

		add(new FeedbackPanel("feedback"));
	}

	// Zentrale css datei, hier schnick schnack einbauen, wie eingeloggt oder
	// nicht etc.
	public void renderHead(IHeaderResponse response) {
		response.renderCSSReference(JukeboxResources.css_base);
		response.renderJavaScriptReference(JukeboxResources.jquery_min);
		response.renderCSSReference(JukeboxResources.css_menu);
		response.renderCSSReference(JukeboxResources.css_jquery);
	}
}
