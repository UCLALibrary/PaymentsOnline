package edu.ucla.library.libservices.webservices.ecommerce.web.clients;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import edu.ucla.library.libservices.webservices.ecommerce.constants.XeroConstants;
import edu.ucla.library.libservices.webservices.ecommerce.utility.handlers.PropertiesHandler;
import edu.ucla.library.libservices.webservices.ecommerce.utility.handlers.XeroTokenHandler;

import java.util.Properties;

public abstract class AbstractXeroClient
{
  // port for http/s connections, mostly used for unit tests
  protected int port;
  // collection of values needed to access Xero API
  protected Properties secrets;
  // path for properties file with URIs and IDs to access Xero API
  protected String secretsFile;
  // path for file with OAuth tokens to access Xero API
  protected String tokensFile;
  
  public AbstractXeroClient()
  {
    super();
  }

  public void setPort(int port)
  {
    this.port = port;
  }

  public int getPort()
  {
    return port;
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

  /**
   * utility method to retrieve the properties needed by class
   */
  protected void loadProperties()
  {
    // utility to retrieve properties
    PropertiesHandler secretGetter;
    secretGetter = new PropertiesHandler();
    secretGetter.setFileName(getSecretsFile());
    secrets = secretGetter.loadProperties();
  }

  /**
   * @return String representation of OAuth token used to call Xero API
   */
  protected String getAccessToken()
  {
    //utility to retrieve current access/refresh tokens
    XeroTokenHandler tokenGetter;
    tokenGetter = new XeroTokenHandler();
    tokenGetter.setSecretsFile(getSecretsFile());
    tokenGetter.setTokensFile(getTokensFile());
    return tokenGetter.getTokens().getAccess_token();
  }

  protected String getTenantID()
  {
    return secrets.getProperty(XeroConstants.TENANT_ID);
  }

 /**
   * kludgey method to handle possible non-stamdard port number,
   * needed for unit tests
   * @param queryOrID Query string or invoice/account ID passed to API
   * @param type Whether call for account or invoice data
   * @return formatted URL for API call
   */
  protected String buildURL(String queryOrID, String type)
  {
    String url;
    url = type.equals(XeroConstants.ACCOUNT) ? getAccountURL() : getInvoiceURL();
    if ( getPort() != 0 )
    {
      url = url.replace("{port}", ":".concat(String.valueOf(getPort())));
    }
    return url.concat(queryOrID);
  }
  
  protected WebResource getWebResource(String url)
  {
    Client client;
    WebResource webResource;

    client = Client.create();
    webResource = client.resource(url);

    return webResource;
  }
  
  protected ClientResponse getResponse(WebResource resource)
  {
    return resource.accept(XeroConstants.JSON_ACCEPT)
                          .header(XeroConstants.AUTH_HEADER, getAuthString())
                          .header(XeroConstants.TENANT_HEADER, getTenantID())
                          .get(ClientResponse.class);
  }
  
  protected String getAuthString()
  {
    return XeroConstants.AUTH_BASE.concat(getAccessToken());
  }

  private String getAccountURL()
  {
    return secrets.getProperty(XeroConstants.ACCOUNT_URL);
  }

  private String getInvoiceURL()
  {
    return secrets.getProperty(XeroConstants.INVOICE_URL);
  }
}
