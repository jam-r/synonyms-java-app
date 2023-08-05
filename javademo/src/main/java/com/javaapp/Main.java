package com.javaapp;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.management.RuntimeErrorException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main {
    public static void main(String[] args) {
        System.out.println("Enter a word:");
        Scanner in = new Scanner(System.in);
        String word=in.nextLine();
        in.close();
        String urlContact = "https://api.dictionaryapi.dev/api/v2/entries/en/"+word;
        try {
            URL url = new URL(urlContact);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();
            if(responseCode == 200){
                StringBuilder responseString = new StringBuilder();
                Scanner scanner = new Scanner (url.openStream());
                while(scanner.hasNext()){
                    responseString.append(scanner.nextLine());
                }
                scanner.close();
               // System.out.println(responseString);

                System.out.println("\n\n\n\n");


                JSONParser parse = new JSONParser();
                JSONArray dataObject = (JSONArray) parse.parse(String.valueOf(responseString));

                JSONObject dictionaryData = (JSONObject)dataObject.get(0);
                JSONArray meaningsData = (JSONArray)dictionaryData.get("meanings");
                for(Object o:meaningsData){
                    if(o instanceof JSONObject ){
                        JSONObject meaning = (JSONObject) o;
                        String partOfSpeech = (String) meaning.get("partOfSpeech");
                        if("adjective".equals(partOfSpeech)){
                            JSONArray synonyms = (JSONArray) meaning.get("synonyms");
                            for (Object syn : synonyms) {
                                if(syn instanceof String){
                                    System.out.println(syn);
                                }
                            }
                        }
                    }
                }
            }else{
                throw new RuntimeException("code: " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}