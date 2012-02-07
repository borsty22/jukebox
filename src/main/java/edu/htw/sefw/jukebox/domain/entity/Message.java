package edu.htw.sefw.jukebox.domain.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.joda.time.DateTime;

import edu.htw.sefw.jukebox.domain.entity.base.AbstractEntityBase;

@Entity
public class Message extends AbstractEntityBase {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	private Swapping swapping;

	@ManyToOne
	private ApplicationUser receiver ;
	
	private boolean unreadStatus ;
	
	private DateTime dateTime;

	private String message;

	public Swapping getSwapping() {
		return swapping;
	}

	public void setSwapping(Swapping swapping) {
		this.swapping = swapping;
	}

	public DateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(DateTime dateTime) {
		this.dateTime = dateTime;
	}

	public String getMessage() {
		return message;
	}
	

	public void setMessage(String message) {
		this.message = message;
	}

	public ApplicationUser getReceiver() {
		return receiver;
	}

	public void setReceiver(ApplicationUser receiver) {
		this.receiver = receiver;
	}

	public boolean isUnreadStatus() {
		return unreadStatus;
	}

	public void setUnreadStatus(boolean unreadStatus) {
		this.unreadStatus = unreadStatus;
	}
}
