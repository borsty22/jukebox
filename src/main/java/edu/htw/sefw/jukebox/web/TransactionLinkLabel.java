package edu.htw.sefw.jukebox.web;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.joda.time.DateTime;

public class TransactionLinkLabel extends Label {

	private static final long serialVersionUID = 1L;

	public TransactionLinkLabel(String id) {
		super(id, new StringModel("bla"));
	}

	private static class StringModel extends AbstractReadOnlyModel<String> {

		private static final long serialVersionUID = 1L;

		private final String string;

		public StringModel(String string) {
			this.string = string;
		}

		@Override
		public String getObject() {
			return string + " " + new DateTime() ;
		}
	}

}
