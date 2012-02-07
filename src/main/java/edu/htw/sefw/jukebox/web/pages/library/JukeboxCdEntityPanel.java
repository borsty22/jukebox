package edu.htw.sefw.jukebox.web.pages.library;

import java.util.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.html5.media.MediaSource;
import org.wicketstuff.html5.media.audio.Html5Audio;
import edu.htw.sefw.jukebox.core.SpringWicketAuthenticatedWebSession;
import edu.htw.sefw.jukebox.domain.dao.JukeboxCdDao;
import edu.htw.sefw.jukebox.domain.entity.JukeboxCd;
import edu.htw.sefw.jukebox.web.components.datatable.AbstractJukeboxEntityPanel;
import edu.htw.sefw.jukebox.web.components.upload.audioUpload.AudioUploadPanel;
import edu.htw.sefw.jukebox.web.components.upload.fileupload.FileUploadPanel;
import edu.htw.sefw.jukebox.web.resources.JukeboxResources;

public class JukeboxCdEntityPanel extends AbstractJukeboxEntityPanel<JukeboxCd> {

	private static final long serialVersionUID = 1L;
	private 

	Form<JukeboxCd> cdEntityForm;
	
	private TextField<String> titleTextField = new TextField<String>(
			"titleTextField", new PropertyModel<String>(this, "title"));
	private TextField<String> interpretTextField = new TextField<String>(
			"interpretTextField", new PropertyModel<String>(this, "interpret"));
	private TextField<String> genreTextField = new TextField<String>(
			"genreTextField", new PropertyModel<String>(this, "genre"));
	private TextField<Integer> yearTextField = new TextField<Integer>(
			"yearTextField", new PropertyModel<Integer>(this, "year"));
	private TextField<String> descriptionTextField = new TextField<String>(
			"descriptionTextField", new PropertyModel<String>(this, "description"));
	private List<MediaSource> music = new ArrayList<MediaSource>();
	
	//Laden der Musik
    IModel<List<MediaSource>> mediaSourceList = new AbstractReadOnlyModel<List<MediaSource>>() {
		private static final long serialVersionUID = 184656142287823787L;

		public List<MediaSource> getObject() {
				music.clear();
				music.add(new MediaSource(selectedEntity.getAudio_mp3l()));
            return music;
        }
    };

	
	//Bilder hochladen
	private FileUploadPanel coverUpload = new FileUploadPanel(
			"coverUpload", new PropertyModel<byte[]>(this, "cover"),
			new PackageResourceReference(JukeboxCdEntityPanel.class,
					"res/missingCover.png")) {

		private static final long serialVersionUID = 1L;

		@Override
		public void handleUploadedFile(FileUpload uploadedFile) {
			selectedEntity.setCover((uploadedFile.getBytes()));			
			super.setDefaultModel(new PropertyModel<byte[]>(selectedEntity, "cover")) ;
 		}
	};
	
    //Audio Komponente
    private Html5Audio audio = new Html5Audio("audio", mediaSourceList) {
		private static final long serialVersionUID = 1L;
		@Override
        protected boolean isControls() {
            return true;
        }
    };
	
	//Musik hochladen
	private AudioUploadPanel musicUpload = new AudioUploadPanel(
			"musicUpload") {

		private static final long serialVersionUID = 1L;
		
		@Override
		public void handleUploadedFile(FileUpload uploadedFile, String serverFileName) {
			//neuer Name
			String name;
			//name = selectedEntity.getUser().getId()+"-"+selectedEntity.getId().toString();
			Date dt = new Date();
			int salt = (int) (Math.random() * (99999 - 10000)+10000);
			int salt2 = (int) (Math.random() * (999999 - 100000)+100000);
			name = salt2+"-"+dt.getTime()+"-"+salt;
			System.out.println("name: "+name);
			//endung filtern
			try {
				int point = uploadedFile.getClientFileName().lastIndexOf('.');
				name = name + uploadedFile.getClientFileName().substring(point, uploadedFile.getClientFileName().length());
				String suffix = uploadedFile.getClientFileName().substring(point+1, uploadedFile.getClientFileName().length());
				System.out.println("suffix: "+suffix);
				//Nur ogg Dateien erlauben
				if(suffix.toLowerCase().compareTo("ogg")!=0) {
					//Fehlermeldung
					super.
					info("\nEs dürfen nur Audiodateien im OGG Format hochgeladen werden.");
				}
				else
				{
					//Datein hochladen
					System.out.println("new file name: "+name);
					System.out.println("suffix: "+suffix);		
					//selectedEntity.setAudio_mp3l(JukeboxResources.music_path+uploadedFile.getClientFileName());
					selectedEntity.setAudio_mp3l(JukeboxResources.music_path+name);
					super.setServerFileName(name);
					try {
						super.handleUploadedFile(uploadedFile, name);
					}
					catch(Exception e) {
						info("Datei konnte nicht hochgeladen werden, Konflikt beim Upload.");
					}
	
					//cdDao.save(selectedEntity);
				}
			}
			catch(Exception e) {
				info("Es dürfen nur OGG Dateien mit der Endung ogg/OGG und kleiner als 15 MB hochgeladen werden.");
			}
 		}
	};
	
	//Panel mit Buttons
	FeedbackPanel cdEntityFormFeedbackPanel = new FeedbackPanel("feedback");
	
	@SpringBean
	JukeboxCdDao cdDao;

	private Button saveButton = new Button("saveButton") {

		private static final long serialVersionUID = 1L;

		@Override
		public void onSubmit() {
			super.onSubmit();
			onSaveEntity(selectedEntity) ;
		}
	};

	private Button deleteButton = new Button("deleteButton") {

		private static final long serialVersionUID = 1L;

		@Override
		public void onSubmit() {
			super.onSubmit();
			onDeleteEntitiy(selectedEntity) ;
		}
	};
	private Button cancelButton = new Button("cancelButton") {

		private static final long serialVersionUID = 1L;

		@Override
		public void onError() {
			onCancel() ;
		}
	};
	
	@Override
	public void onDeleteEntitiy(JukeboxCd t) {
		cdDao.delete(selectedEntity);
	}

	@Override
	public void onSaveEntity(JukeboxCd t) {
		SpringWicketAuthenticatedWebSession session = (SpringWicketAuthenticatedWebSession) getSession();
		
		selectedEntity.setUser(session.getUser()) ;
		
		cdDao.save(selectedEntity);
	}

	@Override
	public void onCancel() {
		this.getParent().setVisible(false);

		setResponsePage(JukeboxCdWebPage.class);
	}

	//Konstruktor
	public JukeboxCdEntityPanel(String id, JukeboxCd cd) {
		super(id);
		
		cdEntityForm = new Form<JukeboxCd>("cdEntityForm",
				new LoadableDetachableModel<JukeboxCd>(cd) {

					private static final long serialVersionUID = 1L;

					@Override
					protected JukeboxCd load() {
						return cdDao.findById(selectedEntity.getId());
					}
				}) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				super.onSubmit();
				this.getParent().setVisible(false);

				setResponsePage(JukeboxCdWebPage.class);
			}
		};
		
		cdEntityForm.add(titleTextField) ;
		cdEntityForm.add(interpretTextField) ;
		cdEntityForm.add(genreTextField) ;
		cdEntityForm.add(yearTextField) ;
		cdEntityForm.add(descriptionTextField) ;

		//cdEntityForm.add(audio) ;

		
		cdEntityForm.add(saveButton);
		cdEntityForm.add(cancelButton);
		cdEntityForm.add(deleteButton);
		
		/*add(saveButton);
		add(cancelButton);
		add(deleteButton);*/

		add(cdEntityFormFeedbackPanel) ;
		add(coverUpload);
		add(musicUpload);
		add(cdEntityForm) ;
		add(audio);
		
	}

	@Override
	public void setSelectedEntity(final JukeboxCd selectedEntity) {
		this.selectedEntity = selectedEntity ;
		
		titleTextField.setDefaultModel(new PropertyModel<String>(
				selectedEntity, "title"));
		interpretTextField.setDefaultModel(new PropertyModel<String>(
				selectedEntity, "interpret"));
		genreTextField.setDefaultModel(new PropertyModel<String>(
				selectedEntity, "genre"));

		yearTextField.setDefaultModel(new PropertyModel<String>(
				selectedEntity, "year"));
		
		descriptionTextField.setDefaultModel(new PropertyModel<String>(
				selectedEntity, "description"));
		

		coverUpload.setDefaultModel(new PropertyModel<byte[]>(
				selectedEntity, "cover"));
		
		this.setDefaultModel(new LoadableDetachableModel<JukeboxCd>(
				selectedEntity) {

			private static final long serialVersionUID = 1L;

			@Override
			protected JukeboxCd load() {
				return cdDao.findById(selectedEntity.getId());
			}
		});
	}
}
