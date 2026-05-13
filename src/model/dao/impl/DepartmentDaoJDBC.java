package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoJDBC implements DepartmentDao {
    Connection conn = null;

    public DepartmentDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Department obj) {
        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement(
                    "Insert into department (Name) " +
                            "Values " +
                            "(?)", Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, obj.getName());

            int row = ps.executeUpdate();

            if (row > 0) {
                System.out.println(obj + "\nInsert operation completed");
            }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(ps);
        }
    }

    @Override
    public void update(Department obj) {
        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement(
                    "UPDATE department " +
                            "set name = ? " +
                            "where id = ?");

            ps.setString(1, obj.getName());
            ps.setInt(2, obj.getId());

            int row = ps.executeUpdate();

            if (row > 0) {
                System.out.println(this.findById(obj.getId()) + " was updated");
            }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(ps);
        }
    }

    @Override
    public void deleteById(Integer id) {
        
    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(
                    "Select department.* from department where department.id = ?");

            ps.setInt(1, id);

            rs = ps.executeQuery();

            if (rs.next()) {
                return instantiateDepartment(rs);
            }

            return null;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(ps);
            DB.closeResultSet(rs);
        }
    }

    public Department findBySeller(Seller s) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(
                    "Select department.* " +
                            "from department inner join seller " +
                            "on department.id = seller.DepartmentId " +
                            "where seller.id = ?");

            ps.setInt(1, s.getId());

            rs = ps.executeQuery();

            if (rs.next()) {
                return instantiateDepartment(rs);
            }

            return null;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(ps);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Department> findAll() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Department> l = new ArrayList<>();

        try {
            ps = conn.prepareStatement(
                    "Select department.* from department order by department.Name asc");

            rs = ps.executeQuery();

            while (rs.next()) {
                l.add(instantiateDepartment(rs));
            }

            return l;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(ps);
            DB.closeResultSet(rs);
        }
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();
        dep.setId(rs.getInt("Id"));
        dep.setName(rs.getString("Name"));
        return dep;
    }
}
