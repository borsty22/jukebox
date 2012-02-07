package edu.htw.sefw.jukebox.web.components.menu;

import java.util.List;

import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;

import edu.htw.sefw.jukebox.web.base.JukeboxBasePanel;
import edu.htw.sefw.jukebox.web.components.link.LinkLabel;

@SuppressWarnings("serial")
public class JukeboxMenuTab extends JukeboxBasePanel {

	public JukeboxMenuTab(String id, LinkLabel tabLink, List<LinkLabel> linkList) {
		super(id);
		
		RepeatingView tabRepeater = new RepeatingView("tabRepeater", new Model<LinkLabel>()) ;
		
		if (linkList != null) { 
			int menuListCounter = 0 ;
			for (LinkLabel link : linkList)
				tabRepeater.add(new JukeboxMenuElement(String.valueOf(menuListCounter++), new LinkLabel(String.valueOf(menuListCounter++), link.getResponsePage(), link.getLabelText()))) ;
		}
		
		add(tabRepeater) ;
		
		add(new LinkLabel("tabLink", tabLink.getResponsePage(), tabLink.getLabelText())) ;
	}
}
