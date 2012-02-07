package edu.htw.sefw.jukebox.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.string.Strings;

import edu.htw.sefw.jukebox.domain.dao.JukeboxCdDao;
import edu.htw.sefw.jukebox.domain.entity.JukeboxCd;
import edu.htw.sefw.jukebox.web.base.JukeboxBasePage;

@SuppressWarnings("serial")
public class CdSearchTest extends JukeboxBasePage {
	JukeboxCdDao cdDao;
	
	/**
	 * Constructor
	 */
	public CdSearchTest() {
		Form<Void> form = new Form<Void>("form");
		add(form);

		final AutoCompleteTextField<String> field = new AutoCompleteTextField<String>(
				"ac", new Model<String>("")) {
			@Override
			protected Iterator<String> getChoices(String input) {
				if (Strings.isEmpty(input)) {
					List<String> emptyList = Collections.emptyList();
					return emptyList.iterator();
				}
				List<String> choices = new ArrayList<String>(10);
				
				//Laden der Daten
				ArrayList<JukeboxCd> cds = cdDao.findAll();
				
				if (cds == null) {
					cds = new ArrayList<JukeboxCd>();
				}
				
				//Anzeige der Daten
				for (final JukeboxCd cd : cds) {
					final String titel = cd.getTitle();

					if (titel.toUpperCase().startsWith(input.toUpperCase())) {
						choices.add(titel);
						if (choices.size() == 10) {
							break;
						}
					}
				}

				return choices.iterator();
			}
		};
		form.add(field);

		final Label label = new Label("selectedValue", field.getDefaultModel());
		label.setOutputMarkupId(true);
		form.add(label);

		field.add(new AjaxFormSubmitBehavior(form, "onchange") {
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				target.addComponent(label);
			}

			@Override
			protected void onError(AjaxRequestTarget target) {
			}
		});
	}
}