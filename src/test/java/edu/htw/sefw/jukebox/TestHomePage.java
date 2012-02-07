package edu.htw.sefw.jukebox;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * Simple test using the WicketTester
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:applicationContext.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class TestHomePage {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	private WicketTester tester;

	@Autowired
	private WicketApplication wicketApplication ;

	@Before
	public void setUp() {
		
		logger.info(wicketApplication.getName()) ;
        tester = new WicketTester(wicketApplication);
	}

	@Test
	public void homepageRendersSuccessfully() {
		// start and render the test page
		tester.startPage(HomePage.class);

		// assert rendered page class
		tester.assertRenderedPage(HomePage.class);
	}
}
