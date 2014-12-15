/*
 * Nebarti
 * Copyright © 2012-2013. All rights reserved.
 */
package com.idot.dataingest.namedentities.dictionaries;

import com.idot.dataingest.namedentities.dictionaries.Dictionary.DictionaryType;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * 
 */
public class Dictionaries {
    private Map<String, Dictionary> dictionaries = new HashMap<String, Dictionary>();
    public static final Logger logger = Logger.getLogger(Dictionaries.class.getName());
    
    public Dictionaries() {
        dictionaries.put(DictionaryType.COMPANY.toString(), new CompanyDictionary("dictionary.companies.file"));
        dictionaries.put(DictionaryType.STATE.toString(), new StateDictionary("dictionary.states.file"));
        dictionaries.put(DictionaryType.COUNTRY.toString(), new CountryDictionary("dictionary.countries.file"));
        dictionaries.put(DictionaryType.FAMOUS_PEOPLE.toString(), new FamousPeopleDictionary("dictionary.famouspeople.file"));
        dictionaries.put(DictionaryType.CITY.toString(), new CityDictionary("dictionary.cities.file"));
    }

    public Map<String, Dictionary> getDictionaries() {
        return dictionaries;
    }
    
    public Collection<Dictionary> getDictionaryCollection() {
        return dictionaries.values();
    }
    
    /**
     * Returns the value to which the specified key is mapped, or null if this 
     * map contains no mapping for the key.
     * 
     * @param name
     * @return the named dictionary or null if none found.
     */
    public Dictionary getDictionary(String name) {
        return dictionaries.get(name);
    }
    
}
