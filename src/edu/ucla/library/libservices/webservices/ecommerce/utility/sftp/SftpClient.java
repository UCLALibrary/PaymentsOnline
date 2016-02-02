package edu.ucla.library.libservices.webservices.ecommerce.utility.sftp;

import java.io.File;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;

public class SftpClient
{
  private File keyFile;
  private FileSystemManager fsManager = null;
  private FileSystemOptions opts = null;

  private String keyfilePath;
  private String localDir;
  private String remoteDir;
  private String fileName;
  String user;
  String host;

  public SftpClient()
  {
    super();
  }

  public void getSftpConnect()
  {
    try
    {
      opts = new FileSystemOptions();
      SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking( opts,
                                                                          "no" );
      SftpFileSystemConfigBuilder.getInstance().setUserInfo( opts,
                                                             new SftpUserInfo() );
      keyFile = new File( getKeyfilePath() );
      SftpFileSystemConfigBuilder.getInstance().setIdentities( opts,
                                                               new File[]
          { keyFile } );
      fsManager = VFS.getManager();
    }
    catch ( FileSystemException fse )
    {
      System.err.println( "problem with sftp connect: " +
                          fse.getMessage() );
      fsManager = null;
    }
  }

  public void uploadFile()
  {
    File file;
    FileObject localFile;
    FileObject remoteFile;
    StandardFileSystemManager manager;
    String startPath;

    file = new File( getLocalDir().concat( "/" ).concat( getFileName() ) );

    startPath =
        "sftp://" + getUser() + "@" + getHost() +
        "/" + getRemoteDir() + "/" + getFileName();

    manager = new StandardFileSystemManager();

    // Create local file object
    try
    {
      manager.init();
      localFile = manager.resolveFile( file.getAbsolutePath() );
      remoteFile = manager.resolveFile( startPath, opts );
      remoteFile.copyFrom( localFile, Selectors.SELECT_SELF );
    }
    catch ( FileSystemException fse )
    {
      fse.printStackTrace();
    }
    finally
    {
      manager.close();
    }
  }

  public void setKeyfilePath( String keyfilePath )
  {
    this.keyfilePath = keyfilePath;
  }

  private String getKeyfilePath()
  {
    return keyfilePath;
  }

  public void setLocalDir( String localDir )
  {
    this.localDir = localDir;
  }

  private String getLocalDir()
  {
    return localDir;
  }

  public void setRemoteDir( String remoteDir )
  {
    this.remoteDir = remoteDir;
  }

  private String getRemoteDir()
  {
    return remoteDir;
  }

  public void setFileName( String fileName )
  {
    this.fileName = fileName;
  }

  private String getFileName()
  {
    return fileName;
  }

  public void setUser( String user )
  {
    this.user = user;
  }

  private String getUser()
  {
    return user;
  }

  public void setHost( String host )
  {
    this.host = host;
  }

  private String getHost()
  {
    return host;
  }
}
