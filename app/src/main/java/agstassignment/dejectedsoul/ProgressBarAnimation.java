package agstassignment.dejectedsoul;


import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;

public class ProgressBarAnimation extends Animation {
    private ProgressBar mProgressBar;
    private int mTo;
    private int mFrom;
    private long mStepDuration;

    /**
     * @param fullDuration - time required to fill progress from 0% to 100%
     */
    public ProgressBarAnimation(ProgressBar progressBar, long fullDuration, int MTO) {
        super();
        mProgressBar = progressBar;
        mStepDuration = fullDuration / progressBar.getMax();
        mTo = MTO;

        if (mTo < 0) {
            mTo = 0;
        }

        if (mTo > mProgressBar.getMax()) {
            mTo = mProgressBar.getMax();
        }
        mFrom = mProgressBar.getProgress();
        if(Math.abs(mTo - mFrom) * mStepDuration >= 0){
            setDuration(Math.abs(mTo - mFrom) * mStepDuration);
        }
        mProgressBar.startAnimation(this);
    }


    public void setProgress(int progress) {

    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        float value = mFrom + (mTo - mFrom) * interpolatedTime;
        mProgressBar.setProgress((int) value);
    }

}
