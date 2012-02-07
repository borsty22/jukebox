package edu.htw.sefw.jukebox.web.components.datatable;

import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.request.resource.IResource;

import edu.htw.sefw.jukebox.web.base.JukeboxBasePanel;

public class ImagePanel extends JukeboxBasePanel {

	private static final long serialVersionUID = 1L;

	public ImagePanel(String id, IResource image) {
		super(id);
		add(new Image("image", image));
	}
}
