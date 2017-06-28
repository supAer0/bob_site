package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.*;
import play.Logger;
import play.Routes;
import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import util.Secured;

import java.util.List;
import java.util.UUID;

import static play.libs.Json.toJson;

@Security.Authenticated(Secured.class)
public class Admin extends Controller {
    static Form<Product> productForm = Form.form(Product.class);
    static Form<GroupRoot> groupRootForm = Form.form(GroupRoot.class);
    static Form<GroupProduct> groupProductForm = Form.form(GroupProduct.class);
    static Form<ProductValue> productValuesForm = Form.form(ProductValue.class);
    public static Result index() {
        return redirect(routes.Admin.dashboars());
    }

    public static Result dashboars() {
        return ok(
                views.html.admin.index.render()
        );
    }

    public static Result products() {
        return ok(
                views.html.admin.products.render(productForm)
        );
    }

    public static Result groups() {
        return ok(
                views.html.admin.groups.render(groupRootForm)
        );
    }

    public static Result viewGroup(String transliterationName){
        // TODO: g - NPE
        GroupRoot g = GroupRoot.findByTransliterationName(transliterationName);
        return ok(
                views.html.admin.viewgroup.render(groupProductForm,g)
        );
    }
    public static Result viewProduct(String transliterationNameOfRoot,String transliterationNameOfGroup){
        // TODO: g - NPE
        GroupRoot gr = GroupRoot.findByTransliterationName(transliterationNameOfRoot);
        GroupProduct g = GroupProduct.findByTransliterationName(transliterationNameOfGroup,gr);
        return ok(
                views.html.admin.viewProduct.render(productForm,gr,g)
        );
    }
    public static Result viewProductValues(String transliterationNameOfRoot,String transliterationNameOfGroup,String transliterationNameOfProduct){
        // TODO: g - NPE
        GroupRoot gr = GroupRoot.findByTransliterationName(transliterationNameOfRoot);
        GroupProduct g = GroupProduct.findByTransliterationName(transliterationNameOfGroup,gr);
        Product p = Product.findByTransliterationName(transliterationNameOfProduct,g);
        return ok(
                views.html.admin.viewProductValues.render(productValuesForm,p)
        );
    }

    private static Result errorJsonResult(String errorMessage) {
        return badRequest(errorJson(errorMessage));
    }

    private static JsonNode errorJson(String errorMessage) {
        return Json.newObject().put("error", errorMessage);
    }

    public static Result productsJson() {
        String testtest= (String) request().queryString().keySet().toArray()[0];
        String test =  testtest.split("}")[0].split(",")[1].split(":")[1];
        String testId = test.substring(1,test.length()-1);
        UUID tetete = UUID.fromString(testId);
        GroupProduct g = GroupProduct.findById(tetete);
        //List<Product> p = Product.productsByGroup(g);
        return ok(toJson(g.getProducts()));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result deleteProductJson() {
        play.Logger.info("deleteProductJson()");
        JsonNode json = request().body().asJson();
        if (json == null) {
            return errorJsonResult("Json expected");
        } else {
            UUID id = null;
            try {
                if (json.findPath("id").asText() != "null") {
                    id = UUID.fromString(json.findPath("id").asText());
                }
            } catch (IllegalArgumentException nfe) {
                return errorJsonResult("wrong type id");
            } finally {
//                    if (id == null) {
//                        play.Logger.info(json.findPath("id").asText().toString());
//                        return errorJsonResult("id must be specified");
//                    }
            }
            assert(id!=null);

            Product product = Product.find(id);
            play.Logger.debug(product.toString());
            if (product == null) {
                return notFound(errorJson("product is not found"));
            }
            JsonNode result = Json.toJson(product);
            product.delete(id);
            return ok(result);
        }
    }
    @BodyParser.Of(BodyParser.Json.class)
    public static Result saveProductJson() {
        play.Logger.info("saveProductJson");
        JsonNode json = request().body().asJson();
        if (json == null) {
            return errorJsonResult("JSON expected");
        } else {
            UUID id = null;
            try {
                if (json.findPath("id").asText() != "null") {
                    id = UUID.fromString(json.findPath("id").asText());
                }
            } catch (IllegalArgumentException nfe) {
                return errorJsonResult("wrong type id");
            } finally {
//                    if (id == null) {
//                        play.Logger.info(json.findPath("id").asText().toString());
//                        return errorJsonResult("id must be specified");
//                    }
            }
            Product product = null;
            if (id != null) {
                product = Product.find(id);
            }
            boolean itsNew = false;
            if (product == null) {
                if (json.findPath("name").asText().length()>2){
                    product = new Product(json.findPath("name").asText());
                    itsNew = true;
                }else{
                    return errorJsonResult("name must be longer then 2 symbols");
                }
            }
            product.setName(json.findPath("name").asText());
            product.setAmount(json.findPath("amount").asDouble());
            product.setCost(json.findPath("cost").asDouble());
            product.setDescription(json.findPath("description").asText());
            UUID groupId = null;
            try {
                groupId = UUID.fromString(json.findPath("groupProductId").asText());
            }catch (IllegalArgumentException nfe) {

            }
            GroupProduct groupProduct = null;
            if (groupId!=null){
                groupProduct = GroupProduct.find(groupId);
                if (groupProduct != null){
                    product.setGroupProduct(groupProduct);
                }
            }
            play.Logger.info("trying to save to DB");
            product.save();
            groupProduct.addProduct(product);
            groupProduct.save();
            if (itsNew){
                List<Attribute> listAttributes = groupProduct.getGroupRoot().getAttributes();
                for (Attribute a:listAttributes) {
                    ProductValue productValue = new ProductValue(a,product);
                    productValue.save();
                }
            }
            return ok(Json.toJson(product));
        }

    }
    //----GROUP-----
    public static Result groupsJson() {
        List<GroupRoot> all = GroupRoot.all();
        return ok(toJson(all));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result deleteGroupJson() {
        play.Logger.info("deleteGroupJson()");
        JsonNode json = request().body().asJson();
        if (json == null) {
            return errorJsonResult("Json expected");
        } else {
            UUID id = null;
            try {
                if (json.findPath("id").asText() != "null") {
                    id = UUID.fromString(json.findPath("id").asText());
                }
            } catch (IllegalArgumentException nfe) {
                return errorJsonResult("wrong type id");
            } finally {
//                    if (id == null) {
//                        play.Logger.info(json.findPath("id").asText().toString());
//                        return errorJsonResult("id must be specified");
//                    }
            }
            assert(id!=null);

            GroupRoot group = GroupRoot.find(id);
            play.Logger.debug(group.toString());
            if (group == null) {
                return notFound(errorJson("group is not found"));
            }
            JsonNode result = Json.toJson(group);
            group.delete(id);
            return ok(result);
        }
    }
    @BodyParser.Of(BodyParser.Json.class)
    public static Result saveGroupJson() {
        play.Logger.info("saveGroupJson");
        JsonNode json = request().body().asJson();
        if (json == null) {
            return errorJsonResult("JSON expected");
        } else {
            UUID id = null;
            try {
                if (json.findPath("id").asText() != "null") {
                    id = UUID.fromString(json.findPath("id").asText());
                }
            } catch (IllegalArgumentException nfe) {
                return errorJsonResult("wrong type id");
            } finally {
//                    if (id == null) {
//                        play.Logger.info(json.findPath("id").asText().toString());
//                        return errorJsonResult("id must be specified");
//                    }
            }
            GroupRoot group = null;
            if (id != null) {
                group = GroupRoot.find(id);
            }
            if (group == null) {
                if (json.findPath("name").asText().length()>2){
                    group = new GroupRoot(json.findPath("name").asText());
                }else{
                    return errorJsonResult("name must be longer then 2 symbols");
                }
            }
            group.setName(json.findPath("name").asText());
            play.Logger.info("trying to save to DB");
            group.save();
            return ok(Json.toJson(group));
        }
    }

    //----GROUPPRODUCT-----
    //@BodyParser.Of(BodyParser.Json.class)
    public static Result groupsProductJson(){
        play.Logger.info("groupsProductJson()");
        // TODO : It's magic!!
        String testtest= (String) request().queryString().keySet().toArray()[0];
        String test =  testtest.split(":")[1];
        String testId = test.substring(1,test.length()-2);
        //JsonNode json = request().body().asJson();
//        if (json == null) {
//            return errorJsonResult("Json expected");
//        } else {
//            UUID groupRootId = null;
//            try {
//                if (json.findPath("groupRootId").asText() != "null") {
//                    groupRootId = UUID.fromString(json.findPath("groupRootId").asText());
//                }
//            } catch (IllegalArgumentException nfe) {
//                return errorJsonResult("wrong type groupRootId");
//            }
//            assert(groupRootId!=null);
        UUID tetete = UUID.fromString(testId);
        GroupRoot g = GroupRoot.findById(tetete);

//        }
        return ok(toJson(g.getGroupProducts()));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result deleteGroupProductJson() {
        play.Logger.info("deleteGroupProductJson()");
        JsonNode json = request().body().asJson();
        if (json == null) {
            return errorJsonResult("Json expected");
        } else {
            UUID id = null;
            UUID groupRootId = null;
            try {
                if (json.findPath("id").asText() != "null") {
                    id = UUID.fromString(json.findPath("id").asText());
                }
                if (json.findPath("groupRootId").asText() != "null"){
                    groupRootId = UUID.fromString(json.findPath("groupRootId").asText());
                }
            } catch (IllegalArgumentException nfe) {
                return errorJsonResult("wrong type id");
            } finally {
//                    if (id == null) {
//                        play.Logger.info(json.findPath("id").asText().toString());
//                        return errorJsonResult("id must be specified");
//                    }
            }
            assert(id!=null);

            GroupProduct groupProduct = GroupProduct.find(id);
            play.Logger.debug(groupProduct.toString());
            if (groupProduct == null) {
                return notFound(errorJson("group is not found"));
            }
            JsonNode result = Json.toJson(groupProduct);
            GroupRoot groupRoot = GroupRoot.findById(groupRootId);
            groupRoot.deleteGroupProducts(groupProduct);
            groupProduct.delete(id);
            groupRoot.save();
            //g.deleteGroupProducts(groupProduct);
            return ok(result);
        }
    }
    @BodyParser.Of(BodyParser.Json.class)
    public static Result saveGroupProductJson() {
        play.Logger.info("saveGroupProductJson");
        JsonNode json = request().body().asJson();
        if (json == null) {
            return errorJsonResult("JSON expected");
        } else {
            UUID groupRootId = null;
            UUID id = null;
            try {
                if (json.findPath("id").asText() != "null") {
                    id = UUID.fromString(json.findPath("id").asText());
                }
                if (json.findPath("groupRootId").asText() != "null"){
                    groupRootId = UUID.fromString(json.findPath("groupRootId").asText());
                }
            } catch (IllegalArgumentException nfe) {
                return errorJsonResult("wrong type id or groupRootId");
            } finally {
//                    if (id == null) {
//                        play.Logger.info(json.findPath("id").asText().toString());
//                        return errorJsonResult("id must be specified");
//                    }
            }
            GroupProduct groupProduct = null;
            if (id != null) {
                groupProduct = GroupProduct.find(id);
            }
            if (groupProduct == null) {
                if (json.findPath("name").asText().length()>2){
                    groupProduct = new GroupProduct(json.findPath("name").asText());
                }else{
                    return errorJsonResult("name must be longer then 2 symbols ");
                }
            }
            GroupRoot groupRoot = GroupRoot.findById(groupRootId);
            groupProduct.setGroupRoot(groupRoot);
            groupProduct.setName(json.findPath("name").asText());
            play.Logger.info("trying to save to DB");
            groupProduct.save();
            groupRoot.addGroupProducts(groupProduct);
            groupRoot.save();
            return ok(Json.toJson(groupProduct));
        }
    }

    //----ATTRIBUTES AND PRODUCTVALUES-----
    public static Result attributesJson(){
        play.Logger.info("attributesJson()");
        String testtest= (String) request().queryString().keySet().toArray()[0];
        String test =  testtest.split("}")[0].split(",")[0].split(":")[1];
        String testId = test.substring(1,test.length()-1);
        UUID tetete = UUID.fromString(testId);
        GroupRoot g = GroupRoot.findById(tetete);
        return ok(toJson(g.getAttributes()));
    }

    public static Result productValuesJson(){
        play.Logger.info("productValuesJson()");
        // TODO : It's magic!!
        String testtest= (String) request().queryString().keySet().toArray()[0];
        String test =  testtest.split("}")[0].split(",")[0].split(":")[1];
        String testId = test.substring(1,test.length()-1);
        UUID tetete = UUID.fromString(testId);
        Product p = Product.findById(tetete);
        return ok(toJson(p.getProductValuesList()));
    }


    @BodyParser.Of(BodyParser.Json.class)
    public static Result deleteAttributeJson() {
        play.Logger.info("deleteAttributeJson()");
        JsonNode json = request().body().asJson();
        if (json == null) {
            return errorJsonResult("Json expected");
        } else {
            UUID id = null;
            UUID groupRootId = null;
            try {
                if (json.findPath("id").asText() != "null") {
                    id = UUID.fromString(json.findPath("id").asText());
                }
                if (json.findPath("groupRootId").asText() != "null"){
                    groupRootId = UUID.fromString(json.findPath("groupRootId").asText());
                }
            } catch (IllegalArgumentException nfe) {
                return errorJsonResult("wrong type id");
            } finally {
//                    if (id == null) {
//                        play.Logger.info(json.findPath("id").asText().toString());
//                        return errorJsonResult("id must be specified");
//                    }
            }
            assert(id!=null);

            Attribute attribute = Attribute.find(id);
            play.Logger.debug(attribute.toString());
            if (attribute == null) {
                return notFound(errorJson("attribute is not found"));
            }
            JsonNode result = Json.toJson(attribute);
            GroupRoot groupRoot = GroupRoot.findById(groupRootId);
            groupRoot.deleteAttribute(attribute);
            attribute.delete(id);
            groupRoot.save();
            return ok(result);
        }
    }
    @BodyParser.Of(BodyParser.Json.class)
    public static Result saveAttributeJson() {
        play.Logger.info("saveAttributeJson");
        JsonNode json = request().body().asJson();
        if (json == null) {
            return errorJsonResult("JSON expected");
        } else {
            UUID groupRootId = null;
            UUID id = null;
            try {
                if (json.findPath("id").asText() != "null") {
                    id = UUID.fromString(json.findPath("id").asText());
                }
                if (json.findPath("groupRootId").asText() != "null"){
                    groupRootId = UUID.fromString(json.findPath("groupRootId").asText());
                }
            } catch (IllegalArgumentException nfe) {
                return errorJsonResult("wrong type id or groupRootId");
            } finally {
//                    if (id == null) {
//                        play.Logger.info(json.findPath("id").asText().toString());
//                        return errorJsonResult("id must be specified");
//                    }
            }
            Attribute attribute = null;
            if (id != null) {
                attribute = Attribute.find(id);
            }
            if (attribute == null) {
                if (json.findPath("name").asText().length()>2){
                    attribute = new Attribute(json.findPath("name").asText());
                }else{
                    return errorJsonResult("name must be longer then 2 symbols ");
                }
            }
            GroupRoot groupRoot = GroupRoot.findById(groupRootId);
            attribute.setGroupRootAtr(groupRoot);
            attribute.setName(json.findPath("name").asText());
            play.Logger.info("trying to save to DB");
            attribute.save();
            groupRoot.addAttribute(attribute);
            groupRoot.save();
            return ok(Json.toJson(attribute));
        }
    }

    public static Result jsRoutes() {
        response().setContentType("text/javascript");
        return ok(
                Routes.javascriptRouter("jsRoutes",
                        controllers.routes.javascript.Admin.products(),
                        controllers.routes.javascript.Admin.productsJson(),
                        controllers.routes.javascript.Admin.saveProductJson(),
                        controllers.routes.javascript.Admin.deleteProductJson(),
                        controllers.routes.javascript.Admin.groups(),
                        controllers.routes.javascript.Admin.groupsJson(),
                        controllers.routes.javascript.Admin.saveGroupJson(),
                        controllers.routes.javascript.Admin.deleteGroupJson(),
                        controllers.routes.javascript.Admin.groupsProductJson(),
                        controllers.routes.javascript.Admin.saveGroupProductJson(),
                        controllers.routes.javascript.Admin.deleteGroupProductJson(),
                        controllers.routes.javascript.Admin.attributesJson(),
                        controllers.routes.javascript.Admin.saveAttributeJson(),
                        controllers.routes.javascript.Admin.deleteAttributeJson(),
                        controllers.routes.javascript.Admin.productValuesJson()

                )
        );
    }



}

