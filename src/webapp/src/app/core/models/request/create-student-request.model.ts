export class CreateStudentRequest {
    public studentId: string;
    public firstName: string;
    public lastName: string;
    public cin: string;
    public group: string;
    public email: string;
    public classId: string;

    constructor(studentId: string, firstName: string, lastName: string, cin: string, classId: string, email?: string, group?: string) {
        this.studentId = studentId;
        this.cin = cin;
        this.firstName = firstName;
        this.lastName = lastName;
        this.group = group;
        this.email = email;
        this.classId = classId;
    }
}