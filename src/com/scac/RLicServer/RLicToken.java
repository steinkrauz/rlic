package com.scac.RLicServer;
import java.util.ArrayList;
/** class RLicToken.
*/
public final class RLicToken
{
   /** Constructor. */
   public RLicToken()
   {
      _Users = new ArrayList();
   }
   /** Users
   *is a field to store all the allowed logins for the given IP subnet
   *Should be filled explicitly from XML-file
   */
   private ArrayList _Users;
   public ArrayList getUsers(){
 	return _Users;
   }
   public void setUsers(ArrayList newUsers){
      _Users = newUsers;
   }
   public boolean hasUser(String login){
      boolean res = false;
      String name;
      for (int i = 0; i < _Users.size(); i++) {
			name = (String) _Users.get(i);
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
