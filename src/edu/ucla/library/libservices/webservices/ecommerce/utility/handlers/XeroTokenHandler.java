package edu.ucla.library.libservices.webservices.ecommerce.utility.handlers;

import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroTokenBean;

import edu.ucla.library.libservices.webservices.ecommerce.web.clients.XeroTokenClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.log4j.Logger;

public class XeroTokenHandler
{
  private static final Logger LOGGER = Logger.getLogger(XeroTokenHandler.class);
  private static DateTimeFormatter FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
  private String secretsFile;
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
