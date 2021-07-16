package edu.ucla.library.libservices.webservices.ecommerce.utility.strings;

public class StringHandler
{
  public StringHandler()
  {
    super();
  }
  
  public static String extractInvoiceID(String source)
  {
    return source.replace("alma", "");
  }
  
  public static String extractFeeType(String input)
  {
    return input.substring(input.indexOf("~") + 1);
  }
}
