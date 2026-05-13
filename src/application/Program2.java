package application;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.ArrayList;
import java.util.List;

public class Program2 {
    public static void main(String[] args) {
        DepartmentDao depDao = DaoFactory.createDepartmentDao();
        SellerDao selDao = DaoFactory.createSellerDao();

        System.out.println("==== Test 1: department find by id ====");
        Department dep = depDao.findById(1);
        System.out.println("id = 1\n" + dep);

        System.out.println("==== Test 2: department find by seller ====");
        Seller s = new Seller();
        s.setId(3);
        dep = depDao.findBySeller(s);

        System.out.println("Department localized: "
                + dep
                + " from seller: "
                + selDao.findById(s.getId())
        );

        System.out.println("==== Test 3: find all departments ====");
        List<Department> l = depDao.findAll();
        l.forEach(System.out::println);

        System.out.println("==== Test 4: Insert a department ====");
        dep.setName("Clothes");
        dep.setId(null);
        //depDao.insert(dep);

        System.out.println("==== Test 5: update a department ====");
        dep.setName("Tablets");
        dep.setId(7);
        //depDao.update(dep);

        System.out.println("==== Test 6: delete a department ====");
        //depDao.deleteById(6);
    }
}
