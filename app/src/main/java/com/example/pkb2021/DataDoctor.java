package com.example.pkb2021;

public class DataDoctor {
    private String doctorName, speciality, about, profile_pic, pic;

    DataDoctor(String doctorName, String speciality, String about, String profile_pic, String pic) {
        this.doctorName = doctorName;
        this.speciality = speciality;
        this.about = about;
        this.profile_pic = profile_pic;
        this.pic = pic;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getSpeciality() {
        return speciality;
    }

    public String getAbout() {
        return about;
    }

    public String getprofilePic() {
        return profile_pic;
    }

    public String getPic() { return pic;}

}