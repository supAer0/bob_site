package models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import play.data.validation.Constraints.*;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Attribute extends Model{
    @Id
    private UUID id;
    @Required
    private String name;
    @ManyToOne
    @JsonBackReference
    private GroupRoot groupRootAtr;
    @OneToMany(mappedBy = "attribute",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ProductValue> productValues;


    public static Finder<UUID, Attribute> find = new Finder<UUID, Attribute>(
            UUID.class, Attribute.class
    );
    public static Attribute find(UUID id) {
        return find.ref(id);
    }

    public static List<Attribute> all() {
        return find.all();
    }

    public static void delete(UUID id){
        find.ref(id).delete();
    }

    public Attribute(String name){
        this.name = name;
        productValues = new ArrayList<>();
        this.save();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GroupRoot getGroupRootAtr() {
        return groupRootAtr;
    }

    public void setGroupRootAtr(GroupRoot groupRootAtr) {
        this.groupRootAtr = groupRootAtr;
    }

    public UUID getId() {return id;}

    public List<ProductValue> getProductValues() {
        return productValues;
    }

    public void setProductValues(List<ProductValue> productValues) {
        this.productValues = productValues;
    }
    public void addProductValue(ProductValue productValue) {
        productValues.add(productValue);
    }


    public void deleteProductValue(ProductValue productValue){
        productValues.remove(productValue);
    }
}
