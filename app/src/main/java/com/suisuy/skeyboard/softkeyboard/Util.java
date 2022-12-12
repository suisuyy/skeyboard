package com.suisuy.skeyboard.softkeyboard;

import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Util {

    public static void test() {

        String urlString = "https://corsp.suisuy.eu.org/?https://inputtools.google.com/request?itc=zh-t-i0-pinyin&num=100&cp=0&cs=1&ie=utf-8&oe=utf-8&app=demopage&text=t";
        // "https://corsp.suisuy.eu.org/?https://inputtools.google.com/request?itc=zh-t-i0-pinyin&num=100&cp=0&cs=1&ie=utf-8&oe=utf-8&app=demopage&text=t";
        String jSonResultString = "[\"SUCCESS\",[[\"t\",[\"他\",\"她\",\"同\",\"它\",\"天\",\"太\",\"听\",\"头\",\"图\",\"挺\",\"条\",\"台\",\"特\",\"谈\",\"T\",\"t\",\"提\",\"贴\",\"通\",\"退\",\"套\",\"体\",\"团\",\"痛\",\"跳\",\"汤\",\"土\",\"推\",\"题\",\"疼\",\"调\",\"唐\",\"投\",\"铁\",\"田\",\"挑\",\"停\",\"填\",\"腿\",\"拖\",\"偷\",\"塔\",\"堂\",\"托\",\"糖\",\"吐\",\"脱\",\"踢\",\"替\",\"甜\",\"弹\",\"逃\",\"透\",\"帖\",\"泰\",\"厅\",\"涂\",\"添\",\"铜\",\"趟\",\"涛\",\"躺\",\"陶\",\"童\",\"烫\",\"兔\",\"贪\",\"谭\",\"胎\",\"亭\",\"桶\",\"庭\",\"探\",\"舔\",\"抬\",\"淘\",\"桃\",\"腾\",\"婷\",\"讨\",\"吞\",\"态\",\"坛\",\"统\",\"塘\",\"摊\",\"藤\",\"痰\",\"叹\",\"掏\",\"踏\",\"廷\",\"筒\",\"突\",\"徒\",\"碳\",\"屯\",\"捅\",\"途\",\"坦\"],[],{\"annotation\":[\"ta\",\"ta\",\"tong\",\"ta\",\"tian\",\"tai\",\"ting\",\"tou\",\"tu\",\"ting\",\"tiao\",\"tai\",\"te\",\"tan\",\"t\",\"t\",\"ti\",\"tie\",\"tong\",\"tui\",\"tao\",\"ti\",\"tuan\",\"tong\",\"tiao\",\"tang\",\"tu\",\"tui\",\"ti\",\"teng\",\"tiao\",\"tang\",\"tou\",\"tie\",\"tian\",\"tiao\",\"ting\",\"tian\",\"tui\",\"tuo\",\"tou\",\"ta\",\"tang\",\"tuo\",\"tang\",\"tu\",\"tuo\",\"ti\",\"ti\",\"tian\",\"tan\",\"tao\",\"tou\",\"tie\",\"tai\",\"ting\",\"tu\",\"tian\",\"tong\",\"tang\",\"tao\",\"tang\",\"tao\",\"tong\",\"tang\",\"tu\",\"tan\",\"tan\",\"tai\",\"ting\",\"tong\",\"ting\",\"tan\",\"tian\",\"tai\",\"tao\",\"tao\",\"teng\",\"ting\",\"tao\",\"tun\",\"tai\",\"tan\",\"tong\",\"tang\",\"tan\",\"teng\",\"tan\",\"tan\",\"tao\",\"ta\",\"ting\",\"tong\",\"tu\",\"tu\",\"tan\",\"tun\",\"tong\",\"tu\",\"tan\"],\"candidate_type\":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],\"lc\":[\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"0\",\"0\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\",\"16\"]}]]]";

        try {

            String tmpstr = getResposeAsString(urlString);


                      getCandidatesFromGooglePinyin("wo") ;

//            Log.println(Log.INFO,"test",tmpstr  );
//            Log.println(Log.INFO, "paseurl", parseCandidatesAsJSONarray(tmpstr).getString(1));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
public static JSONArray getCandidatesBaiduPinyinAsJSONArray(String pinyinStr){
        String apiURL = "https://olime.baidu.com/py?rn=0&pn=20&py="+pinyinStr;
        JSONArray resultjsonArray=new JSONArray();
        JSONObject jsonObject;
    try {

        jsonObject=new JSONObject(getResposeAsString(apiURL));
        try {
            resultjsonArray=jsonObject.getJSONArray("0");
            resultjsonArray=new JSONArray().put(resultjsonArray.getJSONArray(0).getJSONArray(0).getString(0));
            return  resultjsonArray;
        } catch (JSONException e) {
            e.printStackTrace();
            return  new JSONArray();
        }
    } catch (JSONException e) {
        e.printStackTrace();
        return  new JSONArray();  //return empty jsonarray if error
    }

}

    public static List getCandidatesFromGooglePinyin(String pinyinStr){
        String apiURL = "https://corsp.suisuy.eu.org/?https://inputtools.google.com/request?itc=zh-t-i0-pinyin&num=100&cp=0&cs=1&ie=utf-8&oe=utf-8&app=demopage&text="+pinyinStr;
        List<String> candidatesList=new ArrayList<>();
        JSONArray candidateJsonArray=parseCandidatesAsJSONarray(getResposeAsString(apiURL));
        for(int i=0;i<candidateJsonArray.length();i++){
            try {
                candidatesList.add(candidateJsonArray.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return candidatesList;
    }

    public static JSONArray getCandidatesJSONArrayFromGoogleSPin(String pinyinStr){
        String apiURL = "https://corsp.suisuy.eu.org/?https://inputtools.google.com/request?itc=zh-t-i0-pinyin-x0-shuangpin-ms&num=100&cp=0&cs=1&ie=utf-8&oe=utf-8&app=demopage&text="+pinyinStr;
        List<String> candidatesList=new ArrayList<>();
        JSONArray candidateJsonArray=parseCandidatesAsJSONarray(getResposeAsString(apiURL));
        return candidateJsonArray;
    }
    public static List getCandidatesFromGoogleSPin(String pinyinStr){
        String apiURL = "https://corsp.suisuy.eu.org/?https://inputtools.google.com/request?itc=zh-t-i0-pinyin-x0-shuangpin-ms&num=100&cp=0&cs=1&ie=utf-8&oe=utf-8&app=demopage&text="+pinyinStr;
        List<String> candidatesList=new ArrayList<>();
        JSONArray candidateJsonArray=parseCandidatesAsJSONarray(getResposeAsString(apiURL));
        for(int i=0;i<candidateJsonArray.length();i++){
            try {
                candidatesList.add(candidateJsonArray.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return candidatesList;
    }

    //use JSONArray.getString to get each cadidates
    public static JSONArray parseCandidatesAsJSONarray(String arraystr) {

        try {
            JSONArray arr = new JSONArray(arraystr);
            return (arr.getJSONArray(1).getJSONArray(0).getJSONArray(1));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static String getResposeAsString(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            Scanner s = new Scanner(connection.getInputStream()).useDelimiter("\\A");
            String result = s.hasNext() ? s.next() : "";
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return "";

    }

    public static List<String> createStrListFromJsonArray(JSONArray ja){
        List<String> resultList=new ArrayList();
        for(int i=0;i<ja.length();i++){
            try {
                resultList.add(ja.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return  resultList;
    }

    public static JSONArray createJSONArrayFromStrList(List<String> slist){
        JSONArray resultArray=new JSONArray();
        for(int i=0;i<slist.toArray().length;i++){
            resultArray.put(slist.get(i));
        }
        return  resultArray;
    }

    public  static  void savePinyinRecordToSdcard(String pinYinRecorde){
         File sdCardDir = Environment.getExternalStorageDirectory();
                File targetFile = new File(sdCardDir, "shuangpin.txt");
                FileOutputStream writer = null;
                try {
                    writer = new FileOutputStream(targetFile,true);

                        JSONObject pyJsonObj=new JSONObject();

                            try  {
                                writer.write( (pinYinRecorde+"\n").getBytes(StandardCharsets.UTF_8) );
                            } catch (IOException e) {
                                e.printStackTrace();
                            }




                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

    }

    public  static  JSONObject getPinyinRecordFromSdcard(){
        File sdCardDir = Environment.getExternalStorageDirectory();
        File targetFile = new File(sdCardDir, "shuangpin.txt");
        JSONObject pyRecodObj=new JSONObject();
        try {
            Scanner scanner = new Scanner(targetFile);

            while (scanner.hasNextLine()) {
                JSONObject singleRecordObj=new JSONObject(scanner.nextLine());
                String akey=singleRecordObj.keys().next();
                pyRecodObj.put(akey,singleRecordObj.getJSONArray(akey )  );
            }

            scanner.close();

            return  pyRecodObj;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new JSONObject();
    }


    public static   String convertShuangPinToPinyin(String spStr){
        String pyStr="";
        for(int i=0;i<spStr.length();i++){
            if(i%2==0){
                switch (spStr.charAt(i)){
                    case 'v': pyStr+="zh";
                        break;
                    case 'u': pyStr+="sh";
                        break;
                    case 'i': pyStr+="ch";
                        break;
                    default:
                        pyStr+=spStr.charAt(i);


                }
            }
            else{
                switch (spStr.charAt(i)){
//                    case 'a':

                    case 'b': pyStr+="ou";
                        break;
                    case 'c': pyStr+="iao";
                        break;
                    case 'd': pyStr+="iang";
                        break;
//                    case 'e': ;
                    case 'f': pyStr+="en";
                        break;
                    case 'g': pyStr+="uang";
                        break;
                    case 'h': pyStr+="ang";
                        break;
//                    case 'i': ;

                    case 'j': pyStr+="an";
                        break;
                    case 'k': pyStr+="ao";
                        break;
                    case 'l': pyStr+="ai";
                        break;
                    case 'm': pyStr+="ian";
                        break;
                    case 'n': pyStr+="in";
                        break;
                    case 'o': pyStr+="o";
                        break;
                    case 'p': pyStr+="un";
                        break;
                    case 'q': pyStr+="iu";
                        break;
                    case 'r': pyStr+="er";
                        break;
                    case 's': pyStr+="ong";
                        break;
                    case 't': pyStr+="ue";
                        break;
//                    case 'u': ;
                    case 'v': pyStr+="ui";
                        break;
                    case 'w': pyStr+="ia";
                        break;
                    case 'x': pyStr+="ie";
                        break;
                    case 'z': pyStr+="ei";
                        break;

                    default:
                        pyStr+=spStr.charAt(i);


                }
            }
        }

        return pyStr;
    }
    public static JSONObject getJSONObjectFromURL(String urlString) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);
        urlConnection.setDoOutput(true);
        urlConnection.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();

        String jsonString = sb.toString();
        System.out.println("JSON: " + jsonString);

        return new JSONObject(jsonString);
    }
}


