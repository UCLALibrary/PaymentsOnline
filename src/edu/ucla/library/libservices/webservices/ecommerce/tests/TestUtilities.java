package edu.ucla.library.libservices.webservices.ecommerce.tests;

import com.google.gson.Gson;

import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroTokenBean;

import edu.ucla.library.libservices.webservices.ecommerce.utility.handlers.TokenFileHandler;

import java.io.IOException;

import java.net.ServerSocket;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestUtilities
{
  private static DateTimeFormatter FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
  private static final String ACCESS_TOKEN = "fake_access_token";
  private static final String REFRESH_TOKEN = "fake_refresh_token";
  private static final String FUTURE_SECS = "1800";
  private static final String PAST_SECS = "-1800";
  private static final String SCOPE = "accounting.settings accounting.transactions accounting.contacts offline_access";
  private static final String BASE_PATH = Paths.get(System.getProperty("user.dir"), "public_html", "resources").toString();
  private static final String EXPIRED_FILE = Paths.get(BASE_PATH, "expired.txt").toString();
  private static final String FUTURE_FILE = Paths.get(BASE_PATH, "future_proof.txt").toString();
  private static final String WRITE_FILE = Paths.get(BASE_PATH, "output.txt").toString();

  public TestUtilities()
  {
  }

  public static int findFreePort()
    throws IOException
  {
    try (ServerSocket socket = new ServerSocket(0))
    {
      socket.setReuseAddress(true);
      return socket.getLocalPort();
    }
  }
  
  public static String getFutureFile()
  {
    return FUTURE_FILE;
  }
  
  public static String getExpiredFile()
  {
    return EXPIRED_FILE;
  }
  
  public static String getWriteFile()
    {
      return WRITE_FILE;
    }

  public static void writeFutureFile()
  {
    Gson gson;
    TokenFileHandler handler;
    XeroTokenBean future_bean;

    future_bean = new XeroTokenBean();
    future_bean.setAccess_token(ACCESS_TOKEN);
    future_bean.setExpires_in(FUTURE_SECS);
    future_bean.setRefresh_token(REFRESH_TOKEN);
    future_bean.setScope(SCOPE);

    gson = new Gson();

    handler = new TokenFileHandler();
    handler.setTokensFile(FUTURE_FILE);
    handler.writeTokensFile(gson.toJson(future_bean));
  }
  
  public static void writeExpiredFile()
  {
    Gson gson;
    TokenFileHandler handler;
    XeroTokenBean expired_bean;

    expired_bean = new XeroTokenBean();
    expired_bean.setAccess_token(ACCESS_TOKEN);
    expired_bean.setExpires_in(PAST_SECS);
    expired_bean.setRefresh_token(REFRESH_TOKEN);
    expired_bean.setScope(SCOPE);

    gson = new Gson();

    handler = new TokenFileHandler();
    handler.setTokensFile(EXPIRED_FILE);
    handler.writeTokensFile(gson.toJson(expired_bean));
  }
  
  public static XeroTokenBean populateBean(String theTime, boolean format)
  {
    XeroTokenBean theBean;

    theBean = new XeroTokenBean();
    theBean.setAccess_token(ACCESS_TOKEN);
    if (format)
    {
      theBean.setExpires_in(LocalDateTime.now().plusSeconds(Long.parseLong(theTime)).format(FORMAT));;
    }
    else
    {
      theBean.setExpires_in(theTime);;
    }
    
    theBean.setRefresh_token(REFRESH_TOKEN);
    theBean.setScope(SCOPE);
    
    return theBean;
  }
  
  public static void clearFiles()
  {
    try
    {
      Files.deleteIfExists(Paths.get(EXPIRED_FILE));
      Files.deleteIfExists(Paths.get(FUTURE_FILE));
      Files.deleteIfExists(Paths.get(WRITE_FILE));
    }
    catch (IOException ioe)
    {
      ioe.printStackTrace();
    }

  }
}
