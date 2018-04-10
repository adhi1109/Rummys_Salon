package application.adhiraj.testscroll;

/**
 * Created by Adhiraj on 30/05/17.
 */

public class StaffProfile {
    private String mName;
    private String mDescription;
    private String mNumber;
    private float mRating;
    private int mPhoto;

    public StaffProfile (String name, String description, String number, float rating, int photo){
        mName = name;
        mDescription = description;
        mNumber = number;
        mRating = rating;
        mPhoto = photo;
    }

    public String getName (){
        return mName;
    }

    public String getDescription (){
        return mDescription;
    }

    public String getNumber (){
        return mNumber;
    }

    public float getRating (){
        return mRating;
    }

    public int getPhoto (){
        return mPhoto;
    }
}
