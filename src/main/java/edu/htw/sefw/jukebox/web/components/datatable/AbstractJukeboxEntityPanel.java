package edu.htw.sefw.jukebox.web.components.datatable;

import org.apache.wicket.model.IModel;

import edu.htw.sefw.jukebox.domain.entity.base.AbstractEntityBase;
import edu.htw.sefw.jukebox.web.base.JukeboxBasePanel;

public abstract class AbstractJukeboxEntityPanel<T extends AbstractEntityBase> extends JukeboxBasePanel {

	private static final long serialVersionUID = 1L;

	protected T selectedEntity ;
	
	public abstract void onDeleteEntitiy(T t) ;
	public abstract void onSaveEntity(T t) ;
	public abstract void onCancel() ;
	
	public AbstractJukeboxEntityPanel(String id) {
		super(id);
	}

	public AbstractJukeboxEntityPanel(String id, IModel<?> model) {
		super(id, model);
	}

	public abstract void setSelectedEntity(final T t) ; 
	
	public T getSelectedEntity() {
		return selectedEntity;
	}
	
}
