package club.nsuer.nsuer;

public class Item {

    private String name;
    private String start;
    private String end;
    private String room;
    private String date;





    public Item(String n, String s, String e, String r, String d) {

        name = n;
        start = s;
        end = e;
        room = r;
        date = d;

    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {

        return name;
    }
    public String getStart() {

        return start;
    }
    public String getEnd() {

        return end;
    }
    public String getRoom() {

        return room;
    }

    public void setName(String name) {
        this.name = name;
    }
}