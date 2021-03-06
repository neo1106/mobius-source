/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.loginserver.crypt;

import java.security.MessageDigest;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PasswordHash
{
	private final static Logger _log = LoggerFactory.getLogger(PasswordHash.class);
	private final String name;
	
	/**
	 * Constructor for PasswordHash.
	 * @param name String
	 */
	public PasswordHash(String name)
	{
		this.name = name;
	}
	
	/**
	 * Method compare.
	 * @param password String
	 * @param expected String
	 * @return boolean
	 */
	public boolean compare(String password, String expected)
	{
		try
		{
			return encrypt(password).equals(expected);
		}
		catch (Exception e)
		{
			_log.error(name + ": encryption error!", e);
			return false;
		}
	}
	
	/**
	 * Method encrypt.
	 * @param password String
	 * @return String
	 * @throws Exception
	 */
	public String encrypt(String password) throws Exception
	{
		MessageDigest checksum = MessageDigest.getInstance(name);
		byte[] raw = password.getBytes("UTF-8");
		raw = checksum.digest(raw);
		return Base64.getEncoder().encodeToString(raw);
	}
}
