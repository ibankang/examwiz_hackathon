package examwizhackathon.ibankang.com.adminUi;

public class ExamDetails {
    private String examName;
    private String examType;
    private String examYear;
    private String examDate;
    private String examTime;

    public ExamDetails() {
    }

    public ExamDetails(String examName, String examType, String examYear, String examDate, String examTime) {
        this.examName = examName;
        this.examType = examType;
        this.examYear = examYear;
        this.examDate = examDate;
        this.examTime = examTime;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public String getExamYear() {
        return examYear;
    }

    public void setExamYear(String examYear) {
        this.examYear = examYear;
    }

    public String getExamDate() {
        return examDate;
    }

    public void setExamDate(String examDate) {
        this.examDate = examDate;
    }

    public String getExamTime() {
        return examTime;
    }

    public void setExamTime(String examTime) {
        this.examTime = examTime;
    }
}
