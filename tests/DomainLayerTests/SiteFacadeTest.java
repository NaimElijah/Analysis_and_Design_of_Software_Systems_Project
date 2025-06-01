package DomainLayerTests;
import DomainLayer.TransportDomain.SiteSubModule.SiteFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.AttributeNotFoundException;
import javax.management.openmbean.KeyAlreadyExistsException;
import javax.naming.ContextNotEmptyException;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class SiteFacadeTest {
    private SiteFacade siteFacade;

    @BeforeEach
    public void setup() throws SQLException {
        siteFacade = new SiteFacade();
        siteFacade.loadDBData();
    }

    @Test
    public void testAddShippingArea() throws KeyAlreadyExistsException, SQLException, AttributeNotFoundException, ContextNotEmptyException {
        siteFacade.addShippingArea(4, "Area 1");
        assertTrue(siteFacade.getSiteRepo().getShippingAreas().containsKey(1));
        siteFacade.deleteShippingArea(4);
    }

    @Test
    public void testAddShippingAreaDuplicate() throws AttributeNotFoundException, SQLException, ContextNotEmptyException {
        assertThrows(KeyAlreadyExistsException.class, () -> {
            siteFacade.addShippingArea(4, "Area 1");
            siteFacade.addShippingArea(4, "Area 1 Duplicate");
        });
        siteFacade.deleteShippingArea(4);
    }

    @Test
    public void testDeleteShippingArea() throws KeyAlreadyExistsException, AttributeNotFoundException, ContextNotEmptyException, SQLException {
        siteFacade.addShippingArea(4, "Area 1");
        siteFacade.deleteShippingArea(4);
        assertFalse(siteFacade.getSiteRepo().getShippingAreas().containsKey(4));
    }

    @Test
    public void testDeleteShippingAreaNotExist() {
        assertThrows(AttributeNotFoundException.class, () -> {
            siteFacade.deleteShippingArea(99);
        });
    }

    @Test
    public void testDeleteShippingAreaWithSites() throws KeyAlreadyExistsException, ClassNotFoundException, SQLException, AttributeNotFoundException, ContextNotEmptyException {
        siteFacade.addShippingArea(4, "Area 1");
        siteFacade.addSiteTOArea(4, "123 Street", "John", 1234567890L);
        assertThrows(ContextNotEmptyException.class, () -> {
            siteFacade.deleteShippingArea(4);
        });
        siteFacade.deleteSiteFromArea(4, "123 Street");
        siteFacade.deleteShippingArea(4);
    }

    //TODO:    from here refactor

    @Test
    public void testSetShippingAreaName() throws KeyAlreadyExistsException, ClassNotFoundException, SQLException {
        siteFacade.addShippingArea(1, "Area 1");
        siteFacade.setShippingAreaName(1, "Updated Area");
        assertEquals("Updated Area", siteFacade.getSiteRepo().getShippingAreas().get(1).getArea_name());
    }

    @Test
    public void testSetShippingAreaNameNonExist() {
        assertThrows(ClassNotFoundException.class, () -> {
            siteFacade.setShippingAreaName(99, "Nonexistent Area");
        });
    }

    @Test
    public void testAddSiteToArea() throws ClassNotFoundException, KeyAlreadyExistsException, SQLException {
        siteFacade.addShippingArea(1, "Area 1");
        siteFacade.addSiteTOArea(1, "123 Street", "John", 1234567890L);
        assertTrue(siteFacade.doesSiteExist(1, "123 Street"));
    }

    @Test
    public void testAddSiteToAreaNonExist() {
        assertThrows(ClassNotFoundException.class, () -> {
            siteFacade.addSiteTOArea(99, "123 Street", "John", 1234567890L);
        });
    }

    @Test
    public void testAddSiteToAreaDuplicate() throws ClassNotFoundException, KeyAlreadyExistsException, SQLException {
        siteFacade.addShippingArea(1, "Area 1");
        siteFacade.addSiteTOArea(1, "123 Street", "John", 1234567890L);
        assertThrows(KeyAlreadyExistsException.class, () -> {
            siteFacade.addSiteTOArea(1, "123 Street", "Jane", 9876543210L);
        });
    }

    @Test
    public void testDeleteSiteFromArea() throws ClassNotFoundException, SQLException {
        siteFacade.addShippingArea(1, "Area 1");
        siteFacade.addSiteTOArea(1, "123 Street", "John", 1234567890L);
        siteFacade.deleteSiteFromArea(1, "123 Street");
        assertFalse(siteFacade.doesSiteExist(1, "123 Street"));
    }

    @Test
    public void testDeleteSiteFromAreaNonExist() {
        assertThrows(ClassNotFoundException.class, () -> {
            siteFacade.deleteSiteFromArea(1, "Nonexistent Street");
        });
    }

    @Test
    public void testSetSiteAddress() throws ClassNotFoundException, KeyAlreadyExistsException, SQLException {
        siteFacade.addShippingArea(1, "Area 1");
        siteFacade.addSiteTOArea(1, "123 Street", "John", 1234567890L);
        siteFacade.setSiteAddress(1, "123 Street", "456 Avenue");
        assertTrue(siteFacade.doesSiteExist(1, "456 Avenue"));
    }

    @Test
    public void testSetSiteAddressDuplicate() throws ClassNotFoundException, KeyAlreadyExistsException, SQLException {
        siteFacade.addShippingArea(1, "Area 1");
        siteFacade.addSiteTOArea(1, "123 Street", "John", 1234567890L);
        siteFacade.addSiteTOArea(1, "456 Avenue", "Jane", 9876543210L);
        assertThrows(KeyAlreadyExistsException.class, () -> {
            siteFacade.setSiteAddress(1, "123 Street", "456 Avenue");
        });
    }

    @Test
    public void testSetSiteAreaNum() throws ClassNotFoundException, KeyAlreadyExistsException, SQLException {
        siteFacade.addShippingArea(1, "Area 1");
        siteFacade.addShippingArea(2, "Area 2");
        siteFacade.addSiteTOArea(1, "123 Street", "John", 1234567890L);
        siteFacade.setSiteAreaNum(1, 2, "123 Street");
        assertTrue(siteFacade.doesSiteExist(2, "123 Street"));
        assertFalse(siteFacade.doesSiteExist(1, "123 Street"));
    }

    @Test
    public void testSetSiteAreaNumNonExist() {
        assertThrows(ClassNotFoundException.class, () -> {
            siteFacade.setSiteAreaNum(99, 2, "123 Street");
        });
    }

    @Test
    public void testSetSiteContName() throws ClassNotFoundException, SQLException {
        siteFacade.addShippingArea(1, "Area 1");
        siteFacade.addSiteTOArea(1, "123 Street", "John", 1234567890L);
        siteFacade.setSiteContName(1, "123 Street", "Jane");
        assertEquals("Jane", siteFacade.getSiteRepo().getShippingAreas().get(1).getSites().get("123 Street").getcName());
    }

    @Test
    public void testSetSiteContNum() throws ClassNotFoundException, SQLException {
        siteFacade.addShippingArea(1, "Area 1");
        siteFacade.addSiteTOArea(1, "123 Street", "John", 1234567890L);
        siteFacade.setSiteContNum(1, "123 Street", 9876543210L);
        assertEquals(9876543210L, siteFacade.getSiteRepo().getShippingAreas().get(1).getSites().get("123 Street").getcNumber());
    }

    @Test
    public void testShowAllSites() throws ClassNotFoundException, SQLException {
        siteFacade.addShippingArea(1, "Area 1");
        siteFacade.addSiteTOArea(1, "123 Street", "John", 1234567890L);
        String result = siteFacade.showAllSites();
        assertTrue(result.contains("123 Street"));
    }

    @Test
    public void testShowAllShippingAreas() throws SQLException {
        siteFacade.addShippingArea(1, "Area 1");
        String result = siteFacade.showAllShippingAreas();
        assertTrue(result.contains("Area 1"));
    }
}

