package utils.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Content {
    private Map<String, String> params;
    private List<String> devices;
    
    public Content() {
        params = new HashMap<>();
        params.put("Наличие", "");
        params.put("Цена", "");
        params.put("Марка", "");
        params.put("Артикул", "");
        params.put("Производитель", "");
        params.put("Товар", "");
        params.put("Узел/Категория", "");
        params.put("Комментарий", "");
        devices = new ArrayList<>();
    }
    
    public void addParam(String name, String value) {
        params.put(name, value);
    }
    
    public void addDevice(String device) {
        devices.add(device);
    }
    
    public Map<String, String> getParams() {
        return params;
    }
    
    public List<String> getDevices() {
        return devices;
    }
    
    public String toString() {
        String div = "±";
        final StringBuilder toReturn = new StringBuilder();
        params.forEach((k, v) -> toReturn.append(k).append(div).append(v).append(div));
        toReturn.append(div).append("=УСТРОЙСТВА=").append(div);
        devices.forEach(it -> toReturn.append(it).append(div));
        return toReturn.toString();
    }
}
