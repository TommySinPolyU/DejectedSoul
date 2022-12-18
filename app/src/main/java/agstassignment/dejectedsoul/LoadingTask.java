package agstassignment.dejectedsoul;


import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

public class LoadingTask extends AsyncTask<String,Double,String> {
    double MAX_SEC = 3;
    Runnable BgTask, onPostTask, onProgressTask;
    ProgressBar LoadingBar;
    TextView LoadingPercentageText;
    double beforeValue;

    public LoadingTask(double sec, ProgressBar loadingBar, TextView loadingPercentageText, Runnable doInBg, Runnable onProgressTask, Runnable onPost){
        MAX_SEC = sec;
        BgTask = doInBg;
        onPostTask = onPost;
        LoadingBar = loadingBar;
        LoadingPercentageText = loadingPercentageText;
        LoadingBar.setProgress(0);
        LoadingPercentageText.setText("Loading..."+ LoadingBar.getProgress() + "%");
    }
    @Override
    protected String doInBackground(String...values){
        if(BgTask != null)
            BgTask.run();
        Random rnd = new Random();
        int time_elapsed = 0; // in msec
        while (time_elapsed <= MAX_SEC * 1000 + (250 * MAX_SEC)) {
            // Sleep randomly from 0.5 to 1 second
            int t = 500 + (int) rnd.nextInt(500);
            try {
                Thread.sleep(t); // sleep t msec
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            time_elapsed += t;
            double x = 100 * time_elapsed / MAX_SEC * 1000;
            publishProgress(new Double(100 * time_elapsed
                    / (MAX_SEC * 1000)));
        }
        return "Done!";
    }
    @Override
    protected void onProgressUpdate(Double... values) {
        LoadingBar.setVisibility(View.VISIBLE);
        LoadingPercentageText.setVisibility(View.VISIBLE);
        if(onProgressTask != null)
            onProgressTask.run();
        beforeValue = LoadingBar.getProgress();
        ProgressBarAnimation anim = new ProgressBarAnimation(LoadingBar, (long) beforeValue, values[0].intValue());
        anim.setDuration(250);
        LoadingBar.startAnimation(anim);
        if(values[0].intValue() >= 100)
            LoadingPercentageText.setText("Loading..." + "100%");
        else LoadingPercentageText.setText("Loading..." + values[0].intValue() + "%");

        //LoadingBar.setProgress(values[0].intValue());
    }
    @Override
    protected void onPostExecute(String result){
        if(onPostTask != null)
            onPostTask.run();
        LoadingBar.setVisibility(View.GONE);
        LoadingPercentageText.setVisibility(View.GONE);
    }
}
