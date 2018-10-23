package com.example.raghavgupta.chadisapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class ExpandableListDataPump {
    public static TreeMap<Object, Object> nameMap = new TreeMap<Object, Object>();
    public static TreeMap<Object, Object> dobMap = new TreeMap<Object, Object>();
    public static String qID;
    private static String text;
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        text = fetchQuest.getResponse().toString();
        try{
            JSONObject object = new JSONObject(text);
            JSONObject patientDetails = (JSONObject) object.get("patient");
            JSONArray nest = (JSONArray) object.get("questionnaires");

            for(int i=0;i<nest.length();i++){
                nameMap.put(nest.getJSONObject(i).get("name").toString(), nest.getJSONObject(i).get("status_id"));
                qID = nest.getJSONObject(i).get("questionnaire_id").toString();
                dobMap.put(nest.getJSONObject(i).get("name").toString(),nest.getJSONObject(i).get("id").toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<String> newQ = new ArrayList<>();
        for (Map.Entry<Object, Object> entry : nameMap.entrySet()) {
            if (entry.getValue().equals(1))
                newQ.add(entry.getKey().toString());
        }

        List<String> progress = new ArrayList<>();
        for (Map.Entry<Object, Object> entry : nameMap.entrySet()) {
            if (entry.getValue().equals(2))
                progress.add(entry.getKey().toString());
        }

        List<String> rsubmit = new ArrayList<>();
        for (Map.Entry<Object, Object> entry : nameMap.entrySet()) {
            if (entry.getValue().equals(3))
                rsubmit.add(entry.getKey().toString());
        }

        List<String> submitted = new ArrayList<>();
        for (Map.Entry<Object, Object> entry : nameMap.entrySet()) {
            if (entry.getValue().equals(4))
                submitted.add(entry.getKey().toString());
        }

        expandableListDetail.put("New questionnaires", newQ);
        expandableListDetail.put("In progress", progress);
        expandableListDetail.put("Ready to submit", rsubmit);
        expandableListDetail.put("Submitted", submitted);

        return expandableListDetail;
    }
}
