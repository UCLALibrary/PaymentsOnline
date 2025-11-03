package edu.ucla.library.libservices.webservices.ecommerce.web.clients;

import com.google.gson.Gson;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroContact;
import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroContactList;
import edu.ucla.library.libservices.webservices.ecommerce.constants.XeroConstants;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

import java.net.URLEncoder;

/**
 * Class that retrieves contact (customer/patron) data from Xero API
 */
public class XeroContactClient extends AbstractXeroClient
{
  private static final Logger LOGGER = Logger.getLogger(XeroContactClient.class);
  // query string for contact lookup
  private static final String QUERY = "AccountNumber=\"{id}\"";
  
  // unique ID for a contact
  private String userID;
  // XeroContact bean returned from class
  private XeroContact theContact;

  public void setUserID(String userID)
  {
    this.userID = userID;
  }

  public String getUserID()
  {
    return userID;
  }

  private String getContactURL()
  {
    try
    {
      return secrets.getProperty(XeroConstants.CONTACT_URL).concat("?where=")
             .concat(URLEncoder.encode(QUERY.replace("{id}", getUserID()), "UTF-8"));
    }
    catch (UnsupportedEncodingException uee)
    {
      LOGGER.error(uee.getMessage());
      return null;
    }
  }

  /**
   * Retrieves properties (contact URI etc), calls Xero service, maps json response to
   * XeroContact bean
   * @return A Xero contact modeled as a XeroContact bean
   */
  public XeroContact getTheContact()
  {
    loadProperties();
    if (this.theContact == null)
    {
      WebResource webResource;
      ClientResponse response;
      
      //System.out.println("calling contact service with URL " + getContactURL());
      webResource = getWebResource(replacePort(getContactURL()));
      response = getResponse(webResource, XeroConstants.JSON_ACCEPT);
      if (response.getStatus() == 200)
      {
        XeroContactList theList;
        String json = response.getEntity(String.class);
        theList = new Gson().fromJson(json, XeroContactList.class);
        this.theContact = theList.getContacts().get(0);
      }
      else
      {
        LOGGER.error("contact service return code " + response.getStatus() + " on request URL " + getContactURL() +"\t" + response.getEntity(String.class));
        this.theContact = new XeroContact();
      }
    }
    return this.theContact;
  }
}
