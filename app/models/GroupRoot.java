package models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.w3c.dom.Attr;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.*;

@Entity
public class GroupRoot extends Model {
    @Id
    private UUID id;
    private String name;
    @OneToMany(mappedBy = "groupRootAtr",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Attribute> attributes;
    @OneToMany(mappedBy = "groupRoot",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<GroupProduct> groupProducts;
    private String transliterationName;

    public GroupRoot(String name){
        this.name = name;
        this.transliterationName = StaticMethods.transliteration(name);
        attributes = new ArrayList<Attribute>();
        groupProducts = new ArrayList<GroupProduct>();
        this.save();
    }

    public static Finder<UUID, GroupRoot> find = new Finder<UUID, GroupRoot>(
            UUID.class, GroupRoot.class
    );
    public static GroupRoot find(UUID id) {
        return find.ref(id);
    }
    public static GroupRoot findById(UUID id) {
        List<GroupRoot> a = all();
        for (GroupRoot g:a) {
            if(g.getId().equals(id))
                return g;
        }
        return null;
    }
    public static GroupRoot findByTransliterationName(String transliterationName){
        List<GroupRoot> all = all();
        for (GroupRoot g: all) {
            if (g.transliterationName.equals(transliterationName)){
                return g;
            }
        }
        return null;
    }
    public void setName(String name) {
        this.name = name;
        this.transliterationName = StaticMethods.transliteration(name);
    }
    public String getName() {
        return name;
    }
    public static List<GroupRoot> all() {
        return find.all();
    }
    public UUID getId() {return id;}
    /*public void setId(UUID id) {
        this.id = id;
    }*/
    public static void delete(UUID id){
        find.ref(id).delete();
    }

    public List<GroupProduct> getGroupProducts() {
        return groupProducts;
    }

    public void setGroupProducts(List<GroupProduct> groupProducts) {
        this.groupProducts = groupProducts;
    }

    public void addGroupProducts(GroupProduct group){
        groupProducts.add(group);
    }
    public void deleteGroupProducts(GroupProduct group){
        groupProducts.remove(group);
    }
    public void addAttribute(Attribute attribute){attributes.add(attribute);}
    public void deleteAttribute(Attribute attribute){attributes.remove(attribute);}

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }
    public List<Attribute> getAttributes(){return attributes;}

    public String getTransliterationName() {
        return transliterationName;
    }
}

