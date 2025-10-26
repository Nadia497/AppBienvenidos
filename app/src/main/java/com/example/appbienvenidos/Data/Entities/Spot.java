package com.example.appbienvenidos.Data.Entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

// @Entity indique que cette classe représente une table SQLite
// tableName est le nom réel de la table dans la base de données
@Entity(tableName = "Spot" ,
// Ici, on utilise @ForeignKey pour indiquer la relation avec la table users et categories.
// Cela aide Room à maintenir l'intégrité référentielle et à générer du code plus robuste.
        foreignKeys = {
                @ForeignKey(entity = User.class ,
                        parentColumns = "id",
                        childColumns = "Publisher_id",
                        onDelete = ForeignKey.CASCADE),//Si l'utilisateur est supprimé , ses spot st aussi supprimés

                @ForeignKey(entity = Category.class,
                        parentColumns = "id",
                        childColumns = "Category_id",
                        onDelete = ForeignKey.RESTRICT)//On peut pas
                //Supprimer une catégorie si des spots y sont liés
        }
)



public class Spot {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "Publisher_id")
    private int Publisher_id;

    @ColumnInfo(name = "Title")
    private String Title;

    @ColumnInfo(name = "Description")
    private String Description;

    @ColumnInfo(name = "Adress")
    private String Adress;

    @ColumnInfo(name = "Category_id")
    private int Category_id;

    @ColumnInfo(name = "Image_URL")
    private String Image_URL;

    @ColumnInfo(name = "Average_Rating")
    private String Average_Rating;

    @ColumnInfo(name = "Total_Rating")
    private String Total_Rating;

    @ColumnInfo(name = "Publication_Date")
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