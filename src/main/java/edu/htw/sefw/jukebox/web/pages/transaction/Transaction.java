package edu.htw.sefw.jukebox.web.pages.transaction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.ui.button.ButtonBehavior;
import org.odlabs.wiquery.ui.core.JsScopeUiEvent;
import org.odlabs.wiquery.ui.dialog.Dialog;

import edu.htw.sefw.jukebox.core.SpringWicketAuthenticatedWebSession;
import edu.htw.sefw.jukebox.domain.dao.JukeboxCdDao;
import edu.htw.sefw.jukebox.domain.dao.MessageDao;
import edu.htw.sefw.jukebox.domain.dao.SwappingDao;
import edu.htw.sefw.jukebox.domain.entity.ApplicationUser;
import edu.htw.sefw.jukebox.domain.entity.JukeboxCd;
import edu.htw.sefw.jukebox.domain.entity.Message;
import edu.htw.sefw.jukebox.domain.entity.Swapping;
import edu.htw.sefw.jukebox.domain.entity.Swapping.SwappingStatus;
import edu.htw.sefw.jukebox.web.base.JukeboxBasePage;
import edu.htw.sefw.jukebox.web.components.cdview.CdListViewPanel;
import edu.htw.sefw.jukebox.web.components.datatable.JukeboxDataTablePanel;
import edu.htw.sefw.jukebox.web.components.dynamicLabel.DynamicLabel;

@AuthorizeInstantiation("ROLE_USER")
public class Transaction extends JukeboxBasePage {

	private static final long serialVersionUID = 1L;

	private LinkedList<JukeboxCd> wantList = new LinkedList<JukeboxCd>();	
	private LinkedList<JukeboxCd> giveList  = new LinkedList<JukeboxCd>();	 //++

	DynamicLabel test;

	private List<Message> messageList;
	
	private JukeboxCd movedObject;
	
	private Boolean boolLastCdMoved = false;

	private JukeboxDataTablePanel<Message> transMessagePanel;

	TextArea<String> messageInputArea;

	DynamicLabel person;

	private LinkedList<JukeboxCd> ownInventoryList;
	private LinkedList<JukeboxCd> otherInventoryList;
	private TextField<String> goForSearchOwnInvInput;
	private TextField<String> goForSearchOtherInvInput;
	
	

	CdListViewPanel ownInventoryListViewPanel;
	CdListViewPanel otherInventoryListViewPanel;
	CdListViewPanel wantListViewPanel;	
	CdListViewPanel giveListViewPanel;

	Dialog affirmNewTransactionCreate;
	Dialog warningDialog;

	ListView<JukeboxCd> giveItemView;
	ListView<JukeboxCd> wantItemView;
	ListView<JukeboxCd> listView2;
	ListView<JukeboxCd> listView;

	Model<String> model;

	String printString;

	MultiLineLabel testLabel;

	AjaxButton newtransactionbutton;

	WebMarkupContainer sortableGive;

	WebMarkupContainer sortableWant;

	WebMarkupContainer sortableWicket;

	WebMarkupContainer sortableWicket2;

	@SpringBean
	JukeboxCdDao cdDao;

	@SpringBean
	SwappingDao swapDao;

	@SpringBean
	MessageDao mesDao;

	/**
	 * Constructor that is invoked when page is invoked without a session.
	 * 
	 * @param parameters
	 *            Page parameters
	 */
	
	

	public Transaction() {
		this(null, null, null, null, false, null);
			
	}

	public Transaction(LinkedList<JukeboxCd> ownListTransact,
			LinkedList<JukeboxCd> otherListTransact,
			LinkedList<JukeboxCd> wantListTransact,
			LinkedList<JukeboxCd> giveListTransact, Boolean editViewSwitch,
			final Swapping swap) {
		super();

		wantList = wantListTransact;
		
		giveList = giveListTransact;

		person = new DynamicLabel();

		person.setName("test");
		
		Form<String> form = new Form<String>("form") {
			private static final long serialVersionUID = 1L;			
		};

		if (!editViewSwitch) {
			ownInventoryList = new LinkedList<JukeboxCd>(
					cdDao.findByUser(((SpringWicketAuthenticatedWebSession) AuthenticatedWebSession
							.get()).getUser()));
			
			otherInventoryList = new LinkedList<JukeboxCd>(cdDao.findAll());

			// eigene CDS nicht in der allgemeinen Liste anzeigen
			for (Integer k = 0; k < otherInventoryList.size(); k++) {
				if (otherInventoryList.get(k).getUser().getId() == ((SpringWicketAuthenticatedWebSession) AuthenticatedWebSession
						.get()).getUser().getId()) {
					otherInventoryList.remove(otherInventoryList.get(k));
					k--;
				}
			}
			
			wantList = new LinkedList<JukeboxCd>();
			
			giveList = new LinkedList<JukeboxCd>();
			
		} else {
			ownInventoryList = ownListTransact;
			otherInventoryList = otherListTransact;
			wantList = wantListTransact;
			giveList = giveListTransact;
		}
		
		ownInventoryListViewPanel = new CdListViewPanel(
				"ownInventoryListViewPanel", ownInventoryList,
				"connectedToGiveItem", Boolean.FALSE,this);

		otherInventoryListViewPanel = new CdListViewPanel(
				"otherInventoryListViewPanel", otherInventoryList,
				"connectedToWantItem", Boolean.FALSE,this);

		wantListViewPanel = new CdListViewPanel("wantListViewPanel", wantList,
				"connectedToWantItem", Boolean.TRUE,this);

		giveListViewPanel = new CdListViewPanel("giveListViewPanel", giveList,
				"connectedToGiveItem", Boolean.TRUE,this);		

		

		AjaxButton buttonCreateTransact = new AjaxButton(
				"buttonCreateTransact", form) {
			private static final long serialVersionUID = 1L;

			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				
				//System.out.println( (((CdListViewPanel)form.get(0)).get(3)).getDefaultModelObject() );  //getModelObject
				
				System.out.println(otherInventoryListViewPanel.getListView().getDefaultModel());
				

				String giveCd = "", wantCd = "";

				Iterator<JukeboxCd> it = giveListViewPanel.getLinkedList()
						.iterator();
				while (it.hasNext()) {
					giveCd = giveCd + it.next().getTitle() + "\n";
				}

				it = wantListViewPanel.getLinkedList().iterator();
				while (it.hasNext()) {
					wantCd = wantCd + it.next().getTitle() + "\n";
				}

				person.setName("<table><tr><td>Biete:</td><td></td><td>Suche:</td></tr><tr><td>"
						+ giveCd
						+ "</td><td><img src='bilder/arrow_refresh.png' alt='swap'></td><td>"
						+ wantCd + "</td></tr></table>"); // Biete:" + giveCd + "
															// gegen " +
															// wantCd);

				target.add(affirmNewTransactionCreate);
				affirmNewTransactionCreate.open(target);
			}

			@Override
			protected void onError(AjaxRequestTarget arg0, Form<?> arg1) {
				// TODO Auto-generated method stub

			}
		};

		AjaxButton buttonSaveEditedTransact = new AjaxButton(
				"buttonSaveEditedTransact", form) {
			private static final long serialVersionUID = 1L;

			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

				if (swap.getProvidedUser().getId() == ((SpringWicketAuthenticatedWebSession) AuthenticatedWebSession
						.get()).getUser().getId()) {
					swap.setProvidedCdList(giveListViewPanel.getLinkedList());
					swap.setRequestedCdList(wantListViewPanel.getLinkedList());
					swap.setStatus(SwappingStatus.requested);
				} else {
					swap.setProvidedCdList(wantListViewPanel.getLinkedList());
					swap.setRequestedCdList(giveListViewPanel.getLinkedList());
					swap.setStatus(SwappingStatus.edited);
				}
				
				swapDao.save(swap);
				setResponsePage(TransactionView.class);
			}

			@Override
			protected void onError(AjaxRequestTarget arg0, Form<?> arg1) {
				// TODO Auto-generated method stub

			}
		};

		AjaxButton buttonDeleteTransact = new AjaxButton(
				"buttonDeleteTransact", form) {
			private static final long serialVersionUID = 1L;

			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				swapDao.delete(swap);
				setResponsePage(TransactionView.class);
			}

			@Override
			protected void onError(AjaxRequestTarget arg0, Form<?> arg1) {
				// TODO Auto-generated method stub

			}
		};

		AjaxButton buttonAcceptTransact = new AjaxButton(
				"buttonAcceptTransact", form) {
			private static final long serialVersionUID = 1L;

			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

				Iterator<JukeboxCd> it = swap.getProvidedCdList().iterator();

				while (it.hasNext()) {
					JukeboxCd cd = it.next();
					cd.setUser(swap.getRequestedUser());
					cdDao.save(cd);
				}

				it = swap.getRequestedCdList().iterator();

				while (it.hasNext()) {
					JukeboxCd cd = it.next();
					cd.setUser(swap.getProvidedUser());
					cdDao.save(cd);
				}

				Set<Swapping> deleteSwappingSet = new HashSet<Swapping>() ;
				
				for (JukeboxCd c : swap.getProvidedCdList()) {
					deleteSwappingSet.addAll(swapDao.findByProvidedCdList(c)) ;
					deleteSwappingSet.addAll(swapDao.findByRequestedCdList(c)) ;
				}
				
				for (JukeboxCd c : swap.getRequestedCdList()) {
					deleteSwappingSet.addAll(swapDao.findByProvidedCdList(c)) ;
					deleteSwappingSet.addAll(swapDao.findByRequestedCdList(c)) ;
				}	
				
				deleteSwappingSet.add(swap) ;
				swapDao.delete(deleteSwappingSet) ;

				setResponsePage(TransactionView.class);

			}

			@Override
			protected void onError(AjaxRequestTarget arg0, Form<?> arg1) {
				// TODO Auto-generated method stub

			}
		};

		buttonCreateTransact.add(new ButtonBehavior());
		buttonDeleteTransact.add(new ButtonBehavior());
		buttonSaveEditedTransact.add(new ButtonBehavior());
		buttonAcceptTransact.add(new ButtonBehavior());

		if (editViewSwitch) {

			buttonDeleteTransact.setVisible(true);
			buttonSaveEditedTransact.setVisible(true);

			buttonCreateTransact.setVisible(false);

			if (swap.getProvidedUser().getId() == ((SpringWicketAuthenticatedWebSession) AuthenticatedWebSession
					.get()).getUser().getId()
					&& swap.getStatus() == SwappingStatus.edited) {
				buttonAcceptTransact.setVisible(true);
			} else if (swap.getRequestedUser().getId() == ((SpringWicketAuthenticatedWebSession) AuthenticatedWebSession
					.get()).getUser().getId()
					&& swap.getStatus() == SwappingStatus.requested) {
				buttonAcceptTransact.setVisible(true);
			} else {
				buttonAcceptTransact.setVisible(false);
			}

		} else {
			buttonDeleteTransact.setVisible(false);
			buttonSaveEditedTransact.setVisible(false);
			buttonCreateTransact.setVisible(true);
			buttonAcceptTransact.setVisible(false);
		}

		

		

		AjaxButton buttonTransactConfirm = new AjaxButton(
				"buttonTransactConfirm", form) {
			private static final long serialVersionUID = 1L;

			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

				affirmNewTransactionCreate.close(target);
				
				

				Swapping testSwapping = new Swapping();

				testSwapping
						.setProvidedUser(((SpringWicketAuthenticatedWebSession) getSession())
								.getUser());
				testSwapping.setProvidedCdList(giveListViewPanel
						.getLinkedList());

				testSwapping.setRequestedUser(wantListViewPanel.getLinkedList()
						.getFirst().getUser());
				testSwapping.setRequestedCdList(wantListViewPanel
						.getLinkedList());

				// target.appendJavaScript("alert('  " +
				// testSwapping.getProvidedUser().getUserName() +
				// " "+testSwapping.getProvidedCdList().get(0).getTitle()+" " +
				// testSwapping.getRequestedUser().getUserName() +
				// " "+testSwapping.getRequestedCdList().get(0).getTitle()+"')");

				testSwapping.setStatus(SwappingStatus.requested);
				testSwapping.setUnreadStatus(true);
				System.out.println("vorher: " + testSwapping.getId());

				swapDao.save(testSwapping);

				System.out.println("nacher: " + testSwapping.getId());

				if (testSwapping.getMessageList() != null)
					for (Message m : testSwapping.getMessageList())
						m.setUnreadStatus(true) ;
				
				Message mes = new Message() ;
				
				/*mes.setReceiver(wantListViewPanel.getLinkedList()
						.getFirst().getUser()) ;*/
				
				mes.setReceiver(((SpringWicketAuthenticatedWebSession) getSession())
						.getUser()) ;
				
				mes.setMessage(messageInputArea.getValue()) ;
				
				System.out.println("hiermessage area: " + messageInputArea.getValue());
				
				mes.setSwapping(testSwapping) ;
				
				
				mesDao.save(mes) ;

				setResponsePage(TransactionView.class);

				setResponsePage(Transaction.class);
			}

			@Override
			protected void onError(AjaxRequestTarget arg0, Form<?> arg1) {
				// TODO Auto-generated method stub

			}
		};
		buttonTransactConfirm.setOutputMarkupId(Boolean.TRUE);
		buttonTransactConfirm.add(new ButtonBehavior());

		AjaxButton buttonTransactDecline = new AjaxButton(
				"buttonTransactDecline", form) {
			private static final long serialVersionUID = 1L;

			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

				affirmNewTransactionCreate.close(target);

			}

			@Override
			protected void onError(AjaxRequestTarget arg0, Form<?> arg1) {
				// TODO Auto-generated method stub

			}
		};
		buttonTransactDecline.add(new ButtonBehavior());
		buttonTransactDecline.setOutputMarkupId(Boolean.TRUE);

		// Dialog zum Bestätigen zur Erstellung einer neuen Anfrage
		affirmNewTransactionCreate = new Dialog("advancedDialog2");

		testLabel = new MultiLineLabel("labelTest2", new PropertyModel(person,
				"name"));
		testLabel.setEscapeModelStrings(Boolean.FALSE);

		affirmNewTransactionCreate.add(buttonTransactConfirm);
		affirmNewTransactionCreate.add(buttonTransactDecline);

		affirmNewTransactionCreate.add(testLabel);

		affirmNewTransactionCreate.setTitle("Neue Tauschanfrage erstellen ?");
		affirmNewTransactionCreate.setOpenEvent(JsScopeUiEvent
				.quickScope(new JsStatement().self()
						.chain("parents", "'.ui-dialog:first'")
						.chain("find", "'.ui-dialog-titlebar-close'")
						.chain("hide").render()));
		affirmNewTransactionCreate.setCloseOnEscape(false);
		affirmNewTransactionCreate.setModal(Boolean.TRUE);

		add(affirmNewTransactionCreate);
		
		
		// Messaging

		MessageEntityPanel transactionMessagePanel = new MessageEntityPanel(
				"transMessageEntityPanel");

		if (editViewSwitch) {

			messageList = swap.getMessageList();
			System.out.println("aktuelle id: " + swap.getId());

		} else {
			messageList = new ArrayList<Message>();
		}

		transMessagePanel = new JukeboxDataTablePanel<Message>(
				"transMessagePanel", messageList, "firstName",
				SortOrder.DESCENDING, transactionMessagePanel) {

			private static final long serialVersionUID = 1L;

			@Override
			public List<IColumn<Message>> createColumns() {
				List<IColumn<Message>> resultColumns = new ArrayList<IColumn<Message>>();

				resultColumns.add(new PropertyColumn<Message>(
						new Model<String>("Von:"), "receiver.userName"));

				resultColumns.add(new PropertyColumn<Message>(
						new Model<String>("Nachricht:"), "message"));

				return resultColumns;
			}

			@Override
			public int compareEntities(Message entity1, Message entity2) {

				int dir = getSortParam().isAscending() ? 1 : -1;

				return super.compareEntities(entity1, entity2);
			}

			@Override
			public void onSelect() {

				System.out.println(getParent());

			}

		};

		transMessagePanel.setOutputMarkupId(true);
		

		test = new DynamicLabel();
		test.setName("");

		messageInputArea = new TextArea<String>("messageInputArea",
				new PropertyModel(test, "name"));
		messageInputArea.setOutputMarkupId(Boolean.TRUE);

		
		
		
		Button saveMessageButton = new Button("saveMessageButton"){
			@Override
			public final void onSubmit() {

				Message mes = new Message();

				mes.setMessage(messageInputArea.getValue());

				test.setName("");

				ApplicationUser user = ((SpringWicketAuthenticatedWebSession) AuthenticatedWebSession
						.get()).getUser() ;
				
				
				mes.setReceiver(user);
				
				mes.setSwapping(swap);
				mes.setUnreadStatus(true) ;
				mesDao.save(mes);
				messageList.add(mes);

				
			}
		};
		
		saveMessageButton.add(new ButtonBehavior());
		
		if(editViewSwitch){
			saveMessageButton.setVisible(true);
			transMessagePanel.setVisible(true);
		} else {
			saveMessageButton.setVisible(false);
			transMessagePanel.setVisible(false);
		}
		
		
		//Suche eigenes Inventar
	    Form searchFormOwnInv = new Form("searchFormOwnInv"){
	    	@Override
			public final void onSubmit() {
	    		
	    		ownInventoryList = new LinkedList<JukeboxCd>(cdDao.findByTitleInterpretAndUser("%" + goForSearchOwnInvInput.getValue() + "%",
	    				((SpringWicketAuthenticatedWebSession) AuthenticatedWebSession
						.get()).getUser()));
	    		
	    		//bereits verwendete CDs entfernen
	    		if(giveListViewPanel.getLinkedList().size()>0){
	    			for(Iterator<JukeboxCd> itAll = ownInventoryList.iterator();itAll.hasNext();){
	    				
	    				for(Iterator<JukeboxCd> itInUse = giveListViewPanel.getLinkedList().iterator();itInUse.hasNext();){
		    				
		    				if(itAll.next().getId() == itInUse.next().getId()){
		    					itAll.remove();
		    				}	
		    			}
	    			}
	    		}
	    		
	    		ownInventoryListViewPanel.setLinkedList(ownInventoryList);					
			}
	    };
		Button goForSearchOwnInvButton = new Button("goForSearchOwnInvButton");	  
		goForSearchOwnInvButton.add(new ButtonBehavior());
		goForSearchOwnInvInput = new TextField<String>("goForSearchOwnInvInput",new Model<String>()); 
		searchFormOwnInv.add(goForSearchOwnInvButton);
		searchFormOwnInv.add(goForSearchOwnInvInput);
		add(searchFormOwnInv);
		
		//Suche fremdes Inventar
	    Form searchFormOtherInv = new Form("searchFormOtherInv"){
	    	@Override
			public final void onSubmit() {
	    		
	    		if(wantListViewPanel.getLinkedList().size() == 0){
	    			otherInventoryList = new LinkedList<JukeboxCd>(cdDao.findByTitleInterpretAndNotUser("%" + goForSearchOtherInvInput.getValue() + "%",
		    				((SpringWicketAuthenticatedWebSession) AuthenticatedWebSession
							.get()).getUser()));
	    		} else {
	    		
		    		otherInventoryList = new LinkedList<JukeboxCd>(cdDao.findByTitleInterpretAndUser("%" + goForSearchOtherInvInput.getValue() + "%",
		    				wantListViewPanel.getLinkedList().getFirst().getUser()));
		    		
	    		}
	    		
	    		//bereits verwendete CDs entfernen
	    		if(wantListViewPanel.getLinkedList().size()>0){
	    			for(Iterator<JukeboxCd> itAll = otherInventoryList.iterator();itAll.hasNext();){
	    				
	    				for(Iterator<JukeboxCd> itInUse = wantListViewPanel.getLinkedList().iterator();itInUse.hasNext();){
		    				
		    				if(itAll.next().getId() == itInUse.next().getId()){
		    					itAll.remove();
		    				}
		    				
		    			}
	    			}
	    		}
	    		
	    		goForSearchOtherInvInput.clearInput();
	    		otherInventoryListViewPanel.setLinkedList(otherInventoryList);					
				
			}
	    };
		Button goForSearchOtherInvButton = new Button("goForSearchOtherInvButton");	 
		goForSearchOtherInvButton.add(new ButtonBehavior());
		goForSearchOtherInvInput = new TextField<String>("goForSearchOtherInvInput",new Model<String>()); 
		searchFormOtherInv.add(goForSearchOtherInvButton);
		searchFormOtherInv.add(goForSearchOtherInvInput);
		add(searchFormOtherInv);
		
		
		
		
		
		add(ownInventoryListViewPanel);
		add(otherInventoryListViewPanel);
		form.add(wantListViewPanel);
		form.add(giveListViewPanel);
		
		form.add(transMessagePanel);
		
		form.add(buttonDeleteTransact);
		form.add(buttonSaveEditedTransact);
		form.add(buttonCreateTransact);
		form.add(buttonAcceptTransact);
		form.add(saveMessageButton);
		form.add(messageInputArea);
		add(form);
	}
	
	public JukeboxCd getMovedObject(){
		return movedObject;
	}
	
	public void setMovedObject(JukeboxCd movedObject){
		this.movedObject = movedObject;
	}
	
	public Boolean getBoolLastCdMoved(){
		return boolLastCdMoved;
	}
	
	public void setBoolLastCdMoved(Boolean boolLastCdMoved){
		this.boolLastCdMoved = boolLastCdMoved;
	}
	
}
