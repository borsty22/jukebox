package edu.htw.sefw.jukebox.domain.dao;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import edu.htw.sefw.jukebox.domain.entity.ApplicationUser;
import edu.htw.sefw.jukebox.domain.entity.JukeboxCd;

@Transactional
public interface JukeboxCdDao extends JpaRepository<JukeboxCd, Long>{
	
	@Query("SELECT	u " +
			"FROM	JukeboxCd u " +
			"WHERE	(LOWER(u.title) LIKE LOWER(?1) OR " +
			"LOWER(u.interpret) LIKE LOWER(?1)) AND " +
			"user=?2")
	public ArrayList<JukeboxCd> findByTitleInterpretAndUser(String nameString,ApplicationUser user) ;	
	
	@Query("SELECT	u " +
			"FROM	JukeboxCd u " +
			"WHERE	(LOWER(u.title) LIKE LOWER(?1) OR " +
			"LOWER(u.interpret) LIKE LOWER(?1)) AND " +
			"user!=?2")
	public ArrayList<JukeboxCd> findByTitleInterpretAndNotUser(String nameString,ApplicationUser user) ;
	
	@Query("SELECT	u " +
			"FROM	JukeboxCd u " +
			"WHERE	(LOWER(u.title) LIKE LOWER(?1) OR " +
			"LOWER(u.interpret) LIKE LOWER(?1) OR " +
			"LOWER(u.genre) LIKE LOWER(?1) OR " +
			"LOWER(u.description) LIKE LOWER(?1))")
	public ArrayList<JukeboxCd> findByTitleInterpretGenreDesc(String nameString) ;	
	
	public ArrayList<JukeboxCd> findByInterpret(String name);
	
	public ArrayList<JukeboxCd> findByUser(ApplicationUser user);
	
	public JukeboxCd findById(Long id) ;
	
	public ArrayList<JukeboxCd> findAll();
	
}


