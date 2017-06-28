package models;
import com.fasterxml.jackson.annotation.JsonBackReference;
import org.w3c.dom.Attr;
import play.db.ebean.Model;
import scala.util.parsing.combinator.testing.Str;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class ProductValue extends Model {
    private UUID id;
    @ManyToOne
    @JsonBackReference
    private Attribute attribute;
    private String name;
    private String value;
    @ManyToOne
    @JsonBackReference
    private Product product;

    public static Finder<UUID, ProductValue> find = new Finder<UUID, ProductValue>(
            UUID.class, ProductValue.class
    );
    public static ProductValue find(UUID id) {
        return find.ref(id);
    }

    public static List<ProductValue> all() {
        return find.all();
    }

    public static void delete(UUID id){
        find.ref(id).delete();
    }

    public ProductValue(Attribute attribute, Product product){
        //this.attribute = attribute;
        this.name = attribute.getName();
        this.product = product;
        this.save();
    }


    public UUID getId() {
        return id;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
