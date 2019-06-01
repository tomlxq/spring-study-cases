package com.example.repository;


import com.example.domain.Student;
import com.example.domain.Teacher;
import com.example.domain.TypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;




@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class PersonRepositoryTest {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;
    @Test
    public void testPerson() throws Exception {
        try {
            //新增数据操作
            insert();
            //查找数据操作分页数据
            findByNameAndAge();
            //删除数据
            delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //新增数据
    public void insert(){
        Student student = new Student();

        student.setPersonId(20180101L);
        student.setType(TypeEnum.STUDENT);
        student.setAge(22);
        student.setAddress("广东省深圳市福田区");
        student.setName("张三");
        student.setLikeBook("java");
        student.setLikeSport("羽毛球");
        student.setSchool("深圳大学");

        Teacher teacher = new Teacher();
        teacher.setPersonId(20180101L);
        teacher.setType(TypeEnum.TEACHER);
        teacher.setAge(32);
        teacher.setAddress("广东省深圳市福田区");
        teacher.setName("张三丰");
        teacher.setTeacherCourse("JavaEE");
        teacher.setLikeStudent("张三");
        for(int i = 0 ;i < 15 ; i++){
            student.setAge(student.getAge()-i/2);
            student.setPersonId(student.getPersonId()+i);
            studentRepository.save(student);
            student.setAge(teacher.getAge()+i/2);
            teacher.setPersonId(teacher.getPersonId()+i);
            teacherRepository.save(teacher);
        }

        LOGGER.info("************************");

        LOGGER.info("新增学生的数据数量为：{}",studentRepository.findAll().size());
        LOGGER.info("新增老师的数据数量为：{}",teacherRepository.findAll().size());
    }

    public void findByNameAndAge(){
        Pageable pageable = PageRequest.of(0,5);
        //获取分页数据，每页5条数，取第0页的数据，
        Page<Student> studentPage = studentRepository.findByNameAndAge2("张三",20,pageable);
        Page<Student> studentPage1 = studentRepository.findByAge2("张三",20,pageable);
        for(Student student : studentPage){
            LOGGER.info("+++++++++++++:{}",student);
        }
        for(Student student : studentPage1){
            LOGGER.info("-------------:{}",student);
        }


        Pageable pageable1 = PageRequest.of(1,3);
        //获取分页数据，每页3条数，取第1页的数据，
        Page<Teacher> teacherPage =teacherRepository.findByNameAndAge2("张三丰",30,pageable1);
        Page<Teacher> teacherPage1 =teacherRepository.findByAge2("张三丰",30,pageable1);

        for(Teacher teacher : teacherPage){
            LOGGER.info("+++++++++++++:{}",teacher);
        }
        for(Teacher teacher : teacherPage1){
            LOGGER.info("-------------:{}",teacher);
        }
    }

    public void delete(){
        try {
            studentRepository.deleteByPersonId(20180101L);
            LOGGER.info("学生ID为20180101的数据删除成功");
            LOGGER.info("删除后学生数据数量为：{}",studentRepository.findAll().size());

            teacherRepository.deleteByPersonId(20180101L);
            LOGGER.info("教师ID为20180101的数据删除成功");
            LOGGER.info("删除后教师数据数量为：{}",teacherRepository.findAll().size());

        }catch (Exception e){
            LOGGER.error("数据删除失败{}",e);
        }

    }

}
