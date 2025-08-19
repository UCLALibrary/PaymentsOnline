package edu.ucla.library.libservices.webservices.ecommerce.beans;

import java.util.ArrayList;

/**
 * Holds a collection of XeroPayment objects, for when an invoice links
 * to multiple Xero accounts/Transact item codes/UCLA FAUs
 */
public class XeroPaymentList
{
  private ArrayList<XeroPayment> Payments;
  
  public XeroPaymentList()
  {
    super();
    Payments = new ArrayList<>();
  }

  public void setPayments(ArrayList<XeroPayment> Payments)
  {
    this.Payments = Payments;
  }

  public ArrayList<XeroPayment> getPayments()
  {
    return Payments;
  }
}
