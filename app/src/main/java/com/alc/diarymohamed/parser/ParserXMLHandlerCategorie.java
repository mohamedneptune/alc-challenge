package com.alc.diarymohamed.parser;

import com.alc.diarymohamed.data.model.CountryModel;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;


public class ParserXMLHandlerCategorie extends DefaultHandler{

	private static String LISTCOUNTRY = "ListCountry";
	private static String COUNTRIES = "Countries";
	private static String COUNTRY = "Country";
	private static String CODE = "Code";
	private static String NAME = "Name";
	private static String PREFIX = "Prefix";
	private static String CURRENCY = "Currency";
	private static String CAPITALE = "Capitale";
	private static String TIMEZONE = "TimeZone";
	private static String DST = "Dst";
	private static String DSTBEGIN = "DstBegin";
	private static String DSTEND = "DstEnd";
	private static CountryModel current_country = new CountryModel();
	private static ArrayList<CountryModel> list_country = new ArrayList<>();
	private boolean inCountry;
	private StringBuffer buffer;

	public void processingInstruction(String target, String data) throws SAXException {
		super.processingInstruction(target, data);
	} 

	public ParserXMLHandlerCategorie(){
		super();		
	}

	public void startDocument() throws SAXException {
		super.startDocument();
		list_country = new ArrayList<CountryModel>();
	}

	public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes) throws SAXException {

		buffer = new StringBuffer();
		if (localName.equalsIgnoreCase(this.COUNTRIES)){
			this.current_country = new CountryModel();
			list_country  = new ArrayList<CountryModel>();
			inCountry = true;
		}
	}

	public void endElement(String uri, String localName, String name) throws SAXException {		
		if (localName.equalsIgnoreCase(this.COUNTRY)){
			if(inCountry){

				list_country.add(current_country.copy());
				this.current_country = new CountryModel();
				buffer = null;
			}
		}

		if (localName.equalsIgnoreCase(this.CODE)){
			if(inCountry){
				current_country = new CountryModel();
				this.current_country.setCode(buffer.toString());
				buffer = null;
			}
		}

		if (localName.equalsIgnoreCase(this.NAME)){
			if(inCountry){
				current_country = new CountryModel();
				this.current_country.setName(buffer.toString());
				buffer = null;
			}
		}

		if (localName.equalsIgnoreCase(this.PREFIX)){
			if(inCountry){
				this.current_country.setPrefix(buffer.toString());
				buffer = null;
			}
		}

		if (localName.equalsIgnoreCase(this.CURRENCY)){
			if(inCountry){
				this.current_country.setCurrency(buffer.toString());
				buffer = null;
			}
		}

		if (localName.equalsIgnoreCase(this.CAPITALE)){
			if(inCountry){
				this.current_country.setCapital(buffer.toString());
				buffer = null;
			}
		}

		if (localName.equalsIgnoreCase(this.TIMEZONE)){
			if(inCountry){
				this.current_country.setTimeZone(buffer.toString());
				buffer = null;
			}
		}

		if (localName.equalsIgnoreCase(this.DST)){
			if(inCountry){
				this.current_country.setDst(buffer.toString());
				buffer = null;
			}
		}

		if (localName.equalsIgnoreCase(this.DSTBEGIN)){
			if(inCountry){
				this.current_country.setDstBegin(buffer.toString());
				buffer = null;
			}
		}

		if (localName.equalsIgnoreCase(this.DSTEND)){
			if(inCountry){
				this.current_country.setDstEnd(buffer.toString());
				buffer = null;
			}
		}

	}

	public void characters(char[] ch,int start, int length) throws SAXException{
		String lecture = new String(ch,start,length);
		if(buffer != null) buffer.append(lecture);
	}

	public ArrayList<CountryModel> getData_Baby(){
		return list_country;
	}
}