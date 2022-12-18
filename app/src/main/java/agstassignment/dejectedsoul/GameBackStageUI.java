package agstassignment.dejectedsoul;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

/*
    GameBackStageUI.java:
    Created by SIN Siu Wa, Tommy.
    Latest Update: 22 / 4 / 2019 0.52
    A Game Backstage UI controller class for control the output on the UI.
    This class for update the "activity_game_screen.xml" layout file and save the game data.
 */

public class GameBackStageUI extends Activity {
    // The elements for control the HTTPConnection and the login data.
    File saveDataFile;
    String PlayerName, PlayerID, PlayerPassword;
    HTTPConnection loginConnection = null;
    boolean isLogin = false;
    String ServerDataReply = "";
    // region Create the Variable to contain the elements.

    // Identifier of the location for saving data.
    public enum SaveGameMode {
        SaveTOLocal,
        SaveTOServer
    }

    // Identifier of the location for loading data
    public enum LoadGameMode {
        LoadFromLocal,
        LoadFromServer
    }

    // Identifier of the battle stage mode
    public enum GameMode {
        TIMEATTACK,
        BOSSCHALLENGE,
        ENDLESSCHALLENGE,
        ODINCHALLENGE
    }

    // Identifier of the boss challenge stage selection.
    StagesContainer.BossStageType bossChallengeType;

    // Identifier of the difficulty of endless challenge.
    public enum EndlessDifficulty {
        Easy,
        Normal,
        Hard,
        Hell
    }

    // Identifier of the game ui update type.
    public enum UpdateStatusType {
        CurrentHP,
        CurrentMP,
        Exp,
        EnemyHP
    }

    // Identifier of the stage ui update type.
    public enum StageValueType {
        EXP,
        MONEY
    }

    protected GameBackStageUI GS;
    public GameMode gameMode;
    public EndlessDifficulty endlessDifficulty;
    /*
        LoadingBar Task Element Setting Start
        */
    ProgressBar loadingBar;
    LoadingTask loadingTask = null;
    TextView loadingpercentage;
    //    LoadingBar Task Element Setting End

    public MediaPlayer mediaPlayer;
    int soundIds[] = new int[2];
    SoundPool sp;
    Character character;
    // Stage UI Elements.
    ProgressBar hpBar, mpBar;
    TextView hpText, mpText;

    // Menu UI on Top Elements.
    ProgressBar expBar;
    TextView expText, lvText, moneyText, tv_playername;
    public int money;

    // TableLayout of The UI.
    public LinearLayout menuTopUI, charStatusUI, stageUI;

    // Bottom Menu UI Elements.
    public ImageButton btn_battle, btn_pause, btn_runaway, btn_character, btn_equipment, btn_setting, btn_skill;
    public TextView battleText, characterText;

    // Stage UI Elements;
    GamePanel GP;
    public LinearLayout StageButtonLayout;
    public TextView expTotalRewardText, moneyTotalRewardText, CurrentStageText;
    public int expTotalReward, moneyTotalReward;
    public ProgressBar enemyHpBar;
    public TextView enemyHpText, enemyName;

    private Context mContext;

    public FrameLayout gameMainScreenContainer, uibottomContainer;
    public boolean isAutoShooting, isPause, inStage, isReturnToMenu;

    // Equipment Elements.
    ImageView selectedEquipment;
    BulletEquipmentContainer bulletEquipmentContainer;
    int redStone = 0, greenStone = 0, blueStone = 0, darkStone = 0;
    // Skill Elements
    ImageView selectedSkill;
    SkillsContainer skillsContainer;
    int MoneyNeed, MaterialsNeed;
    boolean isSuccess = false;


    // Battle Game Mode Selection Elements.
    FrameLayout MIDmenuUIContainer;
    Button btn_EndlessChallenge, btn_BossChallenge;
    ImageButton btn_BossBack, btn_EndlessBack;
    Button btn_Easy, btn_Normal, btn_Hard, btn_Hell;
    Button btn_FireSlime, btn_WaterSlime, btn_ForestSlime, btn_DarkSlime, btn_ODIN;
    LinearLayout battleMenu, endlesschallengeMenu, bossschallengeMenu;

    // Setting Menu Elements.
    LinearLayout settingMenu;
    RelativeLayout saveBtnLayout, resetBtnLayout, abnormalstatusresetBtnLayout, returnMenuBtnLayout, LogoutLayout;
    Button btn_save, btn_reset, btn_abnormalstatusreset, btn_returnMenu, btn_logout;

    //Elements of Stage UI for Enemy Info Display.
    ImageView isBossIcon;
    ImageView isUndeadIcon;
    ImageView isPoisiong;

    SharedPreferences saveData; // the game save data for save and load game function.

    //endregion
    // onCreate function call after loaded the game.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game_screen);
        // Reset all variables related with stage and character.
        mContext = this;
        isAutoShooting = true;
        GS = this;
        money = 0;
        bulletEquipmentContainer = new BulletEquipmentContainer();
        skillsContainer = new SkillsContainer();
        saveData = getSharedPreferences("savedata", MODE_PRIVATE);

        // region Declear all UI Elements.
        loadingBar = findViewById(R.id.loadingBar);
        loadingpercentage = findViewById(R.id.barPercentage);
        gameMainScreenContainer = findViewById(R.id.stageScreencontainer);
        battleText = findViewById(R.id.tv_battle);
        hpBar = findViewById(R.id.hpBar);
        mpBar = findViewById(R.id.mpBar);
        hpText = findViewById(R.id.hpText);
        mpText = findViewById(R.id.mpText);
        expBar = findViewById(R.id.expbar);
        expText = findViewById(R.id.expText);
        menuTopUI = findViewById(R.id.playerInfoUI);
        charStatusUI = findViewById(R.id.charStatusUI);
        stageUI = findViewById(R.id.stageTopUI);
        lvText = findViewById(R.id.tv_LV);
        expTotalRewardText = findViewById(R.id.tv_StageExp);
        moneyTotalRewardText = findViewById(R.id.tv_StageMoney);
        CurrentStageText = findViewById(R.id.tv_Stage);
        enemyHpBar = findViewById(R.id.enemyhpBar);
        enemyHpText = findViewById(R.id.enemyhpText);
        enemyName = findViewById(R.id.tv_enemy);
        moneyText = findViewById(R.id.tv_Money2);
        characterText = findViewById(R.id.tv_charInfo);
        btn_character = findViewById(R.id.btn_charInfo);
        btn_equipment = findViewById(R.id.btn_Equipment);
        uibottomContainer = findViewById(R.id.uibottomcontainer);
        btn_EndlessChallenge = findViewById(R.id.btn_EndlessChallenge);
        btn_BossChallenge = findViewById(R.id.btn_BossChallenge);
        battleMenu = findViewById(R.id.BattleMenu);
        settingMenu = findViewById(R.id.SettingMenu);
        btn_skill = findViewById(R.id.btn_Skill);
        btn_setting = findViewById(R.id.btn_setting);
        btn_save = findViewById(R.id.btn_save);
        btn_reset = findViewById(R.id.btn_reset);
        btn_returnMenu = findViewById(R.id.btn_returntomenu);
        tv_playername = findViewById(R.id.tv_playerName);
        isBossIcon = findViewById(R.id.iv_bossicon);
        isUndeadIcon = findViewById(R.id.iv_undeadicon);
        isPoisiong = findViewById(R.id.iv_poisionicon);
        btn_abnormalstatusreset = findViewById(R.id.btn_resetCharacter);
        resetBtnLayout = findViewById(R.id.resetGameData);
        LogoutLayout = findViewById(R.id.logout);
        btn_logout = findViewById(R.id.btn_logout);
        btn_runaway = findViewById(R.id.btn_runaway);
        StageButtonLayout = findViewById(R.id.buttonLayout);
        btn_Easy = findViewById(R.id.btn_Easy);
        btn_Normal = findViewById(R.id.btn_Normal);
        btn_Hard = findViewById(R.id.btn_Hard);
        btn_Hell = findViewById(R.id.btn_Hell);
        btn_FireSlime = findViewById(R.id.btn_FireSlime);
        btn_WaterSlime = findViewById(R.id.btn_WaterSlime);
        btn_ForestSlime = findViewById(R.id.btn_ForestSlime);
        btn_DarkSlime = findViewById(R.id.btn_DarkSlime);
        btn_ODIN = findViewById(R.id.btn_ODIN);
        endlesschallengeMenu = findViewById(R.id.endlessChallengeDifficultyMenu);
        bossschallengeMenu = findViewById(R.id.BossChallengeSelectionMenu);
        btn_BossBack = findViewById(R.id.btn_BossBack);
        btn_EndlessBack = findViewById(R.id.btn_EndlessBack);
        MIDmenuUIContainer = findViewById(R.id.menuUiContainer);
        saveDataFile = new File("/data/data/" + getPackageName() + "/shared_prefs/savedata.xml");
        // Sound Effect Creator
        mediaPlayer = MediaPlayer.create(this,R.raw.mainmenubgm);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        //endregion

        // region Initialize UI Element.
        gameMainScreenContainer.setVisibility(View.GONE);
        stageUI.setVisibility(View.GONE);
        charStatusUI.setVisibility(View.GONE);
        menuTopUI.setVisibility(View.VISIBLE);
        updateUIStatus(StageValueType.MONEY);
        loadingBar.setMax(100);
        loadingBar.setProgress(0);
        loadingpercentage.setText("");
        // endregion


        // region Handle the Login Function and check the login in below.
        final Intent intent = getIntent(); // get the data intent from previous activity.
        isLogin = intent.getBooleanExtra("isLogin", false); // check is / not login to the server
        if(intent != null && isLogin) { // IF player login to the server success, the game data will load from server database. player can also save the game data to server.
            //btn_LoginDialog.setVisibility(View.GONE);
            PlayerName = intent.getStringExtra("NAME");
            PlayerID = intent.getStringExtra("ID");
            PlayerPassword = intent.getStringExtra("PW");
            tv_playername.setText(PlayerName);
            try {
                URL url = new URL("https://ds.dsgshk.com/login.php?"
                        + "ID=" + PlayerID + "&PW=" + PlayerPassword);
                loginConnection = new HTTPConnection(HTTPConnection.ConnectionFor.Login,PlayerID, PlayerPassword, mContext,GS,true);
                loginConnection.execute(url.toString());
            } catch (MalformedURLException e){

            }
        } else { // When Player play the game without account, the login status will change to offline mode and all game data will be save in the mobile data folder.
            final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(GameBackStageUI.this);
            final View DialogView = getLayoutInflater().inflate(R.layout.alertbox, null);
            final TextView DialogTitle = DialogView.findViewById(R.id.tv_alertTitle);
            final TextView DialogContext = DialogView.findViewById(R.id.tv_alertContext);
            final Button DialogBtnOk = DialogView.findViewById(R.id.btn_OK);
            final Button DialogBtnCancel = DialogView.findViewById(R.id.btn_Cancel);
            DialogBtnCancel.setVisibility(View.GONE);
            DialogContext.setGravity(Gravity.CENTER);
            DialogTitle.setText("Reminder");
            DialogContext.setText("It is recommended to login by your account to ensure that the game data will not cleared by some cache clear apps\nIf You want to create account, please go back to menu and click 'REGISTER' to sign up your own account. ");
            DialogBtnOk.setText("OK");
            DialogBuilder.setView(DialogView);
            isLogin = false;
            saveData.edit().putBoolean("isLogin",false).commit(); // set the login status to false and save in player's local harddisk.
            if(intent.getStringExtra("NAME")==null){
                PlayerName = saveData.getString("OfflinePlayerName","");
            }
            else {
                PlayerName = intent.getStringExtra("NAME");
                saveData.edit().putString("PlayerName",PlayerName).commit();
            }
            tv_playername.setText(PlayerName);
            final AlertDialog alertDialog = DialogBuilder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
            DialogBtnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    isPause = false;
                }
            });
        }
        // endregion

        //region Button Setting for Login Dialog
        /*
        btn_LoginDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(GameBackStageUI.this);
                final View DialogView = getLayoutInflater().inflate(R.layout.login, null);
                final EditText editText_ID = DialogView.findViewById(R.id.et_LoginID);
                final EditText editText_PW = DialogView.findViewById(R.id.et_LoginPW);
                final Button BtnLogin = DialogView.findViewById(R.id.btn_Login);

                BtnLogin.setText("Login");
                DialogBuilder.setView(DialogView);
                final AlertDialog alertDialog = DialogBuilder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
                BtnLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ID = editText_ID.getText().toString();
                        String PW = editText_PW.getText().toString();
                        try {
                            URL url = new URL("https://ds.dsgshk.com/login.php?"
                                    + "ID=" + ID + "&PW=" + PW);

                            if (loginConnection == null ||
                                    loginConnection.getStatus().equals(AsyncTask.Status.FINISHED)) {
                                loginConnection = new HTTPConnection(HTTPConnection.ConnectionFor.Login,ID, PW, mContext, GS);
                                loginConnection.execute(url.toString());
                                alertDialog.dismiss();
                            }

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
*/
        //endregion

        //region Button Setting for save game
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(GameBackStageUI.this);
                        final View DialogView = getLayoutInflater().inflate(R.layout.alertbox, null);
                        final TextView DialogTitle = DialogView.findViewById(R.id.tv_alertTitle);
                        final TextView DialogContext = DialogView.findViewById(R.id.tv_alertContext);
                        final Button DialogBtnOk = DialogView.findViewById(R.id.btn_OK);
                        final Button DialogBtnCancel = DialogView.findViewById(R.id.btn_Cancel);
                        DialogBtnCancel.setVisibility(View.GONE);
                        DialogContext.setGravity(Gravity.CENTER);
                        DialogTitle.setText("Reminder");
                        DialogContext.setText("Game Saved");
                        DialogBtnOk.setText("OK");
                        DialogBuilder.setView(DialogView);
                        final AlertDialog alertDialog = DialogBuilder.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();
                        if (!isLogin)
                            SaveGame(SaveGameMode.SaveTOLocal,true);
                            if(isReturnToMenu){
                                alertDialog.dismiss();
                            }
                        else if (isLogin) {
                            SaveGame(SaveGameMode.SaveTOServer,true);
                            alertDialog.dismiss();
                        }
                        DialogBtnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                        isPause = false;
                    }
                });
                Log.e("SaveGame", "Successfully");
            }
        });
        //endregion

        //region Button Setting for reset game
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(GameBackStageUI.this);
                final View DialogView = getLayoutInflater().inflate(R.layout.alertbox, null);
                final TextView DialogTitle = DialogView.findViewById(R.id.tv_alertTitle);
                final TextView DialogContext = DialogView.findViewById(R.id.tv_alertContext);
                final Button DialogBtnConfirm = DialogView.findViewById(R.id.btn_OK);
                final Button DialogBtnCancel = DialogView.findViewById(R.id.btn_Cancel);
                DialogContext.setGravity(Gravity.CENTER);
                DialogTitle.setText("Warning");
                DialogContext.setText("Are you sure you want to delete the game save data?");
                DialogBtnConfirm.setText("Yes");
                DialogBtnCancel.setText("No");
                DialogBuilder.setView(DialogView);
                final AlertDialog alertDialog = DialogBuilder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
                DialogBtnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                DialogBtnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(GameBackStageUI.this);
                        final View DialogView = getLayoutInflater().inflate(R.layout.alertbox, null);
                        final TextView DialogTitle = DialogView.findViewById(R.id.tv_alertTitle);
                        final TextView DialogContext = DialogView.findViewById(R.id.tv_alertContext);
                        final Button DialogBtnOk = DialogView.findViewById(R.id.btn_OK);
                        final Button DialogBtnCancel = DialogView.findViewById(R.id.btn_Cancel);
                        DialogBtnCancel.setVisibility(View.GONE);
                        DialogContext.setGravity(Gravity.CENTER);
                        DialogTitle.setText("Reminder");
                        DialogContext.setText("Game Data Clear.\n Game will restart when you click the 'OK' Button");
                        DialogBtnOk.setText("OK");
                        DialogBuilder.setView(DialogView);
                        final AlertDialog alertDialog = DialogBuilder.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();
                        saveData.edit().clear().commit();
                        DialogBtnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                                Intent i = getBaseContext().getPackageManager().
                                        getLaunchIntentForPackage(getBaseContext().getPackageName());
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                            }
                        });
                    }
                });
            }
        });
        //endregion

        //region Button Setting for Back To Main Menu
        btn_returnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isReturnToMenu = true;
                btn_save.callOnClick();
                CountDownTimer countDownTimer = new CountDownTimer(3000,1000) {
                    AlertDialog[] showedAD = new AlertDialog[3];
                    @Override
                    public void onTick(long millisUntilFinished) {
                        final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(GameBackStageUI.this);
                        final View DialogView = getLayoutInflater().inflate(R.layout.alertbox, null);
                        final TextView DialogTitle = DialogView.findViewById(R.id.tv_alertTitle);
                        final TextView DialogContext = DialogView.findViewById(R.id.tv_alertContext);
                        final Button DialogBtnOk = DialogView.findViewById(R.id.btn_OK);
                        final Button DialogBtnCancel = DialogView.findViewById(R.id.btn_Cancel);
                        DialogBtnCancel.setVisibility(View.GONE);
                        DialogBtnOk.setVisibility(View.GONE);
                        DialogContext.setGravity(Gravity.CENTER);
                        DialogTitle.setText("Reminder");
                        DialogContext.setText("Return To Main Menu After " + ((millisUntilFinished) / 1000)  +" s");
                        DialogBuilder.setView(DialogView);
                        final AlertDialog alertDialog = DialogBuilder.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();
                        if((millisUntilFinished/1000) >= showedAD.length){
                            showedAD[showedAD.length-1] = alertDialog;
                        }else
                            showedAD[(int)(millisUntilFinished/1000)] = alertDialog;
                    }

                    @Override
                    public void onFinish() {
                        //btn_save.callOnClick();
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        for(int i = 0;i < showedAD.length; i++) {
                            if(showedAD[i] != null)
                            showedAD[i].dismiss();
                        }
                        finish();
                        Intent intent1 = new Intent(mContext,mainmenu.class);
                        mContext.startActivity(intent1);
                    }
                }.start();
            }
        });
        //endregion

        //region Button Setting for Showing Setting Menu
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingMenu.setVisibility(View.VISIBLE);
                battleMenu.setVisibility(View.GONE);
                endlesschallengeMenu.setVisibility(View.GONE);
                bossschallengeMenu.setVisibility(View.GONE);
            }
        });
        //endregion

        //region Button Setting for Abnormal Character Data Reset.
        btn_abnormalstatusreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(GameBackStageUI.this);
                final View DialogView = getLayoutInflater().inflate(R.layout.alertbox, null);
                final TextView DialogTitle = DialogView.findViewById(R.id.tv_alertTitle);
                final TextView DialogContext = DialogView.findViewById(R.id.tv_alertContext);
                final Button DialogBtnOk = DialogView.findViewById(R.id.btn_OK);
                final Button DialogBtnCancel = DialogView.findViewById(R.id.btn_Cancel);
                DialogBtnCancel.setVisibility(View.GONE);
                DialogContext.setGravity(Gravity.CENTER);
                DialogBuilder.setView(DialogView);
                final AlertDialog alertDialog = DialogBuilder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                if(isLogin){
                    SaveGame(SaveGameMode.SaveTOServer,false);
                    CountDownTimer TimerToLogin = new CountDownTimer(1000,1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            DialogTitle.setText("Reminder");
                            DialogBtnOk.setVisibility(View.GONE);
                            DialogContext.setText("Loading data from server\nPlease wait.");
                            alertDialog.show();
                        }

                        @Override
                        public void onFinish() {
                            try {
                                if(loginConnection.getStatus() != AsyncTask.Status.FINISHED) {
                                    URL url = new URL("https://ds.dsgshk.com/login.php?"
                                            + "ID=" + PlayerID + "&PW=" + PlayerPassword);
                                    loginConnection = new HTTPConnection(HTTPConnection.ConnectionFor.LoadGame, PlayerID, PlayerPassword, mContext, GS, false);
                                    loginConnection.execute(url.toString());
                                }

                            } catch (MalformedURLException e){

                            }
                            DialogBtnOk.setVisibility(View.VISIBLE);
                            DialogTitle.setText("Reminder");
                            DialogContext.setText("Your Character Data are reset and load from Save Data.");
                            DialogBtnOk.setText("OK");
                            alertDialog.show();
                        }
                    }.start();
                } else {
                    SaveGame(SaveGameMode.SaveTOLocal, false);
                    DialogTitle.setText("Reminder");
                    DialogContext.setText("Your Character Data are reset and load from Save Data.");
                    DialogBtnOk.setText("OK");
                    alertDialog.show();
                }
                DialogBtnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        solveAbnormalStatus();
                        if (!isLogin) {
                            LoadGame(LoadGameMode.LoadFromLocal);
                        }

                        else if (isLogin) {
                            LoadGame(LoadGameMode.LoadFromServer);
                        }
                        alertDialog.dismiss();

                    }
                });

            }
        });
        //endregion

        // region Button Setting for Show the Battle Menu
        // Battle Button will call following action and start the loading of the stage.
        // the stage value will be reset behind the loading task and all enemies will be initialize after the loading.
        btn_battle = findViewById(R.id.btn_battle);
        btn_battle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                battleMenu.setVisibility(View.VISIBLE);
                settingMenu.setVisibility(View.GONE);
                bossschallengeMenu.setVisibility(View.GONE);
                btn_BossBack.setVisibility(View.GONE);
                endlesschallengeMenu.setVisibility(View.GONE);
            }
        });
        // endregion

        // region Button Setting for Pause
        btn_pause = findViewById(R.id.btn_Pause);
        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPause = true; // Pause the game.
                    // Create a custom alert dialog to remind the player changed the shooting mode.
                    final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(GameBackStageUI.this);
                    final View DialogView = getLayoutInflater().inflate(R.layout.alertbox, null);
                    final TextView DialogTitle = DialogView.findViewById(R.id.tv_alertTitle);
                    final TextView DialogContext = DialogView.findViewById(R.id.tv_alertContext);
                    final Button DialogBtnOk = DialogView.findViewById(R.id.btn_OK);
                    final Button DialogBtnCancel = DialogView.findViewById(R.id.btn_Cancel);

                    DialogBtnCancel.setVisibility(View.GONE);
                    DialogContext.setGravity(Gravity.CENTER);
                    DialogTitle.setText("Reminder");
                    DialogContext.setText("Game Paused.\nClick 'OK' to resume the game.");
                    DialogBtnOk.setText("OK");
                    DialogBuilder.setView(DialogView);
                    final AlertDialog alertDialog = DialogBuilder.create();
                    alertDialog.show();
                    DialogBtnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                            isPause = false; // Resume the game.
                        }
                    });
                }
        });
        // endregion

        // region Button Setting of Runaway
        btn_runaway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPause = true; // Pause the game.
                final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(GameBackStageUI.this);
                final View DialogView = getLayoutInflater().inflate(R.layout.alertbox, null);
                final TextView DialogTitle = DialogView.findViewById(R.id.tv_alertTitle);
                final TextView DialogContext = DialogView.findViewById(R.id.tv_alertContext);
                final Button DialogBtnOk = DialogView.findViewById(R.id.btn_OK);
                final Button DialogBtnCancel = DialogView.findViewById(R.id.btn_Cancel);

                DialogContext.setGravity(Gravity.CENTER);
                DialogTitle.setText("Warning");
                DialogContext.setText("Are you sure escape from this stage?\nYou cannot get any reward from current game, if you confirm to escape.");
                DialogBtnOk.setText("Yes");
                DialogBtnCancel.setText("No");
                DialogBuilder.setView(DialogView);
                final AlertDialog alertDialog = DialogBuilder.create();
                alertDialog.show();
                DialogBtnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        expTotalReward = 0;
                        moneyTotalReward = 0;
                        GP.character.isDead = true;
                        isPause = false; // Resume the game.
                    }
                });
                DialogBtnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        isPause = false; // Resume the game.
                    }
                });
            }
        });
        // endregion

        // region Button Setting for Character Info
        // Setting of character info screen.
        btn_character.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a custom alert dialog to show the player's character attributes and the value.
                final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(GameBackStageUI.this);
                final View DialogView = getLayoutInflater().inflate(R.layout.characterinfo, null);
                //final TextView DialogTitle = DialogView.findViewById(R.id.tv_alertTitle);
                final TextView hpText = DialogView.findViewById(R.id.tv_hp);
                final TextView mpText = DialogView.findViewById(R.id.tv_mp);
                final TextView hprecoveryText = DialogView.findViewById(R.id.tv_hprecovery);
                final TextView mprecoveryText = DialogView.findViewById(R.id.tv_mpcovery);
                final TextView movespeedText = DialogView.findViewById(R.id.tv_movespeed);
                final TextView atkspeedText = DialogView.findViewById(R.id.tv_atkspeed);
                final TextView atkText = DialogView.findViewById(R.id.tv_atk);

                // Setting the text with color.
                String hpcolorText = "<font color=\"#ffffff\">HP: </font><font color=\"#ffffff\">" + character.getMaxHp() + "</font>";
                String mpcolorText = "<font color=\"#ffffff\">MP:</font><font color=\"#ffffff\"> " + character.getMaxMp() + "</font>";
                String hprecoverycolorText = "<font color=\"#ffffff\">HP Recovery Speed: </font><font color=\"#ffffff\"> " + character.getHpRecoveryValue() + " / " + character.getHpRecoverySpeed() / 1000 + "s</font>";
                String mprecoverycolorText = "<font color=\"#ffffff\">MP Recovery Speed:</font><font color=\"#ffffff\"> " + character.getMpRecoveryValue() + " / " + character.getMpRecoverySpeed() / 1000 + "s</font>";
                String movespeedcolorText = "<font color=\"#ffffff\">Move Speed:</font><font color=\"#ffffff\"> " + character.getMoveSpeed() + "</font>";
                String atkspeedcolorText = "<font color=\"#ffffff\">ATK Speed:</font><font color=\"#ffffff\"> " + "Shoot per " + String.format("%.2f", character.getAtkSpeed() / 1000) + " s</font>";
                String atkcolorText = "<font color=\"#ffffff\">ATK:</font><font color=\"#ffffff\"> " + character.getMinDamage() + " ~ " + character.getMaxdamage() + "</font>";

                atkText.setText(Html.fromHtml(atkcolorText));
                atkspeedText.setText(Html.fromHtml(atkspeedcolorText));
                movespeedText.setText(Html.fromHtml(movespeedcolorText));
                mprecoveryText.setText(Html.fromHtml(mprecoverycolorText));
                hprecoveryText.setText(Html.fromHtml(hprecoverycolorText));
                mpText.setText(Html.fromHtml(mpcolorText));
                hpText.setText(Html.fromHtml(hpcolorText));


                DialogBuilder.setView(DialogView);
                final AlertDialog alertDialog = DialogBuilder.create();
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();
            }
        });
        // endregion

        // region Button Setting for Equipment Screen
        // Setting of the equipment screen.
        btn_equipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // region Basic Element Setting
                final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(GameBackStageUI.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                final View DialogView = getLayoutInflater().inflate(R.layout.equipment, null);
                final int MaxLv = character.getLv(); // set the equipment max level from character level.
                // endregion

                // region Equipment Option and Display Elements
                final TextView FireBallSkillText = DialogView.findViewById(R.id.fireBall_skill);
                final TextView BlueFireSkillText = DialogView.findViewById(R.id.blueFire_skill);
                final TextView ForestSkillText = DialogView.findViewById(R.id.forestSubstance_skill);
                final TextView DarkElementSkillText = DialogView.findViewById(R.id.darkelement_skill);
                final TextView FireBallATKText = DialogView.findViewById(R.id.fireBall_atk);
                final TextView BlueFireATKText = DialogView.findViewById(R.id.blueFire_atk);
                final TextView ForestATKText = DialogView.findViewById(R.id.forestSubstance_atk);
                final TextView DarkElementATKText = DialogView.findViewById(R.id.darkelement_atk);
                final TextView FireBallSpeedText = DialogView.findViewById(R.id.fireBall_bulletSpeed);
                final TextView BlueFireSpeedText = DialogView.findViewById(R.id.blueFire_bulletSpeed);
                final TextView ForestSpeedText = DialogView.findViewById(R.id.forestSubstance_bulletSpeed);
                final TextView DarkElementSpeedText = DialogView.findViewById(R.id.darkelement_bulletSpeed);
                final TextView FireBallLV = DialogView.findViewById(R.id.fireBall_lv);
                final TextView BlueFireLV = DialogView.findViewById(R.id.blueFire_lv);
                final TextView ForestLV = DialogView.findViewById(R.id.forestSubstance_lv);
                final TextView DarkElementLV = DialogView.findViewById(R.id.darkelement_lv);
                final TextView FireBallStatus = DialogView.findViewById(R.id.fireBall_status);
                final TextView BlueFireStatus = DialogView.findViewById(R.id.blueFire_status);
                final TextView ForestStatus = DialogView.findViewById(R.id.forestSubstance_status);
                final TextView DarkElementStatus = DialogView.findViewById(R.id.darkElement_status);
                final TextView RedStoneText = DialogView.findViewById(R.id.tv_fireballElement);
                final TextView GreenStoneText = DialogView.findViewById(R.id.tv_forestElement);
                final TextView BlueStoneText = DialogView.findViewById(R.id.tv_bluefireElement);
                final TextView DarkStoneText = DialogView.findViewById(R.id.tv_darkElement);

                // endregion

                // region Button Control and Display Elements.
                final Button BtnEquip = DialogView.findViewById(R.id.btn_equip);
                final Button BtnCancel = DialogView.findViewById(R.id.btn_Close);
                final Button BtnEnhance = DialogView.findViewById(R.id.btn_lvUp);
                // endregion

                // region Declear the Icon of each equipment.
                final ImageView FireBallIcon = DialogView.findViewById(R.id.fireBall_picture), BlueFireIcon = DialogView.findViewById(R.id.blueFire_picture),
                        ForestIcon = DialogView.findViewById(R.id.forestSubstance_picture), DarkElementIcon = DialogView.findViewById(R.id.darkelement_picture);
                final LinearLayout FireBallLayout = DialogView.findViewById(R.id.fireBall), BlueFireLayout = DialogView.findViewById(R.id.blueFire),
                        ForestLayout = DialogView.findViewById(R.id.forestSubstance), DarkLayout = DialogView.findViewById(R.id.darkelement);
                // endregion

                // region Setting of FireBallIcon Button
                // When Player Touch on the FireBall Icon, set the selectedEquipment to FireBallIcon and allow Player to equip or enhance the selection equipment.
                FireBallLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedEquipment = FireBallIcon;
                        FireBallIcon.setBackgroundResource(R.drawable.iconbg);
                        BlueFireIcon.setBackgroundResource(0);
                        ForestIcon.setBackgroundResource(0);
                        DarkElementIcon.setBackgroundResource(0);
                        FireBallLayout.setBackgroundResource(R.drawable.frame1_black);
                        BlueFireLayout.setBackgroundResource(0);
                        ForestLayout.setBackgroundResource(0);
                        DarkLayout.setBackgroundResource(0);
                        BtnEquip.setVisibility(View.VISIBLE);
                        BtnEnhance.setVisibility(View.VISIBLE);
                    }
                });
                // endregion

                // region Setting of BlueFireIcon Button
                // When Player Touch on the BlueFire Icon, set the selectedEquipment to BlueFireIcon and allow Player to equip or enhance the selection equipment.
                BlueFireLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedEquipment = BlueFireIcon;
                        BlueFireIcon.setBackgroundResource(R.drawable.iconbg);
                        FireBallIcon.setBackgroundResource(0);
                        ForestIcon.setBackgroundResource(0);
                        DarkElementIcon.setBackgroundResource(0);
                        FireBallLayout.setBackgroundResource(0);
                        BlueFireLayout.setBackgroundResource(R.drawable.frame1_black);
                        ForestLayout.setBackgroundResource(0);
                        DarkLayout.setBackgroundResource(0);
                        BtnEquip.setVisibility(View.VISIBLE);
                        BtnEnhance.setVisibility(View.VISIBLE);
                    }
                });
                // endregion

                // region Setting of ForestIcon Button
                // When Player Touch on the Forest Icon, set the selectedEquipment to ForestIcon and allow Player to equip or enhance the selection equipment.
                ForestLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedEquipment = ForestIcon;
                        ForestIcon.setBackgroundResource(R.drawable.iconbg);
                        BlueFireIcon.setBackgroundResource(0);
                        FireBallIcon.setBackgroundResource(0);
                        DarkElementIcon.setBackgroundResource(0);
                        FireBallLayout.setBackgroundResource(0);
                        BlueFireLayout.setBackgroundResource(0);
                        ForestLayout.setBackgroundResource(R.drawable.frame1_black);
                        DarkLayout.setBackgroundResource(0);
                        BtnEquip.setVisibility(View.VISIBLE);
                        BtnEnhance.setVisibility(View.VISIBLE);
                    }
                });
                // endregion

                // region Setting of DarkElementIcon Button
                // When Player Touch on the DarkElement Icon, set the selectedEquipment to DarkElementIcon and allow Player to equip or enhance the selection equipment.
                DarkLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedEquipment = DarkElementIcon;
                        DarkElementIcon.setBackgroundResource(R.drawable.iconbg);
                        BlueFireIcon.setBackgroundResource(0);
                        ForestIcon.setBackgroundResource(0);
                        FireBallIcon.setBackgroundResource(0);
                        FireBallLayout.setBackgroundResource(0);
                        BlueFireLayout.setBackgroundResource(0);
                        ForestLayout.setBackgroundResource(0);
                        DarkLayout.setBackgroundResource(R.drawable.frame1_black);
                        BtnEquip.setVisibility(View.VISIBLE);
                        BtnEnhance.setVisibility(View.VISIBLE);
                    }
                });
                // endregion

                // Setting of the screen content.
                 /* Index 0 = FireBall
                                      Index 1 = BlueFire
                                      Index 2 = Forest
                                      Index 3 = Dark
                                */
                // region Update the Description of Equipment (Include Skill and the value of equipment)
                if (bulletEquipmentContainer.bulletEquipments[0].lv <= 11) {
                    FireBallSkillText.setText("Skill:\n1. Poor Mana: Decrease 40 Max Mana Point." + "\n\n2. On Fire: Damage will increase by attack times; 1 extra damage per " + (21 - bulletEquipmentContainer.bulletEquipments[0].lv) + " attack times;\nMaximum extra damage: " + (2 * bulletEquipmentContainer.bulletEquipments[0].lv));
                } else
                    FireBallSkillText.setText("Skill:\n1. Poor Mana: Decrease 40 Max Mana Point." + "\n\n2. On Fire: Damage will increase by attack times; 1 extra damage per " + 10 + " attack times;\nMaximum extra damage: " + (2 * bulletEquipmentContainer.bulletEquipments[0].lv));
                BlueFireSkillText.setText("Skill:\n1. Speed UP: Increase " + (14 + bulletEquipmentContainer.bulletEquipments[1].lv) + "% attack speed." + "\n\n2. Overheat: \nMax Mana + " + (17 + (bulletEquipmentContainer.bulletEquipments[1].lv * 3)) + ". \nMP Recovery Value + " + (6 + (bulletEquipmentContainer.bulletEquipments[1].lv)) + ".\nBut Mana will not recovery if you still have Mana and you can't shoot before full recovery.");
                ForestSkillText.setText("Skill:\n1. Poison: \n15% poisoning the target and make it get hurt per second. the poison effect will hold 5 seconds.\nMaximum Stack: 5" + "\nDamage: " + (1 + bulletEquipmentContainer.bulletEquipments[2].lv / 2) + "\n\n2. Lucky or Not: \n25% cause " + (109 + bulletEquipmentContainer.bulletEquipments[2].lv) + "% damage;\n25% cause 75% damage. \n50% cause " + (bulletEquipmentContainer.bulletEquipments[2].lv / 4) + " damage");
                DarkElementSkillText.setText("Skill:\n1. Vampire: Steal target 1% HP and recover your HP with equal values. \nAt least recover 1 HP per attack. " + "\n\n2. Bloodthirsty: You CAN'T recover hp from the auto recovery. \nWhen your HP% is higher than 50%, your damage will be increase up to 50%.\nConversely, when your HP% is below 50%, your damage will drop.  ");
                FireBallATKText.setText("ATK: " + bulletEquipmentContainer.bulletEquipments[0].totalMinATK + " ~ " + bulletEquipmentContainer.bulletEquipments[0].totalMaxATK);
                BlueFireATKText.setText("ATK: " + bulletEquipmentContainer.bulletEquipments[1].totalMinATK + " ~ " + bulletEquipmentContainer.bulletEquipments[1].totalMaxATK);
                ForestATKText.setText("ATK: " + bulletEquipmentContainer.bulletEquipments[2].totalMinATK + " ~ " + bulletEquipmentContainer.bulletEquipments[2].totalMaxATK);
                DarkElementATKText.setText("ATK: " + bulletEquipmentContainer.bulletEquipments[3].totalMinATK + " ~ " + bulletEquipmentContainer.bulletEquipments[3].totalMaxATK);
                FireBallSpeedText.setText("Bullet Speed: " + bulletEquipmentContainer.bulletEquipments[0].totalBulletSpeed);
                BlueFireSpeedText.setText("Bullet Speed: " + bulletEquipmentContainer.bulletEquipments[1].totalBulletSpeed);
                ForestSpeedText.setText("Bullet Speed: " + bulletEquipmentContainer.bulletEquipments[2].totalBulletSpeed);
                DarkElementSpeedText.setText("Bullet Speed: " + bulletEquipmentContainer.bulletEquipments[3].totalBulletSpeed);
                FireBallLV.setText(bulletEquipmentContainer.bulletEquipments[0].lv + " / " + MaxLv);
                BlueFireLV.setText(bulletEquipmentContainer.bulletEquipments[1].lv + " / " + MaxLv);
                ForestLV.setText(bulletEquipmentContainer.bulletEquipments[2].lv + " / " + MaxLv);
                DarkElementLV.setText(bulletEquipmentContainer.bulletEquipments[3].lv + " / " + MaxLv);
                RedStoneText.setText("" + redStone);
                GreenStoneText.setText("" + greenStone);
                BlueStoneText.setText("" + blueStone);
                DarkStoneText.setText("" + darkStone);
                // endregion

                DialogBuilder.setView(DialogView);
                final AlertDialog EquipmentalertDialog = DialogBuilder.create();
                EquipmentalertDialog.setCanceledOnTouchOutside(false);
                EquipmentalertDialog.show();

                // region Setting of "Equip" Button
                BtnEquip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FireBallStatus.setVisibility(View.INVISIBLE);
                        BlueFireStatus.setVisibility(View.INVISIBLE);
                        ForestStatus.setVisibility(View.INVISIBLE);
                        DarkElementStatus.setVisibility(View.INVISIBLE);
                        if (selectedEquipment == FireBallIcon) {
                            bulletEquipmentContainer.setEquippedBullet(character, BulletEquipment.BulletType.FIREBALL);
                            FireBallStatus.setVisibility(View.VISIBLE);
                            FireBallStatus.setText("Equipped");
                        } else if (selectedEquipment == DarkElementIcon) {
                            bulletEquipmentContainer.setEquippedBullet(character, BulletEquipment.BulletType.DARK);
                            DarkElementStatus.setVisibility(View.VISIBLE);
                            DarkElementStatus.setText("Equipped");
                        } else if (selectedEquipment == ForestIcon) {
                            bulletEquipmentContainer.setEquippedBullet(character, BulletEquipment.BulletType.FOREST);
                            ForestStatus.setVisibility(View.VISIBLE);
                            ForestStatus.setText("Equipped");
                        } else if (selectedEquipment == BlueFireIcon) {
                            bulletEquipmentContainer.setEquippedBullet(character, BulletEquipment.BulletType.BLUEFIRE);
                            BlueFireStatus.setVisibility(View.VISIBLE);
                            BlueFireStatus.setText("Equipped");
                        }
                    }
                });
                // endregion

                // region Setting of "Enhance" Button
                BtnEnhance.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EquipmentalertDialog.dismiss();
                        final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(GameBackStageUI.this);
                        final View DialogView = getLayoutInflater().inflate(R.layout.alertbox_enhance, null);
                        final TextView DialogTitle = DialogView.findViewById(R.id.tv_enhancealertTitle);
                        final TextView DialogContext = DialogView.findViewById(R.id.tv_enhancealertContent);
                        final TextView MaterialsText = DialogView.findViewById(R.id.tv_materialsNeed);
                        final TextView MoneyText = DialogView.findViewById(R.id.tv_MoneyNeed);
                        final ImageView MaterialsIcon = DialogView.findViewById(R.id.iv_MaterialIcon);
                        final Button DialogBtnOk = DialogView.findViewById(R.id.btn_EnhanceOK);
                        final Button DialogBtnCancel = DialogView.findViewById(R.id.btn_EnhanceCancel);
                        DialogContext.setGravity(Gravity.CENTER);
                        DialogTitle.setText("Enhance");
                        DialogContext.setText("Enhance Requirement: ");

                        if (selectedEquipment == FireBallIcon) {
                            MaterialsIcon.setImageResource(R.drawable.fireballelement);
                            MoneyNeed = 125 + bulletEquipmentContainer.bulletEquipments[0].lv * 75;
                            MoneyText.setText("- " + MoneyNeed);
                            if(bulletEquipmentContainer.bulletEquipments[0].lv <= 20)
                                MaterialsNeed = 0;
                             else
                                MaterialsNeed = (int)(0 + Math.floor((double)(bulletEquipmentContainer.bulletEquipments[0].lv) * 0.1));

                            MaterialsText.setText("- " + MaterialsNeed);
                        } else if (selectedEquipment == DarkElementIcon) {
                            MaterialsIcon.setImageResource(R.drawable.darkelement);
                            MoneyNeed = 125 + bulletEquipmentContainer.bulletEquipments[3].lv * 75;
                            MoneyText.setText("- " + MoneyNeed);
                            if(bulletEquipmentContainer.bulletEquipments[3].lv <= 20)
                                MaterialsNeed = 0;
                            else
                                MaterialsNeed = (int)(0 + Math.floor((double)(bulletEquipmentContainer.bulletEquipments[3].lv) * 0.1));
                            MaterialsText.setText("- " + MaterialsNeed);
                        } else if (selectedEquipment == ForestIcon) {
                            MaterialsIcon.setImageResource(R.drawable.forestelement);
                            MoneyNeed = 125 + bulletEquipmentContainer.bulletEquipments[2].lv * 75;
                            MoneyText.setText("- " + MoneyNeed);
                            if(bulletEquipmentContainer.bulletEquipments[2].lv <= 20)
                                MaterialsNeed = 0;
                            else
                                MaterialsNeed = (int)(0 + Math.floor((double)(bulletEquipmentContainer.bulletEquipments[2].lv) * 0.1));
                            MaterialsText.setText("- " + MaterialsNeed);
                        } else if (selectedEquipment == BlueFireIcon) {
                            MaterialsIcon.setImageResource(R.drawable.bluefireelement);
                            MoneyNeed = 125 + bulletEquipmentContainer.bulletEquipments[1].lv * 75;
                            MoneyText.setText("- " + MoneyNeed);
                            if(bulletEquipmentContainer.bulletEquipments[1].lv <= 20)
                                MaterialsNeed = 0;
                            else
                                MaterialsNeed = (int)(0 + Math.floor((double)(bulletEquipmentContainer.bulletEquipments[1].lv) * 0.1));
                            MaterialsText.setText("- " + MaterialsNeed);
                        }

                        DialogBtnOk.setText("Yes");
                        DialogBtnCancel.setText("No");
                        DialogBuilder.setView(DialogView);
                        final AlertDialog EnhancealertDialog = DialogBuilder.create();
                        DialogBtnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EnhancealertDialog.dismiss();
                                final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(GameBackStageUI.this);
                                final View DialogView = getLayoutInflater().inflate(R.layout.alertbox, null);
                                final TextView DialogTitle = DialogView.findViewById(R.id.tv_alertTitle);
                                final TextView DialogContext = DialogView.findViewById(R.id.tv_alertContext);
                                final Button DialogBtnOk = DialogView.findViewById(R.id.btn_OK);
                                final Button DialogBtnCancel = DialogView.findViewById(R.id.btn_Cancel);
                                DialogBtnCancel.setVisibility(View.GONE);
                                DialogContext.setGravity(Gravity.CENTER);
                                DialogBuilder.setView(DialogView);
                                final AlertDialog alertDialog = DialogBuilder.create();
                                DialogBtnOk.setText("OK");
                                DialogBtnOk.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                            alertDialog.dismiss();
                                            btn_equipment.performClick();
                                        updateUIStatus(StageValueType.MONEY);
                                    }
                                });
                                if (selectedEquipment != null) {
                                    if(money < MoneyNeed){
                                        DialogTitle.setText("Warning");
                                        DialogContext.setText("You don't have enough money!");
                                    }
                                    else if (selectedEquipment == FireBallIcon) {
                                        if(redStone < MaterialsNeed){
                                            DialogTitle.setText("Warning");
                                            DialogContext.setText("You don't have enough Red Stone!");
                                        }
                                        else if (bulletEquipmentContainer.bulletEquipments[0].lv < MaxLv) {
                                            bulletEquipmentContainer.enhanceBullet(character, BulletEquipment.BulletType.FIREBALL);
                                            DialogTitle.setText("Enhance Success");
                                            DialogContext.setVisibility(View.GONE);
                                            redStone -= MaterialsNeed;
                                            money -= MoneyNeed;
                                            isSuccess = true;
                                        }else {
                                            DialogTitle.setText("Warning");
                                            DialogContext.setText("Level has reached the MAX level");
                                        }
                                    } else if (selectedEquipment == DarkElementIcon) {
                                        if (darkStone < MaterialsNeed) {
                                            DialogTitle.setText("Warning");
                                            DialogContext.setText("You don't have enough Dark Stone!");
                                        } else if (bulletEquipmentContainer.bulletEquipments[3].lv < MaxLv) {
                                            bulletEquipmentContainer.enhanceBullet(character, BulletEquipment.BulletType.DARK);
                                            DialogTitle.setText("Enhance Success");
                                            DialogContext.setVisibility(View.GONE);
                                            darkStone -= MaterialsNeed;
                                            money -= MoneyNeed;
                                            isSuccess = true;
                                        }else {
                                            DialogTitle.setText("Warning");
                                            DialogContext.setText("Level has reached the MAX level");
                                        }
                                    } else if (selectedEquipment == ForestIcon) {
                                        if (greenStone < MaterialsNeed) {
                                            DialogTitle.setText("Warning");
                                            DialogContext.setText("You don't have enough Green Stone!");
                                        } else if (bulletEquipmentContainer.bulletEquipments[2].lv < MaxLv) {
                                            bulletEquipmentContainer.enhanceBullet(character, BulletEquipment.BulletType.FOREST);
                                            DialogTitle.setText("Enhance Success");
                                            DialogContext.setVisibility(View.GONE);
                                            greenStone -= MaterialsNeed;
                                            money -= MoneyNeed;
                                            isSuccess = true;
                                        } else {
                                            DialogTitle.setText("Warning");
                                            DialogContext.setText("Level has reached the MAX level");
                                        }
                                    } else if (selectedEquipment == BlueFireIcon) {
                                        if (blueStone < MaterialsNeed) {
                                            DialogTitle.setText("Warning");
                                            DialogContext.setText("You don't have enough Blue Stone!");
                                        } else  if (bulletEquipmentContainer.bulletEquipments[1].lv < MaxLv){
                                            bulletEquipmentContainer.enhanceBullet(character, BulletEquipment.BulletType.BLUEFIRE);
                                            DialogTitle.setText("Enhance Success");
                                            DialogContext.setVisibility(View.GONE);
                                            blueStone -= MaterialsNeed;
                                            money -= MoneyNeed;
                                            isSuccess = true;
                                        } else {
                                            DialogTitle.setText("Warning");
                                            DialogContext.setText("Level has reached the MAX level");
                                        }
                                    }
                                    alertDialog.show();
                                }
                            }
                        });
                        DialogBtnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EnhancealertDialog.dismiss();
                            }
                        });
                        EnhancealertDialog.show();
                    }
                });
                //endregion

                // region Setting of "Cancel" Button
                // For Close the equipment windows
                BtnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EquipmentalertDialog.dismiss();
                    }
                });
                // endregion

                // region Initialize the screen status and get the current equipped bullet.
                FireBallStatus.setVisibility(View.INVISIBLE);
                BlueFireStatus.setVisibility(View.INVISIBLE);
                ForestStatus.setVisibility(View.INVISIBLE);
                DarkElementStatus.setVisibility(View.INVISIBLE);

                if (character.getEquippedBullet().getBulletType() == BulletEquipment.BulletType.FIREBALL) {
                    FireBallStatus.setVisibility(View.VISIBLE);
                    FireBallStatus.setText("Equipped");
                } else if (character.getEquippedBullet().getBulletType() == BulletEquipment.BulletType.BLUEFIRE) {
                    BlueFireStatus.setVisibility(View.VISIBLE);
                    BlueFireStatus.setText("Equipped");
                } else if (character.getEquippedBullet().getBulletType() == BulletEquipment.BulletType.FOREST) {
                    ForestStatus.setVisibility(View.VISIBLE);
                    ForestStatus.setText("Equipped");
                } else if (character.getEquippedBullet().getBulletType() == BulletEquipment.BulletType.DARK) {
                    DarkElementStatus.setVisibility(View.VISIBLE);
                    DarkElementStatus.setText("Equipped");
                }

                // Hide the button. these button will show when the player select the bullet by click the bullet icon.
                BtnEquip.setVisibility(View.INVISIBLE);
                BtnEnhance.setVisibility(View.INVISIBLE);
                // endregion
            }
        });
        //endregion

        // region Button Setting for Skill Screen
        // Setting of the equipment screen.
        btn_skill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // region Basic Element Setting
                final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(GameBackStageUI.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                final View DialogView = getLayoutInflater().inflate(R.layout.skill, null);
                final int MaxLv = character.getLv(); // set the equipment max level from character level.
                // endregion
                // region Equipment Option and Display Elements
                final TextView SkillPointText = DialogView.findViewById(R.id.tv_SP);
                final TextView HPUPDescription = DialogView.findViewById(R.id.hpup_description);
                final TextView MPUPDescription = DialogView.findViewById(R.id.mpup_description);
                final TextView HPRecoveryUPDescription = DialogView.findViewById(R.id.hprecoveryup_description);
                final TextView MPRecoveryUPDescription = DialogView.findViewById(R.id.mprecoveryup_description);
                final TextView MoveSpeedUpDescription = DialogView.findViewById(R.id.movespeedup_description);
                final TextView SoulEaterDescription = DialogView.findViewById(R.id.souleater_description);
                final TextView HPUPLvText = DialogView.findViewById(R.id.hpup_lv);
                final TextView MPUPLvText = DialogView.findViewById(R.id.mpup_lv);
                final TextView HPRecoveryUPLvText = DialogView.findViewById(R.id.hprecoveryup_lv);
                final TextView MPRecoveryUPLvText = DialogView.findViewById(R.id.mprecoveryup_lv);
                final TextView MoveSpeedUPLvText = DialogView.findViewById(R.id.movespeedup_lv);
                final TextView SoulEaterLvText = DialogView.findViewById(R.id.souleater_lv);
                // endregion

                // region Button Control and Display Elements.
                final Button BtnLearn = DialogView.findViewById(R.id.btn_learn);
                final Button BtnCancel = DialogView.findViewById(R.id.btn_Close);
                // endregion

                // region Declear the Layout of each equipment.
                final ImageView HPUPIcon = DialogView.findViewById(R.id.hpup_picture), MPUPIcon = DialogView.findViewById(R.id.mpup_picture),
                        HPRecoveryUPIcon = DialogView.findViewById(R.id.hprecoveryup_picture), MPRecoveryUPIcon = DialogView.findViewById(R.id.mprecoveryup_picture),
                        MoveSpeedUPIcon = DialogView.findViewById(R.id.movespeedup_picture), SoulEaterUPIcon = DialogView.findViewById(R.id.souleater_picture);
                final LinearLayout HPUPContainer = DialogView.findViewById(R.id.hpup), MPUPContainer = DialogView.findViewById(R.id.mpup),
                        HPRecoveryUPContainer = DialogView.findViewById(R.id.hprecoveryup), MPRecoveryUPContainer = DialogView.findViewById(R.id.mprecoveryup),
                        MoveSpeedUPContainer = DialogView.findViewById(R.id.movespeedup), SoulEaterContainer = DialogView.findViewById(R.id.souleater);
                // endregion

                // region Setting of HPUP Button
                // When Player Touch on the HPUP Icon, set the selectedSkill to HPUPIcon and allow Player to learn the selection skill.
                HPUPContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedSkill = HPUPIcon;
                        HPUPContainer.setBackgroundResource(R.drawable.frame1_black);
                        MPUPContainer.setBackgroundResource(0);
                        HPRecoveryUPContainer.setBackgroundResource(0);
                        MPRecoveryUPContainer.setBackgroundResource(0);
                        MoveSpeedUPContainer.setBackgroundResource(0);
                        SoulEaterContainer.setBackgroundResource(0);
                        BtnLearn.setVisibility(View.VISIBLE);
                    }
                });
                // endregion

                // region Setting of MPUP Button
                // When Player Touch on the MPUP Icon, set the selectedSkill to MPUPIcon and allow Player to learn the selection skill.
                MPUPContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedSkill = MPUPIcon;
                        HPUPContainer.setBackgroundResource(0);
                        MPUPContainer.setBackgroundResource(R.drawable.frame1_black);
                        HPRecoveryUPContainer.setBackgroundResource(0);
                        MPRecoveryUPContainer.setBackgroundResource(0);
                        MoveSpeedUPContainer.setBackgroundResource(0);
                        SoulEaterContainer.setBackgroundResource(0);
                        BtnLearn.setVisibility(View.VISIBLE);
                    }
                });
                // endregion

                // region Setting of HPRecoveryUP Button
                // When Player Touch on the HPRecoveryUP Icon, set the selectedSkill to HPRecoveryUPIcon and allow Player to learn the selection skill.
                HPRecoveryUPContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedSkill = HPRecoveryUPIcon;
                        HPUPContainer.setBackgroundResource(0);
                        MPUPContainer.setBackgroundResource(0);
                        HPRecoveryUPContainer.setBackgroundResource(R.drawable.frame1_black);
                        MPRecoveryUPContainer.setBackgroundResource(0);
                        MoveSpeedUPContainer.setBackgroundResource(0);
                        SoulEaterContainer.setBackgroundResource(0);
                        BtnLearn.setVisibility(View.VISIBLE);
                    }
                });
                // endregion

                // region Setting of MPRecoveryUP Button
                // When Player Touch on the MPRecoveryUP Icon, set the selectedSkill to MPRecoveryUPIcon and allow Player to learn the selection skill.
                MPRecoveryUPContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedSkill = MPRecoveryUPIcon;
                        HPUPContainer.setBackgroundResource(0);
                        MPUPContainer.setBackgroundResource(0);
                        HPRecoveryUPContainer.setBackgroundResource(0);
                        MPRecoveryUPContainer.setBackgroundResource(R.drawable.frame1_black);
                        MoveSpeedUPContainer.setBackgroundResource(0);
                        SoulEaterContainer.setBackgroundResource(0);
                        BtnLearn.setVisibility(View.VISIBLE);
                    }
                });
                // endregion

                // region Setting of MoveSpeedUp Button
                // When Player Touch on the MoveSpeedUp Icon, set the selectedSkill to MoveSpeedUpIcon and allow Player to learn the selection skill.
                MoveSpeedUPContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedSkill = MoveSpeedUPIcon;
                        HPUPContainer.setBackgroundResource(0);
                        MPUPContainer.setBackgroundResource(0);
                        HPRecoveryUPContainer.setBackgroundResource(0);
                        MPRecoveryUPContainer.setBackgroundResource(0);
                        MoveSpeedUPContainer.setBackgroundResource(R.drawable.frame1_black);
                        SoulEaterContainer.setBackgroundResource(0);
                        BtnLearn.setVisibility(View.VISIBLE);
                    }
                });
                // endregion

                // region Setting of SoulEater Button
                // When Player Touch on the SoulEaterIcon, set the selectedSkill to SoulEaterIcon and allow Player to learn the selection skill.
                SoulEaterContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedSkill = SoulEaterUPIcon;
                        HPUPContainer.setBackgroundResource(0);
                        MPUPContainer.setBackgroundResource(0);
                        HPRecoveryUPContainer.setBackgroundResource(0);
                        MPRecoveryUPContainer.setBackgroundResource(0);
                        MoveSpeedUPContainer.setBackgroundResource(0);
                        SoulEaterContainer.setBackgroundResource(R.drawable.frame1_black);
                        BtnLearn.setVisibility(View.VISIBLE);
                    }
                });
                // endregion

                // Setting of the screen content.
                 /* Index 0 = HPUP
                                      Index 1 = MPUP
                                      Index 2 = HPRecoveryUP
                                      Index 3 = MPRecoveryUP
                                      Index 4 = MoveSpeedUP
                                      Index 5 = SoulEater
                                */
                // region Update the Description of Skill
                SkillPointText.setText("SP: " + skillsContainer.skillPoint);
                HPUPLvText.setText(skillsContainer.skills[0].lv + " / " + MaxLv);
                MPUPLvText.setText(skillsContainer.skills[1].lv + " / " + MaxLv);
                HPRecoveryUPLvText.setText(skillsContainer.skills[2].lv + " / " + MaxLv);
                MPRecoveryUPLvText.setText(skillsContainer.skills[3].lv + " / " + MaxLv);
                MoveSpeedUPLvText.setText(skillsContainer.skills[4].lv + " / " + MaxLv);
                SoulEaterLvText.setText(skillsContainer.skills[5].lv + " / " + MaxLv);
                HPUPDescription.setText("Increase " + String.format("%.0f", skillsContainer.skills[0].totalValue) + " ( +5/Level ) MaxHP");
                MPUPDescription.setText("Increase " + String.format("%.0f", skillsContainer.skills[1].totalValue) + " ( +3/Level ) MaxMP");
                HPRecoveryUPDescription.setText("Increase " + String.format("%.0f", skillsContainer.skills[2].totalValue) + " ( +0.25/Level ) HPRecoveryValue");
                MPRecoveryUPDescription.setText("Increase " + String.format("%.0f", skillsContainer.skills[3].totalValue) + " ( +0.25/Level )MPRecoveryValue");
                MoveSpeedUpDescription.setText("Increase " + skillsContainer.skills[4].totalValue + " ( +0.25/Level )MoveSpeed");
                SoulEaterDescription.setText("(Auto Learn Skill)\nCollect the soul from the dead enemy.\nRecover " + String.format("%.0f", skillsContainer.skills[5].totalValue) + " ( +1 / 4 Level) HP and MP when killing an enemy.");
                // endregion

                DialogBuilder.setView(DialogView);
                final AlertDialog alertDialog = DialogBuilder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();

                // region Setting of "Learn" Button
                BtnLearn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selectedSkill != null) {
                            if (skillsContainer.skillPoint > 0) {
                                boolean isMaxLv = false;
                                BulletEquipment equippedBullet = character.equippedBullet;
                                bulletEquipmentContainer.setEquippedBullet(character, BulletEquipment.BulletType.NULL);
                                character.equippedBullet = null;
//                                Log.e("EType", equippedBullet.bulletType.toString());
                                if (selectedSkill == HPUPIcon) {
                                    if (skillsContainer.skills[0].lv < MaxLv)
                                        skillsContainer.learnSkill(Skill.SkillType.HPUP);
                                    else isMaxLv = true;
                                } else if (selectedSkill == MPUPIcon) {
                                    if (skillsContainer.skills[1].lv < MaxLv)
                                        skillsContainer.learnSkill(Skill.SkillType.MPUP);
                                    else isMaxLv = true;
                                } else if (selectedSkill == HPRecoveryUPIcon) {
                                    if (skillsContainer.skills[2].lv < MaxLv)
                                        skillsContainer.learnSkill(Skill.SkillType.HPRECOVERYUP);
                                    else isMaxLv = true;
                                } else if (selectedSkill == MPRecoveryUPIcon) {
                                    if (skillsContainer.skills[3].lv < MaxLv)
                                        skillsContainer.learnSkill(Skill.SkillType.MPRECOVERYUP);
                                    else isMaxLv = true;
                                } else if (selectedSkill == MoveSpeedUPIcon) {
                                    if (skillsContainer.skills[4].lv < MaxLv)
                                        skillsContainer.learnSkill(Skill.SkillType.MOVESPEEDUP);
                                    else isMaxLv = true;
                                } else if (selectedSkill == SoulEaterUPIcon) {
                                    if (skillsContainer.skills[5].lv < MaxLv)
                                        skillsContainer.learnSkill(Skill.SkillType.SOULEATER);
                                    else isMaxLv = true;
                                }
                                character.calculateTotalValue(skillsContainer);
                                bulletEquipmentContainer.setEquippedBullet(character, equippedBullet.bulletType);
                                if (isMaxLv) {
                                    //region Create a AlertDialog for display the reminder when there are  not enough SkillPoint to learn skill
                                    final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(GameBackStageUI.this);
                                    final View DialogView = getLayoutInflater().inflate(R.layout.alertbox, null);
                                    final TextView DialogTitle = DialogView.findViewById(R.id.tv_alertTitle);
                                    final TextView DialogContext = DialogView.findViewById(R.id.tv_alertContext);
                                    final Button DialogBtnOk = DialogView.findViewById(R.id.btn_OK);
                                    final Button DialogBtnCancel = DialogView.findViewById(R.id.btn_Cancel);
                                    DialogBtnCancel.setVisibility(View.GONE);
                                    DialogContext.setGravity(Gravity.CENTER);
                                    DialogTitle.setText("Warning");
                                    DialogContext.setText("Skill have reached the maximum level");
                                    DialogBtnOk.setText("OK");
                                    DialogBuilder.setView(DialogView);
                                    final AlertDialog alertDialog = DialogBuilder.create();
                                    alertDialog.setCanceledOnTouchOutside(false);
                                    alertDialog.show();
                                    DialogBtnOk.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            alertDialog.dismiss();
                                        }
                                    });
                                    //endregion
                                } else {
                                    skillsContainer.skillPoint--;
                                    alertDialog.dismiss();
                                    btn_skill.performClick();
                                }
                            } else if (skillsContainer.skillPoint <= 0) {
                                //region Create a AlertDialog for display the reminder when there are  not enough SkillPoint to learn skill
                                final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(GameBackStageUI.this);
                                final View DialogView = getLayoutInflater().inflate(R.layout.alertbox, null);
                                final TextView DialogTitle = DialogView.findViewById(R.id.tv_alertTitle);
                                final TextView DialogContext = DialogView.findViewById(R.id.tv_alertContext);
                                final Button DialogBtnOk = DialogView.findViewById(R.id.btn_OK);
                                final Button DialogBtnCancel = DialogView.findViewById(R.id.btn_Cancel);
                                DialogBtnCancel.setVisibility(View.GONE);
                                DialogContext.setGravity(Gravity.CENTER);
                                DialogTitle.setText("Warning");
                                DialogContext.setText("Insufficient skill points");
                                DialogBtnOk.setText("OK");
                                DialogBuilder.setView(DialogView);
                                final AlertDialog alertDialog = DialogBuilder.create();
                                alertDialog.setCanceledOnTouchOutside(false);
                                alertDialog.show();
                                DialogBtnOk.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alertDialog.dismiss();
                                    }
                                });
                                //endregion
                            }
                        }
                    }
                });
                //endregion

                // region Setting of "Cancel" Button
                // For Close the equipment windows
                BtnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                // endregion

                // region Initialize the screen status.

                // Hide the button. these button will show when the player select the bullet by click the bullet icon.
                BtnLearn.setVisibility(View.INVISIBLE);
                // endregion
            }
        });
        //endregion

        //region Button Setting for logout
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLogin){
                    saveData.edit()
                            .putString("PlayerID",null)
                            .putString("PlayerPW",null)
                            .putBoolean("isLogin",false)
                            .commit();
                    btn_returnMenu.callOnClick();
                }
            }
        });
        //endregion

        //region Button Setting for Select the Battle Game Mode
        btn_EndlessBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_EndlessBack.setVisibility(View.INVISIBLE);
                btn_BossBack.setVisibility(View.INVISIBLE);
                battleMenu.setVisibility(View.VISIBLE);
                settingMenu.setVisibility(View.GONE);
                bossschallengeMenu.setVisibility(View.GONE);
                endlesschallengeMenu.setVisibility(View.GONE);
            }
        });
        btn_BossBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_EndlessBack.setVisibility(View.INVISIBLE);
                btn_BossBack.setVisibility(View.INVISIBLE);
                battleMenu.setVisibility(View.VISIBLE);
                settingMenu.setVisibility(View.GONE);
                bossschallengeMenu.setVisibility(View.GONE);
                endlesschallengeMenu.setVisibility(View.GONE);
            }
        });
        btn_BossChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_EndlessBack.setVisibility(View.VISIBLE);
                btn_BossBack.setVisibility(View.VISIBLE);
                battleMenu.setVisibility(View.GONE);
                settingMenu.setVisibility(View.GONE);
                bossschallengeMenu.setVisibility(View.VISIBLE);
                endlesschallengeMenu.setVisibility(View.GONE);
            }
        });
        btn_FireSlime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameMode = GameMode.BOSSCHALLENGE;
                bossChallengeType = StagesContainer.BossStageType.FireSlimeKing;
                LoadingStageTaskStart();
            }
        });
        btn_WaterSlime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameMode = GameMode.BOSSCHALLENGE;
                bossChallengeType = StagesContainer.BossStageType.WaterSlimeKing;
                LoadingStageTaskStart();
            }
        });
        btn_ForestSlime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameMode = GameMode.BOSSCHALLENGE;
                bossChallengeType = StagesContainer.BossStageType.ForestSlimeKing;
                LoadingStageTaskStart();
            }
        });
        btn_DarkSlime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameMode = GameMode.BOSSCHALLENGE;
                bossChallengeType = StagesContainer.BossStageType.DarkSlimeKing;
                LoadingStageTaskStart();
            }
        });
        btn_ODIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameMode = GameMode.BOSSCHALLENGE;
                bossChallengeType = StagesContainer.BossStageType.ODIN;
                LoadingStageTaskStart();
            }
        });
        btn_EndlessChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_EndlessBack.setVisibility(View.VISIBLE);
                btn_BossBack.setVisibility(View.VISIBLE);
                battleMenu.setVisibility(View.GONE);
                settingMenu.setVisibility(View.GONE);
                bossschallengeMenu.setVisibility(View.GONE);
                endlesschallengeMenu.setVisibility(View.VISIBLE);
            }
        });
        btn_Easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameMode = GameMode.ENDLESSCHALLENGE;
                endlessDifficulty = EndlessDifficulty.Easy;
                LoadingStageTaskStart();
            }
        });
        btn_Normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameMode = GameMode.ENDLESSCHALLENGE;
                endlessDifficulty = EndlessDifficulty.Normal;
                LoadingStageTaskStart();
            }
        });
        btn_Hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameMode = GameMode.ENDLESSCHALLENGE;
                endlessDifficulty = EndlessDifficulty.Hard;
                LoadingStageTaskStart();
            }
        });
        btn_Hell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameMode = GameMode.ENDLESSCHALLENGE;
                endlessDifficulty = EndlessDifficulty.Hell;
                LoadingStageTaskStart();
            }
        });
        //endregion


        // region Initialize Player Character Data and the UI for display the character status
        solveAbnormalStatus();
        Log.e("isLogin", "" + isLogin);
        // region check again the login status, if player is login to their account, system will check the local save data is or not exists.
        // If Player play in offline mode, the game will start without extra dialog. and load the data from local storage.
        if(!isLogin) {
                if (saveDataFile.exists()) {
                Log.e("SaveDataFile Status", "Exists");
                LoadGame(LoadGameMode.LoadFromLocal);
                resetBtnLayout.setVisibility(View.VISIBLE);
                LogoutLayout.setVisibility(View.GONE);
                btn_abnormalstatusreset.callOnClick();
            } else {
                Log.e("SaveDataFile Status", "Not Exists");
            }
            // If Player login, and the local save data is exists.
            // If the local save data is not exists, the game will start and load the data from server.
            // The System will ask for the connect of the save data from local storage or server.
        } else if (isLogin){
            // region Situation of local save data is not exists
            saveData.edit()
                    .putString("PlayerName", PlayerName)
                    .putString("PlayerID", PlayerID)
                    .putString("PlayerPW",PlayerPassword)
                    .putBoolean("isLogin",true)
                    .commit();
            resetBtnLayout.setVisibility(View.GONE);
            String OfflineName = saveData.getString("OfflinePlayerName",null);
            if (OfflineName == null) {
                if(loginConnection != null){
                    loginConnection = null;
                }
                Log.e("Offline Name", "" + OfflineName);
                String ID = PlayerID;
                String PW = PlayerPassword;
                try {
                    URL url = new URL("https://ds.dsgshk.com/login.php?"
                            + "ID=" + ID + "&PW=" + PW);
                    Log.e("Login URL", "" + url.toString());
                    if (loginConnection == null ||
                            loginConnection.getStatus().equals(AsyncTask.Status.FINISHED)) {
                        loginConnection = new HTTPConnection(HTTPConnection.ConnectionFor.Login, ID, PW, mContext, GS,false);
                        loginConnection.execute(url.toString());
                        btn_abnormalstatusreset.callOnClick();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            } // endregion
            else {
                // region Situation of local save data is exists
                final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(GameBackStageUI.this);
                final View DialogView = getLayoutInflater().inflate(R.layout.alertbox, null);
                final TextView DialogTitle = DialogView.findViewById(R.id.tv_alertTitle);
                final TextView DialogContext = DialogView.findViewById(R.id.tv_alertContext);
                final Button DialogBtnLoadFromLocal = DialogView.findViewById(R.id.btn_OK);
                final Button DialogBtnLoadFromServer = DialogView.findViewById(R.id.btn_Cancel);
                DialogBtnLoadFromServer.setVisibility(View.VISIBLE);
                DialogContext.setGravity(Gravity.CENTER);
                DialogTitle.setText("Warning");
                DialogContext.setText("System found the game save data file in your local storage.\nAre You want to load game from the data which stored in local storage first and save to server\n(!!It will cover your game data on server!!)\nOR\nLoad from server directly?");
                DialogBtnLoadFromLocal.setText("Load from local storage\nAND\nSave To Server");
                DialogBtnLoadFromServer.setText("Load from Server");
                DialogBuilder.setView(DialogView);
                final AlertDialog alertDialogAskForConnect = DialogBuilder.create();

                // Setting of Load The Game Data From Local Storage first and update the offline data to server.
                DialogBtnLoadFromLocal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(GameBackStageUI.this);
                        final View DialogView = getLayoutInflater().inflate(R.layout.alertbox, null);
                        final TextView DialogTitle = DialogView.findViewById(R.id.tv_alertTitle);
                        final TextView DialogContext = DialogView.findViewById(R.id.tv_alertContext);
                        final Button DialogBtnConfirmLoadFromLocal = DialogView.findViewById(R.id.btn_OK);
                        final Button DialogBtnCancel = DialogView.findViewById(R.id.btn_Cancel);
                        DialogBtnConfirmLoadFromLocal.setVisibility(View.VISIBLE);
                        DialogContext.setGravity(Gravity.CENTER);
                        DialogTitle.setText("Warning");
                        DialogContext.setText("Are you sure load save data from local storage?\nAfter loaded from local storage, the data will upload to server and replace your current data.\nYou can't recover the data after that. ");
                        DialogBtnConfirmLoadFromLocal.setText("Sure");
                        DialogBtnCancel.setText("Cancel");
                        DialogBuilder.setView(DialogView);

                        final AlertDialog alertDialogLoadFromLocal = DialogBuilder.create();
                        alertDialogLoadFromLocal.setCanceledOnTouchOutside(false);
                        alertDialogLoadFromLocal.show();
                        alertDialogAskForConnect.dismiss();
                        DialogBtnConfirmLoadFromLocal.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                LoadGame(LoadGameMode.LoadFromLocal);
                                String ID = PlayerID;
                                String PW = PlayerPassword;
                                SaveGame(SaveGameMode.SaveTOServer,false);
                                alertDialogLoadFromLocal.dismiss();
                                btn_abnormalstatusreset.callOnClick();
                            }
                        });

                        DialogBtnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialogLoadFromLocal.dismiss();
                                alertDialogAskForConnect.show();
                            }
                        });

                    }
                });

                DialogBtnLoadFromServer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ID = PlayerID;
                        String PW = PlayerPassword;
                        try {
                            URL url = new URL("https://ds.dsgshk.com/login.php?"
                                    + "ID=" + ID + "&PW=" + PW);

                            if (loginConnection == null ||
                                    loginConnection.getStatus().equals(AsyncTask.Status.FINISHED)) {
                                loginConnection = new HTTPConnection(HTTPConnection.ConnectionFor.Login, ID, PW, mContext, GS,false);
                                loginConnection.execute(url.toString());
                                alertDialogAskForConnect.dismiss();
                                btn_abnormalstatusreset.callOnClick();
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                });

                alertDialogAskForConnect.setCanceledOnTouchOutside(false);
                alertDialogAskForConnect.show();
                // endregion
            }
        }
        // endregion
    }


    Runnable doBgRunnable = new Runnable() {
        @Override
        public void run() {
            HideStageContainer();
        }
    };
    Runnable onProgress = new Runnable() {
        @Override
        public void run() {

        }
    };
    Runnable doOnPostTask = new Runnable() {
        @Override
        public void run() {
            LoadingStage();
        }
    };

    // Start The Loading Task of enter to stage
    private void LoadingStageTaskStart() {
        if (loadingTask == null || loadingTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
            character.isDead = false;
            expTotalReward = 0;
            moneyTotalReward = 0;
            uibottomContainer.setVisibility(View.INVISIBLE);
            MIDmenuUIContainer.setVisibility(View.GONE);
            loadingTask = new LoadingTask(1.5, loadingBar, loadingpercentage, doBgRunnable, onProgress, doOnPostTask);
            loadingTask.execute();
        }
    }

    // Start The Stage Scene and hide the main menu ui.
    private void HideStageContainer() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                battleMenu.setVisibility(View.GONE);
                gameMainScreenContainer.setVisibility(View.INVISIBLE);
            }
        });
    }

    // Create and Start to loading the stage scene. Also Initialize the UI of the stage.
    private void LoadingStage() {
        Log.e("Game Panel", "Creating...");
        inStage = true;
        moneyTotalRewardText.setText("" + moneyTotalReward);
        expTotalRewardText.setText("" + expTotalReward);
        stageUI.setVisibility(View.VISIBLE);
        charStatusUI.setVisibility(View.VISIBLE);
        menuTopUI.setVisibility(View.GONE);
        mediaPlayer.stop();
        mediaPlayer.release();
        GP = new GamePanel(mContext);
        GP.GameScreen = GS;
        gameMainScreenContainer.addView(GP);
        character.adjustValueInt(Character.AdjustType.CurrentHP, Character.CalculationMethod.Equals, character.getMaxHp());
        character.adjustValueInt(Character.AdjustType.CurrentMP, Character.CalculationMethod.Equals, character.getMaxMp());
        sp = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        soundIds[0] = sp.load(this, R.raw.fireballhitse, 1); // Hit Sound Effect
        soundIds[1] = sp.load(this, R.raw.fireballshootse, 1); // Shoot Sound Effect
        Log.e("Game Panel", "Created!");
    }

    // A method for update the character status which showed on the game UI. (include Enemy and Player's Character.
    public void updateCharacterStatus(int BeforeValue, UpdateStatusType updateStatusType, Character character1) {
        int mBeforeValue = BeforeValue;
        ProgressBarAnimation anim;
        switch (updateStatusType) {
            case CurrentHP:
                anim = new ProgressBarAnimation(hpBar, mBeforeValue, Math.round((float) character1.getCurrentHp() / (float) character1.getMaxHp() * 100));
                anim.setDuration(500);
                hpBar.startAnimation(anim);
                hpText.setText(character1.getCurrentHp() + "/ " + character1.getMaxHp());
                break;
            case CurrentMP:
                anim = new ProgressBarAnimation(mpBar, mBeforeValue, Math.round((float) character1.getCurrentMp() / (float) character1.getMaxMp() * 100));
                anim.setDuration(500);
                mpBar.startAnimation(anim);
                mpText.setText(character1.getCurrentMp() + "/ " + character1.getMaxMp());
                break;
            case Exp:
                character1.setExpRequirement(100, 0.2);
                Log.e("Exp BeforeValue", "" +BeforeValue);
                Log.e("Exp", "" + character1.getExp());
                lvText.setText("" + character1.getLv());
                anim = new ProgressBarAnimation(expBar, mBeforeValue, Math.round((float) character1.getExp() / (float) character1.getExpRequirement() * 100));
                anim.setDuration(500);
                expBar.startAnimation(anim);
                expText.setText(character1.getExp() + "/" + character1.getExpRequirement());
                break;
            case EnemyHP:
                anim = new ProgressBarAnimation(enemyHpBar, mBeforeValue, Math.round((float) character1.getCurrentHp() / (float) character1.getMaxHp() * 100));
                anim.setDuration(100);
                enemyHpBar.startAnimation(anim);
                enemyName.setText(character1.getName());
                enemyHpText.setText(character1.getCurrentHp() + "/ " + character1.getMaxHp());
                if (character1.getPoisonTask() != null) {
                    isPoisiong.setVisibility(View.VISIBLE);
                } else isPoisiong.setVisibility(View.INVISIBLE);
                if (character1.isUnDead) {
                    isUndeadIcon.setVisibility(View.VISIBLE);
                } else isUndeadIcon.setVisibility(View.INVISIBLE);
                if (character1.isBoss)
                    isBossIcon.setVisibility(View.VISIBLE);
                else isBossIcon.setVisibility(View.INVISIBLE);
                break;
        }
    }

    // Disable the back button function.
    @Override
    public void onBackPressed() {

    }

    // A method for add the stage reward and show the total reward in each game.
    public void addStageReward(int Value, StageValueType stageValueType) {
        switch (stageValueType) {
            case EXP:
                expTotalReward += Value;
                expTotalRewardText.setText("" + expTotalReward);
                break;
            case MONEY:
                moneyTotalReward += Value;
                moneyTotalRewardText.setText("" + moneyTotalReward);
                break;
        }
    }

    // A method for update the value of  material of game on UI.
    public void updateUIStatus(StageValueType stageValueType) {
        switch (stageValueType) {
            case MONEY:
                money += moneyTotalReward;
                moneyText.setText("" + money);
                break;
        }
    }

    public void showStageReward(int Stage) {
        final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(GameBackStageUI.this);
        final View DialogView = getLayoutInflater().inflate(R.layout.stagerewardscreen, null);

        final TextView DialogTitle = DialogView.findViewById(R.id.tv_rewardTitle);
        final TextView DialogContext = DialogView.findViewById(R.id.tv_rewardContext);
        final TextView MoneyReward = DialogView.findViewById(R.id.tv_rewardMoney);
        final TextView EXPReward = DialogView.findViewById(R.id.tv_rewardEXP);
        final LinearLayout redStoneRewardLayout = DialogView.findViewById(R.id.rewardRedStone);
        final LinearLayout blueStoneRewardLayout = DialogView.findViewById(R.id.rewardBlueStone);
        final LinearLayout greenStoneRewardLayout = DialogView.findViewById(R.id.rewardGreenStone);
        final LinearLayout darkStoneRewardLayout = DialogView.findViewById(R.id.rewardDarkStone);
        final TextView redStoneRewardText = DialogView.findViewById(R.id.tv_rewardRedStone);
        final TextView blueStoneRewardText = DialogView.findViewById(R.id.tv_rewardBlueStone);
        final TextView greenStoneRewardText = DialogView.findViewById(R.id.tv_rewardGreenStone);
        final TextView darkStoneRewardText = DialogView.findViewById(R.id.tv_rewardDarkStone);
        redStoneRewardLayout.setVisibility(View.GONE);
        blueStoneRewardLayout.setVisibility(View.GONE);
        greenStoneRewardLayout.setVisibility(View.GONE);
        darkStoneRewardLayout.setVisibility(View.GONE);

        DialogTitle.setGravity(Gravity.CENTER);
        DialogContext.setGravity(Gravity.CENTER);
        DialogTitle.setText("Stage Reward");
        if (gameMode == GameMode.BOSSCHALLENGE) {
            if (character.isDead) {
            DialogContext.setText(CurrentStageText.getText() + "\n Game Over!");
                switch (bossChallengeType) {
                    case FireSlimeKing:
                        break;
                    case WaterSlimeKing:
                        break;
                    case ForestSlimeKing:
                        break;
                    case DarkSlimeKing:
                        break;
                    case ODIN:
                        break;
                }
            } else {
                DialogContext.setText(CurrentStageText.getText() + "\n Clear!");
                Random random = new Random();
                int randomValue;
                switch (bossChallengeType) {
                    case FireSlimeKing:
                        randomValue = 1 + random.nextInt(1 + (bulletEquipmentContainer.bulletEquipments[0].lv / 10));
                        redStoneRewardLayout.setVisibility(View.VISIBLE);
                        redStoneRewardText.setText("\t+ " + randomValue);
                        redStone += randomValue;
                        break;
                    case WaterSlimeKing:
                        randomValue = 1 + random.nextInt(1 + (bulletEquipmentContainer.bulletEquipments[1].lv / 10));
                        blueStoneRewardLayout.setVisibility(View.VISIBLE);
                        blueStoneRewardText.setText("\t+ " + randomValue);
                        blueStone += randomValue;
                        break;
                    case ForestSlimeKing:
                        randomValue = 1 + random.nextInt(1 + (bulletEquipmentContainer.bulletEquipments[2].lv / 10));
                        greenStoneRewardLayout.setVisibility(View.VISIBLE);
                        greenStoneRewardText.setText("\t+ " + randomValue);
                        greenStone += randomValue;
                        break;
                    case DarkSlimeKing:
                        randomValue = 1 + random.nextInt(1 + (bulletEquipmentContainer.bulletEquipments[3].lv / 10));
                        darkStoneRewardLayout.setVisibility(View.VISIBLE);
                        darkStoneRewardText.setText("\t+ " + randomValue);
                        darkStone += randomValue;
                        break;
                    case ODIN:
                        randomValue =  50 * character.getLv() + random.nextInt(character.getLv() * 100);
                        expTotalReward += randomValue;
                        money += randomValue;
                        break;
                }
            }
        } else {
            if (character.isDead) {
                switch (endlessDifficulty){
                    case Easy:
                        DialogContext.setText("Easy\nStage " + (Stage) + "\n Game Over!");
                        break;
                    case Normal:
                        DialogContext.setText("Normal\nStage " + (Stage) + "\n Game Over!");
                        break;
                    case Hard:
                        DialogContext.setText("Hard\nStage " + (Stage) + "\n Game Over!");
                        break;
                    case Hell:
                        DialogContext.setText("Hell\nStage " + (Stage) + "\n Game Over!");
                        break;
                }
            } else
                switch (endlessDifficulty){
                    case Easy:
                        DialogContext.setText("Easy\nStage " + (Stage) + "\n Clear!");
                        break;
                    case Normal:
                        DialogContext.setText("Normal\nStage " + (Stage) + "\n Clear!");
                        break;
                    case Hard:
                        DialogContext.setText("Hard\nStage " + (Stage) + "\n Clear!");
                        break;
                    case Hell:
                        DialogContext.setText("Hell\nStage " + (Stage) + "\n Clear!");
                        break;
                }
        }



        StageButtonLayout.setVisibility(View.INVISIBLE);
        MoneyReward.setText("+ " + moneyTotalReward);
        EXPReward.setText("+ " + expTotalReward);
        character.addExp(expTotalReward);
        updateUIStatus(StageValueType.MONEY);

        DialogBuilder.setView(DialogView);
        final AlertDialog alertDialog = DialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
        character.isDead = false;
        expTotalReward = 0;
        moneyTotalReward = 0;
        MIDmenuUIContainer.setVisibility(View.VISIBLE);
        btn_save.callOnClick(); // save automatically when stage end.
    }

    public void SaveGame(SaveGameMode saveGameMode, boolean showAlertDialog) {
        int fireballLv = bulletEquipmentContainer.bulletEquipments[0].lv;
        int bluefireLv = bulletEquipmentContainer.bulletEquipments[1].lv;
        int ForestLv = bulletEquipmentContainer.bulletEquipments[2].lv;
        int DarkLv = bulletEquipmentContainer.bulletEquipments[3].lv;
        int hpupLv = skillsContainer.skills[0].lv;
        int mpupLv = skillsContainer.skills[1].lv;
        int hprecoveryLv = skillsContainer.skills[2].lv;
        int mprecoveryLv = skillsContainer.skills[3].lv;
        int movespeedLv = skillsContainer.skills[4].lv;
        int souleaterLv = skillsContainer.skills[5].lv;
        int charLv = character.getLv();
        int charExp = character.getExp();
        int skillPoint = skillsContainer.skillPoint;
        int equippedbulletIndex;
        if(character.getEquippedBullet() != null){
            equippedbulletIndex = BulletEquipment.BulletType.toInt(character.getEquippedBullet().getBulletType());
        } else {
            equippedbulletIndex = BulletEquipment.BulletType.toInt(BulletEquipment.BulletType.NULL);
        }


        switch (saveGameMode) {
            case SaveTOLocal:
                saveData.edit()
                        .putInt("CharLevel", charLv)
                        .putInt("CharExp", charExp)
                        .putInt("Money", money)
                        .putInt("FireBallLevel", fireballLv)
                        .putInt("BlueFireLevel", bluefireLv)
                        .putInt("ForestLevel", ForestLv)
                        .putInt("DarkElementLevel", DarkLv)
                        .putInt("HPUPLevel", hpupLv)
                        .putInt("MPUPLevel", mpupLv)
                        .putInt("HPRecoveryUPLevel", hprecoveryLv)
                        .putInt("MPRecoveryUPLevel", mprecoveryLv)
                        .putInt("MoveSpeedUPLevel", movespeedLv)
                        .putInt("SoulEaterUPLevel", souleaterLv)
                        .putInt("EquippedBullet", equippedbulletIndex)
                        .putInt("skillpoint", skillPoint)
                        .putBoolean("LoginStatus", isLogin)
                        .putInt("redStone", redStone)
                        .putInt("blueStone", blueStone)
                        .putInt("greenStone", greenStone)
                        .putInt("darkStone", darkStone)
                        .commit();
                break;

            case SaveTOServer:
                String ID = PlayerID;
                String PW = PlayerPassword;
                    try {
                    URL url = new URL("https://ds.dsgshk.com/savegame.php?"
                            + "ID=" + ID + "&PW=" + PW + "&fireballLv=" + fireballLv + "&bluefireLv=" + bluefireLv +
                            "&forestLv=" + ForestLv + "&darkelementLv=" + DarkLv + "&hpupLv=" + hpupLv + "&mpupLv=" + mpupLv +
                            "&hprecoveryupLv=" + hprecoveryLv + "&mprecoveryupLv=" + mprecoveryLv + "&movespeedupLv=" + movespeedLv +
                            "&souleaterupLv=" + movespeedLv + "&charLv=" + charLv + "&charExp=" + charExp + "&money=" + money +
                            "&skillpoint=" + skillPoint + "&EquippedBullet=" + equippedbulletIndex + "&redstone=" + redStone+
                            "&bluestone=" + blueStone + "&greenstone=" + greenStone + "&darkstone=" + darkStone);
                    Log.e("SaveGame URL: " , "" + url.toString());
                    if (loginConnection == null ||
                            loginConnection.getStatus().equals(AsyncTask.Status.FINISHED)) {
                        if(showAlertDialog) {
                            loginConnection = new HTTPConnection(HTTPConnection.ConnectionFor.SaveGame, ID, PW, mContext, GS, true);
                        } else {
                            loginConnection = new HTTPConnection(HTTPConnection.ConnectionFor.SaveGame, ID, PW, mContext, GS, false);
                        }
                        loginConnection.execute(url.toString());
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                break;
        }
//        Log.e("EquippedBullet ToString", "" + BulletEquipment.BulletType.toInt(character.getEquippedBullet().getBulletType()));

    }

    public void LoadGame(LoadGameMode loadGameMode) {
        boolean canLoadData = false;
        String[] segment;
        segment = ServerDataReply.split(",");
        character.equippedBullet = null;
        switch (loadGameMode) {
            case LoadFromLocal:
                bulletEquipmentContainer.bulletEquipments[0].lv = saveData.getInt("FireBallLevel", 1);
                bulletEquipmentContainer.bulletEquipments[1].lv = saveData.getInt("BlueFireLevel", 1);
                bulletEquipmentContainer.bulletEquipments[2].lv = saveData.getInt("ForestLevel", 1);
                bulletEquipmentContainer.bulletEquipments[3].lv = saveData.getInt("DarkElementLevel", 1);
                skillsContainer.skills[0].lv = saveData.getInt("HPUPLevel", 0);
                skillsContainer.skills[1].lv = saveData.getInt("MPUPLevel", 0);
                skillsContainer.skills[2].lv = saveData.getInt("HPRecoveryUPLevel", 0);
                skillsContainer.skills[3].lv = saveData.getInt("MPRecoveryUPLevel", 0);
                skillsContainer.skills[4].lv = saveData.getInt("MoveSpeedUPLevel", 0);
                //skillsContainer.skills[5].lv = saveData.getInt("SoulEaterUPLevel", 0);
                skillsContainer.skills[5].lv = character.getLv();
                character.setLv(saveData.getInt("CharLevel", 1));
                updateCharacterStatus(0,UpdateStatusType.Exp,character);
                character.addExp(saveData.getInt("CharExp", 0));
                money = saveData.getInt("Money", 0);
                skillsContainer.skillPoint = saveData.getInt("skillpoint", 0);
                redStone = saveData.getInt("redStone", 0);
                blueStone = saveData.getInt("blueStone", 0);
                greenStone = saveData.getInt("greenStone", 0);
                darkStone = saveData.getInt("darkStone", 0);
                canLoadData = true;
                break;

            case LoadFromServer:
                Log.e("Server Reply: ", "" + ServerDataReply);
                for (int i = 0; i < segment.length; i++) {
                    if(segment[i] == ""){
                        Log.e("Fail: ", "Fail To Load Game From Data.\n Error on Segment " + i + "\nPlease Contact Admin To Edit your data again");
                        final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(GameBackStageUI.this);
                        final View DialogView = getLayoutInflater().inflate(R.layout.alertbox, null);
                        final TextView DialogTitle = DialogView.findViewById(R.id.tv_alertTitle);
                        final TextView DialogContext = DialogView.findViewById(R.id.tv_alertContext);
                        final Button DialogBtnOk = DialogView.findViewById(R.id.btn_OK);
                        final Button DialogBtnCancel = DialogView.findViewById(R.id.btn_Cancel);
                        DialogBtnCancel.setVisibility(View.GONE);
                        DialogContext.setGravity(Gravity.CENTER);
                        DialogTitle.setText("Error:");
                        DialogContext.setText("Fail To Load Game From Data.\n Error on Segment " + i + "\nPlease Contact Admin To Edit your data again\nGame will restart.");
                        DialogBtnOk.setText("OK");
                        DialogBuilder.setView(DialogView);
                        final AlertDialog alertDialog = DialogBuilder.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();
                        DialogBtnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                                isPause = false;
                                Intent i = getBaseContext().getPackageManager().
                                        getLaunchIntentForPackage(getBaseContext().getPackageName());
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                            }
                        });
                        return;
                    }
                    Log.e("Segment ", "" + segment[i]);
                }
                PlayerName = segment[1];
                tv_playername.setText(PlayerName);
                bulletEquipmentContainer.bulletEquipments[0].lv = Integer.parseInt(segment[2]);
                bulletEquipmentContainer.bulletEquipments[1].lv = Integer.parseInt(segment[3]);
                bulletEquipmentContainer.bulletEquipments[2].lv = Integer.parseInt(segment[4]);
                bulletEquipmentContainer.bulletEquipments[3].lv = Integer.parseInt(segment[5]);
                skillsContainer.skills[0].lv = Integer.parseInt(segment[6]);
                skillsContainer.skills[1].lv = Integer.parseInt(segment[7]);
                skillsContainer.skills[2].lv = Integer.parseInt(segment[8]);
                skillsContainer.skills[3].lv = Integer.parseInt(segment[9]);
                skillsContainer.skills[4].lv = Integer.parseInt(segment[10]);
                //skillsContainer.skills[5].lv = Integer.parseInt(segment[11]);
                //character.setLv(Integer.parseInt(segment[12]));
                money = Integer.parseInt(segment[14]);
                skillsContainer.skillPoint = Integer.parseInt(segment[15]);
                redStone = Integer.parseInt(segment[17]);
                blueStone = Integer.parseInt(segment[18]);
                greenStone = Integer.parseInt(segment[19]);
                darkStone = Integer.parseInt(segment[20]);
                canLoadData = true;
                Log.e("LV","Character LV: " + character.getLv());
                character.setLv(Integer.parseInt(segment[12]));
                updateCharacterStatus(0,UpdateStatusType.Exp,character);
                character.addExp(Integer.parseInt(segment[13]));
                skillsContainer.skills[5].lv = character.getLv();
                Log.e("LV","Character LV: " + character.getLv());
                Log.e("LV From SERVER","" + Integer.parseInt(segment[12]));
                break;
        }
        if (canLoadData) {
            updateUIStatus(StageValueType.MONEY);
            for (int i = 0; i < skillsContainer.skills.length; i++) {
                skillsContainer.skills[i].calculateTotalValue(skillsContainer.skills[i].skillType);
            }
            character.calculateTotalValue(skillsContainer);
            switch (loadGameMode) {
                case LoadFromLocal:
                    bulletEquipmentContainer.setEquippedBullet(character, BulletEquipment.BulletType.values()[saveData.getInt("EquippedBullet", 0)]);
                    break;
                case LoadFromServer:
                    bulletEquipmentContainer.setEquippedBullet(character, BulletEquipment.BulletType.values()[Integer.parseInt(segment[16])]);
                    break;
            }
            hpBar.setProgress((character.getCurrentHp() / character.getMaxHp()) * 100);
            mpBar.setProgress((character.getCurrentMp() / character.getMaxMp()) * 100);
            hpText.setText(character.getCurrentHp() + "/ " + character.getMaxHp());
            mpText.setText(character.getCurrentMp() + "/ " + character.getMaxMp());
            character.setExpRequirement(100, 0.2);
            expBar.setProgress((character.getExp() / character.getExpRequirement()) * 100);
            expText.setText(character.getExp() + "/" + character.getExpRequirement());
            lvText.setText("" + character.getLv());
        }
    }

    public void solveAbnormalStatus(){
        GameBackStageUI gameBackStageUI = this;
        character = new Character(
                BitmapFactory.decodeResource(getResources(),
                        R.drawable.character_scaled), 4, 4, 10, 0, 0,
                1, 150, 75, 5, 10, 5, 2,
                150, 15, 10, 5000, 1000);
        for (int i = 0; i < bulletEquipmentContainer.bulletEquipments.length; i++) {
            bulletEquipmentContainer.bulletEquipments[i].lv = 1;
        }
        for (int i = 0; i < skillsContainer.skills.length; i++) {
            skillsContainer.skills[i].lv = 1;
        }
        //bulletEquipmentContainer.setEquippedBullet(character, BulletEquipment.BulletType.FIREBALL);
        character.gameScreen = this;
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

}
