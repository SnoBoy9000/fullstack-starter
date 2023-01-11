package com.starter.fullstack.dao;

import com.starter.fullstack.api.Inventory;
import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Test Inventory DAO.
 */
@DataMongoTest
@RunWith(SpringRunner.class)
public class InventoryDAOTest {
  @ClassRule
  public static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

  @Resource
  private MongoTemplate mongoTemplate;
  private InventoryDAO inventoryDAO;
  private static final String NAME = "Amber";
  private static final String PRODUCT_TYPE = "hops";
  private static final String ID = "Id";

  @Before
  public void setup() {
    this.inventoryDAO = new InventoryDAO(this.mongoTemplate);
  }

  @After
  public void tearDown() {
    this.mongoTemplate.dropCollection(Inventory.class);
  }

  /**
   * Test Find All method.
   */
  @Test
  public void findAll() {
    Inventory inventory = new Inventory();
    inventory.setName(NAME);
    inventory.setProductType(PRODUCT_TYPE);
    this.mongoTemplate.save(inventory);
    List<Inventory> actualInventory = this.inventoryDAO.findAll();
    Assert.assertFalse(actualInventory.isEmpty());
  }

  // Test 'Create' DAO method
  @Test
  public void create() {
    Inventory inventory = new Inventory();
    inventory.setId(ID);
    inventory.setName(NAME);
    inventory.setProductType(PRODUCT_TYPE);

    Inventory createdInventory = this.inventoryDAO.create(inventory);

    String id = createdInventory.getId();
    String name = createdInventory.getName();
    String productType = createdInventory.getProductType();

    // Check createdInventory has a unique id and therefore an inventory was saved
    Assert.assertNotNull(id);
    /* Check that id, name, and product type of the save inventory is different 
    from the initial string values */ 
    Assert.assertNotSame(ID, id);
    Assert.assertSame(NAME, name);
    Assert.assertSame(PRODUCT_TYPE, productType);
  }

  // Test 'Delete' DAO Method 
  @Test
  public void delete() {
    Inventory inventory = new Inventory();
    String id = inventory.getId();
    Optional<Inventory> deletedInventory = this.inventoryDAO.delete(id);
    Assert.assertFalse(deletedInventory.isPresent());
  }
}
