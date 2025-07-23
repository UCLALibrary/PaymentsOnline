package edu.ucla.library.libservices.webservices.ecommerce.beans;

/**
 * Java bean to model a tenant access in Xero
 */
public class XeroTenantID
{
  private String tenantId;
  private String tenantType;
  private String tenantName;

  public XeroTenantID()
  {
    super();
  }

  public void setTenantId(String tenantId)
  {
    this.tenantId = tenantId;
  }

  public String getTenantId()
  {
    return tenantId;
  }

  public void setTenantType(String tenantType)
  {
    this.tenantType = tenantType;
  }

  public String getTenantType()
  {
    return tenantType;
  }

  public void setTenantName(String tenantName)
  {
    this.tenantName = tenantName;
  }

  public String getTenantName()
  {
    return tenantName;
  }
}