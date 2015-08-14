package ua.com.oilversusruble.oilversusruble;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by User on 16.10.2014.
 *
 */
public class InfoDownloader  {


    Stock rubUsd, rubEur, brent, wti, usdUah, eurUah;
    private float oldOilBrentPrice = 0, oilBrentPrice = 0, oldOilWtiPrice = 0, oilWtiPrice = 0, oldRubPriceUSD = 0, rubPriceUSD = 0, rubPriceEUR = 0, oldRubPriceEUR = 0, uahPriceUSD = 0,
            oldUahPriceUsd = 0, uahPriceEUR, oldUahPriceEUR;
    private String rubUsdLink, rubEurLink, oilBrentTicket, oilWtiTicket, uahUsdLink, uahEurLink;

    private String rubPriceTextUSD, rubPriceTextEUR, oilBrentPriceText, oilWtiPriceText, uahPriceTextUSD, uahPriceTextEUR;








    public void updateResPath()  {
        Document resPath = null;
        try {
            resPath = Jsoup.connect("http://myfiles.ucoz.ua/res/newrespath.html").get();

        } catch (IOException ignored) {

        }
        Element links = resPath.select("res").first();
        oilBrentTicket = links.attr("oilbrent");
        oilWtiTicket = links.attr("oilwti");


    }

    public boolean updateRubToUsd(){
        try {
            rubUsd = StockFetcher.getStock("USDRUB=X");
            getOldRubPriceUSD();

                    rubPriceUSD = (float) rubUsd.getPrice();
                    rubPriceTextUSD = rubUsd.getPrice() + "";



            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public boolean updateRubToEur(){
        try {
            rubEur = StockFetcher.getStock("EURRUB=X");

            getOldRubPriceEUR();

                    rubPriceEUR = (float) rubEur.getPrice();
                    rubPriceTextEUR = rubEur.getPrice() + "";

            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public boolean updateOilBrent(){
        try {
           brent = StockFetcher.getStock(oilBrentTicket);

            getOldOilPriceBrent();

                    oilBrentPrice = (float) brent.getPrice();
                    oilBrentPriceText = brent.getPrice() + "";
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean updateOilWti(){
        try {
            wti = StockFetcher.getStock(oilWtiTicket);

            getOldOilPriceWti();

                    oilWtiPrice = (float) wti.getPrice();
                    oilWtiPriceText = wti.getPrice() + "";

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean updateUahToUsd(){
        try {
            usdUah = StockFetcher.getStock("USDUAH=X");

            getOldUahPriceUSD();

                    uahPriceUSD = (float) usdUah.getPrice();
                    uahPriceTextUSD = usdUah.getPrice() + "";
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public boolean updateUahToEur(){
        try {
            eurUah = StockFetcher.getStock("EURUAH=X");

            getOldUahPriceEUR();

                    uahPriceEUR = (float) eurUah.getPrice();
                    uahPriceTextEUR = eurUah.getPrice() + "";
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    private void getOldOilPriceBrent(){

        oldOilBrentPrice = (float) brent.getDayhigh();

    }

    private void getOldOilPriceWti(){
        oldOilWtiPrice = (float) wti.getDayhigh();

    }

    public boolean isOilBrentComingExpensive(){
        if (oldOilBrentPrice>oilBrentPrice){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean isOilWtiComingExpensive(){
        if (oldOilWtiPrice>oilWtiPrice){
            return false;
        }
        else{
            return true;
        }
    }


    private void getOldRubPriceUSD(){
        oldRubPriceUSD = (float) rubUsd.getDayhigh();

    }

    private void getOldRubPriceEUR(){
        oldRubPriceUSD = (float) rubEur.getDayhigh();

    }

    private void getOldUahPriceUSD(){
        oldUahPriceUsd = (float) usdUah.getDayhigh();


    }

    private void getOldUahPriceEUR(){
        oldUahPriceEUR = (float) eurUah.getDayhigh();


    }


    public boolean isRubUSDComingExpensive(){
        if (oldRubPriceUSD < rubPriceUSD){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean isRubEURComingExpensive(){
        if (oldRubPriceEUR < rubPriceEUR){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean isUahUSDComingExpensive(){
        if (oldUahPriceUsd < uahPriceUSD){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean isUahEURComingExpensive(){
        if (oldUahPriceEUR < uahPriceEUR){
            return true;
        }
        else{
            return false;
        }
    }

    public String getRubPriceTextUSD() {
        return rubPriceTextUSD;
    }

    public String getRubPriceTextEUR() {
        return rubPriceTextEUR;
    }

    public String getOilBrentPriceText() {
        return oilBrentPriceText;
    }

    public String getOilWtiPriceText() {
        return oilWtiPriceText;
    }

    public String getUahPriceTextUSD() {
        return uahPriceTextUSD;
    }

    public String getUahPriceTextEUR() {
        return uahPriceTextEUR;
    }


}

