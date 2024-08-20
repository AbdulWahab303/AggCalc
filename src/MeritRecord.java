public class MeritRecord {
    private String department;
    private Double merit;

    public MeritRecord(String department, Double merit) {
        this.department = department;
        this.merit = merit;
    }

    public String getDepartment() {
        return department;
    }

    public Double getMerit() {
        return merit;
    }
}
