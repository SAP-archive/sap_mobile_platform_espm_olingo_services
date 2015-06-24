package com.xsmp.deltasupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.uri.info.GetEntitySetUriInfo;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPATombstoneContext;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPATombstoneEntityListener;
import org.apache.olingo.odata2.jpa.processor.api.exception.ODataJPAModelException;
import org.apache.olingo.odata2.jpa.processor.api.exception.ODataJPARuntimeException;
import org.apache.olingo.odata2.jpa.processor.api.jpql.JPQLContext;
import org.apache.olingo.odata2.jpa.processor.api.jpql.JPQLContextType;
import org.apache.olingo.odata2.jpa.processor.api.jpql.JPQLStatement;

/**
 * @author Riley Rainey <riley.rainey@sap.com>
 *
 * Respond to Olingo Delta Token handling events.  This class is reusable for all
 * of the tables in this project since the "Last Updated" column has the same name in 
 * each table where delta tracking is enabled.
 * @see http://olingo.apache.org/doc/odata2/tutorials/DeltaQuerySupport.html
 */
public class ESPMJPADeltaListener extends ODataJPATombstoneEntityListener {
	
	static final String DATEFORMAT = "yyyy-MM-dd'T'HH:mm:ss";

	/*
	 * Generate a web-service-specific delta token to be passed back to the
	 * client with a response.  In this case, we simply generate an ISO-8601-style
	 * string reflecting the current UTC date/time.
	 * @see org.apache.olingo.odata2.jpa.processor.api.ODataJPATombstoneEntityListener#generateDeltaToken(java.util.List, javax.persistence.Query)
	 */
	@Override
	public String generateDeltaToken(List<Object> deltas, Query query) {
		final SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
	    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
	    final String utcTime = sdf.format(new Date());
	    return utcTime;
	}

	/* 
	 * Modify get Entity Set queries that include a delta token in the URI
	 * 
	 * @see org.apache.olingo.odata2.jpa.processor.api.ODataJPATombstoneEntityListener#getQuery(org.apache.olingo.odata2.api.uri.info.GetEntitySetUriInfo, javax.persistence.EntityManager)
	 */
	@Override
	public Query getQuery(GetEntitySetUriInfo resultsView, EntityManager em) {
		
		JPQLContextType contextType;
		JPQLContext jpqlContext = null;
		JPQLStatement jpqlStatement = null;
		
		try {
			
			if (!resultsView.getStartEntitySet().getName().equals(resultsView.getTargetEntitySet().getName())) {    
				contextType = JPQLContextType.JOIN;
			} else {    
				contextType = JPQLContextType.SELECT;  
			}

			jpqlContext = JPQLContext.createBuilder(contextType, resultsView).build();  
			jpqlStatement = JPQLStatement.createBuilder(jpqlContext).build();

		} catch (EdmException e) {
			e.printStackTrace();
			return null;
		}
		catch (ODataJPAModelException e) {
			e.printStackTrace();
			return null;
		} catch (ODataJPARuntimeException e) {
			e.printStackTrace();
			return null;
		}
		
		String deltaToken = ODataJPATombstoneContext.getDeltaToken();
		// remove "T" from the date string;
		if (deltaToken != null) {
			deltaToken = deltaToken.replace("T"," ");
		}

		Query query = null;

		if (deltaToken != null) {
			String statement = jpqlStatement.toString();  
			String[] statementParts = statement.split(JPQLStatement.KEYWORD.WHERE);  
			String deltaCondition = jpqlContext.getJPAEntityAlias() + ".updatedTimestamp >= {ts '" + deltaToken + "'}";  
			if (statementParts.length > 1)  
			{    
				statement = statementParts[0] + JPQLStatement.DELIMITER.SPACE + JPQLStatement.KEYWORD.WHERE + JPQLStatement.DELIMITER.SPACE + deltaCondition + JPQLStatement.DELIMITER.SPACE + JPQLStatement.Operator.AND + statementParts[1];  
			}  
			else {    
				statement = statementParts[0] + JPQLStatement.DELIMITER.SPACE + JPQLStatement.KEYWORD.WHERE + JPQLStatement.DELIMITER.SPACE + deltaCondition;  
			}

			query = em.createQuery(statement);
		}
		else  {
			query = em.createQuery(jpqlStatement.toString());
		}

		return query;
	}

}
