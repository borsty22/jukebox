package edu.htw.sefw.jukebox.web.components.upload.fileupload;

import java.io.File;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.util.lang.Bytes;

import edu.htw.sefw.jukebox.web.base.JukeboxBasePanel;
import edu.htw.sefw.jukebox.web.resources.JukeboxResources;

@SuppressWarnings("serial")
public class FileUploadPanel extends JukeboxBasePanel {
	
	private String upload_folder = JukeboxResources.music_full_path;

	protected String getUpload_folder() {
		return upload_folder;
	}

	protected void setUpload_folder(String upload_folder) {
		this.upload_folder = upload_folder;
	}

	PackageResourceReference defaultImageResourceReference  ;
	
	Image uploadedImage ;

	private FileUploadField fileUpload;
	private Button uploadButton = new Button("uploadButton");

	public FileUploadPanel(String id, IModel<byte[]> imageModel, PackageResourceReference defaultImageResourceReference) {
		super(id, imageModel);

		this.defaultImageResourceReference = defaultImageResourceReference ;

		uploadedImage = new Image("uploadedImage",
				new Model<PackageResourceReference>(this.defaultImageResourceReference));
		add(uploadedImage);

		Form<byte[]> form = new Form<byte[]>("uploadForm") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {

				final FileUpload uploadedFile = fileUpload.getFileUpload();

				if (uploadedFile != null) {
					handleUploadedFile(uploadedFile);
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
	
	public FileUploadPanel(String id, IModel<byte[]> imageModel) {
		this(id, imageModel, new PackageResourceReference(
					FileUploadPanel.class, "res/noImage.jpg"));
	}

	@Override
	public MarkupContainer setDefaultModel(final IModel<?> model) {

		if (model.getObject() == null) {
			uploadedImage.setDefaultModel(new Model<PackageResourceReference>(this.defaultImageResourceReference)) ;
		} else {
			uploadedImage.setDefaultModel(new Model<DynamicImageResource>(
					new DynamicImageResource() {

						private static final long serialVersionUID = 1L;

						@Override
						protected byte[] getImageData(Attributes attributes) {
							return ((byte[]) model.getObject());
						}
					}));
		}

		return super.setDefaultModel(model);
	}

	public void handleUploadedFile(FileUpload uploadedFile) {
		// write to a new file
		File newFile = new File(upload_folder
				+ uploadedFile.getClientFileName());
		if (newFile.exists()) {
			newFile.delete();
		}

		try {
			newFile.createNewFile();
			uploadedFile.writeTo(newFile);
			
			info("saved file: " + uploadedFile.getClientFileName());
			System.out.println("saved file: " + upload_folder
					+ uploadedFile.getClientFileName());
		} catch (Exception e) {
			throw new IllegalStateException("Error");
		}
	}
}
