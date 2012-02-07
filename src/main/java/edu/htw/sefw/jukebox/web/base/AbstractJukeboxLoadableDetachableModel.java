package edu.htw.sefw.jukebox.web.base;

import org.apache.wicket.model.LoadableDetachableModel;
import org.springframework.data.jpa.repository.JpaRepository;

import edu.htw.sefw.jukebox.domain.entity.base.AbstractEntityBase;

public abstract class AbstractJukeboxLoadableDetachableModel<T extends AbstractEntityBase> extends
		LoadableDetachableModel<T> {

	private static final long serialVersionUID = 1L;
	
	JpaRepository<T, Long> dao ;
	
	public abstract void setDao(JpaRepository<T, Long> dao) ;

}
