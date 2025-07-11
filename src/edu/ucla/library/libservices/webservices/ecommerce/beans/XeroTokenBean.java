package edu.ucla.library.libservices.webservices.ecommerce.beans;

public class XeroTokenBean
{
  private String access_token;
  private String expires_in;
  private String refresh_token;
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

    return this.getAccess_token() == aBean.getAccess_token() && this.getExpires_in() == aBean.getExpires_in() &&
           this.getRefresh_token() == aBean.getRefresh_token() && this.getScope() == aBean.getScope();
  }
}
