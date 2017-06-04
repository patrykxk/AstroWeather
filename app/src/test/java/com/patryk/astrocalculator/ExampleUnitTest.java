package com.patryk.astrocalculator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void astro() throws Exception {
//        String request = "http://api.search.yahoo.com/WebSearchService/V1/webSearch?appid=YahooDemo&query=umbrella&results=10&output=json";
//
//
//        URL url = new URL("http://samples.openweathermap.org/data/2.5/weather?q=Lodz&appid=e355a0eb9dd784bd22af704c34053e6a");
//        try (InputStream is = url.openStream();
//             JsonElement jelement = new JsonParser().parse(jsonLine);
//             JsonObject jobject = jelement.getAsJsonObject();
//             jobject = jobject.getAsJsonObject("data");
//        JsonArray jarray = jobject.getAsJsonArray("translations");
//        jobject = jarray.get(0).getAsJsonObject();
//        String result = jobject.get("translatedText").toString();
//        return result;
    }
}