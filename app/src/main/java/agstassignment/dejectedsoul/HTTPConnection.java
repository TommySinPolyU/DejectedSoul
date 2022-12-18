package agstassignment.dejectedsoul;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class HTTPConnection extends AsyncTask<String, Integer, String> {

    enum ConnectionFor{
        Login,
        Register,
        SaveGame,
        LoadGame
    }

    ConnectionFor connectionFor;
    String ID,PW, PlayerName;
    Context context;
    GameBackStageUI gameBackStageUI;
    mainmenu Mainmenu1;
    String reply;
    int trytime;
    Timer responseTimer; // a timer for check the response time on this connnection
    boolean showAlertDialog;

    // Create a HTTPConnect For savegame, loadgame and login after enter to the game
    public HTTPConnection(ConnectionFor connectionFor, String ID, String PW, Context mContext, GameBackStageUI gameBackStageUI, boolean showAlertDialog){
        this.context = mContext;
        this.gameBackStageUI = gameBackStageUI;
        this.ID = ID;
        this.PW = PW;
        this.connectionFor = connectionFor;
        this.showAlertDialog = showAlertDialog;
        trytime = 0;
        responseTimer = new Timer();
    }

    // Create a HTTPConnect For Register in Main Menu Only.
    public HTTPConnection(ConnectionFor connectionFor, String ID, String PW, String PlayerName, Context mContext, mainmenu Mainmenu1, boolean showAlertDialog){
        this.context = mContext;
        this.Mainmenu1 = Mainmenu1;
        this.ID = ID;
        this.PW = PW;
        this.PlayerName = PlayerName;
        this.connectionFor = connectionFor;
        this.showAlertDialog = showAlertDialog;
        trytime = 0;
        responseTimer = new Timer();
    }

    // Create a HTTPConnect For Login in Main Menu Only.
    public HTTPConnection(ConnectionFor connectionFor, String ID, String PW, Context mContext, mainmenu Mainmenu1 , boolean showAlertDialog){
        this.context = mContext;
        this.Mainmenu1 = Mainmenu1;
        this.ID = ID;
        this.PW = PW;
        this.connectionFor = connectionFor;
        this.showAlertDialog = showAlertDialog;
        trytime = 0;
        responseTimer = new Timer();
    }

    @Override
    protected String doInBackground(String... params) {
        reply = null;
        try {
                // prepare URL and execute http request
                URL url = new URL(params[0]);
                final HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //Log.e("Error: ",urlConnection.toString());
                responseTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    trytime++;
                    Log.e("Error: ","" + trytime);
                    // Disconnect from the urlConnection, after 3 second when can't connect to the server.
                    if(trytime >= 5){
                        urlConnection.disconnect();
                        cancel();
                    }
                }
                }, 1000, 1000);
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream is = urlConnection.getInputStream();
                StringBuffer sb = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = reader.readLine()) != null)
                    sb.append(line + "\n");
                reader.close();
                urlConnection.disconnect();
                reply = sb.toString().trim();
        } catch (Exception e) {
            Log.e("Error: ",e.toString());
            e.printStackTrace();
            reply = "Fail To Access the server.";
        }
        return reply;
    }
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        AlertDialog.Builder alertbox = new AlertDialog.Builder(context);
        switch (connectionFor){
            case Login:
                if (reply.contains("Login Success")) {
                    if(Mainmenu1 != null) { // Login Account from Main Menu - Start Menu By Clicked 'LOGIN TO SERVER'
                        String[] segment = reply.split(",");
                        final String playername = segment[1];
                        PlayerName = playername;
                        //alertbox.setMessage("Login is successful!\nWelcome back " + playername + " !");
                        alertbox.show().dismiss();
                        Intent intent = new Intent(Mainmenu1, GameBackStageUI.class);
                        intent.putExtra("ID",ID);
                        intent.putExtra("PW",PW);
                        intent.putExtra("NAME",PlayerName);
                        intent.putExtra("isLogin",true);
                        Mainmenu1.startActivity(intent);
                        try {
                            Mainmenu1.mediaPlayer.stop();
                            Mainmenu1.mediaPlayer.release();
                        }catch (Exception e){

                        }
                        Mainmenu1.finish();
                    } else {
                        String[] segment = reply.split(",");
                        final String playername = segment[1];
                        alertbox.setTitle("Login Success");
                        alertbox.setMessage("Login is successful!\nWelcome back " + playername + " !");
                        if(gameBackStageUI != null) {
                            gameBackStageUI.ServerDataReply = reply;
                        }
                        alertbox.setNeutralButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                    }
                                });
                    }
                }else if(reply.contains("Fail To Access the server.")){
                    alertbox.setTitle("Login Failed");
                    alertbox.setMessage("Connection Error: \nFailed Connection,\nPlease Check Your Internet!\n\nAlso, The server may not opening.\n\nTurning To Offline Mode");
                    alertbox.setNeutralButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                }
                            });
                }
                else {
                    alertbox.setTitle("Login Failed");
                    alertbox.setMessage(reply);
                    alertbox.setNeutralButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                }
                            });
                }
                break;
            case Register:
                alertbox.setMessage(reply);
                if(reply.contains("Your account has been successfully created")) {
                    alertbox.setTitle("Registration Success");
                    alertbox.setMessage(reply + "\n\nNow You can save game data to server and load data from server.");
                    alertbox.setPositiveButton("Enter To Game", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Mainmenu1, GameBackStageUI.class);
                            intent.putExtra("ID",ID);
                            intent.putExtra("PW",PW);
                            intent.putExtra("NAME",PlayerName);
                            intent.putExtra("isLogin",true);
                            Mainmenu1.mediaPlayer.stop();
                            Mainmenu1.mediaPlayer.release();
                            Mainmenu1.startActivity(intent);
                        }
                    });
                } else if(reply.contains("This ID or Game Name is exists")){
                    alertbox.setTitle("Registration failed");
                    alertbox.setMessage(reply);
                    alertbox.setNeutralButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                }
                            });
                } else if(reply.contains("Please Fill all fields!")){
                    alertbox.setTitle("Registration failed");
                    alertbox.setMessage(reply);
                    alertbox.setNeutralButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                }
                            });
                } else if(reply.contains("Register Requirement")){
                    alertbox.setTitle("Registration failed");
                    String replyfromserver = reply;
                    if(replyfromserver.contains("<br />")){
                        replyfromserver.replaceAll("<br />","\n");
                    }
                    alertbox.setMessage(replyfromserver);
                    alertbox.setNeutralButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                }
                            });
                }
                else {
                    alertbox.setTitle("Registration failed");
                    alertbox.setMessage(reply);
                    alertbox.setNeutralButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                }
                            });
                }
                break;
            case LoadGame:
                Log.e("Load Game", "isLoading Game");
                if(gameBackStageUI != null) {
                    gameBackStageUI.LoadGame(GameBackStageUI.LoadGameMode.LoadFromServer);
                }
                alertbox.setMessage("Load Game Successful");
                alertbox.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });
                break;

            case SaveGame:
                alertbox.setMessage("Save Game Successful");
                Log.e("Save Success!", "Player Name: " + gameBackStageUI.PlayerName + ", Player ID: " + gameBackStageUI.PlayerID);
                alertbox.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        });
                break;
        }
        if (showAlertDialog) {
            alertbox.show();
        }else {

        }


        responseTimer.cancel();
    }

}