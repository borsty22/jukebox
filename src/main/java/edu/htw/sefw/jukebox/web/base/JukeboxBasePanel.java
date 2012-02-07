package edu.htw.sefw.jukebox.web.base;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JukeboxBasePanel extends Panel {

	public JukeboxBasePanel(String id, IModel<?> model) {
		super(id, model);
	}

	public JukeboxBasePanel(String id) {
		this(id, null) ;
	}

	private static final long serialVersionUID = 1L;

	protected final Logger logger = LoggerFactory.getLogger(getClass());
}
