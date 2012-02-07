package edu.htw.sefw.jukebox.web.pages.administration;

import java.util.Arrays;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.jasypt.digest.PooledStringDigester;

import edu.htw.sefw.jukebox.HomePage;
import edu.htw.sefw.jukebox.core.SpringWicketAuthenticatedWebSession;
import edu.htw.sefw.jukebox.domain.dao.ApplicationUserDao;
import edu.htw.sefw.jukebox.domain.entity.ApplicationUser;
import edu.htw.sefw.jukebox.domain.entity.ApplicationUser.GenderType;
import edu.htw.sefw.jukebox.domain.entity.ApplicationUser.UserType;
import edu.htw.sefw.jukebox.web.components.datatable.AbstractJukeboxEntityPanel;
import edu.htw.sefw.jukebox.web.components.upload.fileupload.FileUploadPanel;
import edu.htw.sefw.jukebox.web.pages.library.JukeboxCdWebPage;

public class ApplicationUserEntityPanel extends
		AbstractJukeboxEntityPanel<ApplicationUser> {

	private static final long serialVersionUID = 1L;

	@SpringBean
	private ApplicationUserDao userDao;

	@SpringBean
	PooledStringDigester stringDigester;

	Form<ApplicationUser> userEntityForm;

	private TextField<String> lastNameTextField = new TextField<String>(
			"lastNameTextfield", new PropertyModel<String>(this, "lastName"));
	private TextField<String> firstNameTextField = new TextField<String>(
			"firstNameTextfield", new PropertyModel<String>(this, "firstName"));
	private TextField<String> userNameTextField = new TextField<String>(
			"userNameTextfield", new PropertyModel<String>(this, "userName"));
	private TextField<String> emailTextField = new TextField<String>(
			"emailTextfield", new PropertyModel<String>(this, "email"));

	private DropDownChoice<GenderType> genderDropDownChoice = new DropDownChoice<GenderType>(
			"genderDropDownChoice", new PropertyModel<GenderType>(this,
					"gender"), Arrays.asList(GenderType.values()));

	private DropDownChoice<UserType> userTypeDropDownChoice = new DropDownChoice<UserType>(
			"userTypeDropDownChoice", new PropertyModel<UserType>(this,
					"userType"), Arrays.asList(UserType.values()));

	private PasswordTextField passwordTextField1 = new PasswordTextField(
			"passwordTextfield1", new Model<String>());
	private PasswordTextField passwordTextField2 = new PasswordTextField(
			"passwordTextfield2", new Model<String>());

	private FileUploadPanel imageUploadPanel = new FileUploadPanel(
			"imageUploadPanel", new PropertyModel<byte[]>(this, "userPicture"),
			new PackageResourceReference(ApplicationUserEntityPanel.class,
					"res/missingUser.png")) {

		private static final long serialVersionUID = 1L;

		@Override
		public void handleUploadedFile(FileUpload uploadedFile) {
			selectedEntity.setUserPicture(uploadedFile.getBytes());
			
			super.setDefaultModel(new PropertyModel<byte[]>(selectedEntity, "userPicture")) ;
			
 		}
	};
	
	private Link<String> userCdLibLink = new Link<String>("userCdLibLink") {

		private static final long serialVersionUID = 1L;

		@Override
		public void onClick() {
			setResponsePage(new JukeboxCdWebPage(selectedEntity.getCdList()));
		}
	};

	private Button saveButton = new Button("saveButton") {

		private static final long serialVersionUID = 1L;

		@Override
		public void onSubmit() {
			onSaveEntity(selectedEntity);

			super.onSubmit();
		}
	};

	private Button deleteButton = new Button("deleteButton") {

		private static final long serialVersionUID = 1L;

		@Override
		public void onSubmit() {
			onDeleteEntitiy(selectedEntity);
			
			if (selectedEntity.getId().equals(((SpringWicketAuthenticatedWebSession) AuthenticatedWebSession.get()).getUser().getId()))
			{
				setResponsePage(HomePage.class) ;
			}
			super.onSubmit();
		}
	};
	private Button cancelButton = new Button("cancelButton") {

		private static final long serialVersionUID = 1L;

		@Override
		public void onError() {
			onCancel();
		}
	};

	private MarkupContainer fallbackContainer ;

	@Override
	public void onDeleteEntitiy(ApplicationUser user) {
		userDao.delete(user);
	}

	@Override
	public void onSaveEntity(ApplicationUser user) {
		if (!passwordTextField1.getValue().equals("")) {
			user.setPassword(stringDigester.digest(passwordTextField1
					.getValue()));
		}

		userDao.save(user);
	}

	@Override
	public void onCancel() {
		this.getParent().setVisible(false);
		fallbackContainer.setVisible(true);
		//setResponsePage(this.fallbackPage);
	}

	public ApplicationUserEntityPanel(String id, ApplicationUser user, final MarkupContainer fallbackContainer) {
		super(id);

		this.fallbackContainer = fallbackContainer ;
		
		if (user != null)
			this.setSelectedEntity(user);

		userEntityForm = new Form<ApplicationUser>("userEntityForm",
				new LoadableDetachableModel<ApplicationUser>(user) {

					private static final long serialVersionUID = 1L;

					@Override
					protected ApplicationUser load() {
						return userDao.findById(selectedEntity.getId());
					}
				}) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				super.onSubmit();
				this.getParent().setVisible(false);
				fallbackContainer.setVisible(true);
			}
		};

		lastNameTextField.setRequired(true);
		firstNameTextField.setRequired(true);

		if (selectedEntity == null)
			userNameTextField.add(new UniqueApplicationUserNameValidator());
		else {
			userNameTextField.removeAll();
		}

		userNameTextField.add(StringValidator.minimumLength(3));
		userNameTextField.add(new UniqueApplicationUserNameValidator());
		userNameTextField.setRequired(true);

		emailTextField.setRequired(true);
		emailTextField.add(EmailAddressValidator.getInstance());

		userEntityForm.add(lastNameTextField);
		userEntityForm.add(firstNameTextField);
		userEntityForm.add(userNameTextField);
		userEntityForm.add(emailTextField);

		userEntityForm.add(genderDropDownChoice);
		userEntityForm.add(userTypeDropDownChoice);

		passwordTextField1.setRequired(false);
		passwordTextField2.setRequired(false);

		userEntityForm.add(passwordTextField1);
		userEntityForm.add(passwordTextField2);
		userEntityForm.add(new EqualPasswordInputValidator(passwordTextField1,
				passwordTextField2));

		userEntityForm.add(saveButton);
		userEntityForm.add(cancelButton);
		userEntityForm.add(deleteButton);
		
		add(imageUploadPanel);

		add(userEntityForm);
		
		add(userCdLibLink);
	}

	@Override
	public void setSelectedEntity(final ApplicationUser selectedEntity) {
		this.selectedEntity = selectedEntity;

		lastNameTextField.setDefaultModel(new PropertyModel<String>(
				selectedEntity, "lastName"));
		firstNameTextField.setDefaultModel(new PropertyModel<String>(
				selectedEntity, "firstName"));
		userNameTextField.setDefaultModel(new PropertyModel<String>(
				selectedEntity, "userName"));

		emailTextField.setDefaultModel(new PropertyModel<String>(
				selectedEntity, "email"));
		genderDropDownChoice.setDefaultModel(new PropertyModel<GenderType>(
				selectedEntity, "gender"));
		userTypeDropDownChoice.setDefaultModel(new PropertyModel<UserType>(
				selectedEntity, "userType"));

		imageUploadPanel.setDefaultModel(new PropertyModel<byte[]>(
				selectedEntity, "userPicture"));

		this.setDefaultModel(new LoadableDetachableModel<ApplicationUser>(
				selectedEntity) {

			private static final long serialVersionUID = 1L;

			@Override
			protected ApplicationUser load() {
				return userDao.findById(selectedEntity.getId());
			}
		});
	}

	private class UniqueApplicationUserNameValidator extends StringValidator {

		private static final long serialVersionUID = 1L;

		@Override
		protected void onValidate(IValidatable<String> validatable) {
			String username = validatable.getValue();

			ApplicationUser user = userDao.findByUserName(username);

			if (user != null) {
				if (user.getId() != selectedEntity.getId()) {
					ValidationError error = new ValidationError();
					error.setMessage("Username is not unique!");
					validatable.error(error);
				}
			}
		}
	}
}
