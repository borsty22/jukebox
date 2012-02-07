package edu.htw.sefw.jukebox.web.components.menu;

import java.util.List;

import org.apache.wicket.markup.repeater.RepeatingView;

import edu.htw.sefw.jukebox.web.base.JukeboxBasePanel;

@SuppressWarnings("serial")
public class JukeboxMenu extends JukeboxBasePanel {

	public JukeboxMenu(String id, List<JukeboxMenuTab> menuTabList) {
		super(id);
		RepeatingView tabList = new RepeatingView("tabRepeater") ;
		
		for (JukeboxMenuTab tab : menuTabList) {
			if (tab != null)			
				tabList.add(tab) ;
		}
		
		add(tabList) ;
	}

}
