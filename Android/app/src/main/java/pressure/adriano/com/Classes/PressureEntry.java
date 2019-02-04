package pressure.adriano.com.Classes;

import java.util.Date;

public class PressureEntry {

    private String id;
    private Integer systole;
    private Integer diastole;
    private Integer bpm;
    private Date createTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSystole() {
        return systole;
    }

    public void setSystole(Integer systole) {
        this.systole = systole;
    }

    public Integer getDiastole() {
        return diastole;
    }

    public void setDiastole(Integer diastole) {
        this.diastole = diastole;
    }

    public Integer getBpm() {
        return bpm;
    }

    public void setBpm(Integer bpm) {
        this.bpm = bpm;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public PressureEntry(String id, Integer systole, Integer diastole, Integer bpm, Date createTime){
        setId(id);
        setSystole(systole);
        setDiastole(diastole);
        setBpm(bpm);
        setCreateTime(createTime);
    }


}
