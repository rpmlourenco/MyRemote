package pt.rpmlourenco.myremote;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

/**
 * A class, that can be used as a TouchListener on any view (e.g. a Button).
 * It cyclically runs a clickListener, emulating keyboard-like behaviour. First
 * click is fired immediately, next one after the initialInterval, and subsequent
 * ones after the normalInterval.
 * <p>
 * <p>Interval is scheduled after the onClick completes, so it has to run fast.
 * If it runs slow, it does not generate skipped onClicks. Can be rewritten to
 * achieve this.
 */
public class RepeatListener implements OnTouchListener {

    private final Handler handler = new Handler();
    private final int normalInterval;
    private final RepeatClickListener clickListener;
    private int initialInterval;
    private Vibrator vibe;
    private View downView;
    private final Runnable handlerRunnable = new Runnable() {
        @Override
        public void run() {
            clickListener.setInit(false);
            handler.postDelayed(this, normalInterval);
            clickListener.onClick(downView);
        }
    };

    /**
     * @param initialInterval The interval after first click event
     * @param normalInterval  The interval after second and subsequent click
     *                        events
     */
    public RepeatListener(int initialInterval, int normalInterval, String hex,
                          MainActivity main) {
        clickListener = new RepeatClickListener(hex, main);
        if (initialInterval < 0 || normalInterval < 0)
            throw new IllegalArgumentException("negative interval");

        this.initialInterval = initialInterval;
        this.normalInterval = normalInterval;
        vibe = (Vibrator) main.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        clickListener.setInit(true);
    }

    public RepeatListener(String hex,
                          MainActivity main) {
        this(10000, 10000, hex, main);
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        ImageView iview;

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if (view instanceof ImageView) {
                    iview = (ImageView) view;
                    //overlay is black with transparency of 0x77 (119)
                    iview.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                    iview.invalidate();
                }

                handler.removeCallbacks(handlerRunnable);
                handler.postDelayed(handlerRunnable, initialInterval);
                vibe.vibrate(20);
                clickListener.setInit(true);
                downView = view;
                downView.setPressed(true);
                clickListener.onClick(view);
                return true;
            case MotionEvent.ACTION_UP:

            case MotionEvent.ACTION_CANCEL:

                if (view instanceof ImageView) {
                    iview = (ImageView) view;
                    //clear the overlay
                    iview.getDrawable().clearColorFilter();
                    iview.invalidate();
                }

                handler.removeCallbacks(handlerRunnable);
                downView.setPressed(false);
                downView = null;
                return true;
        }

        return false;
    }

}