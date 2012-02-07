package edu.htw.sefw.jukebox.web.pages.transaction;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.ui.button.ButtonBehavior;

import edu.htw.sefw.jukebox.core.SpringWicketAuthenticatedWebSession;
import edu.htw.sefw.jukebox.domain.dao.JukeboxCdDao;
import edu.htw.sefw.jukebox.domain.dao.SwappingDao;
import edu.htw.sefw.jukebox.domain.entity.ApplicationUser;
import edu.htw.sefw.jukebox.domain.entity.JukeboxCd;
import edu.htw.sefw.jukebox.domain.entity.Message;
import edu.htw.sefw.jukebox.domain.entity.Swapping;
import edu.htw.sefw.jukebox.domain.entity.Swapping.SwappingStatus;
import edu.htw.sefw.jukebox.web.base.JukeboxBasePage;
import edu.htw.sefw.jukebox.web.components.datatable.AbstractJukeboxEntityPanel;
import edu.htw.sefw.jukebox.web.components.datatable.JukeboxDataTablePanel;
import edu.htw.sefw.jukebox.web.pages.administration.ApplicationUserEntityPanel;
import edu.htw.sefw.jukebox.web.pages.library.JukeboxCdEntityPanel;

@AuthorizeInstantiation("ROLE_USER")
public class TransactionView extends JukeboxBasePage {
	private static final long serialVersionUID = 1L;
	
	@SpringBean
	SwappingDao swapDao;
	
	//final JukeboxCdEntityPanel entityPanel ;
	
	@SpringBean
	JukeboxCdDao cdDao;
	
	
	public TransactionView(){
		
		List<Swapping> cdList = new ArrayList<Swapping>(swapDao.findAll());
		List<Swapping> newCdList = new ArrayList<Swapping>();
		
		Iterator<Swapping> it = cdList.iterator();
		
		while(it.hasNext()){
			Swapping swap = it.next();
			if(swap.getRequestedUser().getId() == ((SpringWicketAuthenticatedWebSession) AuthenticatedWebSession.get()).getUser().getId() ||
					swap.getProvidedUser().getId() == ((SpringWicketAuthenticatedWebSession) AuthenticatedWebSession.get()).getUser().getId()){
				newCdList.add(swap);
				List list = swap.getProvidedCdList();				
				
				
				
			}
		}
		
		cdList = newCdList;

		//entityPanel = new JukeboxCdEntityPanel("cdPanel", null);
		//entityPanel.setVisible(false);
				
		TransactionEntityPanel transactionEditPanel = new TransactionEntityPanel("transEntityPanel");

		add(new JukeboxDataTablePanel<Swapping>("cdTablePanel", cdList,
				"firstName", SortOrder.DESCENDING, transactionEditPanel) {

			private static final long serialVersionUID = 1L;

			@Override
			public List<IColumn<Swapping>> createColumns() {
				List<IColumn<Swapping>> resultColumns = new ArrayList<IColumn<Swapping>>();
				
				resultColumns.add(new PropertyColumn<Swapping>(
						new Model<String>("Anfrage von:"), "providedUser.userName", "providedUser.userName"));

				resultColumns.add(new PropertyColumn<Swapping>(
						new Model<String>("an:"), "requestedUser.userName", "requestedUser.userName"));
				
				resultColumns.add(new PropertyColumn<Swapping>(
						new Model<String>("n:"), "providedCdList.size","providedCdList.size"));
				
				resultColumns.add(new PropertyColumn<Swapping>(
						new Model<String>("n"), "requestedCdList.size","requestedCdList.size"));

				resultColumns.add(new PropertyColumn<Swapping>(
						new Model<String>("Status:"), "status.name","status.name"));
				
				
				
				//resultColumns.add(new Column<String>("test","test","test");
//
//				resultColumns.add(new PropertyColumn<Swapping>(
//						new Model<String>("Year"), "year", "year"));
//
//				resultColumns.add(new PropertyColumn<Swapping>(
//						new Model<String>("Genre"), "genre", "genre"));

				return resultColumns;
			}

			@Override
			public int compareEntities(Swapping entity1, Swapping entity2) {

				int dir = getSortParam().isAscending() ? 1 : -1;

				/*if ("title".equals(getSortParam().getProperty())) {
					return dir
							* (entity1.getTitle().compareTo(entity2.getTitle()));
				} else if ("interpret".equals(getSortParam().getProperty())) {
					return dir
							* (entity1.getInterpret().compareTo(entity2
									.getInterpret()));
				} else if ("year".equals(getSortParam().getProperty())) {
					return -1 ;
							//dir
							//* (entity1.getYear() entity2.getYear());
				} else if ("genre".equals(getSortParam().getProperty())) {
					return dir
							* (entity1.getGenre().compareTo(entity2.getGenre()));
				}*/

				return super.compareEntities(entity1, entity2);
			}
		});
		

		//add(entityPanel);
		
		/*Form<ApplicationUser> cdTableForm = new Form<ApplicationUser>(
				"cdTableForm", null) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				entityPanel.setVisible(true);
			}
		};

		Button addButton = new Button("addButton") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {
				entityPanel.setSelectedEntity(new JukeboxCd());
				entityPanel.setVisible(true);
			}
		};*/

		//addButton.setDefaultFormProcessing(false);
		//cdTableForm.add(addButton);

		//add(cdTableForm);
        
	}	
	
	
}
