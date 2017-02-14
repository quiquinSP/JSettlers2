package soc.winner;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import javax.swing.JOptionPane;

public class SOCWinner {
	
	private static final String password = "424242";
	
	public static final String md5Pass = encDec(password);
	
	private static int totalWinCount = 3;
	
	private static int winCount = 0;
	
	public static void addWin() {
		winCount++;
		saveWins();
	}
	
	private static void loadWins() {
		
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("game.properties");
			// load a properties file
			prop.load(input);
			// get the property value and print it out
			winCount = Integer.parseInt(prop.getProperty("games.won"));
		} catch (IOException e){e.printStackTrace();}
		
	}
	
	private static void saveWins() {	
		Properties prop = new Properties();
		OutputStream output = null;
		try {
			output = new FileOutputStream("game.properties");
			// set the properties value
			prop.setProperty("games.won", Integer.toString(winCount));
			prop.store(output, null);

		} catch (IOException io) {io.printStackTrace();}
	}
	
	public static boolean winner(){
		loadWins();
		return (winCount >= totalWinCount);
	}
	
    public static void createWinnerDialog() {
    	JOptionPane.showMessageDialog(null,"Enhorabuena Mary y Johny ... una ultima cosa, si digo 'SHA1' y '" + SOCWinner.md5Pass + "'.\n"
    			+ "El codigo de la caja obtendreis pero debeis elegir sabiamente. PD Que la criptografia te guie Johny :P\n"
    			+ "Si quereis buscar en vez de usar el ingenio ... https://github.com/quiquinSP/JSettlers2\n"
    			+ "PD2 Hasta Yenko podia haber editado el fichero 'game.properties' poner un 3 y ale !!!");
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