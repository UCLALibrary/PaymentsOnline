package edu.ucla.library.libservices.webservices.ecommerce.web.clients;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import edu.ucla.library.libservices.invoicing.webservices.invoices.beans.CashNetLine;
import edu.ucla.library.libservices.webservices.ecommerce.beans.AlmaFees;
import edu.ucla.library.libservices.webservices.ecommerce.beans.AlmaInvoice;
import edu.ucla.library.libservices.webservices.ecommerce.beans.AlmaUser;
import edu.ucla.library.libservices.webservices.ecommerce.utility.strings.StringHandler;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class AlmaClient
{
  private static final Logger LOGGER = Logger.getLogger(AlmaClient.class);

  private AlmaFees theFees;
  private AlmaInvoice theInvoice;
  private AlmaUser thePatron;
  private Client client;
  private WebResource webResource;
  private String userID;
  private String fineID;
  private String amount;
  private String payMethod;
  private String transNo;
  private String resourceURI;
  private String uriBase;
  private String key;
  private String dbName;
  //private String feeType;

  public AlmaClient()
  {
    super();
    client = null;
    webResource = null;
    userID = null;
  }

  public void setUserID(String userID)
  {
    this.userID = userID;
  }

  private String getUserID()
  {
    return userID;
  }

  public void setResourceURI(String resourceURI)
  {
    this.resourceURI = resourceURI;
  }

  private String getResourceURI()
  {
    return resourceURI;
  }

  public void setUriBase(String uriBase)
  {
    this.uriBase = uriBase;
  }

  private String getUriBase()
  {
    return uriBase;
  }

  public void setKey(String key)
  {
    this.key = key;
  }

  private String getKey()
  {
    return key;
  }

  public void setFineID(String fineID)
  {
    this.fineID = StringHandler.extractInvoiceID(fineID);
  }

  private String getFineID()
  {
    return fineID;
  }

  public void setAmount(String amount)
  {
    this.amount = amount;
  }

  private String getAmount()
  {
    return amount;
  }

  public void setPayMethod(String payMethod)
  {
    this.payMethod = payMethod;
  }

  private String getPayMethod()
  {
    return payMethod;
  }

  public void setTransNo(String transNo)
  {
    this.transNo = transNo;
  }

  private String getTransNo()
  {
    return transNo;
  }

  public void setDbName(String dbName)
  {
    this.dbName = dbName;
  }

  /*private String getDbName()
  {
    return dbName;
  }

  public void setFeeType(String feeType)
  {
    this.feeType = StringHandler.extractFeeType(feeType);
  }

  private String getFeeType()
  {
    return feeType;
  }*/

  public AlmaUser getThePatron()
  {
    if (thePatron == null)
    {
      client = Client.create();
      webResource = client.resource(getUriBase().concat(getUserID()).concat("?apikey=").concat(getKey()));
      thePatron = webResource.accept("application/json").get(AlmaUser.class);
    }
    return thePatron;
  }

  public AlmaFees getTheFees()
  {
    if (theFees == null)
    {
      client = Client.create();
      webResource = client.resource(getUriBase().concat(getUserID())
                                                .concat(getResourceURI())
                                                .concat(getKey()));
      theFees = webResource.get(AlmaFees.class);
    }
    return theFees;
  }

  public AlmaInvoice getTheInvoice()
  {
    if (theInvoice == null)
    {
      CashNetLine theLine;
      List<CashNetLine> lines;
      
      client = Client.create();
      webResource = client.resource(getUriBase().concat(getUserID())
                                                .concat("/fees/")
                                                .concat(getFineID())
                                                .concat("?apikey=")
                                                .concat(getKey()));
      theInvoice = webResource.get(AlmaInvoice.class);
      theLine = new CashNetLine();
      theLine.setInvoiceNumber(theInvoice.getInvoiceNumber());
      theLine.setTotalPrice(theInvoice.getBalance());
      //theLine.setItemCode(DataHandler.getfeeData(getDbName(), theInvoice.getType().getDescription()));
      theLine.setItemCode("45400-BKPR-A");
      lines = new ArrayList<>();
      lines.add(theLine);
      theInvoice.setLineItems(lines);
    }
    return theInvoice;
  }

  public int postPayment()
  {
    ClientResponse response;
    client = Client.create();
    webResource = client.resource(getUriBase().concat(getUserID())
                                              .concat("/fees/")
                                              .concat(getFineID())
                                              .concat("?op=pay&amount=")
                                              .concat(getAmount())
                                              .concat("&method=")
                                              .concat(getPayMethod())
                                              .concat("&external_transaction_id=")
                                              .concat(getTransNo())
                                              .concat("&apikey=")
                                              .concat(getKey()));
    LOGGER.info(webResource.getURI());
    response = webResource.type("text/xml").post(ClientResponse.class);
    return response.getClientResponseStatus().getStatusCode();
  }
}
