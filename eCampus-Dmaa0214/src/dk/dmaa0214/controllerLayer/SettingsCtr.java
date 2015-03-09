package dk.dmaa0214.controllerLayer;

import java.util.prefs.Preferences;

import dk.dmaa0214.modelLayer.Settings;

public class SettingsCtr {
	
	private static Preferences prefs;
	
	
	public static boolean saveSettings(Settings settings) {
		boolean ret = false;
		try {
			setPreferences();
			
			putSetting("username", settings.getUsername());
			putSetting("localpath", settings.getLocalPath());
			putSetting("sitepath", settings.getSitePath());
			
			ret = true;
		} catch (Exception e) {
			e.printStackTrace();
			ret = false;
		}
		System.out.println("SettingsCtr saveSettings done: " + ret);
		return ret;
	}
	
	public static Settings loadSettings() {
		Settings ret = null;
		try {
			setPreferences();
			
			
			ret = new Settings();
			ret.setUsername(prefs.get("username", null));
			ret.setLocalPath(prefs.get("localpath", null));
			ret.setSitePath(prefs.get("sitepath", null));
			
		} catch (Exception e) {
			e.printStackTrace();
			ret = null;
		}
		return ret;
	}
	
	public static boolean deleteSettings() {
		boolean ret = false;
		try {
			setPreferences();
			prefs.clear();
			ret = true;
		} catch (Exception e) {
			e.printStackTrace();
			ret = false;
		}
		return ret;
	}
	
	private static void putSetting(String key, String value) {
		if (key != null && value != null && !value.trim().isEmpty()) {
			prefs.put(key, value);
		}
	}
	
	private static void setPreferences() {
		prefs = Preferences.userNodeForPackage(SettingsCtr.class);
	}

}
