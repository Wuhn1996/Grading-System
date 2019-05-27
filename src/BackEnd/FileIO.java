package BackEnd;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by wuhn on 2019/4/14.
 */

/*
Bugs:
1. Note.time String --> Date error
2. Cell.score is raw data (deduction or percentage type), should be processed
*/

public class FileIO {
    /*
    Each file's name is content type with an additional filename from high level class

    There're 2 methods for each data type, write/read
    In writeData method, firstly we put metadata of each instance into a JSONObject,
    then put these instances into the other JSONObject and finally write it to file.

    In readData method, on the contrary, we decode the total JSONObject above all,
    then read instances iteratively and construct an arraylist as output.
    */

    private String filePath = "./file";

    public FileIO(){
        File folderFile = new File("file");
        folderFile.mkdirs();
    }

    public void writeCriteria(Criteria cri, String filename){
        //Input an instance of Criteria class, write it to JSON file
        JSONObject obj1 = new JSONObject();
        obj1.put("Weights",cri.getWeight());

        try(FileWriter fw1 = new FileWriter(filePath+"\\"+filename+"Criteria.json")){
            fw1.write(obj1.toJSONString());
            fw1.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Criteria readCriteria(String filename){
        //Input the additional JSON file name, output a list of Criteria instances, whose data are read
        //from JSON file name
        JSONParser parser1 = new JSONParser();
        ArrayList<Double> listWeights = new ArrayList<Double>();
        try (FileReader reader = new FileReader(filePath+"\\"+filename+"Criteria.json")){
            //Read JSON file
            Object obj = parser1.parse(reader);
            JSONObject readCriteria = (JSONObject) obj;
            listWeights = (ArrayList<Double>) readCriteria.get("Weights");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Criteria(listWeights);
    }

    public void writeExtraCredit(Extra_credit extraCredit, String filename){
        //Input an instance of Extra_credit class, write it to JSON file
        JSONObject obj1 = new JSONObject();
        ArrayList<String> tempNull = new ArrayList<String>();
        if(extraCredit.getExtra_credits() == null){
            tempNull.add("NULL");
            obj1.put("ExtraCredits",tempNull);
        } else {
            obj1.put("ExtraCredits",extraCredit.getExtra_credits());
        }

        try(FileWriter fw1 = new FileWriter(filePath+"\\"+filename+"ExtraCredit.json")){
            fw1.write(obj1.toJSONString());
            fw1.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Extra_credit readExtraCredit(String filename){
        //Input the additional JSON file name, output a list of Criteria instances, whose data are read
        //from JSON file name
        JSONParser parser1 = new JSONParser();
        ArrayList<Double> listExtraCredits = new ArrayList<Double>();
        Extra_credit res = new Extra_credit(null);
        try (FileReader reader = new FileReader(filePath+"\\"+filename+"ExtraCredit.json")){
            //Read JSON file
            Object obj = parser1.parse(reader);
            JSONObject readExtra = (JSONObject) obj;
            ArrayList<String> tempStr = (ArrayList<String>)readExtra.get("ExtraCredits");
            if(tempStr.get(0) instanceof  String){
                res = new Extra_credit(null);
            } else {
                listExtraCredits = (ArrayList<Double>) readExtra.get("ExtraCredits");
                res = new Extra_credit(listExtraCredits);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

    public void writeStudentInfo(ArrayList<Student> listStu, String filename){
        JSONObject out1 = new JSONObject();
        int count1 = 0;

        for(Student stu : listStu){
            JSONObject obj1 = new JSONObject();
            obj1.put("firstName",stu.getFirstName());
            obj1.put("secondName",stu.getMiddleInitial());
            obj1.put("thirdName",stu.getLastName());
            obj1.put("studentId",stu.getStudentId());
            obj1.put("emailAddress",stu.getEmailAddress());
            obj1.put("isRemovedAfterExam",stu.isRemovedAfterExam());
            if(stu instanceof Undergraduate){
                obj1.put("Type","U");
            } else if (stu instanceof Graduate){
                obj1.put("Type","G");
            }
            out1.put("jOut"+Integer.toString(count1),obj1);

            count1 = count1 + 1;
        }

        try(FileWriter fw1 = new FileWriter(filePath+"\\"+filename+"Student.json")){
            fw1.write(out1.toJSONString());
            fw1.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Student> readStudentInfo(String filename){
        JSONParser parser1 = new JSONParser();
        ArrayList<Student> listStudent = new ArrayList<Student>();
        String fName;
        String sName;
        String tName;
        String email;
        String id;
        boolean removedAfterExam;
        try (FileReader reader = new FileReader(filePath+"\\"+filename+"Student.json"))
        {
            //Read JSON file
            Object obj = parser1.parse(reader);
            JSONObject readStudent = (JSONObject) obj;

            Iterator ite = readStudent.keySet().iterator();
            int countRead = 0;
            int i = 0;
            while(ite.hasNext()){
                countRead = countRead + 1;
                ite.next();
            }
            while(i < countRead){
                JSONObject tempRead = (JSONObject) readStudent.get("jOut"+Integer.toString(i));
                fName = (String) tempRead.get("firstName");
                sName = (String) tempRead.get("secondName");
                tName = (String) tempRead.get("thirdName");
                id = (String) tempRead.get("studentId");
                email = (String) tempRead.get("emailAddress");
                removedAfterExam = (boolean) tempRead.get("isRemovedAfterExam");
                String stuType = (String) tempRead.get("Type");
                if (stuType.equals("U")){
                    Undergraduate s = new Undergraduate(fName,sName,tName,id,email,removedAfterExam);
                    listStudent.add(s);
                } else if (stuType.equals("G")){
                    Graduate s = new Graduate(fName,sName,tName,id,email,removedAfterExam);
                    listStudent.add(s);
                } else {
                }
                i = i + 1;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return listStudent;
    }

    public void writeAssignment(ArrayList<Assignment> listAssign, String filename){
        JSONObject out1 = new JSONObject();
        int count1 = 0;

        for( Assignment assign : listAssign){
            JSONObject obj1 = new JSONObject();
            obj1.put("name",assign.getName());
            obj1.put("total",assign.getTotal());
            obj1.put("scoring_method",assign.getScoring_method());
            if(assign instanceof Exam){
                obj1.put("Type","Exam");
            } else {
                obj1.put("Type","Others");
            }

            out1.put("jOut"+Integer.toString(count1),obj1);

            count1 = count1 + 1;
        }

        try(FileWriter fw1 = new FileWriter(filePath+"\\"+filename+"Assignment.json")){
            fw1.write(out1.toJSONString());
            fw1.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Assignment> readAssignment(String filename){
        JSONParser parser1 = new JSONParser();
        ArrayList<Assignment> listAssign = new ArrayList<Assignment>();
        String name;
        double total;
        String scoring_method;
        try (FileReader reader = new FileReader(filePath+"\\"+filename+"Assignment.json"))
        {
            //Read JSON file
            Object obj = parser1.parse(reader);
            JSONObject readAssign = (JSONObject) obj;

            Iterator ite = readAssign.keySet().iterator();
            int countRead = 0;
            int i = 0;
            while(ite.hasNext()){
                countRead = countRead + 1;
                ite.next();
            }
            while(i < countRead){
                JSONObject tempRead = (JSONObject) readAssign.get("jOut"+Integer.toString(i));
                name = (String) tempRead.get("name");
                total = (double)tempRead.get("total");
                scoring_method = (String) tempRead.get("scoring_method");
                String assignType = (String) tempRead.get("Type");
                if(assignType.equals("Exam")){
                    Exam assign = new Exam(name, total, scoring_method);
                    listAssign.add(assign);
                } else if(assignType.equals("Others")){
                    Assignment assign = new Assignment(name, total, scoring_method);
                    listAssign.add(assign);
                } else {
                }
                i = i + 1;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return listAssign;
    }

    public void writeCell(ArrayList<ArrayList<Cell>> listCell, String filename){
        //JSONObject out1 = new JSONObject();
        JSONObject out2 = new JSONObject();
        int count1 = 0;
        int count2 = 0;

        for(ArrayList<Cell> listC : listCell){
            JSONObject out1 = new JSONObject();
            count2 = 0;
            for(Cell c : listC){
                JSONObject obj1 = new JSONObject();
                obj1.put("noteInfo",c.getNote().getInformation());
                //obj1.put("noteDate",c.getNote().getTime());
                obj1.put("noteDate",c.getNote().getTime());//fix it later with real time
                obj1.put("score",c.getScore());

                out1.put("jOut1"+Integer.toString(count2),obj1);
                count2 = count2 + 1;
            }
            out2.put("jOut2"+Integer.toString(count1),out1);
            count1 = count1 + 1;
        }
        try(FileWriter fw1 = new FileWriter(filePath+"\\"+filename+"Cell.json")){
            fw1.write(out2.toJSONString());
            fw1.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ArrayList<Cell>> readCell(String filename){
        JSONParser parser1 = new JSONParser();
        //ArrayList<Cell> listCell = new ArrayList<Cell>();
        ArrayList<ArrayList<Cell>> matrixCell = new ArrayList<ArrayList<Cell>>();
        String noteInfo;
        String noteDate;
        double score;

        try (FileReader reader = new FileReader(filePath+"\\"+filename+"Cell.json")){
            //Read JSON file
            Object obj = parser1.parse(reader);
            JSONObject readCellMatrix = (JSONObject) obj;
            Iterator ite = readCellMatrix.keySet().iterator();
            int countRead = 0;
            int i = 0;
            while(ite.hasNext()){
                countRead = countRead + 1;
                ite.next();
            }
            while(i < countRead){
                JSONObject readCellRow = (JSONObject) readCellMatrix.get("jOut2"+Integer.toString(i));
                Iterator iteRow = readCellRow.keySet().iterator();
                ArrayList<Cell> listCell = new ArrayList<Cell>();
                //trybegin
                int countRead2 = 0;
                int j = 0;
                while(iteRow.hasNext()){
                    countRead2 = countRead2 + 1;
                    iteRow.next();
                }
                while(j < countRead2){
                    JSONObject tempRead = (JSONObject) readCellRow.get("jOut1"+Integer.toString(j));
                    noteInfo = (String) tempRead.get("noteInfo");
                    noteDate = (String) tempRead.get("noteDate");
                    score = (double) tempRead.get("score");
                    Note tempNote = new Note(noteInfo, noteDate);
                    Cell tempCell = new Cell(tempNote, score);
                    listCell.add(tempCell);
                    j = j + 1;
                }
                //tryend
//                while(iteRow.hasNext()){
//                    String sKeyRow = (String) iteRow.next();
//                    JSONObject tempRead = (JSONObject) readCellRow.get(sKeyRow);
//                    noteInfo = (String) tempRead.get("noteInfo");
//                    noteDate = (String) tempRead.get("noteDate");
//                    /*
//                    try{DateFormat dateFmt = new SimpleDateFormat("dd/MM/yyyy");
//                            noteDate = dateFmt.parse(strDate);}
//                    catch (java.text.ParseException e){
//                        e.printStackTrace();
//                    }
//                    */
//                    //score = Double.valueOf((String)tempRead.get("score"));
//                    score = (double) tempRead.get("score");
//                    Note tempNote = new Note(noteInfo, noteDate);
//                    Cell tempCell = new Cell(tempNote, score);
//                    listCell.add(tempCell);
//                }
                matrixCell.add(listCell);
                i = i + 1;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return matrixCell;
    }

    public void writeCourse(ArrayList<Course> listCourse){
        JSONObject out1 = new JSONObject();
        int count1 = 0;
        //testtest

        for( Course course : listCourse){
            JSONObject obj1 = new JSONObject();
            obj1.put("courseName",course.getCourseName());
            obj1.put("lecturerName",course.getLecturerName());
            obj1.put("semester",course.getSemester());
            obj1.put("end",course.isEnd());
            obj1.put("show_total",course.isShow_Total());
            //testtest
            //write cell matrix instead of sheet
            writeCell(course.getSheet().getAllCell(), course.getCourseName()+course.getSemester());
            writeStudentInfo(course.getStudents(), course.getCourseName()+course.getSemester());
            writeAssignment(course.getAssignments(), course.getCourseName()+course.getSemester());
            writeCriteria(course.getCriteria_UG(), course.getCourseName()+course.getSemester()+"UG");
            writeCriteria(course.getCriteria_G(), course.getCourseName()+course.getSemester()+"G");
            writeExtraCredit(course.getExtra_credits(),course.getCourseName()+course.getSemester());

            out1.put("jOut"+Integer.toString(count1),obj1);

            count1 = count1 + 1;
        }

        try(FileWriter fw1 = new FileWriter(filePath+"\\"+"CourseList.json")){
            fw1.write(out1.toJSONString());
            fw1.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Course> readCourse(){
        JSONParser parser1 = new JSONParser();
        ArrayList<Course> listCourse = new ArrayList<Course>();
        String courseName;
        String lecturerName;
        String semester;
        Sheet sheet;
        ArrayList<Student> students;
        ArrayList<Assignment> assignments;
        boolean end;
        boolean show_total;
        Extra_credit extra_credit;
        //ArrayList<Criteria> criteria;
        Criteria criUG;
        Criteria criG;
        try (FileReader reader = new FileReader(filePath+"\\"+"CourseList.json")){
            //Read JSON file
            Object obj = parser1.parse(reader);
            JSONObject readCourse = (JSONObject) obj;
            Iterator ite = readCourse.keySet().iterator();
            int countRead = 0;
            int i = 0;
            while(ite.hasNext()){
                countRead = countRead + 1;
                ite.next();
            }
            while(i < countRead){
                JSONObject tempRead = (JSONObject) readCourse.get("jOut"+Integer.toString(i));
                courseName = (String) tempRead.get("courseName");
                lecturerName = (String) tempRead.get("lecturerName");
                semester = (String) tempRead.get("semester");
                end = (boolean) tempRead.get("end");
                show_total = (boolean) tempRead.get("show_total");
                extra_credit = readExtraCredit(courseName+semester);
                //read cell matrix and use it to construct a sheet
                sheet = new Sheet(readCell(courseName+semester));
                students = readStudentInfo(courseName+semester);
                assignments = readAssignment(courseName+semester);
                criUG = readCriteria(courseName+semester+"UG");
                criG = readCriteria(courseName+semester+"G");
                Course course = new Course(courseName,lecturerName,semester,sheet,students,assignments,criUG,criG,end,extra_credit,show_total);
                listCourse.add(course);
                i = i + 1;
            }
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            try(FileWriter fw1 = new FileWriter(filePath+"\\"+"CourseList.json")){
                //fw1.write(out1.toJSONString());
                fw1.flush();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return listCourse;
    }

    public void writeTempAddAssign(ArrayList<Assignment> tempAssignList, String filename){
        JSONObject out1 = new JSONObject();
        int count1 = 0;

        for( Assignment assign : tempAssignList){
            JSONObject obj1 = new JSONObject();
            obj1.put("tempName",assign.getName());
            obj1.put("tempTotal",assign.getTotal());
            obj1.put("tempScoring_method",assign.getScoring_method());
            if(assign instanceof Exam){
                obj1.put("Type","Exam");
            } else {
                obj1.put("Type","Others");
            }
            out1.put("jOut"+Integer.toString(count1),obj1);

            count1 = count1 + 1;
        }

        try(FileWriter fw1 = new FileWriter(filePath+"\\"+filename+"TempAddAssign.json")){
            fw1.write(out1.toJSONString());
            fw1.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Assignment> readTempAddAssign(String filename){
        JSONParser parser1 = new JSONParser();
        ArrayList<Assignment> listAssign = new ArrayList<Assignment>();
        String name;
        double total;
        String scoring_method;
        try (FileReader reader = new FileReader(filePath+"\\"+filename+"TempAddAssign.json"))
        {
            //Read JSON file
            Object obj = parser1.parse(reader);
            JSONObject readAssign = (JSONObject) obj;

            Iterator ite = readAssign.keySet().iterator();
            int countRead = 0;
            int i = 0;
            while(ite.hasNext()){
                countRead = countRead + 1;
                ite.next();
            }
            while(i < countRead){
                JSONObject tempRead = (JSONObject) readAssign.get("jOut"+Integer.toString(i));
                name = (String) tempRead.get("tempName");
                total = (double)tempRead.get("tempTotal");
                scoring_method = (String) tempRead.get("tempScoring_method");
                String assignType = (String) tempRead.get("Type");
                if(assignType.equals("Exam")){
                    Exam assign = new Exam(name, total, scoring_method);
                    listAssign.add(assign);
                } else if(assignType.equals("Others")){
                    Assignment assign = new Assignment(name, total, scoring_method);
                    listAssign.add(assign);
                } else {
                }
                i = i + 1;
            }

        } catch (FileNotFoundException e) {
            try(FileWriter fw1 = new FileWriter(filename+"TempAddAssign.json")){
                //JSONObject out1 = new JSONObject();
                //ArrayList<Assignment> tempNullAssign = new ArrayList<Assignment>();
                //out1.put("jOut"+Integer.toString(0),obj1);
                //fw1.write(out1.toJSONString());
                fw1.flush();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            //e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return listAssign;
    }

    public void deleteClassFile(String filename){
        try{
            File file = new File(filePath+"\\"+filename+"GCriteria.json");
            file.delete();
            file = new File(filePath+"\\"+filename+"UGCriteria.json");
            file.delete();
            file = new File(filePath+"\\"+filename+"ExtraCredit.json");
            file.delete();
            file = new File(filePath+"\\"+filename+"Student.json");
            file.delete();
            file = new File(filePath+"\\"+filename+"Assignment.json");
            file.delete();
            file = new File(filePath+"\\"+filename+"Cell.json");
            file.delete();
            file = new File(filePath+"\\"+filename+"TempAddAssign.json");
            file.delete();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
