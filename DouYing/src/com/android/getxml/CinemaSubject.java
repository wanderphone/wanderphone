package com.android.getxml;

import java.io.Serializable;
import java.util.List;


public class CinemaSubject  implements Serializable{


	private static final long serialVersionUID = 1L;
	
	private String CinemaAddress;
	private String CinemaName;
	private List<String> Movie_Ptime;
	
	public List<String> getTime() {
		return Movie_Ptime;
	}

	public void setTime(List<String> time) {
		this.Movie_Ptime = time;
	}
	
	public void set_cinema_address(String addr){
		this.CinemaAddress = addr;
	}
	public String get_cinema_address(){
		return CinemaAddress;
	}
	
	public void set_cinema_name(String cname){
		this.CinemaName = cname;
	}
	public String get_cinema_name(){
		return CinemaName;
	}
	
	

}
