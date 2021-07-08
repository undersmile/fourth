package club.guoshizhan.PO;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

public class English {
    public static void main(String[] args) throws Exception {
        Date date = new Date();
        SimpleDateFormat englishDate = new SimpleDateFormat("yyyy-MM-dd");
        String format = englishDate.format(date);
        System.out.println(format);
        int count = 0;

        // "https://dict.youdao.com/infoline?mode=publish&date=" + format + "&update=auto&apiversion=5.0"
        // https://apiv3.shanbay.com/weapps/dailyquote/quote/?date=2020-11-21
        for (int j = 1; j <= 11; j++) {
            String month = "";
            if (j < 10) {
                month = "0" + j;
            } else {
                month = j + "";
            }
            int day = 0;
            if (j == 2) {
                day = 28;
            }
            if (j == 1 || j == 3 || j == 5 || j == 7 || j == 8 || j == 10) {
                day = 31;
            }
            if (j == 4 || j == 6 || j == 9 || j == 11) {
                day = 30;
            }
            for (int i = 1; i <= day; i++) {
                count++;
                String temp = "";
                if (i < 10) {
                    temp = "0" + i;
                } else {
                    temp = i + "";
                }
                String path = "https://apiv3.shanbay.com/weapps/dailyquote/quote/?date=2020-" + month + "-" + temp;
                URL url = new URL(path);
                URLConnection urlConnection = url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();

                String readline = null;
                String res = "";
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

                while ((readline = br.readLine()) != null) {
                    if (readline != null) {
                        res = readline;
                    }
                }

                Gson gson = new Gson();
                OneSentence oneSentence = gson.fromJson(res, OneSentence.class);
                System.out.println(oneSentence);
            }
        }
        System.out.println(count);
    }
}
