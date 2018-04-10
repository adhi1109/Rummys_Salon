package application.adhiraj.testscroll;

/**
 * Created by Adhiraj on 30/05/17.
 */

public class AppointmentObj {
    private String mName;
    private String mDate;
    private String mTime;
    private String[] mTechnician;
    private String[] mService;
    private String mOffers;

    public AppointmentObj(String mName, String mDate, String mTime, String[] mTechnician, String[] mService, String mOffers) {
        this.mName = mName;
        this.mDate = mDate;
        this.mTime = mTime;
        this.mTechnician = mTechnician;
        this.mService = mService;
        this.mOffers = mOffers;
    }

    public String getmName() {
        return mName;
    }

    public String getmOffers() {
        return mOffers;
    }

    public String[] getmService() {
        return mService;
    }

    public String[] getmTechnician() {
        return mTechnician;
    }

    public String getmDate() {
        return mDate;
    }

    public String getmTime() {
        return mTime;
    }
}
