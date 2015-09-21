package tw.edu.ncu.cc.NCUCalendar;

public class Event {
    private String id;
    private String created_at;
    private String updated_at;
    private String summary;
    private String description;
    private String location;
    private String category;
    private String start;
    private String end;
    private String link;

    public String getId(){return id;}

    public void setId(String id){this.id = id;}

    public String getCreated_at(){return created_at;}

    public void setCreated_at(String created_at){this.created_at = created_at;}

    public String getUpdated_at(){return updated_at;}

    public void setUpdated_at(String updated_at){this.updated_at = updated_at;}

    public String getSummary(){return summary;}

    public void setSummary(String summary){this.summary = summary;}

    public String getDescription(){return description;}

    public void setDescription(String description){this.description = description;}

    public String getLocation(){return location;}

    public void setLocation(String location){this.location = location;}

    public String getCategory(){return category;}

    public void setCategory(String category){this.category = category;}

    public String getStart(){return start;}

    public void setStart(String start){this.start = start;}

    public String getEnd(){return end;}

    public void setEnd(String end){this.end = end;}

    public String getLink(){return link;}

    public void setLink(String link){this.link = link;}
}
