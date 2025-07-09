package edu.ucla.library.libservices.webservices.ecommerce.utility.handlers;

import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroTokenBean;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.Gson;

import edu.ucla.library.libservices.webservices.ecommerce.utility.db.DataHandler;

import java.io.FileNotFoundException;

import org.apache.log4j.Logger;

public class TokenFileHandler
{
  private static final Logger LOGGER = Logger.getLogger(DataHandler.class);
  private static DateTimeFormatter FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
  private String tokensFile;

  public TokenFileHandler()
  {
    super();
  }

  public void writeTokensFile(String json)
  {
    BufferedWriter writer;
    Gson gson;
    LocalDateTime expireTime;
    XeroTokenBean tokens;

    gson = new Gson();
    tokens = gson.fromJson(json, XeroTokenBean.class);
    expireTime = LocalDateTime.now().plusSeconds(Long.parseLong(tokens.getExpires_in()));

    try
    {
      writer = new BufferedWriter(new FileWriter(new File(getTokensFile())));
      writer.write("access_token = " + tokens.getAccess_token());
      writer.newLine();

      writer.write("expires_in = " + expireTime.format(FORMAT));
      writer.newLine();

      writer.write("refresh_token = " + tokens.getRefresh_token());
      writer.newLine();

      writer.write("scope = " + tokens.getScope());
      writer.newLine();

      writer.newLine();
      writer.flush();
      writer.close();
    }
    catch (IOException ioe)
    {
      LOGGER.error(ioe.getMessage());
    }
  }

  public XeroTokenBean readTokensFile()
  {
    BufferedReader reader;
    String line;
    XeroTokenBean tokens;

    tokens = new XeroTokenBean();
    try
    {
      reader = new BufferedReader(new FileReader(new File(getTokensFile())));
      while ((line = reader.readLine()) != null)
      {
        String[] lineTokens;
        lineTokens = line.split(" = ");
        switch (lineTokens[0])
        {
          case "access_token":
            tokens.setAccess_token(lineTokens[1]);
            break;
          case "expires_in":
            tokens.setExpires_in(lineTokens[1]);
            break;
          case "refresh_token":
            tokens.setRefresh_token(lineTokens[1]);
            break;
          case "scope":
            tokens.setScope(lineTokens[1]);
            break;
          default:
            ;
        }
      }
      reader.close();
    }
    catch (FileNotFoundException fnfe)
    {
      LOGGER.error(fnfe.getMessage());
    }
    catch (IOException ioe)
    {
      LOGGER.error(ioe.getMessage());
    }
    return tokens;
  }

  public void setTokensFile(String tokensFile)
  {
    this.tokensFile = tokensFile;
  }

  public String getTokensFile()
  {
    return tokensFile;
  }
}
