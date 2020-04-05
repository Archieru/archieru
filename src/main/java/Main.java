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

public class Main
{
    public static void main(String[] args)
    {
        Set<String> toGather = getPartListPages(getUrlContent(parseEntryPoint));
        Set<String> toCrawl = new HashSet<>();
        for (String url : toGather) {
            toCrawl.addAll(generateLinkList(getUrlContent(url+"&pgsize=0")));
        }
        List<Content> allContent = new ArrayList<>();
        for (String url : toCrawl) {
            allContent.add(new DocumentParserService(getUrlContent(url)).loadContent());
        }
        if (!generateCsv(allContent)) {
            System.out.println("Не удалось сгенерировать CSV");
        }
    }
    
    public static Set<String> getPartListPages(String html) {
        return new HashSet<>(new DocumentParserService(html).getPartLists());
    }
    
    public static Set<String> generateLinkList(String html) {
        return new HashSet<>(new DocumentParserService(html).getPartLinks());
    }
    
    public static boolean generateCsv(List<Content> allContent)
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(new File("prices.csv"));
    
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
    
            for (Content content: allContent) {
                bw.write(content.toString());
                bw.newLine();
            }
    
            bw.close();
        } catch (IOException e) {
            return false;
        }
        return true;
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
            System.out.println("Get content while creating article " +
                "cannot execute GET for url: " + url + ", message is:" + e.getMessage());
        }
        return toReturn;
    }
}
