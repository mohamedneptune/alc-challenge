package com.alc.diarymohamed.parser;

import android.content.Context;
import android.util.Log;

import com.alc.diarymohamed.data.model.CountryModel;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


public class ContainerData {

	static public Context context;
	private static ArrayList<CountryModel> countries;
	private DefaultHandler handler;
	
	    public ContainerData(Context cont) {
	    	context=cont;
	    }
	    
		 public  ArrayList<CountryModel> getCountries(){
		    	  
		    	SAXParserFactory fabrique = SAXParserFactory.newInstance();
		    	SAXParser parseur = null;
		    	  try {
		    		     parseur = fabrique.newSAXParser();
		    		}
		    	  catch (ParserConfigurationException e) {
		    			e.printStackTrace();
		    		   } 
		    	  catch (SAXException e) {
		    			   e.printStackTrace();
		    			   }
		    	   handler = new ParserXMLHandlerCategorie();     
			    	  try {
			    		  InputStream input = context.getAssets().open("Countries.xml");
			    		  		if(input == null)
			    	            	  Log.v("erreur android","null");
			    	          else{
			    	        	  parseur.parse(input, handler);
						    	  }    	          
			    	  } catch (SAXException e) {
						    	              e.printStackTrace();
			    	  } catch (IOException e) {
						    	              e.printStackTrace();
						    	  
			    	  }
			    	  countries = ((ParserXMLHandlerCategorie) handler).getData_Baby();
		    	if (countries==null)
		    		Log.v("ContainerData","null");
				return ContainerData.countries;
		    }
}
