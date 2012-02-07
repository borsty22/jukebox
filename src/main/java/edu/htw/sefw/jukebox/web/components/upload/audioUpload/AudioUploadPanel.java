package edu.htw.sefw.jukebox.web.components.upload.audioUpload;

import java.io.File;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.IResource.Attributes;
import org.apache.wicket.util.lang.Bytes;
import edu.htw.sefw.jukebox.web.base.JukeboxBasePanel;
import edu.htw.sefw.jukebox.web.resources.JukeboxResources;

public class AudioUploadPanel extends JukeboxBasePanel {

	private static final long serialVersionUID = 1L;
	
	private String upload_folder = JukeboxResources.music_full_path;

	private FileUploadField fileUpload;
	private Button uploadButton = new Button("uploadButton");
	private String serverFileName = "";
	
	
	//neue rname für die datei
	protected String getServerFileName() {
		return serverFileName;
	}

	protected void setServerFileName(String serverFileName) {
		this.serverFileName = serverFileName;
	}

	public AudioUploadPanel(String id) {
		//super(id, imageModel);
		super(id);
		Form<byte[]> form = new Form<byte[]>("uploadForm") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {

				final FileUpload uploadedFile = fileUpload.getFileUpload();

				if (uploadedFile != null) {
					handleUploadedFile(uploadedFile, serverFileName);
				}
			}
		};

		// Enable multipart mode (need for uploads file)
		form.setMultiPart(true);

		form.setMaxSize(Bytes.megabytes(15));

		form.add(fileUpload = new FileUploadField("fileUpload"));
		form.add(uploadButton);

		add(form);

	}
	
	
	/*
	 * Neuer Name für den Upload auf den Server
	 */
	public void handleUploadedFile(FileUpload uploadedFile, String serverFileName) {
		File newFile = new File(upload_folder
				+ serverFileName);
		if (newFile.exists()) {
			newFile.delete();
		}
		System.out.println("upload path: "+JukeboxResources.music_full_path);
		try {
			newFile.createNewFile();
			uploadedFile.writeTo(newFile);

			info("saved file: " + newFile.getName());
			System.out.println("saved file: " + upload_folder
					+ newFile.getName());
		} catch (Exception e) {
			throw new IllegalStateException("Error");
		}
	}
}
