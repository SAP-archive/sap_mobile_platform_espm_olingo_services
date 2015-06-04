package com.xsmp.espm.model;

import java.util.Calendar;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.xsmp.espm.util.Utility;

@Entity
@Table(name = "ESPM_NOTIFICATION_TARGET")
public class NotificationTarget {
	@TableGenerator(name = "NotificationGenerator", table = "ESPM_ID_GENERATOR", pkColumnName = "GENERATOR_NAME", valueColumnName = "GENERATOR_VALUE", pkColumnValue = "NotificationTarget", initialValue = 1, allocationSize = 10)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "NotificationGenerator")
	@Column(name = "NOTIFICATION_TARGET_ID", length = 10)
	private Integer notficationtargetId;

	@Column(name = "HOSTNAME", unique = true, length = 128)
	private String hostname;

	@Column(name = "PORT")
	private Integer port;
	
	@Column(name = "USE_HTTPS")
	private Integer https;

	@Column(name = "NOTIFICATION_USER", length = 128)
	private String notificationUser;

	@Column(name = "NOTIFICATION_PASSWORD", length = 128)
	private String password;
	
	@Column(name = "APPLICATION_NAME", length = 256)
	private String applicationName;

	public Integer getNotficationtargetId() {
		return notficationtargetId;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public Integer getHttps() {
		return https;
	}

	public void setHttps(Integer https) {
		this.https = https;
	}

	// This getter tricks Olingo into being 
	// unable to return the value in a web service
	public String getNotificationUser() {
		return "******";
	}
	
	public String getActualNotificationUser() {
		return notificationUser;
	}

	public void setNotificationUser(String username) {
		this.notificationUser = username;
	}

	// This getter tricks Olingo into being 
	// unable to return the value in a web service
	public String getPassword() {
		return "******";
	}
	
	public String getActualPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

}