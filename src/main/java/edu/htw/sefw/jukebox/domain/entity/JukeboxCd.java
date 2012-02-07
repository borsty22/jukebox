package edu.htw.sefw.jukebox.domain.entity;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import edu.htw.sefw.jukebox.domain.entity.base.AbstractEntityBase;

@Entity
public class JukeboxCd extends AbstractEntityBase {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	private ApplicationUser user;
	
	private String title;

	private String interpret;

	private String genre;

	private int year;

	private String description;

	@Lob
	private byte[] audio_mp3;

	private String audio_mp3l;

	@Lob
	private byte[] audio_ogg;

	public JukeboxCd(String title, String interpret, String genre) {
		setTitle(title);
		setInterpret(interpret);
		setGenre(genre);
	}

	public JukeboxCd() {

	}

	@Lob
	private byte[] cover;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getInterpret() {
		return interpret;
	}

	public void setInterpret(String interpret) {
		this.interpret = interpret;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public byte[] getCover() {
		return cover;
	}

	public void setCover(byte[] bs) {
		this.cover = bs;
	}

	public ApplicationUser getUser() {
		return user;
	}

	public void setUser(ApplicationUser user) {
		this.user = user;
	}

	public byte[] getAudio_mp3() {
		return audio_mp3;
	}

	public void setAudio_mp3(byte[] audio_mp3) {
		this.audio_mp3 = audio_mp3;
	}

	public byte[] getAudio_ogg() {
		return audio_ogg;
	}

	public void setAudio_ogg(byte[] audio_ogg) {
		this.audio_ogg = audio_ogg;
	}
	
	public String getAudio_mp3l() {
		return audio_mp3l;
	}

	public void setAudio_mp3l(String audio_mp3l) {
		this.audio_mp3l = audio_mp3l;
	}
}
