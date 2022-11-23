package com.cj.devops.domin.domainexpiration.service;

import com.cj.devops.domin.domainexpiration.dto.DomainExpiration;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.whois.WhoisClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class DomainExpirationService {

    private static final String domainBaseUrl = "http://whois.kisa.or.kr/openapi/whois.jsp?&answer=json";
    private static final String key = "2021031511201370240238";

    public DomainExpiration getDomainExpiration(String domainName) {
        String ext = domainName.substring(domainName.lastIndexOf(".") + 1).toLowerCase();
        String expireDate = "";

        log.info("ext : " + ext);

        try {
            if ("kr".equals(ext)) {
                expireDate = checkExpireDateByKr(domainName);
            } else if ("com".equals(ext) || "net".equals(ext) || "edu".equals(ext)) {
                expireDate = checkExpireDateByEtc(domainName);
            }

            Pattern pattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
            Matcher matcher = pattern.matcher(expireDate);
            if(!matcher.find()) {
                expireDate = "Invalid Domain Name.";
            }

            log.info(domainName + " : " + expireDate);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return DomainExpiration.builder()
                    .domainName(domainName)
                    .expiredDate(expireDate)
                    .build();
    }

    public String checkExpireDateByKr(String domainName) throws Exception{
        String expireDate = "";
        String url = domainBaseUrl + "&key=" + key + "&query=" + domainName;

        URL requestUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection)requestUrl.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        if(conn.getResponseCode() == 200) {
            Scanner sc = new Scanner(requestUrl.openStream(), "UTF-8");
            String inline="";
            while(sc.hasNext()){
                inline+=sc.nextLine();
            }
            sc.close();

            JSONParser parse = new JSONParser();
            JSONObject jobj = (JSONObject) parse.parse(inline);
            JSONObject whois = (JSONObject) jobj.get("whois");
            JSONObject krdomain = (JSONObject) whois.get("krdomain");
            if(krdomain.get("endDate") != null) {
                expireDate = (String) krdomain.get("endDate");
                expireDate = expireDate.replaceAll("\\. ", "-").replaceAll("\\.", "");
            }
        }
        conn.disconnect();

        return expireDate;
    }


    private String checkExpireDateByEtc(String domainName) throws Exception{
        WhoisClient whois = new WhoisClient();
        whois.connect(WhoisClient.DEFAULT_HOST);
        String orginText = whois.query(domainName);
        String indexStr = "Registry Expiry Date: ";
        int start = orginText.indexOf(indexStr)+indexStr.length();
        String expireDate = orginText.substring(start, start + 10);//.replaceAll("-","");
        if(expireDate.length() != 10){
            expireDate = "";
        }
        whois.disconnect();



        return expireDate;
    }
}
