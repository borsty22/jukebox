package edu.htw.sefw.jukebox.web.components.menu;

import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.request.resource.PackageResourceReference;

import edu.htw.sefw.jukebox.web.base.JukeboxBasePage;
import edu.htw.sefw.jukebox.web.base.JukeboxBasePanel;
import edu.htw.sefw.jukebox.web.components.link.LinkLabel;

@SuppressWarnings("serial")
public class JukeboxMenuElement extends JukeboxBasePanel {

	public JukeboxMenuElement(String id, LinkLabel link) {
		super(id);
		
		add(new Image("corner_inset_left", new PackageResourceReference(
				JukeboxBasePage.class, "res/dot.gif")));
		add(new Image("corner_inset_right", new PackageResourceReference(
				JukeboxBasePage.class, "res/dot.gif")));
		
		add(new LinkLabel("link", link.getResponsePage(), link.getLabelText())) ;
	}

}
