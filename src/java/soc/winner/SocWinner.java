package soc.winner;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SocWinner {
	
	private static final String password = "424242";
	
	public static final String md5Pass = encDec(password);
	
	private static final int totalWinCount = 4;
	
	private static int winCount = 0;
	
	public static void addWin() {
		winCount++;
	}
	
	public static boolean winner(){
		return (winCount == totalWinCount);
	}
	
	public static String encDec(String password) {
		
		try {
			
			final MessageDigest md = MessageDigest.getInstance("SHA1");
			md.reset();
			md.update(password.getBytes());
			final StringBuilder sb = new StringBuilder();
			final byte []encodedPassword = md.digest();
			for (int i = 0; i < encodedPassword.length; i++) {
				if ((encodedPassword[i] & 0xff) < 0x10) {
					sb.append("0");
				}
				
				sb.append(Long.toString(encodedPassword[i] & 0xff, 16));
			}
			return sb.toString();
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public static void main(String[] args) {
		
		System.out.println("El regalo de la boda de Johny y Mary .... el secreto reside en ... " + md5Pass);
		
		if (args.length > 0) {
			if (md5Pass.equals(encDec(args[0])))
				System.out.println("YEAH !!! Lo has conseguido ..... disfruta la pasta !! Codigo Correcto: " + password);
			else
				System.out.println("No No No .... no has dicho la palabra magica ....");
		}
		
	}
	
	
}