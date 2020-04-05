package utils;

public class Constants
{
    private static String format = "zip";
    private static String protocol = "https://";
    private static String country = "ru";
    public static String baseUrl = protocol + format + format + "." + country;
    public static String parseEntryPoint = baseUrl + "/price2/?kind=1&brand=HP";
    public static String paramsSelector = ".main_content .right_coloumn ul.description>li";
    public static String devicesSelector = ".main_content ul.description .three_coloumn li";
    public static String partListSelector = "td.box_text table.forsearch td a";
    public static String partLinkSelector = "table.prod_table td a.screenshot";
}
