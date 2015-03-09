package dk.dmaa0214.controllerLayer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import dk.dmaa0214.modelLayer.Settings;

public class SettingsCtr {
	private static final byte[] key = "zukowskiFd4sW5cS".getBytes();
	private static final String transformation = "AES/ECB/PKCS5Padding";
	private static SecretKey sKey;
	private static Cipher cipher;
	private static String filepath;
		
	private static void init() {
		try {
			setFilePath();
		    sKey = new SecretKeySpec(key, "AES");
		    cipher = Cipher.getInstance(transformation);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException  e) {
			e.printStackTrace();
		}
	}
	
	private static void setFilePath() {
		filepath = "user.bin";
	}
	
	public static Settings loadUser(){
		init();
		File f = new File(filepath);
		Settings ret = null;
		if(f.exists()){
			FileInputStream fs = null;
			BufferedInputStream bis = null;
			CipherInputStream cis = null;
			ObjectInputStream ois = null;
			try {
				cipher.init(Cipher.DECRYPT_MODE, sKey);
				fs = new FileInputStream(f);
				bis = new BufferedInputStream(fs);
			    cis = new CipherInputStream(bis, cipher);
			    ois = new ObjectInputStream(cis);
			    
				ret = (Settings) ois.readObject();
				
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
				ret = null;
			} catch (Exception e) {
				ret = null;
				e.printStackTrace();
			}
			finally {
				closeStream(ois);
				closeStream(cis);
				closeStream(bis);
				closeStream(fs);
			}
		}
		return ret;
	}
	
	public static boolean saveSettings(Settings settings) {
		init();
		boolean ret = false;
		FileOutputStream fs = null;
		BufferedOutputStream bos = null;
		CipherOutputStream cos = null;
		ObjectOutputStream oos = null;
		try {
			cipher.init(Cipher.ENCRYPT_MODE, sKey);
			File f = new File(filepath);			
			
			fs = new FileOutputStream(f);
			bos = new BufferedOutputStream(fs);
			cos = new CipherOutputStream(bos, cipher);
			oos = new ObjectOutputStream(cos);
			oos.writeObject(settings);
			ret = true;
		} catch (IOException | InvalidKeyException e) {
			e.printStackTrace();
		} finally {
			closeStream(oos);
			closeStream(cos);
			closeStream(bos);
			closeStream(fs);
		}
		return ret;
	}
	
	public static void deleteSettings() {
		setFilePath();
		File f = new File(filepath);
		if(f.exists()) {
			f.delete();
		}
	}
		
	private static void closeStream(Closeable s){
	    try{
	        if(s!=null)s.close();
	    }catch(IOException e){
	    	
	    }
	}

}
