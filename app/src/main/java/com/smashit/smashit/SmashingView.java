package com.smashit.smashit;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * View for drawing the smashing on screen
 */
public class SmashingView extends SurfaceView implements SurfaceHolder.Callback {
  private Bitmap originalBackground;
  private Bitmap smashedBackground;
  private SmashingThread smashingThread;
  Context context;
  float scale_x;
  float scale_y;

  volatile float touched_x, touched_y;
  volatile boolean touched = false;

  public SmashingView (Context ctxt, AttributeSet attrs) {
    super (ctxt, attrs);
    context = ctxt;
    getHolder ().addCallback (this);
  }

  public static int calculateInSampleSize (BitmapFactory.Options options, int reqWidth, int reqHeight) {
    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {

      final int halfHeight = height / 2;
      final int halfWidth = width / 2;

      // Calculate the largest inSampleSize value that is a power of 2 and keeps both
      // height and width larger than the requested height and width.
      while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
        inSampleSize *= 2;
      }
    }

    return inSampleSize;
  }

  public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
    // First decode with inJustDecodeBounds=true to check dimensions
    final BitmapFactory.Options options = new BitmapFactory.Options ();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeResource (res, resId, options);

    // Calculate inSampleSize
    options.inSampleSize = calculateInSampleSize (options, reqWidth, reqHeight);

    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false;
    return BitmapFactory.decodeResource (res, resId, options);
  }

  @Override
  public void surfaceCreated (SurfaceHolder holder) {
    SharedPreferences prefs = context.getSharedPreferences ("picture_settings", Context.MODE_PRIVATE);
    boolean stretchPic = prefs.getBoolean ("stretch_picture", true);
    //TODO: use stretchPic setting
    originalBackground = decodeSampledBitmapFromResource (getResources (), R.drawable.grass, this.getWidth (), this.getHeight ());
    // rotate the image if it has a different rotation than canvas
    if (originalBackground.getWidth () < originalBackground.getHeight () && this.getWidth () > this.getHeight () ||
        originalBackground.getWidth () > originalBackground.getHeight () && this.getWidth () < this.getHeight ()) {
      Matrix matrix = new Matrix ();
      matrix.postRotate (90);
      originalBackground = Bitmap.createBitmap (originalBackground, 0, 0, originalBackground.getWidth (), originalBackground.getHeight (), matrix, true);
    }
    smashedBackground = originalBackground.copy (originalBackground.getConfig (), true);
    scale_x = (float)smashedBackground.getWidth () / this.getWidth ();
    scale_y = (float)smashedBackground.getHeight () / this.getHeight ();
    //originalBackground = BitmapFactory.decodeResource (getResources (), R.drawable.bricks);
    smashingThread = new SmashingThread (holder, context);
    smashingThread.setRunning (true);
    smashingThread.start ();
  }

  @Override
  public void surfaceChanged (SurfaceHolder holder, int format, int width, int height) {
  }

  @Override
  public void surfaceDestroyed (SurfaceHolder holder) {
    smashingThread.setRunning (false);
    boolean retry = true;
    while (retry) {
      try {
        smashingThread.join ();
        retry = false;
      }
      catch (Exception e)
      {
        Log.v ("Exception occurred", e.getMessage ());
      }
    }
  }

  void doDraw (Canvas canvas) {
    RectF rect = new RectF (0, 0, canvas.getWidth (), canvas.getHeight ());
    canvas.drawBitmap (smashedBackground, null, rect, null);
  }

  @Override
  public boolean onTouchEvent (MotionEvent event)
  {
    touched_x = event.getX ();
    touched_y = event.getY ();

    int action = event.getAction ();
    switch (action) {
      case MotionEvent.ACTION_DOWN:
        Paint paint = new Paint (Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth (25);
        paint.setColor (Color.BLACK);
        Canvas canvas = new Canvas (smashedBackground);
        canvas.drawPoint (touched_x * scale_x, touched_y * scale_y, paint);
        touched = true;
        break;
      case MotionEvent.ACTION_MOVE:
        touched = true;
        break;
      case MotionEvent.ACTION_UP:
        touched = false;
        break;
      case MotionEvent.ACTION_CANCEL:
        touched = false;
        break;
      case MotionEvent.ACTION_OUTSIDE:
        touched = false;
        break;
      default:
    }
    return true; //processed
  }

  public class SmashingThread extends Thread {
    boolean running;
    Canvas canvas;
    SurfaceHolder surfaceHolder;
    Context context;

    public SmashingThread (SurfaceHolder sHolder, Context ctx)
    {
      surfaceHolder = sHolder;
      context = ctx;
      running = false;
    }

    void setRunning (boolean br)
    {
      running = br;
    }

    @Override
    public void run() {
      super.run ();
      while (running) {
        canvas = surfaceHolder.lockCanvas ();
        if (canvas != null) {
          SmashingView.this.doDraw (canvas);
          surfaceHolder.unlockCanvasAndPost (canvas);
        }
      }
    }
  }
}