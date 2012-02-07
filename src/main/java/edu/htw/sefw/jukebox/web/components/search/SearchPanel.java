package edu.htw.sefw.jukebox.web.components.search;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.odlabs.wiquery.ui.button.ButtonBehavior;

import edu.htw.sefw.jukebox.web.components.dialog.DialogPanel;

public class SearchPanel extends Panel{
	private static final long serialVersionUID = 1L;
	
	private TextField<String> searchTextField;
	private DialogPanel dialogPanel;

	public SearchPanel(String id,String buttonLabelText) {
		super(id);
		
		Label buttonLabel = new Label("buttonLabel",buttonLabelText);
		
		Form<String> buttonForm = new Form<String>("buttonForm");	    	    
	    
	    searchTextField = new TextField<String>("searcTextField",new Model<String>());
	    
	    buttonForm.add(searchTextField);
	    	    
	    AjaxButton ajaxButton = new AjaxButton("button", buttonForm) {
			private static final long serialVersionUID = 1L;

			/**
			 * {@inheritDoc}
			 * @see org.apache.wicket.ajax.markup.html.form.AjaxButton#onSubmit(org.apache.wicket.ajax.AjaxRequestTarget, org.apache.wicket.markup.html.form.Form)
			 */
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				
				String searchString = searchTextField.getModelObject();
				
				if(searchString == null) {
					
					dialogPanel.open(target);
					
				} else {
					
					target.appendJavaScript("alert('Suche in eigenem Inventar nach: " + searchString + "');");
					
				}
				
			}

			@Override
			protected void onError(AjaxRequestTarget arg0, Form<?> arg1) {				
				
			}
		};
				
		
		ajaxButton.add(new ButtonBehavior());
		ajaxButton.add(buttonLabel);
		buttonForm.add(ajaxButton);	
		
		
		add(buttonForm);
		
		dialogPanel = new DialogPanel("dialog","Hinweis","Bitte geben Sie einen zu suchenden text ein.");
		add(dialogPanel);
		
	}

}
