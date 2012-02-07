package edu.htw.sefw.jukebox.web.components.link;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.component.IRequestablePage;

import edu.htw.sefw.jukebox.HomePage;

@SuppressWarnings("serial")
public class LinkLabel extends Panel {

	private String labelText ;
	private Class<? extends IRequestablePage> responsePage ;
	
	public LinkLabel(String id,
			final Class<? extends IRequestablePage> responsePage,
			String labelText) {
		
		super(id);
		
		this.labelText = labelText ;
		this.responsePage = responsePage ;

		Link<String> link = new Link<String>("link") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				if (responsePage != null) {
					setResponsePage(responsePage);
				}
			}
		};

		link.add(new Label("linklabel", labelText));
		add(link);
	}

	public LinkLabel(String id) {
		super(id);

		Link<String> link = new Link<String>("link") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(HomePage.class);
			}
		};

		link.add(new Label("linklabel", "text"));

		add(link);
	}

	public String getLabelText() {
		return labelText;
	}

	public Class<? extends IRequestablePage> getResponsePage() {
		return responsePage;
	}
}
