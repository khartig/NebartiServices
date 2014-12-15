/*
 * 
 */
package com.idot.geocoding;

import geo.google.GeoAddressStandardizer;
import geo.google.GeoException;
import geo.google.datamodel.GeoCoordinate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author khartig
 */
public class Geocoder {
    private static String googleMapKey = "ABQIAAAAbvuxMMrAAZhSvAdxdv-QhxRWcphQbK8gdqpVLJsR6TQ-wd-D9hQK15C89IgQEy14GBFzbvWXb2mbuw";
    public static final Logger logger = Logger.getLogger(Geocoder.class.getName());

    public static String getLatLon(String location) {
        GeoCoordinate coordinate = null;
        
        try {
            GeoAddressStandardizer st = new GeoAddressStandardizer("googleMapKey");
            coordinate = st.standardizeToGeoCoordinate(location);           
        } catch (GeoException e) {
            logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        
        if (coordinate != null) {
            return coordinate.getLatitude() + ", " + coordinate.getLongitude();
        } else {
            return "0.0, 0.0";
        }
    }

    public static void main(String[] args) {
        logger.info(Geocoder.getLatLon("Boulder, CO"));
    }
}
