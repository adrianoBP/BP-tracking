package pressure.adriano.com;

import java.text.DateFormat;
import java.util.Date;

public class PressureEntry {

    private String id;
    private String systole;
    private String diastole;
    private Date createTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSystole() {
        return systole;
    }

    public void setSystole(String systole) {
        this.systole = systole;
    }

    public String getDiastole() {
        return diastole;
    }

    public void setDiastole(String diastole) {
        this.diastole = diastole;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public PressureEntry(String id, String systole, String diastole, Date createTime){
        setId(id);
        setSystole(systole);
        setDiastole(diastole);
        setCreateTime(createTime);
    }


}
