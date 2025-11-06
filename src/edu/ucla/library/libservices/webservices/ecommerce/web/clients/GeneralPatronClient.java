package edu.ucla.library.libservices.webservices.ecommerce.web.clients;

import edu.ucla.library.libservices.webservices.ecommerce.beans.AlmaUser;
import edu.ucla.library.libservices.webservices.ecommerce.beans.GeneralPatron;
import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroContact;

public class GeneralPatronClient
{
  private GeneralPatron thePatron;
  private String key;
  private String secretsFile;
  private String tokensFile;
  private String uriBase;
  private String userID;

  public GeneralPatronClient()
  {
    super();
  }

  /**
   * Retrieve minimal patron data needed for JSP displays.
   * First check Alma data for patron record.
   * If no Alma patron, check for a Xero contact.
   * @return Patron data mapped to GeneralPatron bean
   */
  public GeneralPatron getThePatron()
  {
    if (thePatron == null)
    {
      thePatron = new GeneralPatron();

      //first, check for an Alma patron record
      AlmaClient almaClient;
      AlmaUser almaUser ;
      almaClient = new AlmaClient();
      almaClient.setUserID(getUserID());
      almaClient.setUriBase(getUriBase());
      almaClient.setKey(getKey());
      almaUser = almaClient.getThePatron();
      if (almaUser.getFirstName() != null || almaUser.getFirstName().length() == 0)
      {
        mapAlmaToGeneral(almaUser, thePatron);
        thePatron.setXeroID(getXeroID(getUserID()));
      }
      else
      {
        // no Alma record? then check for a Xero record
        XeroContactClient contactClient;
        XeroContact theContact;

        contactClient = new XeroContactClient();
        contactClient.setUserID(getUserID());
        contactClient.setTokensFile(getTokensFile());
        contactClient.setSecretsFile(getSecretsFile());
        theContact = contactClient.getTheContact();
        mapXeroToGeneral(theContact, thePatron);
      }
    }
    return thePatron;
  }

  public void setKey(String key)
  {
    this.key = key;
  }

  public String getKey()
  {
    return key;
  }

  public void setSecretsFile(String secretsFile)
  {
    this.secretsFile = secretsFile;
  }

  public String getSecretsFile()
  {
    return secretsFile;
  }

  public void setTokensFile(String tokensFile)
  {
    this.tokensFile = tokensFile;
  }

  public String getTokensFile()
  {
    return tokensFile;
  }

  public void setUriBase(String uriBase)
  {
    this.uriBase = uriBase;
  }

  public String getUriBase()
  {
    return uriBase;
  }

  public void setUserID(String userID)
  {
    this.userID = userID;
  }

  public String getUserID()
  {
    return userID;
  }

  private void mapAlmaToGeneral(AlmaUser almaUser, GeneralPatron thePatron)
  {
    thePatron.setFirstName(almaUser.getFirstName());
    thePatron.setLastName(almaUser.getLastName());
    thePatron.setAlmaID(almaUser.getPatronID());
  }

  private void mapXeroToGeneral(XeroContact theContact, GeneralPatron thePatron)
  {
    thePatron.setFirstName(theContact.getFirstName());
    thePatron.setLastName(theContact.getLastName());
    thePatron.setXeroID(theContact.getContactID());
  }

  private String getXeroID(String string)
  {
    XeroContactClient contactClient;
    XeroContact theContact;

    contactClient = new XeroContactClient();
    contactClient.setUserID(getUserID());
    contactClient.setTokensFile(getTokensFile());
    contactClient.setSecretsFile(getSecretsFile());
    theContact = contactClient.getTheContact();
    return theContact.getContactID();
  }
}
