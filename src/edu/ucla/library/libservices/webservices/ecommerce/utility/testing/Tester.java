package edu.ucla.library.libservices.webservices.ecommerce.utility.testing;

import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroTokenBean;

public class Tester
{

  public Tester()
  {
    super();
  }

  public static void main(String[] args)
    throws Exception
  {
    XeroTokenBean expired;
    expired = new XeroTokenBean();
    expired.setAccess_token("eyJhbGciOiJSUzI1Ni");
    expired.setExpires_in("1800");
    expired.setRefresh_token("wSzpv1rx0k9gCkvGrzXT");
    expired.setScope("accounting.settings accounting.transactions accounting.contacts offline_access");

    String json;
    StringBuffer edited;
    String access;
    json =
      "{\"access_token\":\"eyJhbGciOiJSUzI1NiIsImt\",\"expires_in\":1800,\"token_type\":\"Bearer\",\"refresh_token\":\"qFpgy6tDeAKr4mAo_3bICSaTfPvOBzkHkba34-0p6-k\",\"scope\":\"accounting.settings accounting.transactions accounting.contacts offline_access\"}";
    access = json.substring(1, json.indexOf(","));
    access = access.replaceAll("\"", "").replace(":", "").replace("access_token", "");
    System.out.println(access);
    edited = new StringBuffer(json);
    edited = edited.insert(json.lastIndexOf("}"), ",\"tenant_id\":\"d9e3104a-1995-4ab7-b3f7-c889b9e7338a\"");
    System.out.println(edited);
  }

}
