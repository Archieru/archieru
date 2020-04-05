package content;

import utils.models.Content;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static utils.Constants.*;

public class DocumentParserService
{
    Document doc;
    
    public DocumentParserService(String html) {
        doc = Jsoup.parse(html);
    }
    
    public Set<String> getPartLists() {
        Set<String> toReturn = new HashSet<>();
        Elements elements = doc.select(partListSelector);
        for (Element element : elements) {
            String toAdd = element.attr("href");
            if (toAdd.startsWith("#")) { continue; }
            if (toAdd.startsWith("/")) { toAdd = baseUrl + toAdd; }
            toReturn.add(toAdd);
        }
        return toReturn;
    }
    
    public Set<String> getPartLinks() {
        Set<String> toReturn = new HashSet<>();
        Elements elements = doc.select(partLinkSelector);
        for (Element element : elements) {
            String toAdd = element.attr("href");
            if (toAdd.startsWith("/")) { toAdd = baseUrl + toAdd; }
            toReturn.add(toAdd);
        }
        return toReturn;
    }
    
    public Content loadContent() {
        Content toReturn = new Content();
        List<String> params = getParams();
        for (String param : params) {
            String[] kv = param.split(":");
            StringBuilder v = new StringBuilder();
            for (int i=1; i<kv.length; i++) {
                v.append(" ").append(kv[i]);
            }
            toReturn.addParam(kv[0].trim(), v.toString().trim());
        }
        List<String> devices = getDevices();
        for (String device : devices) {
            toReturn.addDevice(device);
        }
        return toReturn;
    }
  
    private List<String> getParams() {
        List<String> toReturn = new ArrayList<>();
        Elements elements = doc.select(paramsSelector);
        for (Element element : elements) {
            toReturn.add(element.text());
        }
        return toReturn;
    }
    
    private List<String> getDevices() {
        List<String> toReturn = new ArrayList<>();
        Elements elements = doc.select(devicesSelector);
        for (Element element : elements) {
            toReturn.add(element.text());
        }
        return toReturn;
    }
}