package edu.htw.sefw.jukebox.web.pages.administration;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
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

import edu.htw.sefw.jukebox.domain.dao.ApplicationUserDao;
import edu.htw.sefw.jukebox.domain.entity.ApplicationUser;
import edu.htw.sefw.jukebox.web.base.JukeboxBasePage;
import edu.htw.sefw.jukebox.web.components.datatable.ImagePanel;
import edu.htw.sefw.jukebox.web.components.datatable.JukeboxDataTablePanel;

@AuthorizeInstantiation("ROLE_ADMIN")
public class UserAdministrationWebPage extends JukeboxBasePage {

	private static final long serialVersionUID = 1L;

	@SpringBean
	ApplicationUserDao applicationUserDao;

	private WebMarkupContainer dataTableContainer = new WebMarkupContainer("dataTableContainer") ;

	/*
	 * table to display Users
	 */
	private JukeboxDataTablePanel<ApplicationUser> userDataTablePanel;
	
	/*
	 * ApplicationUserEntityPanel
	 */
	final ApplicationUserEntityPanel entityPanel = new ApplicationUserEntityPanel(
			"userPanel", null, dataTableContainer);


	private List<ApplicationUser> userList;

	private Form<ApplicationUser> userTableForm = new Form<ApplicationUser>(
			"userTableForm", null) {
		private static final long serialVersionUID = 1L;

		@Override
		protected void onSubmit() {
			userTableForm.setVisible(false) ;
			entityPanel.setVisible(true);
		}
	};

	private Button addButton = new Button("addButton") {

		private static final long serialVersionUID = 1L;

		@Override
		public void onSubmit() {
			entityPanel.setSelectedEntity(new ApplicationUser());
			entityPanel.setVisible(true);
			userTableForm.setVisible(false) ;
		}
	};

	private Form<String> userSearchForm = new Form<String>("userSearchForm",
			new Model<String>()) {
		private static final long serialVersionUID = 1L;

		@Override
		public void onSubmit() {

			userList = new ArrayList<ApplicationUser>(
					applicationUserDao
							.findByNameString(searchNameStringTextfield
									.getValue() + "%"));

			userDataTablePanel.setSelectedEntityList(userList);
		}
	};

	private TextField<String> searchNameStringTextfield = new TextField<String>(
			"searchNameStringTextfield", new Model<String>());

	
	public UserAdministrationWebPage() {

		userList = new ArrayList<ApplicationUser>(
				applicationUserDao.findByNameString(searchNameStringTextfield
						.getValue() + "%"));

		userSearchForm.add(searchNameStringTextfield);
		userSearchForm.add(new Button("searchButton"));

		dataTableContainer.add(userSearchForm);
		
		userDataTablePanel = new JukeboxDataTablePanel<ApplicationUser>(
				"userTablePanel", userList, "firstName", SortOrder.DESCENDING,
				entityPanel) {

			private static final long serialVersionUID = 1L;

			@Override
			public List<IColumn<ApplicationUser>> createColumns() {
				List<IColumn<ApplicationUser>> resultColumns = new ArrayList<IColumn<ApplicationUser>>();

				/*resultColumns.add(new PropertyColumn<ApplicationUser>(
						new Model<String>("Id"), "id"));*/

				resultColumns.add(new PropertyColumn<ApplicationUser>(
						new Model<String>("Profilbild"), "cover") {

					private static final long serialVersionUID = 1L;

					@Override
					public void populateItem(
							Item<ICellPopulator<ApplicationUser>> item,
							String componentId, IModel<ApplicationUser> model) {
						final byte[] value = model.getObject().getUserPicture();

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
							resource = new PackageResourceReference(ApplicationUserEntityPanel.class,
									"res/missingUser.png").getResource() ;
						
						item.add(new ImagePanel(componentId, resource));
					}

					@Override
					public String getCssClass() {
						
						return "tableUserPicture";
					}
				});

				resultColumns.add(new PropertyColumn<ApplicationUser>(
						new Model<String>("Vorname"), "firstName",
						"firstName"));
				resultColumns
						.add(new PropertyColumn<ApplicationUser>(
								new Model<String>("Nachname"), "lastName",
								"lastName"));

				return resultColumns;
			}

			@Override
			public int compareEntities(ApplicationUser entity1,
					ApplicationUser entity2) {

				int dir = getSortParam().isAscending() ? 1 : -1;

				if ("lastName".equals(getSortParam().getProperty())) {
					return dir
							* (entity1.getLastName().compareTo(entity2
									.getLastName()));
				} else if ("firstName".equals(getSortParam().getProperty())) {
					return dir
							* (entity1.getFirstName().compareTo(entity2
									.getFirstName()));
				}

				return super.compareEntities(entity1, entity2);
			}

			@Override
			public void onSelect() {
				super.onSelect();
				dataTableContainer.setVisible(false) ;
			}
		};
		
		dataTableContainer.add(userDataTablePanel);

		addButton.setDefaultFormProcessing(false);
		userTableForm.add(addButton);
		dataTableContainer.add(userTableForm);

		add(dataTableContainer) ;

		entityPanel.setMarkupId("entityPanel");
		entityPanel.setVisible(false);
		
		add(entityPanel);
	}
}
