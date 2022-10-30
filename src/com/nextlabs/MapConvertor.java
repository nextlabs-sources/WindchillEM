package com.nextlabs;
//import javax.xml.bind.annotation.adapters.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
@XmlType(name="MapConvertor")  
@XmlAccessorType(XmlAccessType.FIELD)  
public class MapConvertor {  
  
    private List<MapEntry> entries = new ArrayList<MapConvertor.MapEntry>();  
  
    public void addEntry(MapEntry entry) {  
        entries.add(entry);  
    }  
  
    public List<MapEntry> getEntries() {  
        return entries;  
    }  
  
    public static class MapEntry {  
        private String key;  
        private String value;  
  
        public MapEntry() {  
  
        }  
  
        public MapEntry(String key, String value) {  
            this.key = key;  
            this.value = value;  
        }  
  
        public MapEntry(Map.Entry<String, String> entry) {  
            this.key = entry.getKey();  
            this.value = entry.getValue();  
        }  
  
        public String getKey() {  
            return key;  
        }  
  
        public void setKey(String key) {  
            this.key = key;  
        }  
  
        public String getValue() {  
            return value;  
        }  
  
        public void setValue(String value) {  
            this.value = value;  
        }  
  
    }  
  
}  