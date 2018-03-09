package app.src.main.comparator;

import java.util.Comparator;
import app.src.main.entity.Dealer;

/**
 * It's a simple comparator to know what Dealer is close to
 * the point given in the constructor
 */
public class DealerComparatorByDistance implements Comparator<Dealer>
{
    // current latitude used to compare
    private double latitude;
    // current longitude used to compare
    private double longitude;

    public DealerComparatorByDistance(double latitude, double longitude)
    {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * @param Dealer d1
     * @param Dealer d2
     *
     * @return int - if 1 then d1 is further than d2
     *               if -1 then d2 is further than d1
     *               if 0 then d1 is as far as d2
     */
    @Override
    public int compare(Dealer d1, Dealer d2)
    {
        double distance1 = distance(this.latitude, d1.getLatitude(), this.longitude, d1.getLongitude());
        double distance2 = distance(this.latitude, d2.getLatitude(), this.longitude, d2.getLongitude());

        if (distance1 > distance2) {
            return 1;
        } else if (distance1 < distance2) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * @param double lat1 - first latitude in degrees
     * @param double lat2 - second latitude in degrees
     * @param double lon1 - first longitude in degrees
     * @param double lon2 - second longitude in degrees
     *
     * @return double - distnace in km between two points - (lat, lon1) and (lat2, lon2)
     */
    private double distance(double lat1, double lat2, double lon1, double lon2) {
        final int R = 6371; // Radius of the earth in km

        // latitude and longitude to radians
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        // application of the Haversine formula
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // distance in km
        double distance = R * c;

        return distance;
    }
}
