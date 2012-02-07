package edu.htw.sefw.jukebox.web.components.cdview;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.request.resource.PackageResourceReference;

import edu.htw.sefw.jukebox.domain.entity.JukeboxCd;
import edu.htw.sefw.jukebox.web.pages.library.JukeboxCdEntityPanel;

@SuppressWarnings("serial")
public class CdListView extends ListView<JukeboxCd> {

	public CdListView(String id, List<? extends JukeboxCd> list) {
		super(id, list);
		
	}

	private static final long serialVersionUID = 1L;

	
	
	@Override
	protected void populateItem(final ListItem<JukeboxCd> item) {
		if(item != null){	
			//default cover setzen wenn keins angegeben
			if(item.getModelObject().getCover() != null){
				item.add(  new Image("cover",
		                    new DynamicImageResource() {
								@Override
								protected byte[] getImageData(Attributes attributes) {								
									return item.getModelObject().getCover();
								}
		                    }
		                ));		
			} else {
				item.add( new Image("cover",new PackageResourceReference(JukeboxCdEntityPanel.class,
					"res/missingCoverSmall.png")));
			}
			
			//zu langen text abschneiden und durch "..." kennzeichnen
			if(item.getModelObject().getTitle() != null && item.getModelObject().getTitle().length() > 16){				
				item.add(new Label("title",item.getModelObject().getTitle().substring(0,13) + "..."));
			} else {			
				item.add(new Label("title",item.getModelObject().getTitle()));
			}
			
			//zu langen text abschneiden und durch "..." kennzeichnen
			if(item.getModelObject().getInterpret() != null && item.getModelObject().getInterpret().length()>16){
				item.add(new Label("interpret",item.getModelObject().getInterpret().substring(0,13) + "..."));
			} else {
				item.add(new Label("interpret",item.getModelObject().getInterpret()));
			}
			
			
			item.add(new AjaxLink<String>("playMusic") {

	  			private static final long serialVersionUID = 1L;	  			

				@Override
				public void onClick(AjaxRequestTarget target) {		
	  				playMusic(target,getParent().getDefaultModelObject());					
				}								
				
	  		});
			item.add(new AjaxLink<String>("showInfo") {

	  			private static final long serialVersionUID = 1L;	  			

				@Override
				public void onClick(AjaxRequestTarget target) {	  				
					showInfo(target,getParent().getDefaultModelObject());					
				}
	  		});
			
			item.setOutputMarkupId(true);
		}
	}
	
	public void playMusic(AjaxRequestTarget target,Object defModelObj){
		
	}
	
	public void showInfo(AjaxRequestTarget target,final Object object){
				
	}

	

}
