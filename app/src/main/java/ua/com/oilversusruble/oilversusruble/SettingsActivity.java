package ua.com.oilversusruble.oilversusruble;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;


public class SettingsActivity extends ActionBarActivity implements View.OnClickListener {

    FrameLayout containerForAds;
    CheckBox cbRubEnabled, cbRubUsdTracking, cbRubEurTracking, cbBrentTracking, cbWtiTracking, cbUahEnabled, cbUahUsdTracking, cbUahEurTracking, cbFullScreen, cbEnablePutin;
    Button btnSave;
    SharedPreferences sharedPreferences;

    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        if(sharedPreferences.getBoolean("fullScreen", false)){
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else{
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }
        setContentView(R.layout.activity_settings);



        containerForAds = (FrameLayout) findViewById(R.id.containerForAds);
        cbRubEnabled = (CheckBox) findViewById(R.id.cbRubEnabled);
        cbRubUsdTracking = (CheckBox) findViewById(R.id.cbRubUsdTracking);
        cbRubEurTracking = (CheckBox) findViewById(R.id.cbRubEurTracking);
        cbBrentTracking = (CheckBox) findViewById(R.id.cbBrentTracking);
        cbWtiTracking = (CheckBox) findViewById(R.id.cbWtiTracking);
        cbUahEnabled = (CheckBox) findViewById(R.id.cbUahEnabled);
        cbUahUsdTracking = (CheckBox) findViewById(R.id.cbUahUsdTracking);
        cbUahEurTracking = (CheckBox) findViewById(R.id.cbUahEurTracking);
        cbFullScreen = (CheckBox) findViewById(R.id.cbFullScreen);
        cbEnablePutin = (CheckBox) findViewById(R.id.cbEnablePutin);

        cbRubEnabled.setChecked(sharedPreferences.getBoolean("rubEnabled", true));
        cbRubUsdTracking.setChecked(sharedPreferences.getBoolean("rubUSDTracking", true));
        cbRubEurTracking.setChecked(sharedPreferences.getBoolean("rubEURTracking", true));
        cbBrentTracking.setChecked(sharedPreferences.getBoolean("brentEnabled", true));
        cbWtiTracking.setChecked(sharedPreferences.getBoolean("wtiEnabled", true));
        cbUahEnabled.setChecked(sharedPreferences.getBoolean("uahEnabled", true));
        cbUahUsdTracking.setChecked(sharedPreferences.getBoolean("uahUSDTracking", true));
        cbUahEurTracking.setChecked(sharedPreferences.getBoolean("uahEURTracking", true));
        cbFullScreen.setChecked(sharedPreferences.getBoolean("fullScreen", true));
        cbEnablePutin.setChecked(sharedPreferences.getBoolean("isPutinEnabled", true));

        btnSave = (Button) findViewById(R.id.btnSave);

        btnSave.setOnClickListener(this);







        adView = new AdView(this);
        adView.setAdUnitId("ca-app-pub-7672991449155931/7801792008");
        adView.setAdSize(AdSize.BANNER);
        containerForAds.addView(adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnSave:
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putBoolean("rubEnabled", cbRubEnabled.isChecked());
                editor.putBoolean("rubUSDTracking", cbRubUsdTracking.isChecked());
                editor.putBoolean("rubEURTracking", cbRubEurTracking.isChecked());
                editor.putBoolean("brentEnabled", cbBrentTracking.isChecked());
                editor.putBoolean("wtiEnabled", cbWtiTracking.isChecked());
                editor.putBoolean("uahEnabled", cbUahEnabled.isChecked());
                editor.putBoolean("uahUSDTracking", cbUahUsdTracking.isChecked());
                editor.putBoolean("uahEURTracking", cbUahEurTracking.isChecked());
                editor.putBoolean("fullScreen", cbFullScreen.isChecked());
                editor.putBoolean("isPutinEnabled", cbEnablePutin.isChecked());

                editor.apply();
                finish();
            break;
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        Tracker t = GoogleAnalytics.getInstance(this).newTracker("UA-55928846-2");
        t.setScreenName("SettingsActivity");
        t.enableAdvertisingIdCollection(true);
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);

    }

    @Override
    public void onPause() {
        adView.pause();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        adView.resume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adView.destroy();

    }





}
