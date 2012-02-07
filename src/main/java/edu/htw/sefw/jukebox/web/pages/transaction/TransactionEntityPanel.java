package edu.htw.sefw.jukebox.web.pages.transaction;

import java.util.LinkedList;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.spring.injection.annot.SpringBean;

import edu.htw.sefw.jukebox.core.SpringWicketAuthenticatedWebSession;
import edu.htw.sefw.jukebox.domain.dao.SwappingDao;
import edu.htw.sefw.jukebox.domain.entity.JukeboxCd;
import edu.htw.sefw.jukebox.domain.entity.Message;
import edu.htw.sefw.jukebox.domain.entity.Swapping;
import edu.htw.sefw.jukebox.web.components.datatable.AbstractJukeboxEntityPanel;

public class TransactionEntityPanel extends
		AbstractJukeboxEntityPanel<Swapping> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SpringBean
	SwappingDao swappingDao;

	public TransactionEntityPanel(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onDeleteEntitiy(Swapping t) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSaveEntity(Swapping t) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectedEntity(Swapping t) {

		if (t.getId() != null)
		{
			t.setUnreadStatus(false) ;
			for (Message m : t.getMessageList())
				m.setUnreadStatus(false) ;
			
			swappingDao.save(t) ;
		}
		
		LinkedList<JukeboxCd> providedCdList = new LinkedList<JukeboxCd>(
				t.getProvidedCdList());
		LinkedList<JukeboxCd> requestedCdList = new LinkedList<JukeboxCd>(
				t.getRequestedCdList());
		LinkedList<JukeboxCd> ownCdList = new LinkedList<JukeboxCd>(t
				.getRequestedUser().getCdList());
		LinkedList<JukeboxCd> otherCdList = new LinkedList<JukeboxCd>(t
				.getProvidedUser().getCdList());

		for (int i = 0; i < ownCdList.size(); i++) {

			for (int k = 0; k < requestedCdList.size(); k++) {

				if (ownCdList.get(i).getId() == requestedCdList.get(k).getId()) {
					ownCdList.remove(i);
					i--;
					break;

				}
			}
		}

		for (int i = 0; i < otherCdList.size(); i++) {

			for (int k = 0; k < providedCdList.size() && otherCdList.size() > 0; k++) {

				if (otherCdList.get(i).getId() == providedCdList.get(k).getId()) {
					otherCdList.remove(i);
					i--;
					break;
				}
			}
		}

		Transaction transactionEditPage = null;

		if (t.getRequestedUser().getId() == ((SpringWicketAuthenticatedWebSession) AuthenticatedWebSession
				.get()).getUser().getId()) {
			transactionEditPage = new Transaction(ownCdList, otherCdList,
					providedCdList, requestedCdList, true, t);
		} else {
			transactionEditPage = new Transaction(otherCdList, ownCdList,
					requestedCdList, providedCdList, true, t);
		}

		setResponsePage(transactionEditPage);

	}

}
