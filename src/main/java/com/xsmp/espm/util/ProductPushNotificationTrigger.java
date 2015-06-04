/**
 * 
 */
package com.xsmp.espm.util;


import java.io.*;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.hsqldb.HsqlException;
import org.hsqldb.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xsmp.espm.data.DataLoader;
import com.xsmp.espm.model.NotificationTarget;
import com.xsmp.espm.model.Product;

/**
 * @author i838039
 *
 */
public class ProductPushNotificationTrigger implements Trigger {
	
	private static Logger logger = LoggerFactory.getLogger(DataLoader.class);

	/* (non-Javadoc)
	 * @see org.hsqldb.Trigger#fire(int, java.lang.String, java.lang.String, java.lang.Object[], java.lang.Object[])
	 */
	@Override
	public void fire(int type, String trigName, String tabName,
			Object[] oldRow, Object[] newRow) throws HsqlException {
		
		/*
		 * CREATE TRIGGER PRODUCT_PRICE_UPDATE AFTER UPDATE OF PRICE ON ESPM_PRODUCT 
   		 * REFERENCING NEW ROW AS newrow
		 * FOR EACH ROW
		 * CALL "com.xsmp.espm.util.ProductPushNotificationTrigger"
		 */
		
		logger.info("---> Trigger fired <---");
		
		notifySMPApplications(
				com.xsmp.espm.ESPMServiceFactory.getApplicationName(),
				"{ \"alert\": \"Our product pricing has changed\" }"
				);
		
	}
	

	public void notifySMPApplications (String applicationName, String msg) 
	{
        EntityManagerFactory emf = Utility.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        TypedQuery<NotificationTarget> queryNotificationTarget;
		List<NotificationTarget> resNotificationTarget;
        
		// The list of SMP applications to be notified is stored as a table in the database
		// (we implemented it in that manner to allow for it to be reconfigured at runtime.)
        em.getTransaction().begin();
        queryNotificationTarget = em.createQuery("SELECT st FROM NotificationTarget st", NotificationTarget.class);
		resNotificationTarget = queryNotificationTarget.getResultList();
		
		// Each row in the Notification Target table represents one application to be notified
		// (multiple applications might be interested in each change).
		
		for (NotificationTarget t : resNotificationTarget) {
			
			HttpHost target = new HttpHost(t.getHostname(), t.getPort(), (t.getHttps() != 0) ? "https" : "http");
	        CredentialsProvider credsProvider = new BasicCredentialsProvider();
	        credsProvider.setCredentials(
	                new AuthScope(target.getHostName(), target.getPort()),
	                new UsernamePasswordCredentials( t.getActualNotificationUser(), 
	                		                         t.getActualPassword()));
	        CloseableHttpClient httpclient = HttpClients.custom()
	                .setDefaultCredentialsProvider(credsProvider).build();
        
	        try {
	 
		        try {
		
		            // Create AuthCache instance
		            AuthCache authCache = new BasicAuthCache();
		            // Generate BASIC scheme object and add it to the local
		            // auth cache
		            BasicScheme basicAuth = new BasicScheme();
		            authCache.put(target, basicAuth);
		
		            // Add AuthCache to the execution context
		            HttpClientContext localContext = HttpClientContext.create();
		            localContext.setAuthCache(authCache);
		
		            HttpPost post = new HttpPost("/restnotification/application/" + applicationName + "/");
		            post.setHeader("Content-Type", "application/json");
		            
		            StringEntity params = new StringEntity( msg );
		            post.setEntity(params);
		
		            System.out.println("Executing request " + post.getRequestLine() + " to target " + target);
		            System.out.println("Executing request by user " + t.getActualNotificationUser());
	                CloseableHttpResponse response = httpclient.execute(target, post, localContext);
	                try {
	                    System.out.println("----------------------------------------");
	                    System.out.println(response.getStatusLine());
	                    
	                    BufferedReader br = new BufferedReader(
	                            new InputStreamReader((response.getEntity().getContent())));
				   		String output;
				   		while ((output = br.readLine()) != null) {
				   			System.out.println(output);
				   		}
				    
				   		response.close();
				   		
	                } finally {
	                    response.close();
	                }
		        }
		        finally {
		            httpclient.close();
		        }
	        }
	        catch (IOException ioe) {
	        }
		}

	}

}
