package com.nextlabs;

import javax.xml.bind.annotation.adapters.XmlAdapter;
public class MapAdapter extends XmlAdapter<MapConvertor, java.util.Map<String, String>> {  
	  
    @Override  
    public java.util.Map<String, String> unmarshal(MapConvertor convertor) throws Exception {  
        java.util.List<com.nextlabs.MapConvertor.MapEntry> entries = convertor.getEntries();  
        if (entries != null && entries.size() > 0) {  
            java.util.Map<String, String> map = new java.util.HashMap<String, String>();  
            for (com.nextlabs.MapConvertor.MapEntry mapEntry : entries) {  
                map.put(mapEntry.getKey(), mapEntry.getValue());  
            }  
            return map;  
        }  
        return null;  
    }  
  
    @Override  
    public MapConvertor marshal(java.util.Map<String, String> map) throws Exception {  
        MapConvertor convertor = new MapConvertor();  
        for (java.util.Map.Entry<String, String> entry : map.entrySet()) {  
            convertor.addEntry(new MapConvertor.MapEntry(entry));  
        }  
        return convertor;  
    }  
  
}  