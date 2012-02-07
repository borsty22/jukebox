package edu.htw.sefw.jukebox.web.components.datatable;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import edu.htw.sefw.jukebox.domain.entity.base.AbstractEntityBase;
import edu.htw.sefw.jukebox.web.base.JukeboxBasePanel;

/*
 * - durch Typisierung für alle Datentypen anwendbar
 * - neben Standardspalten können weitere Spalten hinzugefügt werden
 * - Verknüpfung mit EntityPanel ist möglich, um Datensatz editieren zu können
 * - Auswahl Link ist wieder zu eigene Komponente
 */

@SuppressWarnings("serial")
public class JukeboxDataTablePanel<T extends AbstractEntityBase> extends
		JukeboxBasePanel {

	List<IColumn<T>> columns = new ArrayList<IColumn<T>>();

	AbstractJukeboxEntityPanel<T> entityPanel;
	
	JukeboxBasePanel thisPanel ;

	private SortOrder sortOrder ;
	
	private String sortString ;
	
	private SortParam sortParam ;
	
	SortableDefaultDataProvider<T> dataProvider;
	
	public SortParam getSortParam() {
		return sortParam;
	}

	public JukeboxDataTablePanel(String id, List<T> entityList,
			String sortString, SortOrder sortOrder,
			AbstractJukeboxEntityPanel<T> entityPanel) {
		
		super(id, new Model<ArrayList<T>>(new ArrayList<T>(entityList)));
		
		this.entityPanel = entityPanel;

		this.sortOrder = sortOrder ;
		this.sortString = sortString ;
		
		columns.addAll(createColumns());

		columns.add(new AbstractColumn<T>(new Model<String>("")) {

			private static final long serialVersionUID = 1L;

			public void populateItem(Item<ICellPopulator<T>> cellItem,
					String componentId, IModel<T> rowModel) {

				cellItem.add(new DefaultEntityActionPanel(componentId, rowModel));
				cellItem.setDefaultModel(rowModel);
			}
		});

		dataProvider = new SortableDefaultDataProvider<T>(entityList, sortString,
				sortOrder) {

			private static final long serialVersionUID = 1L;

			@Override
			public int compareEntites(T o1, T o2) {
				sortParam = getSort() ;
				
				return compareEntities(o1, o2) ;
			}
		} ;
		
		add(new DefaultDataTable<T>("table", columns,
				dataProvider, 8));
		
		thisPanel = this ;
	}
	
	public List<IColumn<T>> createColumns() {

		List<IColumn<T>> resultColumns = new ArrayList<IColumn<T>>();

		resultColumns.add(new PropertyColumn<T>(new Model<String>("Id"), "id"));

		return resultColumns;
	}

	public void setSelectedEntityList(List<T> entityList) {
		dataProvider = new SortableDefaultDataProvider<T>(entityList, sortString,
				sortOrder) {

			private static final long serialVersionUID = 1L;

			@Override
			public int compareEntites(T o1, T o2) {
				sortParam = getSort() ;
				
				return compareEntities(o1, o2) ;
			}
		} ;
		
		replace(new DefaultDataTable<T>("table", columns,
				dataProvider, 8));
	}
	
	public int compareEntities(T entity1, T entity2) {
		
		int dir = getSortParam().isAscending() ? 1 : -1 ;
		
		return dir * (entity1.getId().compareTo(entity2.getId()));
	}
	
	private class DefaultEntityActionPanel extends Panel {

		private static final long serialVersionUID = 1L;

		public DefaultEntityActionPanel(String id, IModel<T> model) {
			super(id, model);

			add(new Link<String>("select") {

				private static final long serialVersionUID = 1L;

				@SuppressWarnings("unchecked")
				@Override
				public void onClick() {
					
					entityPanel.setSelectedEntity((T) getParent()
							.getDefaultModelObject());
					entityPanel.setVisible(true);
					onSelect() ;
				}
			});
		}
	}
	
	public void onSelect() {
		
	};
}
