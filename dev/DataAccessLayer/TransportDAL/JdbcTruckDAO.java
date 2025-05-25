package DataAccessLayer.TransportDAL;

import DataAccessLayer.TransportDAL.Interfaces.TruckDAO;
import Util.Database;

import java.sql.Connection;
import java.sql.SQLException;

public class JdbcTruckDAO implements TruckDAO {
    private Connection connection;
    public JdbcTruckDAO(Connection connection) throws SQLException { this.connection = Database.getConnection(); }


    //TODO
}
