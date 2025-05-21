package DomainLayerTests;
import DomainLayer.TransportDomain.SiteSubModule.SiteFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.AttributeNotFoundException;
import javax.management.openmbean.KeyAlreadyExistsException;
import javax.naming.ContextNotEmptyException;

import static org.junit.jupiter.api.Assertions.*;

class SiteFacadeTest {
    private SiteFacade siteFacade;

    @BeforeEach
    public void setup() {
        siteFacade = new SiteFacade();
    }

    @Test
    public void testAddShippingArea() throws KeyAlreadyExistsException {
        siteFacade.addShippingArea(1, "Area 1");
        assertTrue(siteFacade.getShippingAreas().containsKey(1));
    }

    @Test
    public void testAddShippingAreaDuplicate() {
        assertThrows(KeyAlreadyExistsException.class, () -> {
            siteFacade.addShippingArea(1, "Area 1");
            siteFacade.addShippingArea(1, "Area 1 Duplicate");
        });
    }

    @Test
    public void testDeleteShippingArea() throws KeyAlreadyExistsException, AttributeNotFoundException, ContextNotEmptyException {
        siteFacade.addShippingArea(1, "Area 1");
        siteFacade.deleteShippingArea(1);
        assertFalse(siteFacade.getShippingAreas().containsKey(1));
    }

    @Test
    public void testDeleteShippingAreaNotExist() {
        assertThrows(AttributeNotFoundException.class, () -> {
            siteFacade.deleteShippingArea(99);
        });
    }

    @Test
    public void testDeleteShippingAreaWithSites() throws KeyAlreadyExistsException, ClassNotFoundException {
        siteFacade.addShippingArea(1, "Area 1");
        siteFacade.addSiteTOArea(1, "123 Street", "John", 1234567890L);
        assertThrows(ContextNotEmptyException.class, () -> {
            siteFacade.deleteShippingArea(1);
        });
    }

    @Test
    public void testSetShippingAreaName() throws KeyAlreadyExistsException, ClassNotFoundException {
        siteFacade.addShippingArea(1, "Area 1");
        siteFacade.setShippingAreaName(1, "Updated Area");
        assertEquals("Updated Area", siteFacade.getShippingAreas().get(1).getArea_name());
    }

    @Test
    public void testSetShippingAreaNameNonExist() {
        assertThrows(ClassNotFoundException.class, () -> {
            siteFacade.setShippingAreaName(99, "Nonexistent Area");
        });
    }

    @Test
    public void testAddSiteToArea() throws ClassNotFoundException, KeyAlreadyExistsException {
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
    public void testAddSiteToAreaDuplicate() throws ClassNotFoundException, KeyAlreadyExistsException {
        siteFacade.addShippingArea(1, "Area 1");
        siteFacade.addSiteTOArea(1, "123 Street", "John", 1234567890L);
        assertThrows(KeyAlreadyExistsException.class, () -> {
            siteFacade.addSiteTOArea(1, "123 Street", "Jane", 9876543210L);
        });
    }

    @Test
    public void testDeleteSiteFromArea() throws ClassNotFoundException {
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
    public void testSetSiteAddress() throws ClassNotFoundException, KeyAlreadyExistsException {
        siteFacade.addShippingArea(1, "Area 1");
        siteFacade.addSiteTOArea(1, "123 Street", "John", 1234567890L);
        siteFacade.setSiteAddress(1, "123 Street", "456 Avenue");
        assertTrue(siteFacade.doesSiteExist(1, "456 Avenue"));
    }

    @Test
    public void testSetSiteAddressDuplicate() throws ClassNotFoundException, KeyAlreadyExistsException {
        siteFacade.addShippingArea(1, "Area 1");
        siteFacade.addSiteTOArea(1, "123 Street", "John", 1234567890L);
        siteFacade.addSiteTOArea(1, "456 Avenue", "Jane", 9876543210L);
        assertThrows(KeyAlreadyExistsException.class, () -> {
            siteFacade.setSiteAddress(1, "123 Street", "456 Avenue");
        });
    }

    @Test
    public void testSetSiteAreaNum() throws ClassNotFoundException, KeyAlreadyExistsException {
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
    public void testSetSiteContName() throws ClassNotFoundException {
        siteFacade.addShippingArea(1, "Area 1");
        siteFacade.addSiteTOArea(1, "123 Street", "John", 1234567890L);
        siteFacade.setSiteContName(1, "123 Street", "Jane");
        assertEquals("Jane", siteFacade.getShippingAreas().get(1).getSites().get("123 Street").getcName());
    }

    @Test
    public void testSetSiteContNum() throws ClassNotFoundException {
        siteFacade.addShippingArea(1, "Area 1");
        siteFacade.addSiteTOArea(1, "123 Street", "John", 1234567890L);
        siteFacade.setSiteContNum(1, "123 Street", 9876543210L);
        assertEquals(9876543210L, siteFacade.getShippingAreas().get(1).getSites().get("123 Street").getcNumber());
    }

    @Test
    public void testShowAllSites() throws ClassNotFoundException {
        siteFacade.addShippingArea(1, "Area 1");
        siteFacade.addSiteTOArea(1, "123 Street", "John", 1234567890L);
        String result = siteFacade.showAllSites();
        assertTrue(result.contains("123 Street"));
    }

    @Test
    public void testShowAllShippingAreas() {
        siteFacade.addShippingArea(1, "Area 1");
        String result = siteFacade.showAllShippingAreas();
        assertTrue(result.contains("Area 1"));
    }
}

