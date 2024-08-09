package oracle.todo.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import oracle.todo.model.Customer;
import oracle.todo.model.json.CustomerJSON;
import oracle.todo.util.PBKDF2Encoder;
import oracle.todo.util.TokenUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ApplicationScoped
public class CustomerService {
    @Inject
    PBKDF2Encoder encoder;
    @Inject
    TokenService tokenService;

    @Transactional
    public void saveCustomer(CustomerJSON customerJSON) throws Exception {
        Customer customer = new Customer();
        if (customerJSON.getUsername() != null) {
            Optional<Customer> optionalCustomer = Customer.find("username", customerJSON.getUsername()).firstResultOptional();
            if (optionalCustomer.isPresent()) throw new Exception("customer with this username already exists.");
        } else {
            throw new Exception("Username is a required field.");
        }

        customer.setUsername(customerJSON.getUsername());

        if (customerJSON.getPassword() == null || customerJSON.getPassword().isEmpty()) throw new Exception("Password is a required field.");
        customer.setPassword(encoder.encode(customerJSON.getPassword()));

        if (customerJSON.getMail() == null || customerJSON.getMail().isEmpty()) throw new Exception("Mail is a required field.");
        customer.setMail(customerJSON.getMail());

        if (customerJSON.getName() == null || customerJSON.getName().isEmpty()) throw new Exception("Name is a required field.");
        customer.setName(customerJSON.getName());

        customer.setRoles(Set.of(TokenUtils.ROLE_USER));

        customer.persist();
    }

    public CustomerJSON login(CustomerJSON customerJSON) throws Exception {
        Optional<Customer> customer = Customer.find("username", customerJSON.getUsername()).firstResultOptional();
        if (customer.isEmpty()) throw new Exception("Invalid credentials.");

        if (customerJSON.getUsername() == null || customerJSON.getUsername().isEmpty()) throw new Exception("Username is a required field.");
        if (customerJSON.getPassword() == null || customerJSON.getPassword().isEmpty()) throw new Exception("Password is a required field.");

        if (customer.get().getUsername().equals(customerJSON.getUsername()) && encoder.verify(customerJSON.getPassword(), customer.get().getPassword())) {
            CustomerJSON result = new CustomerJSON();

            result.setUsername(customer.get().getUsername());
            result.setName(customer.get().getName());
            customerJSON.setMail(customer.get().getMail());
            customerJSON.setName(customer.get().getName());
            result.setToken(tokenService.generate(customerJSON));
            result.setMail(customer.get().getMail());
            result.setId(customer.get().id);

            return result;
        } else {
            throw new Exception("Invalid credentials.");
        }
    }

    public List<CustomerJSON> getAllCustomers() {
        List<CustomerJSON> customerJSONS = new ArrayList<>();
        List<Customer> Customers = Customer.listAll();

        for (Customer c : Customers) {
            CustomerJSON customerJSON = new CustomerJSON();
            customerJSON.setUsername(c.getUsername());
            customerJSONS.add(customerJSON);
        }

        return customerJSONS;
    }

    public CustomerJSON getCustomer(Long id) throws Exception {
        Optional<Customer> customer = Customer.findByIdOptional(id);

        if (customer.isEmpty()) throw new Exception("Customer does not exist.");

        CustomerJSON customerJSON = new CustomerJSON();
        customerJSON.setId(customer.get().id);

        return customerJSON;
    }

}
