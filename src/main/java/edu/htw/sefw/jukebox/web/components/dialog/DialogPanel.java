package edu.htw.sefw.jukebox.web.components.dialog;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.core.options.ListItemOptions;
import org.odlabs.wiquery.ui.core.JsScopeUiEvent;
import org.odlabs.wiquery.ui.dialog.Dialog;
import org.odlabs.wiquery.ui.dialog.DialogButton;

public class DialogPanel extends Panel{
	private static final long serialVersionUID = 1L;
	
	private Dialog dialog;

	public DialogPanel(String id,String title,String text) {
		super(id);
		
		Label dialogText = new Label("dialogText",text);
		
		//Hinweis Dialog
        dialog = new Dialog("dialog"); 
        dialog.setTitle(title);
        dialog.setOpenEvent(JsScopeUiEvent.quickScope(new JsStatement().
                self().chain("parents", "'.ui-dialog:first'").
                chain("find", "'.ui-dialog-titlebar-close'").
                chain("hide").render()));
        
        ListItemOptions<DialogButton> buttonsWarningDialog = new ListItemOptions<DialogButton>();
        
        buttonsWarningDialog.add(new DialogButton("Ok", 
        				JsScope.quickScope(dialog.close().render())));                      
        dialog.setButtons(buttonsWarningDialog);  
       
        dialog.add(dialogText);
        
        add(dialog);
        
	}
	
	public void open(AjaxRequestTarget target){
		dialog.open(target);
	}

}
