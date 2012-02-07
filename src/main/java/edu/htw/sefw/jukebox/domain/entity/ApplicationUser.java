package edu.htw.sefw.jukebox.domain.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import org.joda.time.DateTime;

import edu.htw.sefw.jukebox.domain.entity.base.AbstractEntityBase;

@SuppressWarnings("serial")
@Entity
public class ApplicationUser extends AbstractEntityBase {

	private String lastName;

	private String firstName;

	@Column(unique = true)
	private String userName;

	private GenderType gender;

	private String email;

	private String password;

	private UserType userType;
	
	private DateTime registrationDateTime ;

	@OneToMany(mappedBy="user", fetch=FetchType.EAGER)
	private List<JukeboxCd> cdList;

	@OneToMany(mappedBy = "providedUser")
	private List<Swapping> swappingList;

	@Lob
	private byte[] userPicture;

	public ApplicationUser() {
		super();
	}

	public ApplicationUser(String lastName, String firstName) {
		super();
		this.lastName = lastName;
		this.firstName = firstName;
	}

	public GenderType getGender() {
		return gender;
	}

	public void setGender(GenderType gender) {
		this.gender = gender;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public byte[] getUserPicture() {
		return userPicture;
	}

	public void setUserPicture(byte[] userPicture) {
		this.userPicture = userPicture;
	}

	public List<JukeboxCd> getCdList() {
		return cdList;
	}

	public void setCdList(List<JukeboxCd> cdList) {
		this.cdList = cdList;
	}
	
	public void addCd(JukeboxCd cd) {
		if (cdList == null)
			cdList = new ArrayList<JukeboxCd>();
		cdList.add(cd);
	}

	public void removeCd(JukeboxCd cd) {
		if (cdList != null)
			cdList.remove(cd);
	}

	public List<Swapping> getSwappingList() {
		return swappingList;
	}

	public void setSwappingList(List<Swapping> swappingList) {
		this.swappingList = swappingList;
	}

	public void addSwapping(Swapping swapping) {
		if (swappingList == null)
			swappingList = new ArrayList<Swapping>();

		swappingList.add(swapping);
	}

	public void removeSwapping(Swapping swapping) {
		if (swappingList != null)
			swappingList.remove(swapping);
	}

	public enum UserType {
		Admin, User
	}

	public enum GenderType {
		Female, Male
	}

	public DateTime getRegistrationDateTime() {
		return registrationDateTime;
	}

	public void setRegistrationDateTime(DateTime registrationDateTime) {
		this.registrationDateTime = registrationDateTime;
	}
}
