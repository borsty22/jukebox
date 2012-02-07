package edu.htw.sefw.jukebox.domain.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import edu.htw.sefw.jukebox.domain.entity.base.AbstractEntityBase;

@Entity
public class Swapping extends AbstractEntityBase {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	private ApplicationUser requestedUser;

	@ManyToOne
	private ApplicationUser providedUser;

	@ManyToMany(fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	@JoinTable(name = "PROVIDEDCD_SWAPPING", joinColumns = { @JoinColumn(name = "PROVIDEDCD_ID", referencedColumnName = "ID") })
	private List<JukeboxCd> providedCdList;

	@ManyToMany(fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	@JoinTable(name = "REQUESTEDCD_SWAPPING", joinColumns = { @JoinColumn(name = "REQUESTEDCD_ID", referencedColumnName = "ID") })
	private List<JukeboxCd> requestedCdList;

	private SwappingStatus status;

	@OneToMany(mappedBy = "swapping", fetch=FetchType.EAGER, cascade= { CascadeType.MERGE, CascadeType.REMOVE })
	@Fetch(FetchMode.SUBSELECT)
	private List<Message> messageList;

	private boolean unreadStatus ;
	
	public enum SwappingStatus {
		requested, swapped, cancel, successful, edited
	}

	public void addProvidedCd(JukeboxCd cd) {
		if (this.providedCdList == null)
			this.providedCdList = new ArrayList<JukeboxCd>();

		this.providedCdList.add(cd);
	}

	public void addProvidedCdList(Collection<JukeboxCd> cdList) {
		if (this.providedCdList == null)
			this.providedCdList = new ArrayList<JukeboxCd>();

		this.providedCdList.addAll(cdList);
	}

	public void removeProvidedCd(JukeboxCd cd) {

		if (this.providedCdList != null)
			if (this.providedCdList.contains(cd))
				this.providedCdList.remove(cd);
	}

	public void addRequestedCd(JukeboxCd cd) {
		if (this.requestedCdList == null)
			this.requestedCdList = new ArrayList<JukeboxCd>();

		this.requestedCdList.add(cd);
	}

	public void addRequestedCdList(Collection<JukeboxCd> cdList) {
		if (this.requestedCdList == null)
			this.requestedCdList = new ArrayList<JukeboxCd>();

		this.requestedCdList.addAll(cdList);
	}

	public void removeRequestedCd(JukeboxCd cd) {

		if (this.requestedCdList != null)
			if (this.requestedCdList.contains(cd))
				this.requestedCdList.remove(cd);
	}

	public SwappingStatus getStatus() {
		return status;
	}

	public void setStatus(SwappingStatus status) {
		this.status = status;
	}

	public ApplicationUser getRequestedUser() {
		return requestedUser;
	}

	public void setRequestedUser(ApplicationUser requestedUser) {
		this.requestedUser = requestedUser;
	}

	public ApplicationUser getProvidedUser() {
		return providedUser;
	}

	public void setProvidedUser(ApplicationUser providedUser) {
		this.providedUser = providedUser;
	}

	public List<Message> getMessageList() {
		return messageList;
	}

	public void setMessageList(List<Message> messageList) {
		this.messageList = messageList;
	}

	public void addToMessageList(Message message) {
		if (this.messageList == null)
			this.messageList = new ArrayList<Message>();

		if (!this.messageList.contains(message))
			this.messageList.add(message);
	}

	public void removeFromMessageList(Message message) {

		if (this.messageList == null)
			this.messageList = new ArrayList<Message>();

		if (this.messageList.contains(message))
			this.messageList.remove(message);
	}

	public List<JukeboxCd> getProvidedCdList() {
		return providedCdList;
	}

	public void setProvidedCdList(List<JukeboxCd> providedCdList) {
		this.providedCdList = providedCdList;
	}

	public List<JukeboxCd> getRequestedCdList() {
		return requestedCdList;
	}

	public void setRequestedCdList(List<JukeboxCd> requestedCdList) {
		this.requestedCdList = requestedCdList;
	}

	public boolean isUnreadStatus() {
		return unreadStatus;
	}

	public void setUnreadStatus(boolean unreadStatus) {
		this.unreadStatus = unreadStatus;
	}
}
