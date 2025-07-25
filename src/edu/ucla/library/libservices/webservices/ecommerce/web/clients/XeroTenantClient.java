/**
   * Connects to Xero API to retrieve tenant ID
   * @return String representation of a Xero tenant ID
   */
  public String getTenantID()
  {
    Client client;
    ClientResponse response;
    String authString;
    String json;
    WebResource webResource;
    Gson gson;
    XeroTenantID[] tenantBeans;

    gson = new Gson();
    authString = "Bearer ".concat(getAccessToken());

    client = Client.create();
    webResource = client.resource(getTenantURL());
    response = webResource.accept("application/json")
                          .header("Authorization", authString)
                          .get(ClientResponse.class);

    if (response.getStatus() == 200)
    {
      json = response.getEntity(String.class);
      tenantBeans = gson.fromJson(json, XeroTenantID[].class);
      return tenantBeans[0].getTenantId();
    }
    else
    {
      LOGGER.error("tenant service return code " + response.getStatus() + "\t" + response.getEntity(String.class));
      return null;
    }
  }
}