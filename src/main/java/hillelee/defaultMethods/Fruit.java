package hillelee.defaultMethods;

/**
 * Created by User on 28.10.2017.
 */
public interface Fruit {
    String getColor();

    Integer getWeight();

    default Double approximateVitaminC() {
        if (getColor().equals("GREEN")) {
            return getWeight() * 0.0001;
        } else {
            return getWeight() * 0.00005;
        }
    }
}
