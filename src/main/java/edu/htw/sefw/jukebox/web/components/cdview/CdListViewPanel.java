package edu.htw.sefw.jukebox.web.components.cdview;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.IResource.Attributes;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.ui.core.JsScopeUiEvent;
import org.odlabs.wiquery.ui.dialog.Dialog;
import org.odlabs.wiquery.ui.dialog.Dialog.WindowPosition;
import org.odlabs.wiquery.ui.sortable.SortableAjaxBehavior;
import org.wicketstuff.html5.media.MediaSource;
import org.wicketstuff.html5.media.audio.Html5Audio;

import edu.htw.sefw.jukebox.core.SpringWicketAuthenticatedWebSession;
import edu.htw.sefw.jukebox.domain.dao.JukeboxCdDao;
import edu.htw.sefw.jukebox.domain.entity.ApplicationUser;
import edu.htw.sefw.jukebox.domain.entity.JukeboxCd;
import edu.htw.sefw.jukebox.web.components.dynamicLabel.DynamicLabel;
import edu.htw.sefw.jukebox.web.pages.library.JukeboxCdEntityPanel;
import edu.htw.sefw.jukebox.web.pages.transaction.Transaction;


 
public class CdListViewPanel extends Panel{
	
	private LinkedList<JukeboxCd> list;
	private WebMarkupContainer container;
	private ListView<JukeboxCd> listView;
	private SortableAjaxBehavior<Component> sortableAjaxBehavior;
	private String connectWith;
	private Boolean transactionList;
	private Label label;
	private DynamicLabel person;
	private Dialog audioPlayDialog;
	private IModel<List<MediaSource>> mediaSourceList;
	private List<MediaSource> music;
	private Dialog cdInfoDialog;
	private JukeboxCd selectedCd;
	private Html5Audio audio;
	@SpringBean
	JukeboxCdDao cdDao;
	
	private List<JukeboxCd> secureList;

	private JukeboxCd lastMovedObject;

	private static final long serialVersionUID = 1L;

	public CdListViewPanel(String id, LinkedList<JukeboxCd> list,String connectWith,final Boolean isTransactionList,final Transaction transaction) {
		super(id);
		
		setTransactionListFlag(isTransactionList);
		
		setConnectWith(connectWith);
		
		
		//Titel der Liste // zeigt an, wem die cds gehören die in der Liste zu sehen sind
		person = new DynamicLabel();
		
		selectedCd = new JukeboxCd();
		
		secureList = new LinkedList<JukeboxCd>();
		secureList.addAll(list);
		
		if(this.getTransactionListFlag()){
			person.setName("");
		} else {
			
			if(list.size() > 0){
				
				ApplicationUser user = ((SpringWicketAuthenticatedWebSession) AuthenticatedWebSession.get()).getUser();
				
				if(user != null && list.getFirst().getUser().getId() == user.getId()){
					person.setName("deine CDs");
				} else {
					person.setName("alle CDs");
				}
			
			}
			
		}
		
		label = new Label("listViewTitle",new PropertyModel(person, "name"));
		
		
		label.setOutputMarkupId(Boolean.TRUE);
		
		add(label);
		
		
		
		this.list = list;
		
		String con = "." + getConnectWith();
		
		container = new WebMarkupContainer("container")	{
			private static final long serialVersionUID = 1L;

			@Override
		    protected void onComponentTag(ComponentTag tag) {
		     super.onComponentTag(tag);
		     tag.put("class",getConnectWith());
		     
		    }
		   };
		
		
		listView = new CdListView("listView", list){
			private static final long serialVersionUID = 1L;

			@Override
			public void playMusic(AjaxRequestTarget target,final Object object){
				System.out.println(((JukeboxCd)object).getTitle());
				
				audioPlayDialog.removeAll();
				
				music = new ArrayList<MediaSource>();
		  		
		  		//Laden der Musik
		  	    mediaSourceList = new AbstractReadOnlyModel<List<MediaSource>>() {
		  			private static final long serialVersionUID = 184656142287823787L;

		  			public List<MediaSource> getObject() {
		  					music.clear();
		  					music.add(new MediaSource(((JukeboxCd)object).getAudio_mp3l()));
		  	            return music;
		  	        }
		  	    };
		  		
		  	    audio = new Html5Audio("audio", mediaSourceList) {
		  			private static final long serialVersionUID = 1L;
		  			@Override
		  	        protected boolean isControls() {
		  	            return true;
		  	        }
		  	    };
		  	    
		  	   
		  	    
		  	    audioPlayDialog.add(audio);
		  	    target.add(audioPlayDialog);
		  	    audioPlayDialog.open(target);	
			}
			
			@Override
			public void showInfo(AjaxRequestTarget target,final Object object){
				JukeboxCd cd = (JukeboxCd) object;
				
				String emptyValue = "- keine Angabe -";
				
				
				
				PackageResourceReference reference = new PackageResourceReference(JukeboxCdEntityPanel.class,
						"res/missingCoverSmall.png");
				
				
				
				selectedCd.setCover(cd.getCover());
				
			
				
				if(cd.getTitle() == null){
					selectedCd.setTitle(emptyValue);
				} else {
					selectedCd.setTitle(cd.getTitle());
				}
				
				if(cd.getDescription() == null){
					selectedCd.setDescription(emptyValue);
				} else {
					selectedCd.setDescription(cd.getDescription());
				}
				
				if(cd.getGenre() == null){
					selectedCd.setGenre(emptyValue);
				} else {
					selectedCd.setGenre(cd.getGenre());
				}
				
				if(cd.getInterpret() == null){
					selectedCd.setInterpret(emptyValue);
				} else {
					selectedCd.setInterpret(cd.getInterpret());
				}
				
				if(cd.getYear() == 0){
					selectedCd.setYear(0);
				} else {
					selectedCd.setYear(cd.getYear());
				}
				
				selectedCd.setUser(cd.getUser());
				
				System.out.println(getParent().getParent());
				target.add(cdInfoDialog);
				
				cdInfoDialog.setPosition(WindowPosition.CENTER);
				
				cdInfoDialog.open(target);			
			}
			
			
		};
		
		container.add(listView);		
	    
	    sortableAjaxBehavior = new SortableAjaxBehavior<Component>() {
	    	private static final long serialVersionUID = 1L;
	    	
			/* (non-Javadoc)
			 * @see org.odlabs.wiquery.ui.sortable.SortableAjaxBehavior#onReceive(org.apache.wicket.Component, int, org.apache.wicket.Component, org.apache.wicket.ajax.AjaxRequestTarget)
			 */
			@Override
			public void onReceive(Component sortedComponent, int index, 
					Component parentSortedComponent,
					AjaxRequestTarget ajaxRequestTarget) {
								
				List list;
				
				System.out.println(transaction.getMovedObject().getUser());
				System.out.println( ((SpringWicketAuthenticatedWebSession) AuthenticatedWebSession.get()).getUser());
				
				
				if(transaction.getBoolLastCdMoved() && !isTransactionList && (transaction.getMovedObject().getUser().getId() != ((SpringWicketAuthenticatedWebSession) AuthenticatedWebSession.get()).getUser().getId())){
					
					list = cdDao.findByTitleInterpretAndNotUser("%%", ((SpringWicketAuthenticatedWebSession) AuthenticatedWebSession.get()).getUser());
					
				} else {
					list = listView.getList();			
				
					list.add(transaction.getMovedObject());
				}
				
				listView.setList(list);
				
				
				container.add(listView);
				ajaxRequestTarget.add(container);				
			
			}

			/* (non-Javadoc)
			 * @see org.odlabs.wiquery.ui.sortable.SortableAjaxBehavior#onUpdate(org.apache.wicket.Component, int, org.apache.wicket.ajax.AjaxRequestTarget)
			 */
			@Override
			public void onUpdate(Component sortedComponent, int index,
					AjaxRequestTarget ajaxRequestTarget) {	
			}

			/* (non-Javadoc)
			 * @see org.odlabs.wiquery.ui.sortable.SortableAjaxBehavior#onRemove(org.apache.wicket.Component, org.apache.wicket.ajax.AjaxRequestTarget)
			 */
			@Override
			public void onRemove(Component sortedComponent,
					AjaxRequestTarget ajaxRequestTarget) {					
				
				transaction.setMovedObject((JukeboxCd)sortedComponent.getDefaultModelObject());
				listView.getList().remove(transaction.getMovedObject());
				
				if(!isTransactionList){

					listView.getList().remove(transaction.getMovedObject());
					
					for(int i = 0; i < listView.getList().size();i++){
						System.out.println(listView.getList().get(i).getId());
						if(listView.getList().get(i).getId() == transaction.getMovedObject().getId() || listView.getList().get(i).getUser() != transaction.getMovedObject().getUser()){
							System.out.println(transaction.getMovedObject().getId());
							listView.getList().remove(i);
							i--;
						}
					}					
					
				} else {
					if(listView.getList().size() == 0){
						transaction.setBoolLastCdMoved(true);
					} else {
						transaction.setBoolLastCdMoved(false);
					}
				}
				
				container.add(listView);
				ajaxRequestTarget.add(container);	
				
			}
	    };
	    
	  //AudioDialog		
		
	  		music = new ArrayList<MediaSource>();
	  		
	  		//Laden der Musik
	  	    mediaSourceList = new AbstractReadOnlyModel<List<MediaSource>>() {
	  			private static final long serialVersionUID = 184656142287823787L;

	  			public List<MediaSource> getObject() {
	  					music.clear();
	  					//music.add(new MediaSource(cdDao.findAll().get(1).getAudio_mp3l()));
	  	            return music;
	  	        }
	  	    };
	  		
	  	    audio = new Html5Audio("audio", mediaSourceList) {
	  			private static final long serialVersionUID = 1L;
	  			@Override
	  	        protected boolean isControls() {
	  	            return true;
	  	        }
	  	    };
	  	    
	  	    audioPlayDialog = new Dialog("audioPlayDialog");
	  	    audioPlayDialog.setCloseEvent((JsScopeUiEvent) JsScopeUiEvent.quickScope(audioPlayDialog.destroy()));	
	  	    audioPlayDialog.setWidth(340);
	  	    System.out.println(audioPlayDialog.getCssClass());
	  	    audioPlayDialog.add(audio);
	  	    add(audioPlayDialog);
	  	        
	  	    
	  	    //CDInfo
	  	    cdInfoDialog = new Dialog("cdInfoDialog");
	  	  	cdInfoDialog.setTitle("CD Details");
	  	  	cdInfoDialog.setWidth(430);
	  	  	WindowPosition pos = cdInfoDialog.getPosition();
	  	  	cdInfoDialog.setMinHeight(600);
	  	  	System.out.println(pos);
	  	 
	  	    Image newImage = new Image("infoCover",
	  	    	new DynamicImageResource() {
	  	    		@Override
					protected byte[] getImageData(Attributes attributes) {								
						return new PropertyModel<byte[]>(selectedCd,"cover").getObject();
					}
	  	    	});
	  	    
	  	  	
	  
	  	
	  	    
	  	    Label labelTitle = new Label("infoTitle",new PropertyModel(selectedCd, "title"));
	  	    Label labelGenre = new Label("infoGenre",new PropertyModel(selectedCd, "genre"));
	  	    Label labelYear = new Label("infoYear",new PropertyModel(selectedCd, "year"));
	  	    Label labelDescription = new Label("infoDescription",new PropertyModel(selectedCd, "description"));
	  	    Label labelInterpret = new Label("infoInterpret",new PropertyModel(selectedCd, "interpret"));
	  	    //Label labelUser = new Label("infoFromuser",new PropertyModel(selectedCd,"user.userName"));
	  	    cdInfoDialog.add(newImage);
	  	    cdInfoDialog.add(labelTitle);
	  	    cdInfoDialog.add(labelGenre);
	  	    cdInfoDialog.add(labelYear);
	  	    cdInfoDialog.add(labelDescription);
	  	    cdInfoDialog.add(labelInterpret);
	  	    //cdInfoDialog.add(labelUser);
	  	    cdInfoDialog.setModal(Boolean.TRUE);
	  	    
	  	    cdInfoDialog.setPosition(WindowPosition.CENTER);
	  	  
	  	    add(cdInfoDialog);
	  	    
		
		container.setOutputMarkupId(true);	  		
		
		sortableAjaxBehavior.getSortableBehavior().setConnectWith(con);
	    container.add(sortableAjaxBehavior);
	    container.add(listView);
	    
		add(container);
		
		
		
	}
	
	
	
	public void setLinkedList(LinkedList<JukeboxCd> list) {
		listView.setList(list);
		//= list;
		/*
		//update and redraw
		container.removeAll();
		container.setOutputMarkupId(true);
	    listView = new CdListView("listView", getLinkedList());
	    
		//listView.setReuseItems(Boolean.TRUE);
		container.add(listView);
		this.add(container);*/
		
	}
	
	public LinkedList<JukeboxCd> getLinkedList() {
		return (LinkedList<JukeboxCd>) listView.getList();
		//return (LinkedList<JukeboxCd>) this.secureList;
	}
	
	public void setConnectionWith(String connection){
		this.sortableAjaxBehavior.getSortableBehavior().setConnectWith(connection);
		add(container);
	}

	private void setConnectWith(String connectWith){
		this.connectWith = connectWith;
	}
	
	private String getConnectWith(){
		return this.connectWith;
	}
	
	private WebMarkupContainer getContainer(){
		return container;
	}
	
	public void setTransactionListFlag(Boolean flag){
		this.transactionList = flag;
	}
	
	public Boolean getTransactionListFlag(){
		return this.transactionList;
	}
	
	public Label getLabel(){
		return this.label;		
	}
	
	public void setLabel(Label label){
		this.label = label;
	}
	
	public void setDynamicLabel(String string){
		this.person.setName(string);
	}
	
	public ListView getListView(){
		return listView;
	}
	
	
}
