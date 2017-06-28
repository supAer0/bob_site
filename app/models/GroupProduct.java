package models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class GroupProduct extends Model {
    @Id
    private UUID id;
    private String name;
    private String transliterationName;
    @OneToMany(mappedBy = "groupProduct",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonBackReference
    private List<Product> products;
    @ManyToOne
    @JsonBackReference
    private GroupRoot groupRoot;

    public GroupProduct(String name){
        this.name = name;
        this.transliterationName = StaticMethods.transliteration(name);
        products = new ArrayList<Product>();
        this.save();
    }

    public static Finder<UUID, GroupProduct> find = new Finder<UUID, GroupProduct>(
            UUID.class, GroupProduct.class
    );
    public static GroupProduct find(UUID id) {
        return find.ref(id);
    }
    public void setName(String name) {
        this.name = name;
        this.transliterationName = StaticMethods.transliteration(name);
    }
    public static GroupProduct findById(UUID id) {
        List<GroupProduct> a = all();
        for (GroupProduct g:a) {
            if(g.getId().equals(id))
                return g;
        }
        return null;
    }
    public static GroupProduct findByTransliterationName(String transliterationName, GroupRoot groupRoot){
        List<GroupProduct> all = all();
        for (GroupProduct g: all) {
            if (g.transliterationName.equals(transliterationName) && ((g.getGroupRoot().getId()).equals(groupRoot.getId()))){
                return g;
            }
        }
        return null;
    }
    public String getName() {
        return name;
    }
    public void addProduct(Product product){
        products.add(product);
    }
    public void deleteProduct(Product product){
        products.remove(product);
    }
    public List<Product> allProduct(){
        return products;
    }
    public List<Product> getProducts(){
        return products;
    }
    public static List<GroupProduct> all() {
        return find.all();
    }
//    public static List<GroupProduct> groupsProductByRoot(GroupRoot groupRoot){
//        List<GroupProduct> a = all();
//        List<GroupProduct> res = new ArrayList<>();
//        for (GroupProduct g: a) {
//            if (g.getGroupRoot().equals(groupRoot))
//                res.add(g);
//        }
//        return res;
//    }
    public UUID getId() {return id;}
    public static void delete(UUID id){
        find.ref(id).delete();
    }

    public GroupRoot getGroupRoot() {
        return groupRoot;
    }

    public void setGroupRoot(GroupRoot groupRoot) {
        this.groupRoot = groupRoot;
    }

    public String getTransliterationName() {
        return transliterationName;
    }
}
