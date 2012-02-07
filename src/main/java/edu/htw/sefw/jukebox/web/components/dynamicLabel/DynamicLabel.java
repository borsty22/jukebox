package edu.htw.sefw.jukebox.web.components.dynamicLabel;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DynamicLabel implements Serializable {	
	
	       private String name;

	       public String getName()
	       {
	               return name;
	       }

	       public void setName(String name)
	       {
	               this.name = name;
	       }
	
}
