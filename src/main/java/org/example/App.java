package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class App {
    public static void main( String[] args ) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("OrdersDataBase");
        EntityManager em = emf.createEntityManager();

        try{
            em.getTransaction().begin();

            //New Client
           Client client = findOrCreateClient(em, "Ihor", "ihor123@example.com", "456789123");
           Client client1 = findOrCreateClient(em, "Mary", "mary3455554@example.com", "123456789");

           //New Product
            Product product = findOrCreateProduct(em,"Apple red",3.50);
            Product product1 = findOrCreateProduct(em, "Pear", 1.70);

            //New Order
            Order order = findOrCreateOrder(em,"Order1" , client, product );
            Order order1 = findOrCreateOrder(em,"Order2" , client, product1 );
            Order order2 = findOrCreateOrder(em,"Order3" , client1, product1 );

            client.getOrders().add(order);
            client.getOrders().add(order1);
            client.getOrders().add(order2);

            em.getTransaction().commit();
            System.out.println("Success saved: Client, Product, Order");

        }catch (Exception e){
            em.getTransaction().rollback();
            e.printStackTrace();
        }finally {
            em.close();
            emf.close();
        }
    }
    /*
     Checks if a user with the given email already exists in the database.
     If found, returns the existing user
     If not found, creates a new user and inserts it into the database.
    * */
    public static Client findOrCreateClient(EntityManager em,String name, String email,String phone) {
        List<Client> clients = em.createQuery("SELECT c FROM Client  c WHERE c.email = :email", Client.class)
                .setParameter("email", email)
                .getResultList();
        Client client = null;
        if(!clients.isEmpty()){
            client = clients.get(0);
        }
        if (client == null) {
            client = new Client();
            client.setName(name);
            client.setEmail(email);
            client.setPhone(phone);
            em.persist(client);

            System.out.println("Success saved: Client, " + name + ", " + email + ", " + phone);

        }else {
            System.out.println("Client already exists");
        }
        return client;
    }
    public static   Product findOrCreateProduct(EntityManager em,String name, Double price) {
        List<Product> products = em.createQuery("SELECT p FROM Product p WHERE p.name = :name AND p.price = :price", Product.class)
                .setParameter("name", name)
                .setParameter("price", price)
                .getResultList();
        Product product = null;
        if(!products.isEmpty()){
            product = products.get(0);
        }
        if (product == null) {
            product = new Product();
            product.setName(name);
            product.setPrice(price);
            em.persist(product);
            System.out.println("Success saved: Product, " + name + ", " + price);
        }else {
            System.out.println("Product already exists" + name);
        }
        return product;
    }
    public static   Order findOrCreateOrder(EntityManager em,String order_name,Client client,Product product ) {
        List<Order> orders = em.createQuery("SELECT o FROM Order o WHERE o.order = :order_name AND o.client = :client AND o.product = :product", Order.class)
                .setParameter("order_name", order_name)
                .setParameter("client", client)
                .setParameter("product", product)
                .getResultList();
        Order order = null;
        if(!orders.isEmpty()){
            order = orders.get(0);
        }
        if (order == null) {
            order = new Order();
            order.setOrder(order_name);
            order.setClient(client);
            order.setProduct(product);
            em.persist(order);

            System.out.println("Success saved: Order, " + order_name + ", " + client + ", " + product);

        }else {
            System.out.println("Order already exists" + order_name);
        }
        return order;
    }
}
