package edu.htw.sefw.jukebox.web.pages.library;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import edu.htw.sefw.jukebox.core.SpringWicketAuthenticatedWebSession;
import edu.htw.sefw.jukebox.domain.dao.JukeboxCdDao;
import edu.htw.sefw.jukebox.domain.entity.JukeboxCd;
import edu.htw.sefw.jukebox.web.base.JukeboxBasePage;
import edu.htw.sefw.jukebox.web.components.cdview.CdListViewPanel;

public class JukeboxAllCdPage extends JukeboxBasePage {	
	private static final long serialVersionUID = 1L;
	
	private TextField goForSearchInvInput;
	private LinkedList inventoryList;
	private CdListViewPanel allCdPanel;
	
	@SpringBean
	JukeboxCdDao cdDao;

	public JukeboxAllCdPage(){
		
		LinkedList<JukeboxCd> allCdList = new LinkedList<JukeboxCd>(cdDao.findAll());
		
		allCdPanel = new CdListViewPanel("allCdPanel", allCdList, "", false,null);		
		
		//Suche eigenes Inventar
	    Form searchFormInv = new Form("searchFormInv"){
	    	@Override
			public final void onSubmit() {
	    		
	    		inventoryList = new LinkedList<JukeboxCd>(cdDao.findByTitleInterpretGenreDesc("%" + goForSearchInvInput.getValue() + "%"));    			    		
	    		
	    		allCdPanel.setLinkedList(inventoryList);					
				
			}
	    };
		Button goForSearchInvButton = new Button("goForSearchInvButton");	    
		goForSearchInvInput = new TextField<String>("goForSearchInvInput",new Model<String>()); 
		searchFormInv.add(goForSearchInvButton);
		searchFormInv.add(goForSearchInvInput);
		add(searchFormInv);
		
		
		add(allCdPanel);
		
	}
	

}
