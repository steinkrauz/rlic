package com.scac.RLicServer;
import java.util.ArrayList;

/** class RLicConfig.
*Contains Server configuration as well as some licensing data
*/
public final class RLicConfig
{
   /** Constructor. */
   public RLicConfig()
   {
	   _Tokens = new ArrayList<RLicToken>();
   }

   private int _ServerPort;
   public int getServerPort(){
   	return _ServerPort;
   }
   public void setServerPort(int port){
   	_ServerPort = port;
   }

   private String _LogPath;
   public String getLogPath(){
   	return _LogPath;
   }
   public void setLogPath(String path){
   	_LogPath = path;
   }

   private ArrayList<RLicToken> _Tokens;
   public  ArrayList<RLicToken> getTokens(){
   	return _Tokens;
   }
   public void setTokens(ArrayList<RLicToken> data){
   	_Tokens = data;
   }
}