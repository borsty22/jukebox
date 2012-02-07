package edu.htw.sefw.jukebox.domain.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import edu.htw.sefw.jukebox.domain.entity.ApplicationUser;
import edu.htw.sefw.jukebox.domain.entity.JukeboxCd;
import edu.htw.sefw.jukebox.domain.entity.Swapping;
import edu.htw.sefw.jukebox.domain.entity.Swapping.SwappingStatus;

@Transactional
public interface SwappingDao extends JpaRepository<Swapping, Long> {

	List<Swapping> findByRequestedUserAndStatus(ApplicationUser user,
			SwappingStatus requested);
	
	List<Swapping> findByRequestedUserAndUnreadStatus(ApplicationUser user,
			boolean unreadStatus);

	List<Swapping> findByProvidedCdList(JukeboxCd c);

	List<Swapping> findByRequestedCdList(JukeboxCd c);
}
