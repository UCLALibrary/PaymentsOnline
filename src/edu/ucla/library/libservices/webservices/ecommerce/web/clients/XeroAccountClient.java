package edu.ucla.library.libservices.webservices.ecommerce.web.clients;

import com.google.gson.Gson;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroAccountList;
import edu.ucla.library.libservices.webservices.ecommerce.constants.XeroConstants;

import org.apache.log4j.Logger;

public class XeroAccountClient extends AbstractXeroClient
{
  private static final Logger LOGGER = Logger.getLogger(XeroAccountClient.class);

  // unique ID for a contact
  private String accountID;
  // XeroContact bean returned from class
  private String itemCode;

  public XeroAccountClient()
  {
    super();
    port = 0;
  }

  public void setAccountID(String accountID)
  {
    this.accountID = accountID;
  }

  public String getAccountID()
  {
    return accountID;
  }

  private String getAccountURL()
  {
    return secrets.getProperty(XeroConstants.ACCOUNT_URL).concat(getAccountID());
  }

  /**
   * Calls Xero account API to retrieve an item code (stored
   * in account name field)
   * @return Transact item code associated with account
   */
  public String getItemCode()
  {
    ClientResponse response;
    WebResource webResource;

    loadProperties();
    webResource = getWebResource(replacePort(getAccountURL()));
    response = getResponse(webResource, XeroConstants.JSON_ACCEPT);
    if (response.getStatus() == 200)
    {
      XeroAccountList theList;
      String json = response.getEntity(String.class);
      theList = new Gson().fromJson(json, XeroAccountList.class);
      itemCode = theList.getAccounts()
                        .get(0)
                        .getName();
    }
    else
    {
      LOGGER.error("contact service return code " + response.getStatus() + "\t" + response.getEntity(String.class));
      itemCode = new String();
    }

    return itemCode;
  }
}
