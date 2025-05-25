package airport.model;

/**
 *
 * @author edangulo
 */
public class Location {

    private final String airportId;
    private String airportName;
    private String airportCity;
    private String airportCountry;
    private double airportLatitude;
    private double airportLongitude;
    
    public Location(String airportId, String airportName, String airportCity, String airportCountry, double airportLatitude, double airportLongitude) {
        this.airportId = airportId;
        this.airportName = airportName;
        this.airportCity = airportCity;
        this.airportCountry = airportCountry;
        this.airportLatitude = airportLatitude;
        this.airportLongitude = airportLongitude;
    }

    public Location(Location other) {
        this.airportId = other.airportId;
        this.airportName = other.airportName;
        this.airportCity = other.airportCity;
        this.airportCountry = other.airportCountry;
        this.airportLatitude = other.airportLatitude;
        this.airportLongitude = other.airportLongitude;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }

    public void setAirportCity(String airportCity) {
        this.airportCity = airportCity;
    }

    public void setAirportCountry(String airportCountry) {
        this.airportCountry = airportCountry;
    }

    public void setAirportLatitude(double airportLatitude) {
        this.airportLatitude = airportLatitude;
    }

    public void setAirportLongitude(double airportLongitude) {
        this.airportLongitude = airportLongitude;
    }

    public String getAirportId() {
        return airportId;
    }

    public String getAirportName() {
        return airportName;
    }

    public String getAirportCity() {
        return airportCity;
    }

    public String getAirportCountry() {
        return airportCountry;
    }

    public double getAirportLatitude() {
        return airportLatitude;
    }

    public double getAirportLongitude() {
        return airportLongitude;
    }

    @Override
    public String toString() {
        return airportId + " - " + airportName + ", " + airportCity + " (" + airportCountry + ")";
    }
}
