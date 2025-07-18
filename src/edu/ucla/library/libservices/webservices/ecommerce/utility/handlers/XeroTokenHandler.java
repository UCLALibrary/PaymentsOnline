package edu.ucla.library.libservices.webservices.ecommerce.utility.handlers;

import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroTokenBean;

import edu.ucla.library.libservices.webservices.ecommerce.web.clients.XeroTokenClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.log4j.Logger;

public class XeroTokenHandler
{
  private static final Logger LOGGER = Logger.getLogger(XeroTokenHandler.class);
  // Date format: YYYY-MM-DDTHH:mm:SS'
  private static DateTimeFormatter FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
  //properties file with URIs and IDs to access Xero API
  private String secretsFile;
  //file with OAuth tokens to access Xero API
  private String tokensFile;
  
  public XeroTokenHandler()
  {
    super();
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
   * Reads tokens file
   * Checks if access token has expired
   * If token has expired, uses XeroTokenClient to retrieve new tokens
   * @return bean holding OAuth tokens
   */
  public XeroTokenBean getTokens()
  {
    XeroTokenBean theBean;
    TokenFileHandler fileHandler;
    fileHandler = new TokenFileHandler();
    fileHandler.setTokensFile(getTokensFile());
    theBean = fileHandler.readTokensFile();
    LocalDateTime expireTime = LocalDateTime.parse(theBean.getExpires_in(), FORMAT);
    if (!LocalDateTime.now().isBefore(expireTime))
    {
      XeroTokenClient tokenClient;
      tokenClient = new XeroTokenClient();
      tokenClient.setRefreshToken(theBean.getRefresh_token());
      tokenClient.setSecretsFile(getSecretsFile());
      fileHandler.writeTokensFile(tokenClient.getTokens());
      theBean = fileHandler.readTokensFile();
    }
    return theBean;
  }
}
