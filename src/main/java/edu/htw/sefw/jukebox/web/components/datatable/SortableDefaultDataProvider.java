package edu.htw.sefw.jukebox.web.components.datatable;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import edu.htw.sefw.jukebox.domain.entity.base.AbstractEntityBase;

public class SortableDefaultDataProvider<T extends AbstractEntityBase> extends
		SortableDataProvider<T> {

	private static final long serialVersionUID = 1L;

	private List<T> entityList;

	public SortableDefaultDataProvider(List<T> entityList, String sortString,
			SortOrder sortOrder) {
		this.entityList = entityList;

		setSort(sortString, sortOrder);
	}

	public Iterator<? extends T> iterator(int first, int count) {
		Collections.sort(entityList, new Comparator<T>() {

			public int compare(T o1, T o2) {
				return compareEntites(o1, o2);
			}
		});

		return entityList.subList(first, first + count).iterator();
	}

	public int size() {
		return this.entityList.size();
	}

	public IModel<T> model(final T object) {
		return new LoadableDetachableModel<T>(object) {

			private static final long serialVersionUID = 1L;

			@Override
			protected T load() {
				return object;
			}
		};
	}

	public int compareEntites(T o1, T o2) {
		int dir = getSort().isAscending() ? 1 : -1;

		return dir * (o1.getId().compareTo(o2.getId())) ;
	}
}
