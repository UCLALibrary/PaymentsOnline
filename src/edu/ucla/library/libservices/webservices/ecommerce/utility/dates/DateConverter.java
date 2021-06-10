package edu.ucla.library.libservices.webservices.ecommerce.utility.dates;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateConverter
{
  private static final String FORMAT_INPUT = "yyyy-MM-dd";
  private static final String FORMAT_OUTPUT = "MMMM dd, yyyy";

  public DateConverter()
  {
    super();
  }
  
  public static String convert(String input)
  {
    try
    {
      return new SimpleDateFormat(FORMAT_OUTPUT).format(new SimpleDateFormat(FORMAT_INPUT).parse(input));
    }
    catch (ParseException e)
    {
      return null;
    }
  }
}
