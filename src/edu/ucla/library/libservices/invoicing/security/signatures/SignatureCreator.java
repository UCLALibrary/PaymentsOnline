package edu.ucla.library.libservices.invoicing.security.signatures;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import java.io.BufferedReader;

import java.io.IOException;

import java.security.SignatureException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import javax.servlet.http.HttpServletRequest;

public class SignatureCreator
{
  private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

  public SignatureCreator()
  {
    super();
  }

  public static String createSignature( HttpServletRequest request )
    throws IOException
  {
    BufferedReader reader;
    StringBuffer buffer;
    String line;

    buffer = new StringBuffer( request.getMethod() ).append( "\n" );
    buffer.append( request.getRequestURI() ).append( "\n" );
    reader = request.getReader();

    while ( ( line = reader.readLine() ) != null )
      buffer.append( line ).append( "\n" );
    //reader.close();
    buffer.append( "-30-" );
    
    return buffer.toString();
  }

  public static String hashSignature( String data, String key )
    throws SignatureException
  {
    String result;

    result = null;

    try
    {
      // get an hmac_sha1 key from the raw key bytes
      SecretKeySpec signingKey =
        new SecretKeySpec( key.getBytes(), HMAC_SHA1_ALGORITHM );

      // get an hmac_sha1 Mac instance and initialize with the signing key
      Mac mac = Mac.getInstance( HMAC_SHA1_ALGORITHM );
      mac.init( signingKey );

      // compute the hmac on input data bytes
      byte[] rawHmac = mac.doFinal( data.getBytes() );

      // base64-encode the hmac
      result = Base64.encode( rawHmac );
    }
    catch ( Exception e )
    {
      throw new SignatureException( "Failed to generate HMAC : " +
                                    e.getMessage() );
    }
    return result;
  }
}
