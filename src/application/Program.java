package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) throws ParseException {

        Scanner sc = new Scanner(System.in);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println("==== Test 1: seller find by id ====");
        Seller seller = sellerDao.findById(3);
        System.out.println(seller);

        System.out.println("==== Test 2: seller find by department ====");
        Department dep = new Department(2, null);
        List<Seller> sellers = sellerDao.findByDepartment(dep);
        sellers.forEach(System.out::println);

        System.out.println("==== Test 3: find all sellers ====");
        sellers = sellerDao.findAll();
        sellers.forEach(System.out::println);

        System.out.println("==== Test 4: insert seller ====");
        System.out.print("Do you want to do a new insert? (y/n) ");
        String c = sc.nextLine();


        Seller s = new Seller(null,
                "Lucas",
                "lucas@gmail.com",
                sdf.parse("30/08/2005"),
                2000.00, dep);

        if (Objects.equals(c, "y")) {
            sellerDao.insert(s);
        }


        System.out.println("==== Test 5: update seller ====");
        s = sellerDao.findById(9);
        s.setName("Rodrigão");
        //sellerDao.update(s);

        System.out.println("==== Test 6: delete seller ====");

        System.out.println("Declare an ID to delete from data base");
        int id = sc.nextInt();
        sc.nextLine();
        sellerDao.deleteById(id);


        sc.close();
    }


}
