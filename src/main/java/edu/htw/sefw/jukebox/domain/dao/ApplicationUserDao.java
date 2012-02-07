package edu.htw.sefw.jukebox.domain.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import edu.htw.sefw.jukebox.domain.entity.ApplicationUser;

@Transactional
public interface ApplicationUserDao extends JpaRepository<ApplicationUser, Long> {

	public ApplicationUser findByFirstNameAndLastName(String firstName, String lastName) ;
	
	public ApplicationUser findByUserNameAndPassword(String userName, String password) ;

	public ApplicationUser findByUserName(String username);
	
	public ApplicationUser findById(long id);
	
	@Query("SELECT	u " +
			"FROM	ApplicationUser u " +
			"WHERE	LOWER(u.firstName) LIKE LOWER(?1) OR " +
			"LOWER(u.lastName) LIKE LOWER(?1) OR " +
			"LOWER(u.userName) LIKE LOWER(?1)")
	public List<ApplicationUser> findByNameString(String nameString) ;

}