package com.scac.RLicServer;
import java.util.ArrayList;
/** class RLicToken.
*/
public final class RLicToken
{
   /** Constructor. */
   public RLicToken()
   {
      _Users = new ArrayList<String>();
   }
   /** Users
   *is a field to store all the allowed logins for the given IP subnet
   *Should be filled explicitly from XML-file
   */
   private ArrayList<String> _Users;
   public ArrayList<String> getUsers(){
 	return _Users;
   }
   public void setUsers(ArrayList<String> newUsers){
      _Users = newUsers;
   }
   public boolean hasUser(String login){
      boolean res = false;
      for (String name :_Users) {
      	if (login.equals(name)) {
      	   res = true;
      	   break;
      	}
      }
      return res;
   }
   private String NetMask;
   public String getNetMask(){
   	return NetMask;
   }
   public void setNetMask(String mask){
   	NetMask = mask;
   }
}
