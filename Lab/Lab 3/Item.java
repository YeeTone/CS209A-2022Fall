package lab3;

public class Item {
    private String description;
    private int partNumber;

    public Item(String desc, int pnumber) {
        description = desc;
        partNumber = pnumber;
    }

    public String toString() {
        return "[no=" + partNumber + ", desc=" + description + "]";
    }

    public int getPartNumber() {
        return partNumber;
    }

    public String getDescription() {
        return description;
    }
}