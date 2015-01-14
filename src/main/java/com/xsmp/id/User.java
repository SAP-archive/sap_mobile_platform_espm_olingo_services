/**
 * 
 */
package com.xsmp.id;

import javax.persistence.*;

import java.io.Serializable;
import java.sql.Clob;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

/**
 * @author prodemo
 *
 */

//@Entity
//@Table(name = "Users")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	protected int userId;
	@Basic(optional = false)
	protected String displayName;
	@Basic(optional = false)
	protected String displayTitle;
	protected String department;
	protected String phoneNumber;
	protected String location;
	protected String country;
	protected String emailAddress;
	protected String iNumber;
	
	@Basic(optional = true)
	@Temporal(TemporalType.TIMESTAMP)
	protected Calendar lastUpdate;
	
	@Basic(optional = true)
	protected String reportingCID;
	
	private String photoURL;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int user_id) {
		this.userId = user_id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String display_name) {
		this.displayName = display_name;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String display_organization) {
		this.department = display_organization;
	}


	public Calendar getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Calendar last_update) {
		this.lastUpdate = last_update;
	}

	public String getDisplayTitle() {
		return displayTitle;
	}

	public void setDisplayTitle(String displayTitle) {
		this.displayTitle = displayTitle;
	}

	public String getReportingCID() {
		return reportingCID;
	}

	public void setReportingCID(String reportingCID) {
		this.reportingCID = reportingCID;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getINumber() {
		return iNumber;
	}

	public void setINumber(String iNumber) {
		this.iNumber = iNumber;
	}

	public String getiNumber() {
		return iNumber;
	}

	public void setiNumber(String iNumber) {
		this.iNumber = iNumber;
	}

	public String getPhoto() {
		return photoURL;
	}

	public void setPhoto(String photo) {
		this.photoURL = photo;
	}

}
