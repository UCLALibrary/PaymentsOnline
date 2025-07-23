package edu.ucla.library.libservices.webservices.ecommerce.beans;

public class XeroTenantID
{
  private String id;
  private String tenantId;
  private String tenantType;
  private String tenantName;

  public XeroTenantID()
  {
    super();
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public String getId()
  {
    return id;
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
