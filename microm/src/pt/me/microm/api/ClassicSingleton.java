package pt.me.microm.api;

import pt.me.microm.model.stuff.DaBoxModel;

public class ClassicSingleton {
	   private static ClassicSingleton instance = null;
	   protected ClassicSingleton() {
	      // Exists only to defeat instantiation.
	   }
	   public static ClassicSingleton getInstance() {
	      if(instance == null) {
	         instance = new ClassicSingleton();
	      }
	      return instance;
	   }
	   
	   
	   public DaBoxModel m; 
	   
	   public void out(String s) {
		   System.out.println(s);
	   }
	}
