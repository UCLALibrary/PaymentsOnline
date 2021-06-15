package edu.ucla.library.libservices.webservices.ecommerce.utility.strings;

public class StringHandler
{
  public StringHandler()
  {
    super();
  }
  
  public static String extractInvoiceID(String source)
  {
    String temp;
    temp = source.replace("alma", "");
    return temp.substring(0, temp.indexOf("~"));
  }
  
  public static String extractFeeType(String input)
  {
    return input.substring(input.indexOf("~") + 1);
  }
}
