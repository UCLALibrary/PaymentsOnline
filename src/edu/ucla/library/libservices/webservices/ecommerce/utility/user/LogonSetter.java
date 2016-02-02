package edu.ucla.library.libservices.webservices.ecommerce.utility.user;

import edu.ucla.library.libservices.invoicing.utiltiy.testing.ContentTests;

import org.apache.log4j.Logger;

public class LogonSetter
{
  private String userID;
  private String uidParam;
  private String shibUID;
  private String shibName;
  private static final String BAD_CREDENTIAL = "000000000";
  private Logger log;

  public LogonSetter()
  {
    super();
    log = Logger.getLogger( LogonSetter.class );
  }

  public String getUserID()
  {
    userID = null;

    //check for uidParam from receipt page
    if ( !ContentTests.isEmpty( getUidParam() ) &&
         !getUidParam().equalsIgnoreCase( BAD_CREDENTIAL ) )
    {
      log.info( "using UID param" );
      userID = getUidParam();
    }
    //otherwise check for UID from IdP
    else if ( !ContentTests.isEmpty( getShibUID() ) &&
              !getShibUID().equalsIgnoreCase( BAD_CREDENTIAL ) &&
              ContentTests.isUID( getShibUID() ) )
    {
      log.info( "using shib UID" );
      userID = getShibUID();
    }
    // otherwise check for user name from IdP
    else if ( !ContentTests.isEmpty( getShibName() ) )
    {
      log.info( "using shib name" );
      if ( getShibName().indexOf( "@" ) != -1 )
      {
        userID =
            getShibName().substring( 0, getShibName().indexOf( "@" ) );
      }
      else
      {
        userID = getShibName();
      }
    }
    else
    {
      log.info( "no valid param or shib attribute" );
      userID = "-1";
    }

    log.info( "returning " + userID + " as user ID" );
    return userID;
  }

  public void setUidParam( String uidParam )
  {
    this.uidParam = uidParam;
  }

  private String getUidParam()
  {
    return uidParam;
  }

  public void setShibUID( String shibUID )
  {
    this.shibUID = shibUID;
  }

  private String getShibUID()
  {
    return shibUID;
  }

  public void setShibName( String shibName )
  {
    this.shibName = shibName;
  }

  private String getShibName()
  {
    return shibName;
  }
}
