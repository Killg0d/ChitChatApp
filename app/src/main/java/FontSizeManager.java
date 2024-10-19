import android.content.Context;
import android.content.SharedPreferences;

public class FontSizeManager {
    private static final String PREFS_NAME = "ChitChat";
    private static final String FONT_SIZE_KEY = "font_size_key";

    public static void saveFontSize(Context context, float fontSize) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(FONT_SIZE_KEY, fontSize);
        editor.apply();
    }

    public static float getFontSize(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        // Default font size (medium) can be 16f
        return sharedPreferences.getFloat(FONT_SIZE_KEY, 16f);
    }
}
