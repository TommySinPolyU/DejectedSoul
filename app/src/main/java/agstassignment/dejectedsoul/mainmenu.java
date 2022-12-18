package agstassignment.dejectedsoul;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class mainmenu extends AppCompatActivity {
    SharedPreferences saveData;
    File saveDataFile;
    // Main Menu Button Element.
    private Button btn_start, btn_howtoplay, btn_credit;
    private LinearLayout MainMenuLayout;
    // Start Game Menu Button Element.
    private Button btn_login, btn_register, btn_offlicemode, btn_backtoMainMenu;
    private LinearLayout StartMenuLayout;
    HTTPConnection HttpConnection = null;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        final mainmenu MainmenuCont = this;
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.mainmenu);
        saveData = getSharedPreferences("savedata", MODE_PRIVATE);
        saveDataFile = new File("/data/data/" + getPackageName() + "/shared_prefs/savedata.xml");
        mediaPlayer = MediaPlayer.create(this,R.raw.mainmenubgm);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);

        btn_start = findViewById(R.id.btn_startgame);
        btn_howtoplay = findViewById(R.id.btn_howtoplay);
        btn_credit = findViewById(R.id.btn_credit);
        btn_login = findViewById(R.id.btn_playwithac);
        btn_offlicemode = findViewById(R.id.btn_playoffline);
        btn_register = findViewById(R.id.btn_register);
        btn_backtoMainMenu = findViewById(R.id.btn_backtoMainMenu);
        MainMenuLayout = findViewById(R.id.iL_ButtonLayout);
        StartMenuLayout = findViewById(R.id.iL_StartMenuLayout);

        // region Setting of StartGame.Button
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainMenuLayout.setVisibility(View.GONE);
                StartMenuLayout.setVisibility(View.VISIBLE);
            }
        });
        // endregion

        // region Setting of How To Play.Button
        btn_howtoplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(mainmenu.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                final View DialogView = getLayoutInflater().inflate(R.layout.howtoplay, null);
                final Button btn_Close = DialogView.findViewById(R.id.btn_h2pClose);
                final Button btn_LoginTutView = DialogView.findViewById(R.id.btn_playLogin);
                final Button btn_RegisterTutView = DialogView.findViewById(R.id.btn_playRegister);
                final Button btn_SavaLoadTutView = DialogView.findViewById(R.id.btn_playSaveandLoad);
                final Button btn_PlayOfflineTutView = DialogView.findViewById(R.id.btn_playOfflineTut);
                final Button btn_EquipmentTutView = DialogView.findViewById(R.id.btn_playEquipment);
                final Button  btn_SkillsTutView = DialogView.findViewById(R.id.btn_playSkills);
                final Button btn_ResetGameTutView = DialogView.findViewById(R.id.btn_playReset);
                final Button btn_MovementTutView = DialogView.findViewById(R.id.btn_playMovement);

                DialogBuilder.setView(DialogView);
                final AlertDialog HowToPlayalertDialog = DialogBuilder.create();
                HowToPlayalertDialog.show();

                // region Login Tutorial  Setting
                btn_LoginTutView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HowToPlayalertDialog.dismiss();
                        final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(mainmenu.this);
                        final View DialogView = getLayoutInflater().inflate(R.layout.videoviewlayout, null);
                        final TextView title = DialogView.findViewById(R.id.tv_VideoTitle);
                        final Button BtnCancel = DialogView.findViewById(R.id.btn_TutClose);
                        final VideoView vidView = DialogView.findViewById(R.id.vView);
                        String vidAddress = "https://ds.dsgshk.com/videos/login.mp4";
                        MediaController vidControl = new MediaController(DialogView.getContext());
                        Uri vidUri = Uri.parse(vidAddress);
                        vidView.setVideoURI(vidUri);

                        vidControl.setAnchorView(vidView);
                        vidView.setMediaController(vidControl);
                        vidView.start();
                        vidView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mp.setLooping(true);
                            }
                        });
                        BtnCancel.setVisibility(View.VISIBLE);
                        title.setText("Login");
                        title.setGravity(Gravity.CENTER);
                        BtnCancel.setText("Close");
                        DialogBuilder.setView(DialogView);
                        final AlertDialog LoginTutalertDialog = DialogBuilder.create();
                        BtnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                LoginTutalertDialog.dismiss();
                                HowToPlayalertDialog.show();
                            }
                        });
                        LoginTutalertDialog.show();
                    }
                });
                // endregion
                // region Register Tutorial  Setting
                btn_RegisterTutView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HowToPlayalertDialog.dismiss();
                        final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(mainmenu.this);
                        final View DialogView = getLayoutInflater().inflate(R.layout.videoviewlayout, null);
                        final TextView title = DialogView.findViewById(R.id.tv_VideoTitle);
                        final Button BtnCancel = DialogView.findViewById(R.id.btn_TutClose);
                        final VideoView vidView = DialogView.findViewById(R.id.vView);
                        String vidAddress = "https://ds.dsgshk.com/videos/register.mp4";
                        MediaController vidControl = new MediaController(DialogView.getContext());
                        Uri vidUri = Uri.parse(vidAddress);
                        vidView.setVideoURI(vidUri);

                        vidControl.setAnchorView(vidView);
                        vidView.setMediaController(vidControl);
                        vidView.start();
                        vidView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mp.setLooping(true);
                            }
                        });
                        BtnCancel.setVisibility(View.VISIBLE);
                        title.setText("Register");
                        title.setGravity(Gravity.CENTER);
                        BtnCancel.setText("Close");
                        DialogBuilder.setView(DialogView);
                        final AlertDialog RegTutalertDialog = DialogBuilder.create();
                        BtnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RegTutalertDialog.dismiss();
                                HowToPlayalertDialog.show();
                            }
                        });
                        RegTutalertDialog.show();
                    }
                });
                // endregion
                // region Play Offline Mode Tutorial  Setting
                btn_PlayOfflineTutView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HowToPlayalertDialog.dismiss();
                        final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(mainmenu.this);
                        final View DialogView = getLayoutInflater().inflate(R.layout.videoviewlayout, null);
                        final TextView title = DialogView.findViewById(R.id.tv_VideoTitle);
                        final Button BtnCancel = DialogView.findViewById(R.id.btn_TutClose);
                        final VideoView vidView = DialogView.findViewById(R.id.vView);
                        String vidAddress = "https://ds.dsgshk.com/videos/playoffline.mp4";
                        MediaController vidControl = new MediaController(DialogView.getContext());
                        Uri vidUri = Uri.parse(vidAddress);
                        vidView.setVideoURI(vidUri);

                        vidControl.setAnchorView(vidView);
                        vidView.setMediaController(vidControl);
                        vidView.start();
                        vidView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mp.setLooping(true);
                            }
                        });
                        BtnCancel.setVisibility(View.VISIBLE);
                        title.setText("Offline Mode");
                        title.setGravity(Gravity.CENTER);
                        BtnCancel.setText("Close");
                        DialogBuilder.setView(DialogView);
                        final AlertDialog OfflineTutalertDialog = DialogBuilder.create();
                        BtnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                OfflineTutalertDialog.dismiss();
                                HowToPlayalertDialog.show();
                            }
                        });
                        OfflineTutalertDialog.show();
                    }
                });
                // endregion
                // region Character Control Tutorial  Setting
                btn_MovementTutView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HowToPlayalertDialog.dismiss();
                        final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(mainmenu.this);
                        final View DialogView = getLayoutInflater().inflate(R.layout.videoviewlayout, null);
                        final TextView title = DialogView.findViewById(R.id.tv_VideoTitle);
                        final Button BtnCancel = DialogView.findViewById(R.id.btn_TutClose);
                        final VideoView vidView = DialogView.findViewById(R.id.vView);
                        String vidAddress = "https://ds.dsgshk.com/videos/charattackandmove.mp4";
                        MediaController vidControl = new MediaController(DialogView.getContext());
                        Uri vidUri = Uri.parse(vidAddress);
                        vidView.setVideoURI(vidUri);

                        vidControl.setAnchorView(vidView);
                        vidView.setMediaController(vidControl);
                        vidView.start();
                        vidView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mp.setLooping(true);
                            }
                        });
                        BtnCancel.setVisibility(View.VISIBLE);
                        title.setText("Character Control");
                        title.setGravity(Gravity.CENTER);
                        BtnCancel.setText("Close");
                        DialogBuilder.setView(DialogView);
                        final AlertDialog ControlTutalertDialog = DialogBuilder.create();
                        BtnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ControlTutalertDialog.dismiss();
                                HowToPlayalertDialog.show();
                            }
                        });
                        ControlTutalertDialog.show();
                    }
                });
                // endregion
                // region Equipment and Enhancement Tutorial  Setting
                btn_EquipmentTutView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HowToPlayalertDialog.dismiss();
                        final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(mainmenu.this);
                        final View DialogView = getLayoutInflater().inflate(R.layout.videoviewlayout, null);
                        final TextView title = DialogView.findViewById(R.id.tv_VideoTitle);
                        final Button BtnCancel = DialogView.findViewById(R.id.btn_TutClose);
                        final VideoView vidView = DialogView.findViewById(R.id.vView);
                        String vidAddress = "https://ds.dsgshk.com/videos/equipment.mp4";
                        MediaController vidControl = new MediaController(DialogView.getContext());
                        Uri vidUri = Uri.parse(vidAddress);
                        vidView.setVideoURI(vidUri);

                        vidControl.setAnchorView(vidView);
                        vidView.setMediaController(vidControl);
                        vidView.start();
                        vidView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mp.setLooping(true);
                            }
                        });
                        BtnCancel.setVisibility(View.VISIBLE);
                        title.setText("Equipment and Enhancement");
                        title.setGravity(Gravity.CENTER);
                        BtnCancel.setText("Close");
                        DialogBuilder.setView(DialogView);
                        final AlertDialog EquipmentTutalertDialog = DialogBuilder.create();
                        BtnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EquipmentTutalertDialog.dismiss();
                                HowToPlayalertDialog.show();
                            }
                        });
                        EquipmentTutalertDialog.show();
                    }
                });
                // endregion
                // region Skills Tutorial  Setting
                btn_SkillsTutView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HowToPlayalertDialog.dismiss();
                        final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(mainmenu.this);
                        final View DialogView = getLayoutInflater().inflate(R.layout.videoviewlayout, null);
                        final TextView title = DialogView.findViewById(R.id.tv_VideoTitle);
                        final Button BtnCancel = DialogView.findViewById(R.id.btn_TutClose);
                        final VideoView vidView = DialogView.findViewById(R.id.vView);
                        String vidAddress = "https://ds.dsgshk.com/videos/learnskill.mp4";
                        MediaController vidControl = new MediaController(DialogView.getContext());
                        Uri vidUri = Uri.parse(vidAddress);
                        vidView.setVideoURI(vidUri);

                        vidControl.setAnchorView(vidView);
                        vidView.setMediaController(vidControl);
                        vidView.start();
                        vidView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mp.setLooping(true);
                            }
                        });
                        BtnCancel.setVisibility(View.VISIBLE);
                        title.setText("Learn Skills");
                        title.setGravity(Gravity.CENTER);
                        BtnCancel.setText("Close");
                        DialogBuilder.setView(DialogView);
                        final AlertDialog SkillsTutalertDialog = DialogBuilder.create();
                        BtnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SkillsTutalertDialog.dismiss();
                                HowToPlayalertDialog.show();
                            }
                        });
                        SkillsTutalertDialog.show();
                    }
                });
                // endregion
                // region Save and load Tutorial  Setting
                btn_SavaLoadTutView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HowToPlayalertDialog.dismiss();
                        final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(mainmenu.this);
                        final View DialogView = getLayoutInflater().inflate(R.layout.videoviewlayout, null);
                        final TextView title = DialogView.findViewById(R.id.tv_VideoTitle);
                        final Button BtnCancel = DialogView.findViewById(R.id.btn_TutClose);
                        final VideoView vidView = DialogView.findViewById(R.id.vView);
                        String vidAddress = "https://ds.dsgshk.com/videos/saveandload.mp4";
                        MediaController vidControl = new MediaController(DialogView.getContext());
                        Uri vidUri = Uri.parse(vidAddress);
                        vidView.setVideoURI(vidUri);

                        vidControl.setAnchorView(vidView);
                        vidView.setMediaController(vidControl);
                        vidView.start();
                        vidView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mp.setLooping(true);
                            }
                        });
                        BtnCancel.setVisibility(View.VISIBLE);
                        title.setText("Save and Load Game");
                        title.setGravity(Gravity.CENTER);
                        BtnCancel.setText("Close");
                        DialogBuilder.setView(DialogView);
                        final AlertDialog SaveandLoadTutalertDialog = DialogBuilder.create();
                        BtnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SaveandLoadTutalertDialog.dismiss();
                                HowToPlayalertDialog.show();
                            }
                        });
                        SaveandLoadTutalertDialog.show();
                    }
                });
                // endregion
                // region Reset Tutorial  Setting
                btn_ResetGameTutView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HowToPlayalertDialog.dismiss();
                        final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(mainmenu.this);
                        final View DialogView = getLayoutInflater().inflate(R.layout.videoviewlayout, null);
                        final TextView title = DialogView.findViewById(R.id.tv_VideoTitle);
                        final Button BtnCancel = DialogView.findViewById(R.id.btn_TutClose);
                        final VideoView vidView = DialogView.findViewById(R.id.vView);
                        String vidAddress = "https://ds.dsgshk.com/videos/resetgame.mp4";
                        MediaController vidControl = new MediaController(DialogView.getContext());
                        Uri vidUri = Uri.parse(vidAddress);
                        vidView.setVideoURI(vidUri);

                        vidControl.setAnchorView(vidView);
                        vidView.setMediaController(vidControl);
                        vidView.start();
                        vidView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mp.setLooping(true);
                            }
                        });
                        BtnCancel.setVisibility(View.VISIBLE);
                        title.setText("Reset Game");
                        title.setGravity(Gravity.CENTER);
                        BtnCancel.setText("Close");
                        DialogBuilder.setView(DialogView);
                        final AlertDialog ResetTutalertDialog = DialogBuilder.create();
                        BtnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ResetTutalertDialog.dismiss();
                                HowToPlayalertDialog.show();
                            }
                        });
                        ResetTutalertDialog.show();
                    }
                });
                // endregion

                btn_Close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HowToPlayalertDialog.dismiss();
                    }
                });
            }
        });
        // endregion



        // region Setting of Credit.Button
        btn_credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(mainmenu.this);
                final View DialogView = getLayoutInflater().inflate(R.layout.alertbox, null);
                final TextView title = DialogView.findViewById(R.id.tv_alertTitle);
                final TextView content = DialogView.findViewById(R.id.tv_alertContext);
                final Button BtnConfirm = DialogView.findViewById(R.id.btn_OK);
                final Button BtnCancel = DialogView.findViewById(R.id.btn_Cancel);

                BtnConfirm.setVisibility(View.GONE);
                BtnCancel.setVisibility(View.VISIBLE);
                title.setText("Credit");
                title.setGravity(Gravity.CENTER);
                content.setText("Line First: Author\n" +
                        "     : Materials Title /(Usage)\n" +
                        "     Last: Author's Website / Materials Website\n" +
                        "\n" +
                        "Aekashics/Ækashics \n" +
                        "Librariumstatic Battlers (Monster Sprite)\n" +
                        "http://www.akashics.moe/terms-of-use/\n" +
                        "\n" +
                        "Luis Zuno @ansimuz\n" +
                        "Space Ship Shooter Environment (Stage Background)\n" +
                        "http://ansimuz.com/site/\n" +
                        "\n" +
                        "Finalbossblues\n" +
                        "Pixel Shooter and Towers Asset Pack (Coin Sprites Sheet)\n" +
                        "https://finalbossblues.itch.io/pixel-shooter-towers-asset-pack\n" +
                        "\n" +
                        "Clint Bellanger\n" +
                        "Fireball Spell (Bullet Sprites Sheet)\n" +
                        "https://opengameart.org/content/fireball-spell\n" +
                        "\n" +
                        "Shikashiassets\n" +
                        "Shikashi's Fantasy Icons Pack (Game UI Icons)\n" +
                        "https://shikashiassets.itch.io/shikashis-fantasy-icons-pack\n" +
                        "\n" +
                        "AShamaluevMusic\n" +
                        "Epic Battle Trailer - AShamaluevMusic (mainmenubgm)\n" +
                        "Epic Orchestral Trailer - AShamaluevMusic (normalstagebossbgm)\n" +
                        "01. Epic Trailer (battlebgm1)\n" +
                        "Action Trailer - AShamaluevMusic (bossstagebgm)\n" +
                        "https://soundcloud.com/ashamaluevmusic/sets/music-for-gaming-videos\n" +
                        "\n" +
                        "LiamG_SFX\n" +
                        "Fireball Cast 1 (bullet Shooting sound effect)\n" +
                        "https://freesound.org/people/LiamG_SFX/sounds/334234/\n" +
                        "\n" +
                        "HighPixel\n" +
                        "Fireball Explosion (bullet hit sound effect)\n" +
                        "https://freesound.org/people/HighPixel/sounds/431174/\n" +
                        "\n" +
                        "Kevin MacLeod\n" +
                        "Darkling (Odin Challenge BGM)\n" +
                        "https://filmmusic.io / https://incompetech.com\n" +
                        "Licence: CC BY (http://creativecommons.org/licenses/by/4.0/)\n" +
                        "\n" +
                        "Logout Icon\n" +
                        "https://pngtree.com/free-icon/logout_1185635\n" +
                        "\n" +
                        "Appzgear\n" +
                        "Lock Icon\n" +
                        "https://www.flaticon.com/free-icon/lock-icon_26053\n" +
                        "\n" +
                        "xnimrodx\n" +
                        "Login Icon\n" +
                        "https://www.flaticon.com/free-icon/login_1669850#term=login&page=1&position=40\n" +
                        "\n" +
                        "Kisspng\n" +
                        "Offline Icon\n" +
                        "https://www.kisspng.com/png-computer-icons-online-and-offline-curved-line-950127/preview.html\n" +
                        "\n" +
                        "Digital Buddha\n" +
                        "Play Button Free Vector and PNG\n" +
                        "https://pngtree.com/freepng/play-button_3550531.html\n" +
                        "\n" +
                        "chittagongit\n" +
                        "Tutorial Icon Png #86865\n" +
                        "http://chittagongit.com/icon/tutorial-icon-png-12.html\n" +
                        "\n" +
                        "The materials above are not created and designed by myself, \n" +
                        "I do not have any copyright on these materials.\n" +
                        "\n" +
                        "\n" +
                        "Copyright © 2019 Sin Siu Wa,Tommy All rights reserved\n" +
                        "https://dsgshk.com/ is my website which registered on Hostinger.com\n" +
                        "Dejected Souls is the game for my ITP4723 Advanced Game Software Technology Assignment\n" +
                        "It is for educational use only");
                content.setGravity(Gravity.LEFT);
                content.setTextSize(16f);
                BtnCancel.setText("Close");
                DialogBuilder.setView(DialogView);
                final AlertDialog alertDialog = DialogBuilder.create();
                BtnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });
        // endregion

        // region Setting of Play With AC (Login).Button
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(mainmenu.this);
                final View DialogView = getLayoutInflater().inflate(R.layout.login, null);
                final EditText editText_ID = DialogView.findViewById(R.id.et_LoginID);
                final EditText editText_PW = DialogView.findViewById(R.id.et_LoginPW);
                final Button BtnLoginConfirm = DialogView.findViewById(R.id.btn_Login);

                BtnLoginConfirm.setText("Login");
                DialogBuilder.setView(DialogView);
                final AlertDialog alertDialog = DialogBuilder.create();

                if(saveData.getBoolean("isLogin",false)){
                    alertDialog.dismiss();
                    String ID = saveData.getString("PlayerID","");
                    String PW = saveData.getString("PlayerPW","");
                    editText_ID.setText(ID); editText_PW.setText(PW);
                    try {
                        URL url = new URL("https://ds.dsgshk.com/login.php?"
                                + "ID=" + ID + "&PW=" + PW);

                        if (HttpConnection == null ||
                                HttpConnection.getStatus().equals(AsyncTask.Status.FINISHED)) {
                            HttpConnection = new HTTPConnection(HTTPConnection.ConnectionFor.Login,ID, PW, MainmenuCont, MainmenuCont,true);
                            HttpConnection.execute(url.toString());
                            //alertDialog.dismiss();
                        }

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
                BtnLoginConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ID = editText_ID.getText().toString();
                        String PW = editText_PW.getText().toString();
                        try {
                            URL url = new URL("https://ds.dsgshk.com/login.php?"
                                    + "ID=" + ID + "&PW=" + PW);

                            if (HttpConnection == null ||
                                    HttpConnection.getStatus().equals(AsyncTask.Status.FINISHED)) {
                                HttpConnection = new HTTPConnection(HTTPConnection.ConnectionFor.Login,ID, PW, MainmenuCont, MainmenuCont,true);
                                HttpConnection.execute(url.toString());
                                alertDialog.dismiss();
                            }

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                });
                alertDialog.show();
            }
        });
        // endregion

        // region Setting of Register.Button
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(mainmenu.this);
                final View DialogView = getLayoutInflater().inflate(R.layout.register, null);
                final EditText editText_GameName = DialogView.findViewById(R.id.et_gamename);
                final EditText editText_ID = DialogView.findViewById(R.id.et_LoginID);
                final EditText editText_PW = DialogView.findViewById(R.id.et_LoginPW);
                final Button BtnRegister = DialogView.findViewById(R.id.btn_signupConfirm);

                BtnRegister.setText("Register");
                DialogBuilder.setView(DialogView);
                final AlertDialog alertDialog = DialogBuilder.create();
                //alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
                BtnRegister.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ID = editText_ID.getText().toString();
                        String PW = editText_PW.getText().toString();
                        String PlayerName = editText_GameName.getText().toString();
                        try {
                            URL url = new URL("https://ds.dsgshk.com/register.php?"
                                    + "ID=" + ID + "&PW=" + PW + "&NAME=" + PlayerName);

                            if (HttpConnection == null ||
                                    HttpConnection.getStatus().equals(AsyncTask.Status.FINISHED)) {
                                HttpConnection = new HTTPConnection(HTTPConnection.ConnectionFor.Register,ID, PW, PlayerName, MainmenuCont, MainmenuCont,true);
                                HttpConnection.execute(url.toString());
                                //alertDialog.dismiss();
                            }

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        // endregion

        // region Setting of Play Offline.Button
        btn_offlicemode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String OfflineName = saveData.getString("OfflinePlayerName",null);
                if (OfflineName == null) {
                    final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(mainmenu.this);
                    final View DialogView = getLayoutInflater().inflate(R.layout.offlinenamebox, null);
                    final EditText editText_Name = DialogView.findViewById(R.id.et_offlinegameName);
                    final Button BtnLoginConfirm = DialogView.findViewById(R.id.btn_offlinelogon);


                    DialogBuilder.setView(DialogView);
                    final AlertDialog alertDialog = DialogBuilder.create();
                    alertDialog.show();
                    BtnLoginConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(editText_Name.getText().length() >=4 && editText_Name.getText().length() <=24) {
                                Intent intent = new Intent(MainmenuCont, GameBackStageUI.class);
                                intent.putExtra("isLogin", false);
                                intent.putExtra("NAME", editText_Name.getText().toString());
                                saveData.edit().putString("OfflinePlayerName",editText_Name.getText().toString()).commit();
                                startActivity(intent);
                            } else{
                                final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(mainmenu.this);
                                final View DialogView = getLayoutInflater().inflate(R.layout.alertbox, null);
                                final TextView AlertTitle = DialogView.findViewById(R.id.tv_alertTitle);
                                final TextView AlertMessage= DialogView.findViewById(R.id.tv_alertContext);
                                final Button BtnConfirm = DialogView.findViewById(R.id.btn_OK);
                                final Button BtnCancle = DialogView.findViewById(R.id.btn_Cancel);
                                AlertTitle.setText("Warning");
                                AlertMessage.setText("Game Name length must longer than 3 and shorter than 25");
                                BtnConfirm.setText("OK");
                                BtnCancle.setVisibility(View.GONE);

                                DialogBuilder.setView(DialogView);
                                final AlertDialog alertDialog = DialogBuilder.create();
                                alertDialog.show();

                                BtnConfirm.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alertDialog.dismiss();
                                    }
                                });
                            }
                        }
                    });
                } else {
                            Intent intent = new Intent(MainmenuCont, GameBackStageUI.class);
                            intent.putExtra("isLogin", false);
                            mediaPlayer.stop();
                            mediaPlayer.release();
                            finish();
                            startActivity(intent);
                }
            }
        });
        // endregion

        // region Setting of Back Button
        btn_backtoMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainMenuLayout.setVisibility(View.VISIBLE);
                StartMenuLayout.setVisibility(View.GONE);
            }
        });
        // endregion

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        try {
            mediaPlayer.stop();
            mediaPlayer.release();
        }catch (Exception e){

        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        try {
            mediaPlayer.pause();
        }catch (Exception e){

        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        try {
            mediaPlayer.start();
        }catch (Exception e){

        }
    }

    // Disable the back button function.
    @Override
    public void onBackPressed() {
        final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(mainmenu.this);
        final View DialogView = getLayoutInflater().inflate(R.layout.alertbox, null);
        final TextView title = DialogView.findViewById(R.id.tv_alertTitle);
        final TextView content = DialogView.findViewById(R.id.tv_alertContext);
        final Button BtnConfirm = DialogView.findViewById(R.id.btn_OK);
        final Button BtnCancel = DialogView.findViewById(R.id.btn_Cancel);

        BtnConfirm.setVisibility(View.VISIBLE);
        BtnCancel.setVisibility(View.VISIBLE);
        BtnConfirm.setText("Yes");
        BtnCancel.setText("No");
        title.setText("Exit Game");
        title.setGravity(Gravity.CENTER);
        content.setText("Are you want to exit the game?");

        DialogBuilder.setView(DialogView);
        final AlertDialog alertDialog = DialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        BtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        BtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

}
