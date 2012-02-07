package edu.htw.sefw.jukebox.web.resources;

import org.apache.wicket.request.resource.PackageResourceReference;

public class JukeboxResources {
	public final static PackageResourceReference css_base = 
			new PackageResourceReference(JukeboxResources.class, "base.css");
	
	public final static PackageResourceReference css_menu = 
			new PackageResourceReference(JukeboxResources.class, "menu.css");
	
	public final static PackageResourceReference jquery_min = 
			new PackageResourceReference(JukeboxResources.class, "jquery.min.js");
	
	public final static PackageResourceReference css_jquery = 
			new PackageResourceReference(JukeboxResources.class, "jquery-ui-1.8.16.custom.css");
	
	/**
	 * Pfad für den Tomcat Server
	 */
	public final static String music_path = "/music/";
	
	/**
	 * Pfad im Dateisystem
	 */
	public final static String music_full_path = System.getenv("MUSIC") + music_path;
	
	
	//Generische Lösung -> Funktioniert nicht
	/*private static String getHomePath() {
		String s1 = new File("dummy").getAbsolutePath();
		String s2 = new File(s1).getParent();
		String s3 = new File(s2).getParent();
		String path = s3 + "/code/webapps/music/";
		return path;
	}*/
}
