package com.example.appbienvenidos.model;
import java.util.List;
public class Spot {

    private String id;

    private String Publisher_id;

    private String Title;

    private String Description;

    private String Adress;

    private String Category_id;

    private List<String> Image_URLs;

    private String Average_Rating;

    private String Total_Rating;

    private String Publication_Date;


    public Spot(String Publisher_id , String Title,
                String Description, String Adress ,
                String Category_id , List<String> Image_URLs ,
                String Average_Rating , String Total_Rating , String Publication_Date){
        this.Publisher_id = Publisher_id;
        this.Title = Title ;
        this.Description = Description ;
        this.Adress = Adress ;
        this.Category_id = Category_id ;
        this.Image_URLs = Image_URLs ;
        this.Average_Rating = Average_Rating;
        this.Total_Rating = Total_Rating;
        this.Publication_Date = Publication_Date ;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublisher_id() {
        return Publisher_id;
    }

    public void setPublisher_id(String publisher_id) {
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

    public String getCategory_id() {
        return Category_id;
    }

    public void setCategory_id(String category_id) {
        Category_id = category_id;
    }

    public List<String> getImage_URL() {
        return Image_URLs;
    }

    public void setImage_URL(List<String> image_URLs) {
        Image_URLs = image_URLs;
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
