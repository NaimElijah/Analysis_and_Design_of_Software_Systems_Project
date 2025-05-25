package DataAccessLayer.TransportDAL;

import DataAccessLayer.TransportDAL.Interfaces.SiteDAO;
import Util.Database;

import java.sql.Connection;
import java.sql.SQLException;

public class JdbcSiteDAO implements SiteDAO {
    private Connection connection;
    public JdbcSiteDAO(Connection connection) throws SQLException { this.connection = Database.getConnection(); }


    //TODO
}
