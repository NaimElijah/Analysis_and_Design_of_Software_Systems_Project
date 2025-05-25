package DataAccessLayer.TransportDAL;

import DataAccessLayer.TransportDAL.Interfaces.TransportDAO;
import Util.Database;

import java.sql.Connection;
import java.sql.SQLException;

public class JdbcTransportDAO implements TransportDAO {
    private Connection connection;
    public JdbcTransportDAO(Connection connection) throws SQLException { this.connection = Database.getConnection(); }

    // Transports
    //TODO


    // ItemsDocs
    //TODO


    //ItemQs
    
    //TODO

}
