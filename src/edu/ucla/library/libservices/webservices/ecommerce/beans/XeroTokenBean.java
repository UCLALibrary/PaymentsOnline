package edu.ucla.library.libservices.webservices.ecommerce.beans;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * JavaBean used to carry Xero token information through app
 */
public class XeroTokenBean
{
  // Xero access token used in Authorization header to verify access to API
  private String access_token;

  // Seconds until access token expires
  private String expires_in;

  // Token used to request new access token
  private String refresh_token;

  // Which Xero API units are accessible with current access token
  private String scope;

  public XeroTokenBean()
  {
    super();
  }

  public void setAccess_token(String access_token)
  {
    this.access_token = access_token;
  }

  public String getAccess_token()
  {
    return access_token;
  }

  public void setExpires_in(String expires_in)
  {
    this.expires_in = expires_in;
  }

  public String getExpires_in()
  {
    return expires_in;
  }

  public void setRefresh_token(String refresh_token)
  {
    this.refresh_token = refresh_token;
  }

  public String getRefresh_token()
  {
    return refresh_token;
  }

  public void setScope(String scope)
  {
    this.scope = scope;
  }

  public String getScope()
  {
    return scope;
  }

  @Override
  public boolean equals(Object theOther)
  {
    if (this == theOther)
      return true;
    if (theOther == null || getClass() != theOther.getClass())
      return false;

    XeroTokenBean aBean = (XeroTokenBean) theOther;

    return this.getAccess_token().trim().equals(aBean.getAccess_token().trim())
           && compareExpires(this.getExpires_in().trim(), aBean.getExpires_in().trim())
           && this.getRefresh_token().trim().equals(aBean.getRefresh_token().trim())
           && this.getScope().trim().equals(aBean.getScope().trim());
  }

  @Override
  public String toString()
  {
    return this.getAccess_token() + "\t" + this.getExpires_in() + "\t" + this.getRefresh_token()
           + this.getScope();
  }
  
  private boolean compareExpires(String localExpires, String otherExpires)
  {
    // case 1: both values are formatted time strings
    if ( localExpires.contains(":") && otherExpires.contains(":") )
    {
      // clipping milliseconds for convenience
      return LocalDateTime.parse(localExpires).truncatedTo(ChronoUnit.SECONDS)
                          .equals(LocalDateTime.parse(otherExpires).truncatedTo(ChronoUnit.SECONDS));
    }
    //case 2: neither is a formatted time string
    else if ( !localExpires.contains(":") && !otherExpires.contains(":") )
    {
      return localExpires.equals(otherExpires);
    }
    //case 3: one is formatted, the other isn't, therefore can't be equal
    else
    {
      return false;      
    }
  }
}
