import content.DocumentParserService;
import utils.models.Content;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static utils.Constants.parseEntryPoint;
import static utils.Constants.showAllUrl;

public class Main
{
    public static void main(String[] args)
    {
        // get all models
        Set<String> toGather = getPartListPages(parseEntryPoint);

        // get parts for each model
        Set<String> toCrawl = new HashSet<>();
        for (String url : toGather) {
            toCrawl.addAll(generateLinkList(getUrlContent(url + showAllUrl)));
        }
        
        // get details for each part
        List<Content> allContent = new ArrayList<>();
        for (String url : toCrawl) {
            allContent.add(new DocumentParserService(url).loadContent());
        }
    
        // save the details to CSV
        try {
            generateCsv(allContent);
        } catch (IOException e) {
            System.out.println("Не удалось сгенерировать CSV");
            e.printStackTrace();
        }
    }
    
    public static Set<String> getPartListPages(String url) {
        return new HashSet<>(
            new DocumentParserService(
                getUrlContent(url)
            ).getPartLists());
    }
    
    public static Set<String> generateLinkList(String url) {
        return new HashSet<>(
            new DocumentParserService(
                getUrlContent(url)
            ).getPartLinks());
    }
    
    public static void generateCsv(List<Content> allContent) throws IOException
    {
        BufferedWriter bw = new BufferedWriter(
            new OutputStreamWriter(
                new FileOutputStream(
                    new File("prices.csv"))));
        
        for (Content content: allContent) {
            bw.write(content.toString());
            bw.newLine();
        }
        bw.close();
    }
    
    public static String getUrlContent(String url)
    {
        String toReturn = null;
        HttpClient httpClient = HttpClientBuilder.create().build();
        try
        {
            HttpGet request = new HttpGet(url.replace(' ', '+'));
            //request.addHeader("User-Agent",USER_AGENT);
            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                toReturn = EntityUtils.toString(response.getEntity());
            }
        } catch (Exception e)
        {
            System.out.println(
                "Cannot execute GET for url: " + url +
                ", message is:" + e.getMessage());
        }
        return toReturn;
    }
}
