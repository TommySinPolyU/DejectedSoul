package agstassignment.dejectedsoul;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.TimerTask;

public class DrawingThread extends Thread{
    private SurfaceHolder msurfaceHolder;
    private GamePanel mpanel;
    private boolean mrun = false;
    private long tickCount=0;

    public DrawingThread(SurfaceHolder surfaceHolder, GamePanel panel){
        super();
        msurfaceHolder = surfaceHolder;
        mpanel = panel;
    }

    public void setRunning(boolean run){
        mrun = run;
    }

    public void run() {
        Canvas canvas;
        this.mpanel.initGame();
        while (mrun) {
            canvas = null;
// try locking the canvas for exclusive pixel editing
// in the surface
            try {
                canvas = this.msurfaceHolder.lockCanvas();

                synchronized (msurfaceHolder) {
                    tickCount++;
                    this.mpanel.update();
                    this.mpanel.render(canvas);
                }
            } catch(Exception ex) {
            } finally {
                if (canvas != null)
                    msurfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }
}