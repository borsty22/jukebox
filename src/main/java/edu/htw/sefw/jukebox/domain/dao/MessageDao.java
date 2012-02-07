package edu.htw.sefw.jukebox.domain.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import edu.htw.sefw.jukebox.domain.entity.ApplicationUser;
import edu.htw.sefw.jukebox.domain.entity.Message;
import edu.htw.sefw.jukebox.domain.entity.Swapping;

@Transactional
public interface MessageDao extends JpaRepository<Message, Long> {

	public ArrayList<Message> findBySwapping(Swapping swap) ;

	public List<Message> findByReceiverAndUnreadStatus(ApplicationUser user,
			boolean unreadStatus);

}
