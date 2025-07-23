package edu.ucla.library.libservices.webservices.ecommerce.utility.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertiesHandler
{
  private static final Logger LOGGER = Logger.getLogger(PropertiesHandler.class);
  private String fileName;

  public PropertiesHandler()
  {
    super();
  }

  public void setFileName(String fileName)
  {
    this.fileName = fileName;
  }

  public String getFileName()
  {
    return fileName;
  }

  public Properties loadProperties()
  {
    Properties secrets = new Properties();
    try
    {
      secrets.load(new FileInputStream(new File(getFileName())));
    }
    catch (IOException ioe)
    {
      LOGGER.fatal("problem with props file" + ioe.getMessage());
    }
    return secrets;
  }

}
