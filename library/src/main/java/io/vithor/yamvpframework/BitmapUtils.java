package io.vithor.yamvpframework;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import com.orhanobut.logger.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by Hazer on 1/4/16.
 */
public class BitmapUtils {
    public static float pxToDip(Context context, float px) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, context.getResources().getDisplayMetrics());
    }

    public static float dipToPx(Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, dp, context.getResources().getDisplayMetrics());
    }

    @SuppressWarnings("deprecation")
    public static Bitmap loadImage(int resourceId, Context context) {
        Display defaultDisplay = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        final Point point = new Point();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            point.x = defaultDisplay.getWidth();
            point.y = defaultDisplay.getHeight();
        } else {
            defaultDisplay.getSize(point);
        }

        if (isLargeScreen(context)) {
            point.y *= 2.0F;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), resourceId, options);
        int sampleSize = (int) ((float) options.outHeight / point.y);
        if (sampleSize <= 0) {
            sampleSize = 1;
        }

        BitmapFactory.Options options1 = new BitmapFactory.Options();
        options1.inSampleSize = sampleSize;
        options1.inPurgeable = true;

        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options1);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static boolean isLargeScreen(Context context) {
        int type = getScreenType(context);
        return type == 3 || type == 4;
    }

    public static int getScreenType(Context context) {
        return 15 & context.getResources().getConfiguration().screenLayout;
    }

    public static Bitmap fromAssets(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();

        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            // handle exception
        }

        return bitmap;
    }

    // Scale and keep aspect ratio
    public static Bitmap scaleToFitWidth(Bitmap b, int width) {
        float factor = width / (float) b.getWidth();
        return Bitmap.createScaledBitmap(b, width, (int) (b.getHeight() * factor), false);
    }

    // Scale and keep aspect ratio
    public static Bitmap scaleToFitHeight(Bitmap b, int height) {
        float factor = height / (float) b.getHeight();
        return Bitmap.createScaledBitmap(b, (int) (b.getWidth() * factor), height, false);
    }

    public static Bitmap scaleDownToFit(Bitmap b, int maxSize) {
        int width = b.getWidth();
        int height = b.getHeight();

        if (width > height) {
            // landscape
            return BitmapUtils.scaleToFitHeight(b, maxSize);
        } else if (height > width) {
            // portrait
            return BitmapUtils.scaleToFitWidth(b, maxSize);
        }
        return b;
    }

    public static Bitmap decodeAndResizeUri(Context context, Uri imageUri, int imageMaxSize) throws FileNotFoundException {
        Bitmap b = null;

        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        InputStream is = context.getContentResolver().openInputStream(imageUri);
        BitmapFactory.decodeStream(is, null, o);
        try {
            is.close();
            int scale = 1;
            if (o.outHeight > imageMaxSize || o.outWidth > imageMaxSize) {
                scale = (int) Math.pow(2, (int) Math.ceil(Math.log(imageMaxSize /
                        (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
            }

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            is = context.getContentResolver().openInputStream(imageUri);
            b = BitmapFactory.decodeStream(is, null, o2);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }

    public static File decodeAndResize(Context context, File imageFile, File newFile, int imageMaxSize) throws FileNotFoundException {
        Bitmap b = null;

        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        InputStream is = context.getContentResolver().openInputStream(Uri.fromFile(imageFile));
        BitmapFactory.decodeStream(is, null, o);
        try {
            is.close();
            int scale = 1;
            if (o.outHeight > imageMaxSize || o.outWidth > imageMaxSize) {
                scale = (int) Math.pow(2, (int) Math.ceil(Math.log(imageMaxSize /
                        (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
            }

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            is = context.getContentResolver().openInputStream(Uri.fromFile(imageFile));
            b = BitmapFactory.decodeStream(is, null, o2);

            Logger.d("Bitmap Size: " + b.getWidth() + "x" + b.getHeight());

            OutputStream os;

            os = new FileOutputStream(newFile);
            b.compress(Bitmap.CompressFormat.JPEG, 70, os);
            os.flush();
            os.close();

//            int size = b.getRowBytes() * b.getHeight();
//            ByteBuffer byteBuffer = ByteBuffer.allocate(size);
//            writeByteBuffer(byteBuffer, newFile);

            return newFile;
        } catch (IOException e) {
            e.printStackTrace();
            Logger.e(e, "Failed resizing");
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignored) {

                }
            }
        }
        return null;
    }

    public static void writeByteBuffer(ByteBuffer byteBuffer, File newFile) throws IOException {
        // Set to true if the bytes should be appended to the file;
        // set to false if the bytes should replace current bytes
        // (if the file exists)
        boolean append = false;

        // Create a writable file channel
        FileChannel wChannel = new FileOutputStream(newFile, append).getChannel();

        // Write the ByteBuffer contents; the bytes between the ByteBuffer's
        // position and the limit is written to the file
        wChannel.write(byteBuffer);

        // Close the file
        wChannel.close();
    }

    /**
     * Returns Width or Height of the picture, depending on which size is smaller. Doesn't actually
     * decode the picture, so it is pretty efficient to run.
     */
    public static int getSmallerExtentFromBytes(byte[] bytes) {
        final BitmapFactory.Options options = new BitmapFactory.Options();

        // don't actually decode the picture, just return its bounds
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

        // test what the best sample size is
        return Math.min(options.outWidth, options.outHeight);
    }

    /**
     * Returns Width or Height of the picture, depending on which size is smaller. Doesn't actually
     * decode the picture, so it is pretty efficient to run.
     */
    public static int getLargerExtent(InputStream is) {
        final BitmapFactory.Options options = new BitmapFactory.Options();

        // don't actually decode the picture, just return its bounds
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, options);

        // test what the best sample size is
        return Math.max(options.outWidth, options.outHeight);
    }

    public static String bitmapToBase64(Bitmap bitmap, boolean recycle) {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
        if (recycle) {
            recycleBitmap(bitmap);
            System.gc();
        }
        byte[] ba = bao.toByteArray();
        try {
            bao.close();
            bao = null;
        } catch (IOException e) {
            // Ignore
        }
        return "data:image/jpeg;base64," + Base64.encodeToString(ba, Base64.NO_WRAP);
    }


    public static String inputStreamToBase64(InputStream is) {
        InputStream base64InputStream = new Base64InputStream(is, Base64.NO_WRAP);

        //Now that we have the InputStream, we can read it and put it into the String
        final StringWriter writer = new StringWriter();
        try {
            YAIOUtils.copy(base64InputStream, writer, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "data:image/jpeg;base64," + writer.toString();
    }

    public static String base64FromUri(Context context, Uri imageUri, int newSize) throws FileNotFoundException {
        if (newSize > 0) {
            return bitmapToBase64(decodeAndResizeUri(context, imageUri, newSize), true);
        }
        return inputStreamToBase64(context.getContentResolver().openInputStream(imageUri));
    }

    private static void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }
}
