package tw.edu.ncu.cc.NCUCalendar;

public class Categories {
    private Category[] categories;

    public Category[] getCategories(){return categories;}

    public void setCategories(Category[] categories){this.categories = categories;}

    public static class Category {
        private int id;
        private String name;
        private String calendar_id;
        private Boolean addible;

        public int getId(){return id;}

        public void setId(int id){this.id = id;}

        public String getName(){return name;}

        public void setName(String name){this.name = name;}

        public String getCalendar_id(){return calendar_id;}

        public void setCalendar_id(String calendar_id){this.calendar_id = calendar_id;}

        public Boolean getAddible(){return addible;}

        public void setAddible(Boolean addible){this.addible = addible;}
    }
}
