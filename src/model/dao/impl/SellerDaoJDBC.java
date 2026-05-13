package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

    private Connection conn;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller obj) {
        PreparedStatement ps = null;

        try {

            List<Seller> l = this.findAll();

            boolean hasSeller = l.contains(obj);

            if (!hasSeller) {

                ps = conn.prepareStatement(
                        "insert into seller (Name, Email, BirthDate, BaseSalary, DepartmentId) " +
                                "values " +
                                "(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

                ps.setString(1, obj.getName());
                ps.setString(2, obj.getEmail());
                ps.setDate(3, new java.sql.Date(obj.getBirthdate().getTime()));
                ps.setDouble(4, obj.getBaseSalary());
                ps.setInt(5, obj.getDepartment().getId());

                int rows = ps.executeUpdate();
                System.out.println("Update: " + rows + " insert successufull");

                if (rows > 0) {
                    ResultSet rs = ps.getGeneratedKeys();
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        obj.setId(id);
                    }
                    DB.closeResultSet(rs);
                }

            } else {
                System.out.println("Seller already exists in data bank!");
            }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(ps);
        }
    }

    @Override
    public void update(Seller obj) {
        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement(
                    "UPDATE seller SET " +
                            "Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " +
                            "WHERE id = ?", Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, obj.getName());
            ps.setString(2, obj.getEmail());
            ps.setDate(3, new java.sql.Date(obj.getBirthdate().getTime()));
            ps.setDouble(4, obj.getBaseSalary());
            ps.setInt(5, obj.getDepartment().getId());
            ps.setInt(6, obj.getId());

            ps.executeUpdate();

            Seller s = this.findById(obj.getId());

            System.out.println(s + "\nwas updated");

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(ps);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement ps = null;

        try {

            ps = conn.prepareStatement(
                    "DELETE FROM seller WHERE id = ?");

            ps.setInt(1,id);

            Seller s = this.findById(id);

            ps.executeUpdate();

            System.out.println( s + "\nwas deleted");
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(ps);
        }
    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(
                    "select seller.*, department.Name as DepName " +
                            "from seller INNER JOIN department " +
                            "on seller.DepartmentId = department.id " +
                            "where seller.Id = ?;"
            );

            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                Department dep = instantiateDepartment(rs);
                Seller obj = instantiateSeller(rs, dep);
                return obj;
            }

            return null;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(ps);
        }
    }

    @Override
    public List<Seller> findAll() {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(
                    "select seller.*, department.Name as DepName " +
                            "from seller INNER JOIN department " +
                            "on seller.DepartmentId = department.id " +
                            "Order by seller.Id"
            );

            rs = ps.executeQuery();

            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (rs.next()) {

                Department dep = map.get(rs.getInt("DepartmentId"));

                if (dep == null) {
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);
                }

                Seller obj = instantiateSeller(rs, dep);
                list.add(obj);
            }

            return list;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(ps);
        }
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(
                    "select seller.*, department.Name as DepName " +
                            "from seller INNER JOIN department " +
                            "on seller.DepartmentId = department.id " +
                            "where DepartmentId = ? " +
                            "Order by Name"
            );

            ps.setInt(1, department.getId());
            rs = ps.executeQuery();

            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (rs.next()) {

                Department dep = map.get(rs.getInt("DepartmentId"));

                if (dep == null) {
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);
                }

                Seller obj = instantiateSeller(rs, dep);
                list.add(obj);
            }

            return list;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(ps);
        }
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();
        dep.setId(rs.getInt("DepartmentId"));
        dep.setName(rs.getString("DepName"));
        return dep;
    }

    private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
        Seller obj = new Seller();

        obj.setId(rs.getInt("Id"));
        obj.setName(rs.getString("Name"));
        obj.setBirthdate(rs.getDate("BirthDate"));
        obj.setEmail(rs.getString("Email"));
        obj.setBaseSalary(rs.getDouble("BaseSalary"));
        obj.setDepartment(dep);

        return obj;
    }
}
