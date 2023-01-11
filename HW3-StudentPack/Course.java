public class Course {
    private String name;
    private Integer capacity;


    public Course(String courseDefiner) {
        String[] tmp = courseDefiner.split(",");
        this.name = tmp[0];
        this.capacity = Integer.parseInt(tmp[1].trim());
    }

    public String getName() {
        return name;
    }

    public Integer getCapacity() {
        return capacity;
    }
}
