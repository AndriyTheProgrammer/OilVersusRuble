package ua.com.oilversusruble.oilversusruble;


import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.Calendar;
import java.util.Random;
import java.util.Stack;


public class MyActivity extends ActionBarActivity implements View.OnClickListener{





    FrameLayout googleAdsContainer, rubLoadContainer, oilLoadContainer, uahLoadContainer;
    LinearLayout rubLayout, oilLayout, uahLayout;
    ProgressBar pbRub, pbOil, pbUah;
    Button btnUpdateRub, btnUpdateOil, btnUpdateUah, btnSettings, btnOilHistory, btnRubHistory, btnUahHistory;
    TextView tvRubForUsd, tvRubForEur, tvBrentOilPrice, tvWtiOilPrice, tvUahForUsd, tvUahForEur;
    ImageView imageRubUsdArrow, imageRubEurArrow, imageBrentOilArrow, imageWtiOilArrow, imageUahUsdArrow, imageUahEurArrow, rubIndicator, oilIndicator, uahIndicator, imageRUB, imageOil, imageUAH;
    InfoDownloader infoDownloader = new InfoDownloader();
    RubUpdateTask rubUpdateTask;
    OilUpdateTask oilUpdateTask;
    UahUpdateTask uahUpdateTask;
    Random random = new Random();
    LayoutInflater inflater;
    int j = 1;
    SharedPreferences sharedPreferences, settingsFile;
    Calendar c = Calendar.getInstance();







    boolean rubUpdateUSDComplete = false, rubUpdateEURComplete = false, oilBrentUpdateComplete = false, oilWtiUpdateComplete = false, uahUpdateUSDComplete = false, uahUpdateEURComplete = false;
    boolean rubUsdTracking = true, rubEurTracking = true, uahUsdTracking = true, uahEurTracking = true;
    boolean isPutinModeEnabled = true;
    int sad1, sad2, sad3, happy1, happy2, happy3, pSad1, pSad2, pHappy1, pHappy2;


    private Stack<String> brentOilLastPrices = new Stack<String>();
    private Stack<String> wtiOilLastPrices = new Stack<String>();
    private Stack<String> rubUsdLastPrices = new Stack<String>();
    private Stack<String> rubEurLastPrices = new Stack<String>();
    private Stack<String> uahUsdLastPrices = new Stack<String>();
    private Stack<String> uahEurLastPrices = new Stack<String>();

    private AdView adView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        settingsFile = getSharedPreferences("settings", MODE_PRIVATE);
        if(settingsFile.getBoolean("fullScreen", false)){
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        }
        else{
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }

        setContentView(R.layout.activity_my);

        sad1 = R.drawable.sad1;
        sad2 = R.drawable.sad2;
        sad3 = R.drawable.sad3;
        happy1 = R.drawable.happy1;
        happy2 = R.drawable.happy2;
        happy3 = R.drawable.happy3;

        pSad1 = R.drawable.poroshenko_sad1;
        pSad2 = R.drawable.poroshenko_sad2;
        pHappy1 = R.drawable.poroshenko_happy1;
        pHappy2 = R.drawable.poroshenko_happy2;

        googleAdsContainer = (FrameLayout) findViewById(R.id.googleAdsContainer);
        rubLoadContainer = (FrameLayout) findViewById(R.id.rubLoadContainer);
        oilLoadContainer = (FrameLayout) findViewById(R.id.oilLoadContainer);
        uahLoadContainer = (FrameLayout) findViewById(R.id.uahLoadContainer);
        rubLayout = (LinearLayout) findViewById(R.id.rubLayout1);
        oilLayout = (LinearLayout) findViewById(R.id.oilLayout1);
        uahLayout = (LinearLayout) findViewById(R.id.uahLayout1);
        pbRub = (ProgressBar) findViewById(R.id.pbRub);
        pbOil = (ProgressBar) findViewById(R.id.pbOil);
        pbUah = (ProgressBar) findViewById(R.id.pbUah);
        tvRubForUsd = (TextView) findViewById(R.id.tvRubForUsd);
        tvRubForEur = (TextView) findViewById(R.id.tvRubForEur);
        tvBrentOilPrice = (TextView) findViewById(R.id.tvBrentOilPrice);
        tvWtiOilPrice = (TextView) findViewById(R.id.tvWtiOilPrice);
        tvUahForUsd = (TextView) findViewById(R.id.tvUahForUsd);
        tvUahForEur = (TextView) findViewById(R.id.tvUahForEur);
        btnUpdateRub = (Button) findViewById(R.id.btnUpdateRub);
        btnUpdateOil = (Button) findViewById(R.id.btnUpdateOil);
        btnUpdateUah = (Button) findViewById(R.id.btnUpdateUah);
        btnSettings = (Button) findViewById(R.id.btnSettings);
        btnRubHistory = (Button) findViewById(R.id.btnRUBHistory);
        btnOilHistory = (Button) findViewById(R.id.btnOilHistory);
        btnUahHistory = (Button) findViewById(R.id.btnUahHistory);
        imageRubUsdArrow = (ImageView) findViewById(R.id.imageRubUsdArrow);
        imageRubEurArrow = (ImageView) findViewById(R.id.imageRubEurArrow);
        imageBrentOilArrow = (ImageView) findViewById(R.id.imageBrentOilArrow);
        imageWtiOilArrow = (ImageView) findViewById(R.id.imageWtiOilArrow);
        imageUahUsdArrow = (ImageView) findViewById(R.id.imageUahUsdArrow);
        imageUahEurArrow = (ImageView) findViewById(R.id.imageUahEurArrow);
        rubIndicator = (ImageView) findViewById(R.id.rubIndicator);
        oilIndicator = (ImageView) findViewById(R.id.oilIndicator);
        uahIndicator = (ImageView) findViewById(R.id.uahIndicator);
        imageRUB = (ImageView) findViewById(R.id.imageRUB);
        imageOil = (ImageView) findViewById(R.id.imageOil);
        imageUAH = (ImageView) findViewById(R.id.imageUAH);

        checkSettings();

        adView = new AdView(this);
        adView.setAdUnitId("ca-app-pub-7672991449155931/2422373206");
        adView.setAdSize(AdSize.BANNER);
        googleAdsContainer.addView(adView);
        AdRequest adRequest = new AdRequest.Builder().build();



        // Загрузка adView с объявлением.
        adView.loadAd(adRequest);

        btnUpdateRub.setOnClickListener(this);
        btnUpdateOil.setOnClickListener(this);
        btnUpdateUah.setOnClickListener(this);
        btnSettings.setOnClickListener(this);
        btnRubHistory.setOnClickListener(this);
        btnOilHistory.setOnClickListener(this);
        btnUahHistory.setOnClickListener(this);


        inflater = getLayoutInflater();
        loadFromSave();

        Tracker t = GoogleAnalytics.getInstance(this).newTracker("UA-55928846-3");
        t.setScreenName("MyActivity");
        t.enableAdvertisingIdCollection(true);
        t.send(new HitBuilders.AppViewBuilder().build());










    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnUpdateRub:
                rubUpdateTask = new RubUpdateTask();
                rubUpdateTask.execute(null, null, null);
            break;
            case R.id.btnUpdateOil:

                oilUpdateTask = new OilUpdateTask();
                oilUpdateTask.execute(null, null, null);
            break;
            case R.id.btnUpdateUah:
                uahUpdateTask = new UahUpdateTask();
                uahUpdateTask.execute(null, null, null);
            break;
            case R.id.btnSettings:
                startActivity(new Intent(this, SettingsActivity.class));
            break;
            case R.id.btnRUBHistory:

                if(rubEurLastPrices.isEmpty() & rubUsdLastPrices.isEmpty()){
                    Toast.makeText(this, "No Info Found", Toast.LENGTH_SHORT).show();
                }
                else showRubChart();

            break;
            case R.id.btnOilHistory:
                if(brentOilLastPrices.isEmpty() & wtiOilLastPrices.isEmpty()){
                    Toast.makeText(this, "No Info Found", Toast.LENGTH_SHORT).show();
                }
                else showOilChart();
            break;
            case R.id.btnUahHistory:
                if(uahEurLastPrices.isEmpty() & uahUsdLastPrices.isEmpty()){
                    Toast.makeText(this, "No Info Found", Toast.LENGTH_SHORT).show();
                }
                else showUahChart();
            break;



        }
    }

    class RubUpdateTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pbRub.setVisibility(View.VISIBLE);
            rubIndicator.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... v) {
            checkSettings();
            infoDownloader.updateResPath();
            try {
                if (rubUsdTracking) {
                    rubUpdateUSDComplete = infoDownloader.updateRubToUsd();
                }
                if (rubEurTracking) {
                    rubUpdateEURComplete = infoDownloader.updateRubToEur();
                }
            }catch (StringIndexOutOfBoundsException ignored){

            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pbRub.setVisibility(View.GONE);

            if (rubUpdateUSDComplete) {

                String rubPrice = infoDownloader.getRubPriceTextUSD();
                boolean isRubUSDComingExpensive = infoDownloader.isRubUSDComingExpensive();
                saveRubUsd(rubPrice, isRubUSDComingExpensive);
                rubUsdLastPrices.push(rubPrice);
                tvRubForUsd.setText("" + rubPrice);

                if (isRubUSDComingExpensive) {
                    imageRubUsdArrow.setImageResource(R.drawable.arrow_up);
                    if (isPutinModeEnabled) imageRUB.setImageResource(getRandomSadPutin());
                    else imageRUB.setImageResource(R.drawable.rub);

                } else {
                    imageRubUsdArrow.setImageResource(R.drawable.arrow_down);
                    if (isPutinModeEnabled) imageRUB.setImageResource(getRandomHappyPutin());
                    else imageRUB.setImageResource(R.drawable.rub);
                }
            }
            if (rubUpdateEURComplete) {
                String rubPrice = infoDownloader.getRubPriceTextEUR();
                boolean isRubEURComingExpensive = infoDownloader.isRubEURComingExpensive();
                saveRubEur(rubPrice, isRubEURComingExpensive);
                rubEurLastPrices.push(rubPrice);
                tvRubForEur.setText("" + rubPrice);

                if (isRubEURComingExpensive) {
                    imageRubEurArrow.setImageResource(R.drawable.arrow_up);
                } else {
                    imageRubEurArrow.setImageResource(R.drawable.arrow_down);
                }
            }
            rubIndicator.setVisibility(View.VISIBLE);
            if (!(rubUpdateUSDComplete & rubUpdateEURComplete)){
                rubIndicator.setImageResource(R.drawable.load_corrupted);
            }
            if (rubUpdateUSDComplete | rubUpdateEURComplete){
                rubIndicator.setImageResource(R.drawable.load_warning);
            }
            if (rubUpdateUSDComplete & rubUpdateEURComplete){
                rubIndicator.setImageResource(R.drawable.load_complete);
            }

        }


    }

    class OilUpdateTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pbOil.setVisibility(View.VISIBLE);
            oilIndicator.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... v) {
            checkSettings();
            infoDownloader.updateResPath();
            try {
                oilBrentUpdateComplete = infoDownloader.updateOilBrent();
                oilWtiUpdateComplete = infoDownloader.updateOilWti();
            }catch (StringIndexOutOfBoundsException ignored){

            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pbOil.setVisibility(View.GONE);

            if (oilBrentUpdateComplete) {

                String brentOilPrice = infoDownloader.getOilBrentPriceText();
                boolean isBrentOilComingExpensive = infoDownloader.isOilBrentComingExpensive();

                saveBrentOil(brentOilPrice, isBrentOilComingExpensive);
                brentOilLastPrices.push(brentOilPrice);

                tvBrentOilPrice.setText("" + brentOilPrice);



                if (isBrentOilComingExpensive) {
                    imageBrentOilArrow.setImageResource(R.drawable.arrow_up);
                    if (isPutinModeEnabled) imageOil.setImageResource(getRandomHappyPutin());
                    else imageOil.setImageResource(R.drawable.oil);
                } else {
                    imageBrentOilArrow.setImageResource(R.drawable.arrow_down);
                    if (isPutinModeEnabled) imageOil.setImageResource(getRandomSadPutin());
                    else imageOil.setImageResource(R.drawable.oil);
                }

            }
            if (oilWtiUpdateComplete) {

                String wtiOilPrice = infoDownloader.getOilWtiPriceText();
                boolean isWtiOilComingExpensive = infoDownloader.isOilWtiComingExpensive();

                saveWTIOil(wtiOilPrice, isWtiOilComingExpensive);
                wtiOilLastPrices.push(wtiOilPrice);

                tvWtiOilPrice.setText("" + wtiOilPrice);



                if (isWtiOilComingExpensive) {
                    imageWtiOilArrow.setImageResource(R.drawable.arrow_up);
                } else {
                    imageWtiOilArrow.setImageResource(R.drawable.arrow_down);
                }

            }

            oilIndicator.setVisibility(View.VISIBLE);
            if (!(oilBrentUpdateComplete & oilWtiUpdateComplete)){
                oilIndicator.setImageResource(R.drawable.load_corrupted);
            }
            if (oilBrentUpdateComplete | oilWtiUpdateComplete){
                oilIndicator.setImageResource(R.drawable.load_warning);
            }
            if (oilBrentUpdateComplete & oilWtiUpdateComplete){
                oilIndicator.setImageResource(R.drawable.load_complete);
            }

        }


    }

    class UahUpdateTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pbUah.setVisibility(View.VISIBLE);
            uahIndicator.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... v) {
            checkSettings();
            infoDownloader.updateResPath();
            try {
                if (uahUsdTracking) {
                    uahUpdateUSDComplete = infoDownloader.updateUahToUsd();
                }
                if (uahEurTracking) {
                    uahUpdateEURComplete = infoDownloader.updateUahToEur();
                }
            }catch (StringIndexOutOfBoundsException ignored){

            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pbUah.setVisibility(View.GONE);

            if (uahUpdateUSDComplete) {

                String uahPrice = infoDownloader.getUahPriceTextUSD();
                boolean isUahUSDComingExpensive = infoDownloader.isUahUSDComingExpensive();
                saveUahUsd(uahPrice, isUahUSDComingExpensive);
                uahUsdLastPrices.push(uahPrice);
                tvUahForUsd.setText("" + uahPrice);

                if (isUahUSDComingExpensive) {
                    imageUahUsdArrow.setImageResource(R.drawable.arrow_up);
                    if (isPutinModeEnabled) imageUAH.setImageResource(getRandomSadPoroshenko());
                    else imageUAH.setImageResource(R.drawable.uah_ic);
                } else {
                    imageUahUsdArrow.setImageResource(R.drawable.arrow_down);
                    if (isPutinModeEnabled) imageUAH.setImageResource(getRandomHappyPoroshenko());
                    else imageUAH.setImageResource(R.drawable.uah_ic);
                }
            }
            if (uahUpdateEURComplete) {
                String uahPrice = infoDownloader.getUahPriceTextEUR();
                boolean isUahEURComingExpensive = infoDownloader.isUahEURComingExpensive();
                saveUahEur(uahPrice, isUahEURComingExpensive);
                uahEurLastPrices.push(uahPrice);
                tvUahForEur.setText("" + uahPrice);

                if (isUahEURComingExpensive) {
                    imageUahEurArrow.setImageResource(R.drawable.arrow_up);
                } else {
                    imageUahEurArrow.setImageResource(R.drawable.arrow_down);
                }
            }
            uahIndicator.setVisibility(View.VISIBLE);
            if (!(uahUpdateUSDComplete & uahUpdateEURComplete)){
                uahIndicator.setImageResource(R.drawable.load_corrupted);
            }
            if (uahUpdateUSDComplete | uahUpdateEURComplete){
                uahIndicator.setImageResource(R.drawable.load_warning);
            }
            if (uahUpdateUSDComplete & uahUpdateEURComplete){
                uahIndicator.setImageResource(R.drawable.load_complete);
            }

        }


    }

    public void loadFromSave(){
        brentOilLastPrices = new Stack<String>();
        wtiOilLastPrices = new Stack<String>();
        rubUsdLastPrices = new Stack<String>();
        rubEurLastPrices = new Stack<String>();
        uahUsdLastPrices = new Stack<String>();
        uahEurLastPrices = new Stack<String>();

        sharedPreferences = getSharedPreferences("save", MODE_PRIVATE);

        tvRubForUsd.setText(sharedPreferences.getString("rubUsd", "--.----"));
        if (sharedPreferences.getBoolean("rubUsdComingExpensive", false)) {
            imageRubUsdArrow.setImageResource(R.drawable.arrow_up);
            if (isPutinModeEnabled) imageRUB.setImageResource(getRandomSadPutin());
            else imageRUB.setImageResource(R.drawable.rub);
        } else {
            imageRubUsdArrow.setImageResource(R.drawable.arrow_down);
            if (isPutinModeEnabled) imageRUB.setImageResource(getRandomHappyPutin());
            else imageRUB.setImageResource(R.drawable.rub);
        }
        for (int i = 0; i < sharedPreferences.getInt("rubUsdLastPricesSize", 0); i++){
            rubUsdLastPrices.push(sharedPreferences.getString("rubUsdPriceNumber" + i, "0"));

        }


        tvRubForEur.setText(sharedPreferences.getString("rubEur", "--.----"));
        if (sharedPreferences.getBoolean("rubEurComingExpensive", false)) {
            imageRubEurArrow.setImageResource(R.drawable.arrow_up);
        } else {
            imageRubEurArrow.setImageResource(R.drawable.arrow_down);
        }
        for (int i = 0; i < sharedPreferences.getInt("rubEurLastPricesSize", 0); i++){
            rubEurLastPrices.push(sharedPreferences.getString("rubEurPriceNumber" + i, "0"));

        }

        tvBrentOilPrice.setText(sharedPreferences.getString("oil", "--.--"));
        brentOilLastPrices.clear();
        for (int i = 0; i < sharedPreferences.getInt("oilLastPricesSize", 0); i++){
            brentOilLastPrices.push(sharedPreferences.getString("priceNumber" + i, "0"));

        }

        if (sharedPreferences.getBoolean("oilComingExpensive", false)) {
            imageBrentOilArrow.setImageResource(R.drawable.arrow_up);
            if (isPutinModeEnabled) imageOil.setImageResource(getRandomHappyPutin());
            else imageOil.setImageResource(R.drawable.oil);
        } else {
            imageBrentOilArrow.setImageResource(R.drawable.arrow_down);
            if (isPutinModeEnabled) imageOil.setImageResource(getRandomSadPutin());
            else imageOil.setImageResource(R.drawable.oil);
        }

        tvWtiOilPrice.setText(sharedPreferences.getString("WtiOil", "--.--"));
        wtiOilLastPrices.clear();
        for (int i = 0; i < sharedPreferences.getInt("WtiOilLastPricesSize", 0); i++){
            wtiOilLastPrices.push(sharedPreferences.getString("WtiPriceNumber" + i, "0"));

        }

        if (sharedPreferences.getBoolean("WtiOilComingExpensive", false)) {
            imageWtiOilArrow.setImageResource(R.drawable.arrow_up);
        } else {
            imageWtiOilArrow.setImageResource(R.drawable.arrow_down);
        }


        tvUahForUsd.setText(sharedPreferences.getString("uahUsd", "--.----"));
        if (sharedPreferences.getBoolean("uahUsdComingExpensive", false)) {
            imageUahUsdArrow.setImageResource(R.drawable.arrow_up);
            if (isPutinModeEnabled) imageUAH.setImageResource(getRandomSadPoroshenko());
            else imageUAH.setImageResource(R.drawable.uah_ic);
        } else {
            imageUahUsdArrow.setImageResource(R.drawable.arrow_down);
            if (isPutinModeEnabled) imageUAH.setImageResource(getRandomHappyPoroshenko());
            else imageUAH.setImageResource(R.drawable.uah_ic);
        }
        for (int i = 0; i < sharedPreferences.getInt("uahUsdLastPricesSize", 0); i++){
            uahUsdLastPrices.push(sharedPreferences.getString("uahUsdPriceNumber" + i, "0"));

        }

        tvUahForEur.setText(sharedPreferences.getString("uahEur", "--.----"));
        if (sharedPreferences.getBoolean("uahEurComingExpensive", false)) {
            imageUahEurArrow.setImageResource(R.drawable.arrow_up);
        } else {
            imageUahEurArrow.setImageResource(R.drawable.arrow_down);
        }
        for (int i = 0; i < sharedPreferences.getInt("uahEurLastPricesSize", 0); i++){
            uahEurLastPrices.push(sharedPreferences.getString("uahEurPriceNumber" + i, "0"));

        }
    }

    public void saveRubUsd(String rub, boolean isRubComingExpensive){
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("rubUsd", rub);
        editor.putBoolean("rubUsdComingExpensive", isRubComingExpensive);

        editor.putInt("rubUsdLastPricesSize", rubUsdLastPrices.size());
        for (int i = 0; i < rubUsdLastPrices.size(); i++){
            editor.putString("rubUsdPriceNumber" + i, rubUsdLastPrices.get(i));
        }

        editor.apply();
    }

    public void saveRubEur(String rub, boolean isRubComingExpensive){
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("rubEur", rub);
        editor.putBoolean("rubEurComingExpensive", isRubComingExpensive);

        editor.putInt("rubEurLastPricesSize", rubEurLastPrices.size());
        for (int i = 0; i < rubEurLastPrices.size(); i++){
            editor.putString("rubEurPriceNumber" + i, rubEurLastPrices.get(i));
        }

        editor.apply();
    }

    public void saveBrentOil(String oil, boolean isOilComingExpensive){
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("oil", oil);
        editor.putBoolean("oilComingExpensive", isOilComingExpensive);

        editor.putInt("oilLastPricesSize", brentOilLastPrices.size());
        for (int i = 0; i < brentOilLastPrices.size(); i++){
            editor.putString("priceNumber" + i, brentOilLastPrices.get(i));
        }

        editor.apply();
    }

    public void saveWTIOil(String oil, boolean isOilComingExpensive){
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("WtiOil", oil);
        editor.putBoolean("WtiOilComingExpensive", isOilComingExpensive);

        editor.putInt("WtiOilLastPricesSize", wtiOilLastPrices.size());
        for (int i = 0; i < wtiOilLastPrices.size(); i++){
            editor.putString("WtiPriceNumber" + i, wtiOilLastPrices.get(i));
        }

        editor.apply();
    }

    public void saveUahUsd(String uah, boolean isUahComingExpensive){
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("uahUsd", uah);
        editor.putBoolean("uahUsdComingExpensive", isUahComingExpensive);

        editor.putInt("uahUsdLastPricesSize", uahUsdLastPrices.size());
        for (int i = 0; i < uahUsdLastPrices.size(); i++){
            editor.putString("uahUsdPriceNumber" + i, uahUsdLastPrices.get(i));
        }

        editor.apply();
    }

    public void saveUahEur(String uah, boolean isUahComingExpensive){
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("uahEur", uah);
        editor.putBoolean("uahEurComingExpensive", isUahComingExpensive);

        editor.putInt("uahEurLastPricesSize", uahEurLastPrices.size());
        for (int i = 0; i < uahEurLastPrices.size(); i++){
            editor.putString("uahEurPriceNumber" + i, uahEurLastPrices.get(i));
        }

        editor.apply();
    }

    private void checkSettings(){

        if (settingsFile.getBoolean("rubEnabled", true)){
            rubLayout.setVisibility(View.VISIBLE);
        }
        else{
            rubLayout.setVisibility(View.GONE);
        }
        rubUsdTracking = settingsFile.getBoolean("rubUSDTracking", true);
        rubEurTracking = settingsFile.getBoolean("rubEURTracking", true);
        uahUsdTracking = settingsFile.getBoolean("uahUSDTracking", true);
        uahEurTracking = settingsFile.getBoolean("uahEURTracking", true);
        isPutinModeEnabled = settingsFile.getBoolean("isPutinEnabled", true);

        if (settingsFile.getBoolean("oilEnabled", true)){
            oilLayout.setVisibility(View.VISIBLE);
        }
        else{
            oilLayout.setVisibility(View.GONE);
        }
        if (settingsFile.getBoolean("uahEnabled", true)){
            uahLayout.setVisibility(View.VISIBLE);
        }
        else{
            uahLayout.setVisibility(View.GONE);
        }



    }



    @Override
    public void onPause() {

        adView.pause();
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);


    }

    @Override
    public void onResume() {
        super.onResume();
        settingsFile = getSharedPreferences("settings", MODE_PRIVATE);
        if(settingsFile.getBoolean("fullScreen", false)){
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else{
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }
        loadFromSave();
        checkSettings();
        adView.resume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adView.destroy();

    }




    public int getRandomSadPutin(){
        int imgNumber = random.nextInt(3);
        switch (imgNumber){
            case 1: return sad1;
            case 2: return sad2;
            case 3: return sad3;
            default: return sad1;
        }
    }
    public int getRandomHappyPutin(){
        int imgNumber = random.nextInt(3);
        switch (imgNumber){
            case 0: return happy1;
            case 1: return happy2;
            case 2: return happy3;
            default: return happy1;
        }
    }
    public int getRandomSadPoroshenko(){
        int imgNumber = random.nextInt(2);
        switch (imgNumber){
            case 1: return pSad1;
            case 2: return pSad2;
            default: return pSad1;
        }
    }
    public int getRandomHappyPoroshenko(){
        int imgNumber = random.nextInt(2);
        switch (imgNumber){
            case 0: return pHappy1;
            case 1: return pHappy2;
            default: return pHappy1;
        }
    }

    private TextView createLastPriceTextView(float price){
        TextView textView = new TextView(this);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        textView.setText("" + price);
        return textView;
    }




    private void showOilChart(){

        View chart = inflater.inflate(R.layout.chart, null, false);
        LinearLayout mainContainer = (LinearLayout) chart.findViewById(R.id.chartContainer);
        mainContainer.removeAllViews();
        XYSeries brentSeries = new XYSeries(getString(R.string.brent_oil));
        XYSeries wtiSeries = new XYSeries(getString(R.string.wti_oil));
        XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

        for (int i = 0; i < brentOilLastPrices.size(); i++) {
            brentSeries.add(i+1, Double.parseDouble(brentOilLastPrices.get(i)));
        }
        for (int i = 0; i < wtiOilLastPrices.size(); i++) {
            wtiSeries.add(i+1, Double.parseDouble(wtiOilLastPrices.get(i)));
        }



        XYSeriesRenderer brentSeriesRenderer = new XYSeriesRenderer();
        XYSeriesRenderer wtiSeriesRenderer = new XYSeriesRenderer();

        brentSeriesRenderer.setLineWidth(2);
        brentSeriesRenderer.setColor(Color.RED);
        brentSeriesRenderer.setDisplayBoundingPoints(true);
        brentSeriesRenderer.setPointStyle(PointStyle.CIRCLE);
        brentSeriesRenderer.setPointStrokeWidth(3);

        wtiSeriesRenderer.setLineWidth(2);
        wtiSeriesRenderer.setColor(Color.BLUE);
        wtiSeriesRenderer.setDisplayBoundingPoints(true);
        wtiSeriesRenderer.setPointStyle(PointStyle.CIRCLE);
        wtiSeriesRenderer.setPointStrokeWidth(3);

        mRenderer.addSeriesRenderer(brentSeriesRenderer);
        mRenderer.addSeriesRenderer(wtiSeriesRenderer);
        // We want to avoid black border
        mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00)); // transparent margins
        // Disable Pan on two axis
        mRenderer.setPanEnabled(false, false);
        mRenderer.setZoomEnabled(true, false);
        if (brentOilLastPrices.size() > wtiOilLastPrices.size()){
            mRenderer.setXAxisMax(brentOilLastPrices.size() + (int) (brentOilLastPrices.size() * 0.20));
        }
        else { mRenderer.setXAxisMax(wtiOilLastPrices.size() + (int) (wtiOilLastPrices.size() * 0.20)); }
        mRenderer.setXAxisMin(1);
        mRenderer.setYAxisMax(120);
        mRenderer.setYAxisMin(0);
        mRenderer.setShowGrid(true);
        mRenderer.setXTitle(getString(R.string.updates));
        mRenderer.setYTitle(getString(R.string.prices));
        mRenderer.setGridColor(Color.GRAY);
        mRenderer.setAxesColor(Color.BLACK);
        mRenderer.setLabelsColor(Color.BLACK);
        mRenderer.setYLabelsColor(0, Color.BLACK);
        mRenderer.setXLabelsColor(Color.BLACK);
        mDataset.addSeries(brentSeries);
        mDataset.addSeries(wtiSeries);
        GraphicalView chartView = ChartFactory.getLineChartView(this, mDataset, mRenderer);
        mainContainer.addView(chartView);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(chart);
        builder.create();
        builder.show();

    }

    private void showRubChart(){

        View chart = inflater.inflate(R.layout.chart, null, false);
        LinearLayout mainContainer = (LinearLayout) chart.findViewById(R.id.chartContainer);
        mainContainer.removeAllViews();
        XYSeries usdSeries = new XYSeries(getString(R.string.rub_for_usd));
        XYSeries eurSeries = new XYSeries(getString(R.string.rub_for_eur));
        XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

        for (int i = 0; i < rubUsdLastPrices.size(); i++) {
            usdSeries.add(i+1, Double.parseDouble(rubUsdLastPrices.get(i)));
        }
        for (int i = 0; i < rubEurLastPrices.size(); i++) {
            eurSeries.add(i+1, Double.parseDouble(rubEurLastPrices.get(i)));
        }


        XYSeriesRenderer usdRenderer = new XYSeriesRenderer();
        XYSeriesRenderer eurRenderer = new XYSeriesRenderer();
        eurRenderer.setLineWidth(2);
        eurRenderer.setColor(Color.BLUE);
        // Include low and max value
        eurRenderer.setDisplayBoundingPoints(true);
        // we add point markers
        eurRenderer.setPointStyle(PointStyle.CIRCLE);
        eurRenderer.setPointStrokeWidth(3);
        usdRenderer.setLineWidth(2);
        usdRenderer.setColor(Color.GREEN);
        // Include low and max value
        usdRenderer.setDisplayBoundingPoints(true);
        // we add point markers
        usdRenderer.setPointStyle(PointStyle.CIRCLE);
        usdRenderer.setPointStrokeWidth(3);
        mRenderer.addSeriesRenderer(usdRenderer);
        mRenderer.addSeriesRenderer(eurRenderer);
        // We want to avoid black border
        mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00)); // transparent margins
        // Disable Pan on two axis
        mRenderer.setPanEnabled(false, false);
        mRenderer.setZoomEnabled(true, false);

        if (rubUsdLastPrices.size() > rubEurLastPrices.size()){
            mRenderer.setXAxisMax(rubUsdLastPrices.size() + (int) (rubUsdLastPrices.size() * 0.20));
        }
        else { mRenderer.setXAxisMax(rubEurLastPrices.size() + (int) (rubEurLastPrices.size() * 0.20)); }
        mRenderer.setXAxisMin(1);
        mRenderer.setYAxisMax(100);
        mRenderer.setYAxisMin(0);
        mRenderer.setShowGrid(true);
        mRenderer.setGridColor(Color.GRAY);
        mRenderer.setAxesColor(Color.BLACK);
        mRenderer.setLabelsColor(Color.BLACK);
        mRenderer.setYLabelsColor(0, Color.BLACK);
        mRenderer.setXLabelsColor(Color.BLACK);
        mRenderer.setXTitle(getString(R.string.updates));
        mRenderer.setYTitle(getString(R.string.prices));
        mDataset.addSeries(usdSeries);
        mDataset.addSeries(eurSeries);
        GraphicalView chartView = ChartFactory.getLineChartView(this, mDataset, mRenderer);
        mainContainer.addView(chartView);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(chart);
        builder.create();
        builder.show();

    }

    private void showUahChart(){

        View chart = inflater.inflate(R.layout.chart, null, false);
        LinearLayout mainContainer = (LinearLayout) chart.findViewById(R.id.chartContainer);
        mainContainer.removeAllViews();
        XYSeries usdSeries = new XYSeries(getString(R.string.uah_for_usd));
        XYSeries eurSeries = new XYSeries(getString(R.string.uah_for_eur));
        XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

        for (int i = 0; i < uahUsdLastPrices.size(); i++) {
            usdSeries.add(i+1, Double.parseDouble(uahUsdLastPrices.get(i)));
        }
        for (int i = 0; i < uahEurLastPrices.size(); i++) {
            eurSeries.add(i+1, Double.parseDouble(uahEurLastPrices.get(i)));
        }


        XYSeriesRenderer usdRenderer = new XYSeriesRenderer();
        XYSeriesRenderer eurRenderer = new XYSeriesRenderer();
        eurRenderer.setLineWidth(2);
        eurRenderer.setColor(Color.BLUE);

        // Include low and max value
        eurRenderer.setDisplayBoundingPoints(true);
        // we add point markers
        eurRenderer.setPointStyle(PointStyle.CIRCLE);
        eurRenderer.setPointStrokeWidth(3);
        usdRenderer.setLineWidth(2);
        usdRenderer.setColor(Color.GREEN);
        // Include low and max value
        usdRenderer.setDisplayBoundingPoints(true);
        // we add point markers
        usdRenderer.setPointStyle(PointStyle.CIRCLE);
        usdRenderer.setPointStrokeWidth(3);
        mRenderer.addSeriesRenderer(usdRenderer);
        mRenderer.addSeriesRenderer(eurRenderer);
        mRenderer.setGridColor(Color.GRAY);
        mRenderer.setAxesColor(Color.BLACK);
        mRenderer.setLabelsColor(Color.BLACK);
        mRenderer.setYLabelsColor(0, Color.BLACK);
        mRenderer.setXLabelsColor(Color.BLACK);
        // We want to avoid black border
        mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00)); // transparent margins
        // Disable Pan on two axis
        mRenderer.setPanEnabled(false, false);
        mRenderer.setZoomEnabled(true, false);

        if (uahUsdLastPrices.size() > uahEurLastPrices.size()){
            mRenderer.setXAxisMax(uahUsdLastPrices.size() + (int) (uahUsdLastPrices.size() * 0.20));
        }
        else { mRenderer.setXAxisMax(uahEurLastPrices.size() + (int) (uahEurLastPrices.size() * 0.20)); }
        mRenderer.setXAxisMin(1);
        mRenderer.setYAxisMax(50);
        mRenderer.setYAxisMin(0);
        mRenderer.setShowGrid(true);
        mRenderer.setXTitle(getString(R.string.updates));
        mRenderer.setYTitle(getString(R.string.prices));
        mDataset.addSeries(usdSeries);
        mDataset.addSeries(eurSeries);
        GraphicalView chartView = ChartFactory.getLineChartView(this, mDataset, mRenderer);
        mainContainer.addView(chartView);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(chart);
        builder.create();
        builder.show();

    }


}
