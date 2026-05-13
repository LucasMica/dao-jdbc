package application;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Program2 {
    public static void main(String[] args) {
        DepartmentDao depDao = DaoFactory.createDepartmentDao();

        System.out.println("==== Test 1: department find by id ====");
        Department dep = depDao.findById(1);
        System.out.println("id = 1\n" + dep);

    }
}
