package models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import scala.util.parsing.combinator.testing.Str;

import javax.persistence.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class  Product extends Model{
    @Id
    private UUID id;
    @Required(message = "Введите пароль")
    private String name;
    private String transliterationName;
    private String description;
    @Required(message = "Введите стоимость")
    private double cost;
    @Required(message = "Введите количество")
    private double amount;
    private String image;
    @ManyToOne
    @JsonBackReference
    private GroupProduct groupProduct;
    // приязать к каталогу(группе)
    @OneToMany(mappedBy = "product",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ProductValue> productValuesList;

    public static Finder<UUID, Product> find = new Finder<UUID, Product>(
            UUID.class, Product.class
    );
    public static Product find(UUID id) {
        return find.ref(id);
    }

    public Product(String name){
        this.name = name;
        this.transliterationName = StaticMethods.transliteration(name);
        this.save();
    }
    public static List<Product> productsByGroup(GroupProduct groupProduct){
        List<Product> a = all();
        List<Product> res = new ArrayList<>();
        for (Product p:a) {
            if(p.getGroupProduct() == groupProduct)
                res.add(p);
        }
        return res;
    }

    public static Product findById(UUID id) {
        List<Product> all = all();
        for (Product p:all) {
            if(p.getId().equals(id))
                return p;
        }
        return null;
    }
    public void setGroupProduct(GroupProduct groupProduct){
        this.groupProduct =groupProduct;
    }
    public GroupProduct getGroupProduct(){
        return groupProduct;
    };
    public void deleteGroup(GroupProduct groupProduct){
        this.groupProduct.delete();
    };

    public static List<Product> all() {
        return find.all();
    }

    public static void delete(UUID id){
        find.ref(id).delete();
    }

    public double getAmount() {
        return amount;
    }

    public double getCost() {
        return cost;
    }

    public String getImage() {
        return image;
    }

    public UUID getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
        this.transliterationName = StaticMethods.transliteration(name);
    }

    public static Product findByTransliterationName(String transliterationName, GroupProduct groupProduct){
        List<Product> all = all();
        for (Product p: all) {
            if (p.transliterationName.equals(transliterationName) && ((p.getGroupProduct().getId()).equals(groupProduct.getId()))){
                return p;
            }
        }
        return null;
    }

    public List<ProductValue> getProductValuesList() {
        return productValuesList;
    }

    public void setProductValuesList(List<ProductValue> productValuesList) {
        this.productValuesList = productValuesList;
    }
    public void addProductValue(ProductValue productValue){
        productValuesList.add(productValue);
    }
    public void deleteProductValue(ProductValue productValue){
        productValuesList.remove(productValue);
    }

    public String getTransliterationName() {
        return transliterationName;
    }

    public void setTransliterationName(String transliterationName) {
        this.transliterationName = transliterationName;
    }
}
