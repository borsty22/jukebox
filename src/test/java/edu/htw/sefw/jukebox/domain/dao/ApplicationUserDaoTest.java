package edu.htw.sefw.jukebox.domain.dao;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import edu.htw.sefw.jukebox.domain.entity.ApplicationUser;
import edu.htw.sefw.jukebox.domain.entity.ApplicationUser.GenderType;
import edu.htw.sefw.jukebox.domain.entity.ApplicationUser.UserType;
import edu.htw.sefw.jukebox.domain.entity.JukeboxCd;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:applicationContext.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class ApplicationUserDaoTest {
	ApplicationUser user;
	String fname = "qwertzuiopü";
	String lname = "asdfghjklöä";
	String uname = "yxcvbnm";
	String password = "1234";
	GenderType g = GenderType.Male;
	String mail = "testqwertzuiop@mail.com";
	UserType utype = UserType.User;
	List<JukeboxCd> cds = new ArrayList<JukeboxCd>();
	@Autowired
	ApplicationUserDao userDao;
	
	@Before
	public void setUp() {
		user = new ApplicationUser();
		user.setFirstName(fname);
		user.setLastName(lname);
		user.setUserName(uname);
		user.setPassword(password);
		user.setGender(g);
		user.setEmail(mail);
		user.setUserType(utype);
		cds.add(new JukeboxCd("cd1", "ich", "work"));
		cds.add(new JukeboxCd("cd2", "ich", "work"));
		cds.add(new JukeboxCd("cd3", "ich", "work"));
		//löschen des Users falls vorhanden
		ApplicationUser suser = userDao.findByUserName(user.getUserName());
		if(suser != null) {
			userDao.delete(suser);
			suser = null;
		}
		
	}
	
	@Test
	public void testSaveApplicationUser() {
		ApplicationUser suser = userDao.save(user);	
		assertNotNull(suser);
		userDao.delete(suser);
		suser = userDao.findById(suser.getId());
		assertNull(suser);
	}
	
	@Test
	public void testDeleteApplicationUser() {
		ApplicationUser suser = userDao.save(user);	
		if(suser == null) {
			fail("User konnte nicht angelegt werden");
		}
		userDao.delete(suser);
		suser = userDao.findById(suser.getId());
		assertNull(suser);
	}

	@Test
	public void testFindByUserName() {
		assertTrue(true);
	}

}
