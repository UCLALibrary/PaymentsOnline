package edu.ucla.library.libservices.webservices.ecommerce.adapters;

import java.util.Date;
import java.text.SimpleDateFormat;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DateAdapter
  extends XmlAdapter<String, Date>
{
  private static final String FORMAT_INPUT = "yyyy-MM-dd";
  private static final String FORMAT_OUTPUT = "MMMM dd, yyyy";


  public DateAdapter()
  {
    super();
  }

  @Override
  public Date unmarshal(String s)
    throws Exception
  {
    return new SimpleDateFormat(FORMAT_INPUT).parse(s.substring(0, s.lastIndexOf("T")));
  }

  @Override
  public String marshal(Date d)
    throws Exception
  {
    return new SimpleDateFormat(FORMAT_OUTPUT).format(d);
  }
}
