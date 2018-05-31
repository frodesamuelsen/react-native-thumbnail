
package me.hauvo.thumbnail;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.media.MediaMetadataRetriever;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.File;
import java.io.OutputStream;
import java.io.FileOutputStream;


public class RNThumbnailModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public RNThumbnailModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNThumbnail";
  }

  public static String getMD5(String string) {
      byte[] hash;

      try {
          hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
      } catch (NoSuchAlgorithmException e) {
          e.printStackTrace();
          return null;
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
          return null;
      }

      StringBuilder hex = new StringBuilder(hash.length * 2);
      for (byte b : hash) {
          if ((b & 0xFF) < 0x10)
              hex.append("0");
          hex.append(Integer.toHexString(b & 0xFF));
      }

      return hex.toString();
  }


  @ReactMethod
  public void get(String filePath, Promise promise) {
  	try {
	    filePath = filePath.replace("file://","");
	    String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/thumb";
	    String fileName = "thumb-" + getMD5(filePath) + ".jpeg";

	    File cache = new File(fullPath, fileName);
	    if (cache.exists()) {
	      WritableMap map = Arguments.createMap();
	      map.putString("path", "file://" + fullPath + '/' + fileName);
	      promise.resolve(map);
	      return;
	    }

	    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
	    retriever.setDataSource(filePath);
	    Bitmap image = retriever.getFrameAtTime(1000000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);

		File dir = new File(fullPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		OutputStream fOut = null;
		// String fileName = "thumb-" + UUID.randomUUID().toString() + ".jpeg";

		File file = new File(fullPath, fileName);
		file.createNewFile();
		fOut = new FileOutputStream(file);

		// 100 means no compression, the lower you go, the stronger the compression
		image.compress(Bitmap.CompressFormat.JPEG, 60, fOut);
		fOut.flush();
		fOut.close();

		// MediaStore.Images.Media.insertImage(reactContext.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());

		WritableMap map = Arguments.createMap();

		map.putString("path", "file://" + fullPath + '/' + fileName);
		map.putDouble("width", image.getWidth());
		map.putDouble("height", image.getHeight());

		promise.resolve(map);

		} catch (Exception e) {
			//Log.e("E_RNThumnail_ERROR", e.getMessage());
			promise.reject("E_RNThumnail_ERROR", e);
		}
    }
}
