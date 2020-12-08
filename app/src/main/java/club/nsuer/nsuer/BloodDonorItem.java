package club.nsuer.nsuer;

public class BloodDonorItem {

    private String name;
    private String memID;
    private String image;
    private String gender;
    private String bloodGroup;
    private String address;
    private String phone;

    public BloodDonorItem(String name, String memID, String image, String gender, String bloodGroup, String address, String phone) {
        this.name = name;
        this.memID = memID;
        this.image = image;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.address = address;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemID() {
        return memID;
    }

    public void setMemID(String memID) {
        this.memID = memID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
