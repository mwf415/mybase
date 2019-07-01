package cn.onlov.evaluate.util;



import cn.onlov.evaluate.core.dao.entities.User;
import org.apache.shiro.crypto.hash.SimpleHash;

public class PasswordHelper {
	//private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
	private String algorithmName = "md5";
	private int hashIterations = 2;

	public void encryptPassword(User user) {
//		String newPassword = new SimpleHash(algorithmName, user.getUserPwd(),  ByteSource.Util.bytes(Constants.SALT), hashIterations).toHex();
		String newPassword = new SimpleHash(algorithmName, user.getUserPwd()).toHex();
		user.setUserPwd(newPassword);

	}
	public static void main(String[] args) {
		PasswordHelper passwordHelper = new PasswordHelper();
		User user = new User();
		user.setLoginName("admin");
			user.setUserPwd("123456");
		passwordHelper.encryptPassword(user);
		System.out.println(user.getUserPwd());
	}
}
