package edu.htw.sefw.jukebox.web.pages.library;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;

import edu.htw.sefw.jukebox.core.SpringWicketAuthenticatedWebSession;
import edu.htw.sefw.jukebox.domain.dao.JukeboxCdDao;
import edu.htw.sefw.jukebox.domain.entity.ApplicationUser;
import edu.htw.sefw.jukebox.domain.entity.JukeboxCd;
import edu.htw.sefw.jukebox.web.base.JukeboxBasePage;
import edu.htw.sefw.jukebox.web.components.datatable.ImagePanel;
import edu.htw.sefw.jukebox.web.components.datatable.JukeboxDataTablePanel;

@AuthorizeInstantiation("ROLE_USER")
public class JukeboxCdWebPage extends JukeboxBasePage {

	private static final long serialVersionUID = 1L;

	final JukeboxCdEntityPanel entityPanel ;
	
	private JukeboxDataTablePanel<JukeboxCd> cdDataTablePanel;
	
	private List<JukeboxCd> cdList;
	
	@SpringBean
	JukeboxCdDao cdDao;
	
	private Form<String> userSearchForm = new Form<String>("userSearchForm",
			new Model<String>()) {
		private static final long serialVersionUID = 1L;

		@Override
		public void onSubmit() {

			cdList = new ArrayList<JukeboxCd>(
					cdDao.findByTitleInterpretAndUser("%" + searchNameStringTextfield.getValue() + "%",((SpringWicketAuthenticatedWebSession) AuthenticatedWebSession.get()).getUser()));
			
			System.out.println("hier: " + searchNameStringTextfield
					.getValue() + "length: " + cdList.size());

			cdDataTablePanel.setSelectedEntityList(cdList);
		}
	};

	private TextField<String> searchNameStringTextfield = new TextField<String>(
			"searchNameStringTextfield", new Model<String>());

	public JukeboxCdWebPage() {
		this(null) ;
	}
	
	public JukeboxCdWebPage(List<JukeboxCd> cdList) {
		
		userSearchForm.add(searchNameStringTextfield);
		userSearchForm.add(new Button("searchButton"));
		
		if (cdList == null)
			cdList = new ArrayList<JukeboxCd>(cdDao.findByUser(((SpringWicketAuthenticatedWebSession) AuthenticatedWebSession.get()).getUser()));

		entityPanel = new JukeboxCdEntityPanel("cdPanel", null);
		entityPanel.setVisible(false);

		//"cdTablePanel", cdList,"firstName", SortOrder.DESCENDING, entityPanel
		cdDataTablePanel = new JukeboxDataTablePanel<JukeboxCd>("cdTablePanel", cdList, "title", SortOrder.DESCENDING, entityPanel) {

			private static final long serialVersionUID = 1L;

			@Override
			public List<IColumn<JukeboxCd>> createColumns() {
				List<IColumn<JukeboxCd>> resultColumns = new ArrayList<IColumn<JukeboxCd>>();

				/*resultColumns.add(new PropertyColumn<JukeboxCd>(
						new Model<String>("Id"), "id"));*/

				/*------------------------------------------*/
				resultColumns.add(new PropertyColumn<JukeboxCd>(
						new Model<String>("Cover"), "cover") {

					private static final long serialVersionUID = 1L;

					@Override
					public void populateItem(
							Item<ICellPopulator<JukeboxCd>> item,
							String componentId, IModel<JukeboxCd> model) {
						final byte[] value = model.getObject().getCover();

						IResource resource = null ;
						
						if (value != null)
							resource = new DynamicImageResource() {

								private static final long serialVersionUID = 1L;

								@Override
								protected byte[] getImageData(
										Attributes attributes) {
									return value;
									
								}
							} ;
						else
							resource = new PackageResourceReference(JukeboxCdEntityPanel.class,
									"res/missingCover.png").getResource() ;
						
						item.add(new ImagePanel(componentId, resource));
					}

					@Override
					public String getCssClass() {
						
						return "tableUserPicture";
					}
				});
				/*------------------------------------------*/
				resultColumns.add(new PropertyColumn<JukeboxCd>(
						new Model<String>("Titel"), "title", "title"));

				resultColumns.add(new PropertyColumn<JukeboxCd>(
						new Model<String>("Interpret"), "interpret",
						"interpret"));

				resultColumns.add(new PropertyColumn<JukeboxCd>(
						new Model<String>("Jahr"), "year", "year"));

				resultColumns.add(new PropertyColumn<JukeboxCd>(
						new Model<String>("Genre"), "genre", "genre"));

				return resultColumns;
			}

			@Override
			public int compareEntities(JukeboxCd entity1, JukeboxCd entity2) {

				int dir = getSortParam().isAscending() ? 1 : -1;

				if ("title".equals(getSortParam().getProperty())) {
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
				}

				return super.compareEntities(entity1, entity2);
			}

		};
		
		add(cdDataTablePanel);
		
		add(userSearchForm);

		add(entityPanel);
		
		Form<ApplicationUser> cdTableForm = new Form<ApplicationUser>(
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
		};

		addButton.setDefaultFormProcessing(false);
		cdTableForm.add(addButton);

		add(cdTableForm);

	}
}
