package club.nsuer.nsuer;

public class BloodRequestItem {

    private int id;
    private String memID;
    private String name;
    private long postedDate;
    private int bloodGroup;
    private String bags;
    private long whenDate;
    private String phone;
    private String address;
    private String note;
    private boolean managed;



    public BloodRequestItem(int id, String memID, String name, long postedDate, int bloodGroup, String bags, long whenDate, String phone, String address, String note, boolean managed) {
        this.id = id;
        this.memID = memID;
        this.name = name;
        this.postedDate = postedDate;
        this.bloodGroup = bloodGroup;
        this.bags = bags;
        this.whenDate = whenDate;
        this.phone = phone;
        this.address = address;
        this.note = note;
        this.managed = managed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getMemID() {
        return memID;
    }

    public void setMemID(String memID) {
        this.memID = memID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(long postedDate) {
        this.postedDate = postedDate;
    }

    public int getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(int bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getBags() {
        return bags;
    }

    public void setBags(String bags) {
        this.bags = bags;
    }

    public long getWhenDate() {
        return whenDate;
    }

    public void setWhenDate(long whenDate) {
        this.whenDate = whenDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isManaged() {
        return managed;
    }

    public void setManaged(boolean managed) {
        this.managed = managed;
    }
}
