package com.example.appbienvenidos.model;

public class Spot {

    private int id;

    private int Publisher_id;

    private String Title;

    private String Description;

    private String Adress;

    private int Category_id;

    private String Image_URL;

    private String Average_Rating;

    private String Total_Rating;

    private String Publication_Date;


    public Spot(int Publisher_id , String Title,
                String Description, String Adress ,
                int Category_id , String Image_URL ,
                String Average_Rating , String Total_Rating , String Publication_Date){
        this.Publisher_id = Publisher_id;
        this.Title = Title ;
        this.Description = Description ;
        this.Adress = Adress ;
        this.Category_id = Category_id ;
        this.Image_URL = Image_URL ;
        this.Average_Rating = Average_Rating;
        this.Total_Rating = Total_Rating;
        this.Publication_Date = Publication_Date ;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPublisher_id() {
        return Publisher_id;
    }

    public void setPublisher_id(int publisher_id) {
        Publisher_id = publisher_id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getAdress() {
        return Adress;
    }

    public void setAdress(String adress) {
        Adress = adress;
    }

    public int getCategory_id() {
        return Category_id;
    }

    public void setCategory_id(int category_id) {
        Category_id = category_id;
    }

    public String getImage_URL() {
        return Image_URL;
    }

    public void setImage_URL(String image_URL) {
        Image_URL = image_URL;
    }

    public String getAverage_Rating() {
        return Average_Rating;
    }

    public void setAverage_Rating(String average_Rating) {
        Average_Rating = average_Rating;
    }

    public String getTotal_Rating() {
        return Total_Rating;
    }

    public void setTotal_Rating(String total_Rating) {
        Total_Rating = total_Rating;
    }

    public String getPublication_Date() {
        return Publication_Date;
    }

    public void setPublication_Date(String publication_Date) {
        Publication_Date = publication_Date;
    }
}
