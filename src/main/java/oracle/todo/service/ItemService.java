package oracle.todo.service;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import oracle.todo.model.Category;
import oracle.todo.model.Customer;
import oracle.todo.model.Item;
import oracle.todo.model.json.ItemJSON;

import java.text.SimpleDateFormat;
import java.util.*;

@ApplicationScoped
public class ItemService {
    public List<ItemJSON> getAllItems() {
        List<ItemJSON> itemJSONS = new ArrayList<>();

        List<Item> items = Item.listAll();

        for (Item i : items) {
            ItemJSON itemJSON = new ItemJSON();
            itemJSON.setId(i.id);
            itemJSON.setName(i.getName());
            itemJSON.setDate(i.getDate_until().toString());
            itemJSON.setCustomerId(i.getCustomer().id);
            itemJSON.setCategoryId(i.getCategory().id);
            itemJSON.setStatus(i.getStatus());

            itemJSONS.add(itemJSON);
        }

        return itemJSONS;
    }

    public ItemJSON getItem(Long id) throws Exception {
        Optional<Item> optionalItem = Item.findByIdOptional(id);

        if (optionalItem.isEmpty()) throw new Exception("Item does not exist.");

        Item item = optionalItem.get();

        ItemJSON result = new ItemJSON();
        result.setId(id);
        result.setName(item.getName());
        result.setDate(item.getDate_until().toString());
        result.setCustomerId(item.getCustomer().id);
        result.setCategoryId(item.getCategory().id);
        result.setStatus(item.getStatus());

        return result;
    }

    public List<ItemJSON> getItemsByCustomerId(Long id) {
        List<ItemJSON> itemJSONS = new ArrayList<>();
        PanacheQuery<Item> pqi = Item.find("customer.id", id);
        List<Item> li = pqi.list();

        for (Item i : li) {
            ItemJSON itemJSON = new ItemJSON();
            itemJSON.setId(i.id);
            itemJSON.setName(i.getName());
            itemJSON.setCustomerId(i.getCustomer().id);
            itemJSON.setCategoryId(i.getCategory().id);
            itemJSON.setDate(i.getDate_until().toString());
            itemJSON.setStatus(i.getStatus());
            itemJSONS.add(itemJSON);
        }

        return itemJSONS;
    }

    @Transactional
    public void saveItem(ItemJSON itemJSON) throws Exception {
        Item item = new Item();

        if (itemJSON.getName() == null || itemJSON.getName().isEmpty()) throw new Exception("Name is a required field.");
        item.setName(itemJSON.getName());

        if (itemJSON.getDate() == null || itemJSON.getDate().isEmpty()) throw new Exception("Date is a required field.");
        if (!itemJSON.getDate().matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$")) throw new Exception("Date must be in format yyyy-MM-dd");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = formatter.parse(itemJSON.getDate());
        item.setDate_until(date);

        if (itemJSON.getCategoryId() == null || itemJSON.getCategoryId() == 0L) throw new Exception("Category is a required field.");
        Optional<Category> optionalCategory = Category.findByIdOptional(itemJSON.getCategoryId());
        if (optionalCategory.isEmpty()) throw new Exception("Category does not exist.");
        item.setCategory(optionalCategory.get());

        if (itemJSON.getCustomerId() == null || itemJSON.getCustomerId() == 0L) throw new Exception("Customer is a required field.");
        Optional<Customer> optionalCustomer = Customer.findByIdOptional(itemJSON.getCustomerId());
        if (optionalCustomer.isEmpty()) throw new Exception("Customer does not exist.");
        item.setCustomer(optionalCustomer.get());

        item.setStatus("new");

        item.persist();
    }

    @Transactional
    public void updateItem(ItemJSON itemJSON) throws Exception {
        Optional<Item> optionalItem = Item.findByIdOptional(itemJSON.getId());
        if (optionalItem.isEmpty()) throw new Exception("Item does not exist.");
        Item item = optionalItem.get();

        if (itemJSON.getName() == null || itemJSON.getName().isEmpty()) throw new Exception("Name is a required field.");
        item.setName(itemJSON.getName());

        if (itemJSON.getDate() == null || itemJSON.getDate().isEmpty()) throw new Exception("Date is a required field.");
        if (!itemJSON.getDate().matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$")) throw new Exception("Date must be in format yyyy-MM-dd");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = formatter.parse(itemJSON.getDate());
        item.setDate_until(date);

        if (itemJSON.getCategoryId() == null || itemJSON.getCategoryId() == 0L) throw new Exception("Category is a required field.");
        Optional<Category> optionalCategory = Category.findByIdOptional(itemJSON.getCategoryId());
        if (optionalCategory.isEmpty()) throw new Exception("Category does not exist.");
        item.setCategory(optionalCategory.get());

        if (itemJSON.getCustomerId() == null || itemJSON.getCustomerId() == 0L) throw new Exception("Customer is a required field.");
        Optional<Customer> optionalCustomer = Customer.findByIdOptional(itemJSON.getCustomerId());
        if (optionalCustomer.isEmpty()) throw new Exception("Customer does not exist.");
        item.setCustomer(optionalCustomer.get());

        item.persist();
    }

    @Transactional
    public void updateItemStatus(ItemJSON itemJSON) throws Exception {
        Optional<Item> optionalItem = Item.findByIdOptional(itemJSON.getId());
        if (optionalItem.isEmpty()) throw new Exception("Item does not exist.");
        Item item = optionalItem.get();

        if (item.getStatus().equals("new")) item.setStatus("in progress");
        else if (item.getStatus().equals("in progress")) item.setStatus("done");

        item.persist();
    }

    @Transactional
    public void deleteItem(Long id) throws Exception {
        Optional<Item> optionalItem = Item.findByIdOptional(id);
        if (optionalItem.isEmpty()) throw new Exception("Item does not exist.");
        Item.deleteById(id);
    }
}
